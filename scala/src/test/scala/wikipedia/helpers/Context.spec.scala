package wikipedia
package helpers

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ContextSpec extends AnyFlatSpec with Matchers with BeforeAndAfterEach {

  override def afterEach() = {
    Context.closeAllSessions()
  }

  it should "create a single session" in {
    val session = Context.getOrCreateSession()
    import session.implicits._

    session.createDataset(Seq("a", "b")).count shouldBe 2
  }

  it should "kill all sessions" in {
    Context.getOrCreateSession()
    Context.getOrCreateSession()
    Context.getOrCreateSession()
    Context.closeAllSessions()
  }
}
