package sgit_app.sgit_objects

case class SDir(
  children: Seq[STree],
  path: String,
  hash: String
) extends STree