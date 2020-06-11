package wikipedia
package models

import org.scalatest._

class TopPagesSpec extends FlatSpec with Matchers {
  val lines1 = Seq(
    "pt Sete_Povos_das_Missões 3 0",
    "www.wd Q23926758 1 0",
    "ja 高橋しょう子 9 0",
    "pt.m Inconfidentes 2 0",
    "de.m Craig_Parker 2 0",
    "en Christophe_(given_name) 1 0",
    "en.m Angel_Williams 1 0",
    "ko.m 포화_지방산 2 0",
    "pl.m Katarzyna_Figura 2 0",
    "en Leberecht_Maass 1 0",
    "ja.m 合議 1 0",
    "ja こまつドーム 1 0",
    "es Anexo:Composiciones_de_Ludwig_van_Beethoven 3 0",
    "de.m Nowaja_Semlja 2 0",
    "pl Mały_Modelarz 2 0",
    "en.m Sorin_Grindeanu 1 0",
    "en Denmark_national_football_team 10 0",
    "ja 東村山駅 2 0",
  )

  val lines2 = Seq(
    "zh %E9%B8%A1%E8%85%BF%E5%A0%87%E8%8F%9C",
    "en Category:People_educated_at_Wyggeston_Grammar_School_for_Boys",
    "fr Sp%C3%A9cial:Ouvrages_de_r%C3%A9f%C3%A9rence/9788846489654",
    "ro.m Lambada",
    "de.m Spezial:Beitr%C3%A4ge/85.1.15.112",
    "zh.m Template:%E6%97%8C%E5%96%84%E7%BA%BF",
    "ru %C0%F2%EB%E0%ED%F2%FB_(%EF%E5%F1%ED%FF)",
    "vi.d th%E1%BB%B1c_s%E1%BB%B1",
    "sv.m R%C3%B6d_snapper",
    "it Guerra_del_Dominio",
    "en Yelena_Volodina-Antonova",
    "en.d margarida",
    "ar %D9%84%D9%8A%D9%88%D9%86_%D8%A8%D9%84%D9%88%D9%85",
    "en Kim_wilde",
    "en El_rostro_de_la_venganza",
    "pl.m Mercedes-Benz_W203",
    "es P%C3%A9rmico_Superior",
    "en.m.s Page:Chronicle_of_the_law_officers_of_Ireland.djvu/115",
    "en.m Juan_Santamar%C3%ADa_International_Airport",
    "fr Andr%C3%A9-Jacques_Fougerat"
  )

  it should "parse logs" in {
    lines1.flatMap(PageView.parse).length shouldBe 18
    lines2.flatMap(BlacklistedItem.parse).length shouldBe 20
  }

  it should "rank results" in {
    val l = Seq(
      PageView("", "", "a", "...", 5, 1),
      PageView("", "", "a", "...", 1, 1),
      PageView("", "", "a", "...", 2, 1),
      PageView("", "", "a", "...", 3, 1),
      PageView("", "", "a", "...", 4, 1),
      PageView("", "", "b", "...", 1, 1),
      PageView("", "", "c", "...", 1, 1)
    )

    // val v1 = Ranking.topK(l, 3)

    // v1("a").length shouldBe 3
    // v1("b").length shouldBe 1
    //
    // println(Ranking.toCSV(v1))
  }
}
