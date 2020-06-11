package wikipedia
package helpers

trait Bucket

case class LocalStorage(blacklist: String, pageviews: String, output: String) extends Bucket
case class S3Bucket(path: String) extends Bucket
