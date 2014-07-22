package controllers


import models.entities.Login
import play.api.libs.json.{JsObject, JsError, Json, JsString}
import play.api.mvc._

object Application extends Controller {

    def index = Action { implicit request =>

        Ok(views.html.index())
    }

    def login = Action(BodyParsers.parse.json) { implicit request =>
        import api.LoginApi._

        request.body.validate[LoginRequest].fold(
            errors => {
                val jsErrors = JsError.toFlatJson(errors)
                BadRequest(
                    Json.toJson(LoginResponse(None, Some(jsErrors)))
                )
            },
            loginReq => {
                Login.login(loginReq.email, loginReq.password).map { user =>
                    Ok(
                        Json.toJson(LoginResponse(Some(user.id)))
                    ).withSession(
                        "userId" -> user.id.toString
                    )
                }.getOrElse {
                    Unauthorized("")
                }
            }
        )
    }

    def signUp() = Action(BodyParsers.parse.json) { implicit request =>
        import api.LoginApi._

        request.body.validate[LoginRequest].fold(
            errors => {
                val jsErrors = JsError.toFlatJson(errors)
                BadRequest(
                    Json.toJson(LoginResponse(None, Some(jsErrors)))
                )
            },
            signUpReq => {
                Login.create(signUpReq.email, signUpReq.password).map { user =>
                    Ok("")
                }.getOrElse {
                    val error = JsObject(Seq(
                        "error" -> JsString("Duplicate E-mail.")
                    ))

                    BadRequest(
                        Json.toJson(LoginResponse(None, Some(error)))
                    )
                }
            }
        )
    }
}