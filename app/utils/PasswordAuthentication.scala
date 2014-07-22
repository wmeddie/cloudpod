package utils

import java.nio.charset.Charset

import io.github.nremond.PBKDF2
import sun.misc.{BASE64Decoder, BASE64Encoder}

import scala.util.Random

/**
 * Mixin for creating and checking passwords hashed with PBKDF2.
 */
trait PasswordAuthentication {
    private val DefaultDifficulty = 20000
    private val DefaultHashLength = 20
    private val DefaultHashAlgorithm = "HmacSHA256"

    /**
     * Check if the given parameters hash to the same string as challenge.
     * @param challenge The plaintext password
     * @param password The stored PBKDF2 password hash
     * @param salt The random salt
     * @param difficulty The number of PBKDF2 rounds
     * @return true if hashes match
     */
    protected def isPasswordCorrect(challenge: String, password: String,
                                    salt: String, difficulty: Int): Boolean = {
        val base64Enc = new BASE64Encoder()
        val base64Dec = new BASE64Decoder()

        val challengeHash = base64Enc.encode(PBKDF2.apply(
            challenge.getBytes(Charset.forName("UTF-8")),
            base64Dec.decodeBuffer(salt),
            DefaultDifficulty,
            DefaultHashLength,
            DefaultHashAlgorithm
        ))

        challengeHash == password
    }

    /**
     * Hashes the given password with PBKDF2.
     * @param password The plaintext password
     * @return Tuple with hash, salt, and difficulty
     */
    protected def generateHash(password: String): (String, String, Int) = {
        val random = new Random()
        val base64 = new BASE64Encoder()

        val saltArray = new Array[Byte](25)
        random.nextBytes(saltArray)

        val salt = base64.encode(saltArray)
        val hashed = base64.encode(PBKDF2.apply(
            password.getBytes(Charset.forName("UTF-8")),
            saltArray,
            DefaultDifficulty,
            DefaultHashLength,
            DefaultHashAlgorithm
        ))

        (hashed, salt, DefaultDifficulty)
    }
}
