package wikipedia
package tasks

import org.apache.spark.sql.{ Dataset, Encoders, DataFrame, SaveMode, SparkSession }
import org.apache.spark.sql.functions.{ broadcast, collect_list, struct, explode }
import org.slf4j.LoggerFactory
import wikipedia.models._
import helpers.LocalStorage

case class MostVisitedTask(storage: LocalStorage, spark: SparkSession) extends Runnable {
  import spark.implicits._
  implicit private val logger = LoggerFactory.getLogger(getClass)

  def filtered(pageviews: Dataset[PageView], blacklist: Dataset[BlacklistItem]): Dataset[PageView] =
    pageviews.join(broadcast(blacklist), Seq("domain_code", "page_title"), "left_anti")
      .as[PageView]

  override def run(): Unit = {
    logger.info("Computing Top K pageviews on every domains...")

    val pageviews: Dataset[PageView] = spark.read.format("csv")
      .option("header", false)
      .option("sep", " ")
      .schema(Encoders.product[PageView].schema)
      .load(storage.pageviews)
      .as[PageView]

    val blacklist: Dataset[BlacklistItem] = spark.read.format("csv")
      .option("header", false)
      .option("sep", " ")
      .schema(Encoders.product[BlacklistItem].schema)
      .load(storage.blacklist)
      .as[BlacklistItem]

    val pageviews2: Dataset[PageView] = filtered(pageviews, blacklist)

    println(s"Total pageviews: ${pageviews.count}")
    println(s"Total blacklisted: ${blacklist.count}")
    println(s"Remaining filtered pageviews: ${pageviews2.count}")

    pageviews2.show

    val rankings: Dataset[Ranking] = pageviews2.groupBy('domain_code)
      .agg(collect_list(struct('page_title, 'count_views)) as "pages")
      .orderBy('domain_code)
      .as[Ranking]
      .map(_.take(25))

    val topK: DataFrame = rankings.select('domain_code, explode('pages) as "page")
      .select('domain_code, $"page.page_title", $"page.count_views")

    topK.show

    topK.coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .csv(storage.output)
  }
}
