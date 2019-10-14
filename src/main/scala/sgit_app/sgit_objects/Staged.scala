package sgit_app.sgit_objects

case class Staged(
  contentHash: String,
  fileName: String
) {
  override def toString() = fileName + " " + contentHash 
}