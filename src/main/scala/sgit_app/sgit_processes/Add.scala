package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects.Staged

object Add {

  /**
  * Add file contents to the index
  * TODO Deal with delete
  */
  def sgit_add(files: Seq[String], target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))
    || !Files.exists(Paths.get(target + ".sgit/HEAD"))) "fatal: not a sgit repository"
    else {
      if(!Files.exists(Paths.get(target + ".sgit/index"))) Tools.createDirOrFile(false, target + ".sgit/index")
      var index: List[String] = Files.readString(Paths.get(target + ".sgit/index"))
        .replaceAll("\r", "")
        .split("\n")
        .toList
      var emptyFileSeq: Seq[File] = Seq()
      val filesToBeWritten: Seq[Staged] = files.foldLeft(emptyFileSeq)((f1, f2) => {
        f2 match {
          case "."  => f1 ++ Tools.yieldAllFiles(target)
          case _    => f1 ++ Tools.yieldGlobFiles(f2, target)
        }
      }).map(f => {
        sgitTools.createStaged(f)
      })
      index = index.filter(line => {
        filesToBeWritten.count(f => line.contains(f.fileName)) == 1
      })
      filesToBeWritten
        .foreach(f => {
        var staged = f.toString
        if (index.filter(line => line.contains(f.fileName)).size == 0) {
          index = index :+ staged
        }
        else if (index.filter(line => line.contains(f.contentHash)).size == 0) {
          index = index.filter(line => !line.contains(f.fileName))
          index = index :+ staged
        }
      })
      Tools.delete(target + ".sgit/index")
      Tools.createDirOrFile(false, target + ".sgit/index")
      var new_Index = index.foldLeft("")((res, line) => {
        res + line + "\n"
      })
      Tools.writeFile(target + ".sgit/index", new_Index)
      ""
    }
  }

}