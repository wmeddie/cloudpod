package api

import play.api.libs.json.{JsObject, Json}

object LoginApi {
    implicit val loginRequestReads = Json.reads[LoginRequest]
    implicit val loginResponseWrites = Json.writes[LoginResponse]

    case class LoginRequest(email: String, password: String)

    case class LoginResponse(
        userId: Option[Long],
        errors: Option[JsObject] = None
    )
}


