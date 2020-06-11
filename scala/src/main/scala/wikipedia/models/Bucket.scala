package wikipedia
package models

trait Bucket

case class LocalStorage(path: String) extends Bucket
case class S3Bucket(path: String) extends Bucket
