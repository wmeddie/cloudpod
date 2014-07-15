import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

    "Jasmin Tests" should {
        "pass from within a browser" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/tests/js")

            eventually {
                browser.findFirst("span.passed").getText must
                        contain("0 failures")
            }
        }
    }

    "The login page" should {
        "be shown when accessing /" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/")

            eventually {
                browser.pageSource must contain("Sign in")
                browser.pageSource must contain("Sign up")
            }
        }

        "be shown when accessing an invalid url fragment" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/#/foo")

            eventually {
                browser.pageSource must contain("Sign in")
                browser.pageSource must contain("Sign up")
            }
        }
    }
}
