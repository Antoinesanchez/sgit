package sgit_app.io

import better.files._
import java.nio.file.{Files, Paths}
import scala.util.matching.Regex

object Tools {

  val workingDirectoryPath: String = ".sgit/".toFile.parent.pathAsString + "/"

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
  def createDirOrFile(isDirectory: Boolean, name: String): Unit = name.toFile.createIfNotExists(isDirectory, true)

  /**
  * Write content in file
  * @param fileName : name of the file whose content is going to be updated
  * @param content : content
  */
  def writeFile(fileName: String, content: String): Unit = fileName.toFile.append(content)

  /**
  * Yield all files in the working directory, recursively
  */
  def yieldAllFiles(starting_dir: String = "."): Seq[File] = {
    starting_dir
      .toFile
      .listRecursively
      .filterNot(f => f.path.toString.contains(".sgit"))
      .toSeq
  }

  /**
  * Yield all files matching a regex
  * @param glob : Regex that is supposed to match with files
  */
  def yieldGlobFiles(glob: String, starting_dir: String = "."): Seq[File] = {
    val globres = starting_dir
      .toFile
      .glob(glob)
    val resfiltered = globres.filterNot(f => f.path.toString.contains(".sgit"))
    resfiltered.toSeq
  }
}