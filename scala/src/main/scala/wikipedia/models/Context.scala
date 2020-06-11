package wikipedia
package models

import org.apache.spark.sql.SparkSession

object Context {
  def getOrCreateSession() = {
    SparkSession.builder()
      .config("spark.ui.showConsoleProgress", "true")
      .master("local[*]")
      .appName("Wikipedia")
      .getOrCreate()
  }

  def closeAllSessions() =
    SparkSession.getActiveSession.foreach(_.close())
}

case class Context(session: SparkSession) {
  val implicits = session.implicits
}
