package wikipedia
package tasks

import helpers.{ Context, LocalStorage }
import java.time.LocalDateTime

object TopPages {
  def main(args: Array[String]): Unit = {
    val storage = LocalStorage(
      "/tmp/wiki-data/blacklist",
      "/tmp/wiki-data/pageviews.gz",
      "/tmp/wiki-data/domain_top25"
    )

    val session = Context.getOrCreateSession()

    val date = LocalDateTime.now.minusDays(1)

    FetchFilesTask(storage, date).run()
    MostVisitedTask(storage, session).run()

    Context.closeAllSessions()
  }
}
