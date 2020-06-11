package wikipedia
package models

case class GenericPage(page_title: String, count_views: Option[Long]) extends Ordered[GenericPage] {
  def compare(that: GenericPage): Int =
    -this.count_views.getOrElse(0L).compare(that.count_views.getOrElse(0L))
}

case class Ranking(domain_code: String, pages: Seq[GenericPage]) {
  def take(n: Int) = this.copy(pages = pages.sorted.take(n))
}
