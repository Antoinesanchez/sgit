package sgit_app.sgit_processes

import sgit_app.io._
import java.nio.file.{Files, Paths}

object Init {
  
  /**
  * create and initialize .sgit repository in current folder
  */
  def sgit_init(target: String = ""): String = {
    val ok = Initialization.createSgitDirectory(target)
    if (!(Files.readString(Paths.get(target + ".sgit/HEAD")) == "ref: refs/heads/master"))
      Tools.writeFile(target + ".sgit/HEAD", "ref: refs/heads/master")
    if (ok) "Initialised empty sGit repository in " + Paths.get(target).toAbsolutePath + "/.sgit"
    else "Reinitialised existing sGit repository in " + Paths.get(target).toAbsolutePath + "/.sgit"
  }

}