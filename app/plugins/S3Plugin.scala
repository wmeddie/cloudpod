package plugins

import java.io.File

import awscala.Region
import awscala.s3.S3
import com.amazonaws.services.s3.model.{CannedAccessControlList, CopyObjectRequest}
import play.api.{Application, Logger, Plugin}

/**
 * Storage backend for files.
 */
object S3Plugin {
    private lazy val bucket = amazonS3.bucket(bucketName).get
    private val AWS_S3_ENABLED = "aws.s3.enabled"
    private val AWS_S3_BUCKET_KEY = "aws.s3.bucket"
    private val AWS_ACCESS_KEY = "aws.accesskey"
    private val AWS_SECRET_KEY = "aws.secretkey"
    private implicit var amazonS3: S3 = _
    private var bucketName: String = _

    /**
     * Upload the specified file to S3.
     * @param name probably unique file name
     * @param file file to upload
     * @return URL of the resource
     */
    def upload(name: String, file: File): Option[String] = {
        val res = bucket.putAsPublicRead(name, file)

        val meta = bucket.getMetadata(name)
        meta.setContentType("audio/mpeg")

        amazonS3.copyObject(
            new CopyObjectRequest(bucketName, name, bucketName, name)
                .withCannedAccessControlList(CannedAccessControlList.PublicRead)
                .withNewObjectMetadata(meta)
        )

        Some(s"https://s3-ap-northeast-1.amazonaws.com/$bucketName/$name")
    }
}

class S3Plugin(application: Application) extends Plugin {
    import plugins.S3Plugin._

    override def onStart() {
        implicit val region = Region.Tokyo
        val accessKey = application.configuration.getString(AWS_ACCESS_KEY)
        val secretKey = application.configuration.getString(AWS_SECRET_KEY)
        val bucketName = application.configuration.getString(AWS_S3_BUCKET_KEY)

        S3Plugin.amazonS3 = S3(accessKey.get, secretKey.get)
        S3Plugin.bucketName = bucketName.get

        Logger.info("Using S3 Bucket: " + bucketName.get)
    }

    override def enabled: Boolean =
        application.configuration.getBoolean(AWS_S3_ENABLED).getOrElse(false)
}