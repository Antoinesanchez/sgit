package sgit_app.sgit_objects

case class Commit(
  hashId: String,
  desc: String,
  tree: String,
  parentHash: String
)