package models.entities

import java.util.UUID

import com.mpatric.mp3agic.Mp3File
import org.joda.time._
import play.api.libs.Files.TemporaryFile
import plugins.S3Plugin
import scalikejdbc._
import skinny.orm._
import skinny.orm.feature._

import scala.annotation
import scala.annotation.meta

case class Song(
    id: Long,
    title: String,
    artist: String,
    album: String,
    length: Int,
    url: String,
    createdAt: DateTime,
    updatedAt: DateTime,
    deletedAt: Option[DateTime]
)

object Song extends SkinnyCRUDMapper[Song]
    with TimestampsFeature[Song]
    with SoftDeleteWithTimestampFeature[Song] {

    override lazy val tableName = "songs"
    override lazy val defaultAlias = createAlias("s")

    override def extract(rs: WrappedResultSet, rn: ResultName[Song]): Song =
        new Song(
            id = rs.get(rn.id),
            title = rs.get(rn.title),
            artist = rs.get(rn.artist),
            album = rs.get(rn.album),
            length = rs.get(rn.length),
            url = rs.get(rn.url),
            createdAt = rs.get(rn.createdAt),
            updatedAt = rs.get(rn.updatedAt),
            deletedAt = rs.get(rn.deletedAt)
        )

    def createFromFile(file: TemporaryFile): Option[Song] = {
        val meta = new Mp3File(file.file.getCanonicalPath)
        val uuid = UUID.randomUUID().toString

        DB.localTx { implicit session =>
            val tags = if (meta.hasId3v2Tag) {
                meta.getId3v2Tag
            } else {
                meta.getId3v1Tag
            }

            val s = defaultAlias
            val id = createWithAttributes(
                'title -> tags.getTitle,
                'artist -> tags.getArtist,
                'album -> tags.getAlbum,
                'length -> meta.getLengthInSeconds,
                'url -> S3Plugin.upload(uuid, file.file).get
            )

            findById(id)
        }
    }
}
