package sgit_app.io

import better.files._
import java.nio.file.{Files, Paths}
import scala.util.matching.Regex

object Tools {

  val workingDirectory: File = ".sgit/".toFile.parent

  /**
  * Delete everything recursively
  * @param start : starting point of deletion
  */
  def delete (start: String): Unit = start.toFile.delete()

  /**
  * Create a directory or a file with the given name
  * @param isDirectory : true if directory, else false
  * @param name : name of the file or directory
  */
  def createDirOrFile(isDirectory: Boolean, name: String): Unit = name.toFile.createIfNotExists(isDirectory, false)

  /**
  * Write content in file
  * @param fileName : name of the file whose content is going to be updated
  * @param content : content
  */
  def writeFile(fileName: String, content: String): Unit = fileName.toFile.append(content)

  /**
  * Yield all files in the working directory, recursively
  */
  def yieldAllFiles(): Seq[File] = {
    workingDirectory
      .listRecursively
      .filterNot(f => f.path.toString.contains(".sgit"))
      .toSeq
  }

  /**
  * Yield all files whose name has been asked
  * @param fileNames : Names of the files that are asked
  */
  def yieldAskedFiles(fileNames: Seq[String]): Seq[File] = {
    fileNames
      .iterator
      .map((f: String) => if (f.toFile.exists) f.toFile else null)
      .filterNot(f => f == null)
      .filterNot(f => f.path.toString.contains(".sgit"))
      .toSeq
  }

  /**
  * Yield all files matching a regex
  * @param regex : Regex that is supposed to match with files
  */
  def yieldRegexFiles(regex: Regex): Seq[File] = {
    workingDirectory
      .listRecursively
      .map(f => f.name match {
        case regex => f
        case _ => null
      })
      .filterNot(f => f == null)
      .filterNot(f => f.path.toString.contains(".sgit"))
      .toSeq
  }
}