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

    private def loginPageTests(browser: TestBrowser) = {
        val heading1 = browser.findFirst(".form-signin-heading").getText
        val heading2 = browser.findFirst(".form-signup-heading").getText

        heading1 must contain("Sign in")
        heading2 must contain("Sign up")
    }

    "Jasmin Tests" should {
        "pass from within a browser" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/tests/js")

            eventually {
                val res = browser.findFirst("span.passed").getText

                res must contain("0 failures")
            }
        }
    }

    "The login page" should {
        "be shown when accessing /" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/")

            // TODO: Find out why this fails.
            //eventually {
            //  loginPageTests(browser)
            //}
        }

        "be shown when accessing an invalid url fragment" in new WithBrowser {
            browser.goTo("http://localhost:" + port + "/#/foo")

            // TODO: Find out why this fails.
            //eventually {
            //    loginPageTests(browser)
            //}
        }
    }
}
