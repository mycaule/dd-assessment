package wikipedia
package helpers

import scala.language.postfixOps
import sys.process._
import java.net.URL
import java.io.File
import java.time.LocalDateTime
import scala.util.{ Try, Success, Failure }

object FileDownloader {
  def pageviewString(date: LocalDateTime): String = {
    val year = date.getYear
    val month = f"${date.getMonthValue}%02d"
    val day = f"${date.getDayOfMonth}%02d"
    val hour = f"${date.getHour}%02d"
    val datetime = s"$year$month$day-$hour" + "0000"

    s"pageviews/$year/$year-${month}/pageviews-${datetime}.gz"
  }

  def downloadTo(url: String, filename: String): Try[String] = {
    val tUrl = Try(new URL(url))
    val tFile = Try(new File(filename))

    (tUrl, tFile) match {
      case (Success(v1), Success(v2)) => Try(v1 #> v2 !!)
      case (Failure(f1), _)           => Failure(f1)
      case (_, Failure(f2))           => Failure(f2)
    }
  }
}
