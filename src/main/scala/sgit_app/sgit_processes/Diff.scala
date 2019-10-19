package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Diff {

  /**
  * Show change between commits
  */
  def sgit_diff(target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))
    || !Files.exists(Paths.get(target + ".sgit/HEAD"))) "fatal: not a sgit repository"
    else if ((target+".sgit/refs/heads").toFile.children.toList.size == 0) ""
    else {
      sgitTools
        .workingDirectoryCommitDeltas(target)
        .flatMap(file => sgitTools.fileDiff(file, target))
        .foldLeft("")((res, line) => {
          res + line
        })
    }
  }

}