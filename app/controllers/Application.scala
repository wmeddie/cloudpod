package controllers

import models.entities.{Login, Song}
import play.api.libs.json.{JsError, JsObject, JsString, Json}
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

    def signUp = Action(BodyParsers.parse.json) { implicit request =>
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

    def upload = Action(parse.multipartFormData) { implicit request =>
        val form = request.body.asFormUrlEncoded

        request.session.get("userId").map { userId =>
            val res = for {
                songFile <- request.body.file("song")
                song <- Song.createFromFile(songFile.ref)
            } yield song

            res.map { song =>
                // TODO Enqueue
                Accepted("").withHeaders(
                    "Location" -> song.url
                )
            }.getOrElse {
                BadRequest("")
            }
        }.getOrElse {
            Unauthorized("")
        }
    }
}