package wikipedia
package tasks

import org.slf4j.{ LoggerFactory, Logger }
import org.apache.spark.sql.SaveMode
import wikipedia.models.{ LocalStorage, Context }
import models.{ PageView, BlacklistedItem }

case class MostVisitedTask(input: LocalStorage)(implicit context: Context) extends Runnable {
  import context.session.implicits._
  implicit private val logger: Logger = LoggerFactory.getLogger(getClass)

  override def run(): Unit = {
    logger.info("to implement")
  }
}
