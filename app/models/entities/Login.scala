package models.entities

import java.sql.SQLException

import org.joda.time._
import scalikejdbc._
import skinny.orm._
import skinny.orm.feature._
import utils.PasswordAuthentication

/**
 * A valid login credential for Cloudpod.
 * @param id Serial primary key
 * @param email User e-mail.  Required
 * @param password Hashed user password (if not using OAuthID)
 * @param salt Salt for password
 * @param difficulty rounds of PBKDF2 for password
 * @param provider OAuthID provider (if any)
 * @param token Last OAuthID token
 * @param createdAt Created timestamp
 * @param updatedAt Last updated timestamp
 * @param deletedAt Deleted timestamp
 */
case class Login(
    id: Long,
    email: String,
    password: Option[String],
    salt: Option[String],
    difficulty: Option[Int],
    provider: Option[String],
    token: Option[String],
    createdAt: DateTime,
    updatedAt: DateTime,
    deletedAt: Option[DateTime]
)

object Login extends SkinnyCRUDMapper[Login]
    with TimestampsFeature[Login]
    with SoftDeleteWithTimestampFeature[Login]
    with PasswordAuthentication {

    override lazy val tableName = "logins"
    override lazy val defaultAlias = createAlias("l")

    override def extract(rs: WrappedResultSet, rn: ResultName[Login]): Login =
        new Login(
            id = rs.get(rn.id),
            email = rs.get(rn.email),
            password = rs.get(rn.password),
            salt = rs.get(rn.salt),
            difficulty = rs.get(rn.difficulty),
            provider = rs.get(rn.provider),
            token = rs.get(rn.token),
            createdAt = rs.get(rn.createdAt),
            updatedAt = rs.get(rn.updatedAt),
            deletedAt = rs.get(rn.deletedAt)
        )

    def login(email: String, password: String): Option[Login] = {
        val l = defaultAlias

        for {
            user <- findBy(sqls.eq(l.email, email))
            hash <- user.password
            salt <- user.salt
            difficulty <- user.difficulty
            if isPasswordCorrect(
                password,
                hash,
                salt,
                difficulty
            )
        } yield user
    }

    def create(email: String, password: String): Option[Login] = {
        val (hash, salt, diff) = generateHash(password)

        try {
            val id = Login.withColumns { col =>
                Login.createWithNamedValues(
                    col.email -> email,
                    col.password -> Some(hash),
                    col.salt -> Some(salt),
                    col.difficulty -> Some(diff),
                    col.provider -> None,
                    col.token -> None
                )
            }

            Login.findById(id)
        } catch {
            case _: SQLException => {
                None
            }
        }
    }
}
