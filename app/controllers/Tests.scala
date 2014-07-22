package controllers

import play.api.Play
import play.api.libs.concurrent.Akka
import play.api.mvc.{Controller, Action}
import scalikejdbc._


/**
 * A controller exposing non-application actions.
 */
object Tests extends Controller {
    import play.api.Play.current

    def js = Action {
        if (Play.isProd) {
            NotFound("")
        } else {
            Ok(views.html.tests.js())
        }
    }

    def status = Action {
        val actorUptime = Akka.system.uptime
        val db = DB.readOnly { implicit conn =>
            sql"SELECT 2 + 2 AS result"
                .map(rs => rs.long("result"))
                .single()
                .apply()
        }

        Ok(views.html.tests.status(actorUptime, db.get))
    }
}
