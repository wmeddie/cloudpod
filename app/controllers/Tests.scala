package controllers

import play.api.Play
import play.api.db.DB
import anorm._
import play.api.libs.concurrent.Akka
import play.api.mvc.{Controller, Action}

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
        val db = DB.withConnection { implicit conn =>
            SQL"""
                SELECT 2 + 2 AS result
            """.as(SqlParser.long("result").single)
        }

        Ok(views.html.tests.status(actorUptime, db))
    }

}
