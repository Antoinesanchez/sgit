package sgit_app.io

import better.files._

object sgitTools {

  /**
  * Create and return blob objects of given files
  * @param files : given files
  */
  def createObject(files: Seq[File]): Seq[String] = {
    files.map((f: File) => {
      val file: File = (".sgit/objects/"+f.sha1)
        .toFile
        .appendLine(f.name)
        .appendText(f.contentAsString)
      file.sha1
    })
  }

}