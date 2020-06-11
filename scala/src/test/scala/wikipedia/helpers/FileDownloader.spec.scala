package wikipedia
package helpers

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.time.LocalDateTime

class FileDownloaderSpec extends AnyFlatSpec with Matchers {

  it should "generate url string with dates" in {
    val dateMax = LocalDateTime.MAX
    val date = LocalDateTime.of(2010, 12, 25, 23, 59)

    FileDownloader.pageviewString(dateMax) shouldBe
      "pageviews/999999999/999999999-12/pageviews-9999999991231-230000.gz"
    FileDownloader.pageviewString(date) shouldBe
      "pageviews/2010/2010-12/pageviews-20101225-230000.gz"
  }

  it should "download files from WWW" in {
    val url1 = "https://www.google.fr/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
    val url2 = "https://www.google.what/does_not_exist.png"

    FileDownloader.downloadTo(url1, "googlelogo.png").isSuccess shouldBe true
    // FileDownloader.downloadTo(url2, "googlelogo.png").isFailure shouldBe true

  }
}
