package sgit_app.sgit_processes.init

import sgit_app.io._
import java.nio.file.{Files, Paths}

object Init {
  
  /**
  * create and initialize .sgit repository in current folder
  */
  def sgit_init(): String = {
    val ok = Initialization.createSgitDirectory()
    if (!(Files.readString(Paths.get(".sgit/HEAD")) == "ref: refs/heads/master"))
      Tools.writeFile(".sgit/HEAD", "ref: refs/heads/master")
    if (ok) "Initialised empty sGit repository in " + Paths.get(".").toAbsolutePath + ".sgit"
    else "Reinitialised existing sGit repository in " + Paths.get(".").toAbsolutePath + ".sgit"
  }

}