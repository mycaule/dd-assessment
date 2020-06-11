package wikipedia
package tasks

import org.slf4j.{ LoggerFactory, Logger }
import org.apache.spark.sql.SaveMode
import wikipedia.models.{ LocalStorage, Context }
import models.{ PageView, BlacklistedItem }
import java.time.LocalDate
import scala.io.{ Source }

object FileDownloader {
  import sys.process._
  import java.net.URL
  import java.io.File

  def downloadTo(url: String, filename: String) =
    new URL(url) #> new File(filename) !!

  // TODO Download only if files does not exist
}

case class FetchFilesTask(output: LocalStorage, date: LocalDate)(implicit context: Context) extends Runnable {
  import context.session.implicits._
  implicit private val logger: Logger = LoggerFactory.getLogger(getClass)

  override def run(): Unit = {
    val year = date.getYear
    val month = f"${date.getMonthValue}%02d"
    val day = f"${date.getDayOfMonth}%02d"
    val hour = 0
    val datetime = s"$year$month$day-" + f"${hour}%02d" + "0000"

    // TODO application.conf
    val url1 = s"https://s3.amazonaws.com/dd-interview-data/data_engineer/wikipedia/blacklist_domains_and_pages"
    // FileDownloader.downloadTo(url1, "cache/blacklist")

    val blacklist = Source.fromFile("cache/blacklist").getLines.toSeq.toDS
      .as[String].flatMap(BlacklistedItem.parse)
      .filter(_.domain == "en")

    blacklist.show
    logger.info(s"${blacklist.count}")
    // blacklist.repartition(2)
    //   .write.mode(SaveMode.Overwrite)
    //   .parquet(s"${output.path}/blacklist/$date")

    val url2 = s"https://dumps.wikimedia.org/other/pageviews/$year/$year-${month}/pageviews-${datetime}.gz"
    // FileDownloader.downloadTo(url2, "cache/pageviews.gz") // TODO wait this

    val views = context.session.read.format("csv")
      .option("header", "false")
      .load("cache/pageviews.gz")
      .as[String]
      .flatMap(PageView.parse(date, hour, _))
      .filter(_.domain == "en")
      .join(blacklist, Seq("domain", "title", "left_anti"))

    views.show
    logger.info(s"${views.count}")

    // views.repartition(2)
    //   .write.mode(SaveMode.Overwrite)
    //   .parquet(s"${output.path}/views/$date")
  }
}
