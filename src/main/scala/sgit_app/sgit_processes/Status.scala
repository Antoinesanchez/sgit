package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Status {

  /**
  * Return current file status
  */
  def sgit_status(target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))) "fatal: not a sgit repository"
    else {
      "Changes not staged for commit:\n\n" + sgitTools.workingDirectoryStagedDeltas(target) + "\n\nChanges to be committed:\n\n" + sgitTools.stagedCommitDeltas(target)
    }
  }

}