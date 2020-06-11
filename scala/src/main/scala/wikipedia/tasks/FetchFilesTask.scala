package wikipedia
package tasks

import org.slf4j.LoggerFactory
import helpers.{ LocalStorage, FileDownloader }
import java.time.LocalDateTime
import scala.util.{ Success, Failure }

case class FetchFilesTask(storage: LocalStorage, date: LocalDateTime) extends Runnable {
  implicit private val logger = LoggerFactory.getLogger(getClass)

  override def run(): Unit = {
    logger.info("Downloading remote files to local filesystem...")

    val url1 = s"https://s3.amazonaws.com/dd-interview-data/data_engineer/wikipedia/blacklist_domains_and_pages"

    val pvStr = FileDownloader.pageviewString(date)
    val url2 = s"https://dumps.wikimedia.org/other/$pvStr"

    val d1 = FileDownloader.downloadTo(url1, storage.blacklist)
    val d2 = FileDownloader.downloadTo(url2, storage.pageviews)

    (d1, d2) match {
      case (Success(v1), Success(v2)) => logger.info("Downloading finished")
      case (Success(v1), _)           => logger.error("Could not download pageviews")
      case (_, Success(v2))           => logger.error("Could not download blacklist")
      case (Failure(e1), Failure(e2)) =>
        logger.error("Could not download the files, please check your network")
    }
  }
}
