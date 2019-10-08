package sgit_app.io

import java.nio.file.{Files, Paths}
import better.files._

object Initialization {

  /**
  * Create .sgit directory or recreates it
  */
  def createSgitDirectory(): Boolean = {
    if(!Files.exists(Paths.get(".sgit"))) {
      Tools.createDirOrFile(true,  ".sgit")
      Tools.createDirOrFile(false, ".sgit/HEAD")
      Tools.createDirOrFile(true,  ".sgit/objects")
      Tools.createDirOrFile(true,  ".sgit/refs")
      Tools.createDirOrFile(false, ".sgit/refs/heads")
      Tools.createDirOrFile(false, ".sgit/refs/tags")
      Tools.createDirOrFile(false, ".sgit/index")
      true
    }
    else false
  }

}