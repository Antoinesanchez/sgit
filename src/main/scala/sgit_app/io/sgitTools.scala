package sgit_app.io

import better.files._
import sgit_app.sgit_objects.Staged

object sgitTools {

  // /**
  // * Create and return blob objects of given files
  // * @param files : given files
  // */
  // def createObject(files: Seq[File]): Seq[String] = {
  //   files.map((f: File) => {
  //     val file: File = (".sgit/objects/"+f.sha1)
  //       .toFile
  //       .appendLine(f.name)
  //       .appendText(f.contentAsString)
  //     file.sha1
  //   })
  // }

  /**
  * Create and return a Staged object encapsulating a hash of the content of the file as well as its name
  * @param file : given file
  */
  def createStaged(file: File): Staged = {
    Staged(file.sha1.toString, file.pathAsString)
  }

}