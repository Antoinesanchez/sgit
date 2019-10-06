package sgit_app.io

import better.files._
import java.nio.file.{Files, Paths}

object Tools {

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
}