package sgit_app.sgit_objects

case class Blob(
  hash: String,
  path: String,
  hashName: String
) extends STree