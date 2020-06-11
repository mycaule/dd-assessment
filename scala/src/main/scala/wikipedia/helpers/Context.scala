package wikipedia
package helpers

import org.apache.spark.sql.SparkSession

object Context {
  def getOrCreateSession() =
    SparkSession.builder()
      .config("spark.ui.showConsoleProgress", "true")
      .master("local[*]")
      .appName("wikipedia")
      .getOrCreate()

  def closeAllSessions() =
    SparkSession.getActiveSession.foreach(_.close())
}
