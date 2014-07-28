import akka.actor.Props
import akka.testkit.TestProbe
import models.actors.JamHubActor
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.concurrent.Akka
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

    "Application" should {

        "send 404 on a bad request" in new WithApplication {
            route(FakeRequest(GET, "/boum")) must beNone
        }

        "render the index page" in new WithApplication {
            val home = route(FakeRequest(GET, "/")).get

            status(home) must equalTo(OK)
            contentType(home) must beSome.which(_ == "text/html")
        }
    }

    "JamHubActor" should {
        "Support creating Jams" in new WithApplication {
            import models.actors.JamHubActor._

            implicit val system = Akka.system

            val hub = system.actorOf(Props[JamHubActor])
            val probe = TestProbe()
            val void = TestProbe()

            hub.tell(ListJams(), probe.ref)

            val res1 = probe.expectMsgClass(classOf[Set[String]])
            res1 must beEmpty

            hub.tell(CreateJam("test"), void.ref)
            hub.tell(ListJams(), probe.ref)

            val res2 = probe.expectMsgClass(classOf[Set[String]])
            (res2 must not).beEmpty
        }
    }

}
