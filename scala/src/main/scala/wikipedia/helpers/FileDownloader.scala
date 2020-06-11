package wikipedia
package helpers

import sys.process._
import java.net.URL
import java.io.File
import java.time.LocalDateTime
import scala.util.Try

object FileDownloader {
  def pageviewString(date: LocalDateTime): String = {
    val year = date.getYear
    val month = f"${date.getMonthValue}%02d"
    val day = f"${date.getDayOfMonth}%02d"
    val hour = f"${date.getHour}%02d"
    val datetime = s"$year$month$day-$hour" + "0000"

    s"pageviews/$year/$year-${month}/pageviews-${datetime}.gz"
  }

  def downloadTo(url: String, filename: String): Try[String] =
    Try(new URL(url) #> new File(filename) !!)

}
