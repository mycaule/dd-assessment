package wikipedia
package models

import java.time.LocalDate
import tasks._

case class BlacklistedItem(domain: String, title: String)

object BlacklistedItem {
  val validRE = "([a-z\\.]+) ([^ ]+)".r

  def parse(s: String): Option[BlacklistedItem] = s match {
    case validRE(f1, f2) => Some(BlacklistedItem(f1, f2))
    case _               => None
  }
}

case class PageView(date: String, hour: Int, domain: String, title: String, views: Int, size: Int)

object PageView {
  val validRE = "([a-z\\.]+) ([^ ]+) ([0-9]+) ([0-9]+)".r

  def parse(date: LocalDate, hour: Int, s: String): Option[PageView] = s match {
    case validRE(f1, f2, f3, f4) => Some(PageView(date.toString, hour, f1, f2, f3.toInt, f4.toInt))
    case _                       => None
  }
}

object Ranking {
  type Ranking = Map[String, Seq[(String, Int)]]

  // def topK(l: Seq[PageView], k: Int): Ranking =
  //   l.groupMap(_.domain)(x => (x.title, x.views))
  //     .map(x => (x._1, x._2.sortBy(-_._2).take(k)))

  def toCSV(r: Ranking): Seq[String] =
    r.flatMap(x => x._2.map(y => s"${x._1} ${y._1} ${y._2}")).toSeq
}

object TopPages {
  def main(args: Array[String]): Unit = {
    val date = LocalDate.now.minusDays(1) // Defaults to yesterday

    val debug = true

    if (debug) {
      val storage = LocalStorage("/tmp/wiki-data")
      implicit val context = Context(Context.getOrCreateSession())

      val task1 = FetchFilesTask(storage, date)
      val task2 = MostVisitedTask(storage)

      task1.run()
      task2.run()

      context.session.close()
    }
  }
}
