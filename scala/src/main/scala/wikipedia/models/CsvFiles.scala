package wikipedia
package models

case class PageView(domain_code: String, page_title: String, count_views: Long, total_response_size: Long)
case class BlacklistItem(domain_code: String, page_title: String)
