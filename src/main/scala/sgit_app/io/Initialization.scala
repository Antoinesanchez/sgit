package sgit_app.io

import java.nio.file.{Files, Paths}
import better.files._

object Initialization {

  /**
  * Create .sgit directory or recreates it
  */
  def createSgitDirectory(target: String): Boolean = {
    if(!Files.exists(Paths.get(target + ".sgit"))) {
      Tools.createDirOrFile(true,  target + ".sgit")
      Tools.createDirOrFile(false, target + ".sgit/HEAD")
      Tools.createDirOrFile(true,  target + ".sgit/objects")
      Tools.createDirOrFile(true,  target + ".sgit/refs")
      Tools.createDirOrFile(false, target + ".sgit/refs/heads")
      Tools.createDirOrFile(false, target + ".sgit/refs/tags")
      Tools.createDirOrFile(false, target + ".sgit/index")
      true
    }
    else false
  }

}