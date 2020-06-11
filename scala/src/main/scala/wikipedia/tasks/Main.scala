package wikipedia
package tasks

import helpers.{ Context, LocalStorage }
import java.time.LocalDateTime

object TopPages {
  def main(args: Array[String]): Unit = {

    val date = args match {
      case Array(yyyy, mm, dd, hh) =>
        val year = yyyy.toInt
        val month = mm.toInt
        val day = dd.toInt
        val hour = hh.toInt
        LocalDateTime.of(year, month, day, hour, hour, 0)

      case _ => LocalDateTime.now.minusDays(1)
    }

    val storage = LocalStorage("blacklist", "pageviews.gz", "domain_top25")

    FetchFilesTask(storage, date).run()

    val session = Context.getOrCreateSession()
    MostVisitedTask(storage, session).run()

    Context.closeAllSessions()
  }
}
