package wikipedia
package models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RankingSpec extends AnyFlatSpec with Matchers {
  val g1 = GenericPage("Denmark_national_football_team", Some(10))
  val g2 = GenericPage("Christophe_(given_name)", Some(1))
  val g3 = GenericPage("Leberecht_Maass", Some(1))
  val g4 = GenericPage("Kim_wilde", None)

  it should "compare lines" in {
    g1 < g2 shouldBe true
    g2 <= g3 shouldBe true
    g3 < g4 shouldBe true
  }

  it should "retain k results" in {
    val r1 = Ranking("en", Seq(g1, g2, g3))
    val r2 = Ranking("en", Nil)

    r1.take(2).pages.length shouldBe 2
    r2.take(3).pages.length shouldBe 0
  }
}
