package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Tag {

  /**
  * create a tag object signed with GPG
  */
  def sgit_tag(tag: String, target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))
    || !Files.exists(Paths.get(target + ".sgit/HEAD"))) "fatal: not a sgit repository"
    else {
      val branch = (target + ".sgit/HEAD")
        .toFile
        .contentAsString
        .split("/")
        .last
      val ref = target + ".sgit/refs/heads/" + branch
      val tagPath = target + ".sgit/refs/tags/" + tag
      if (!Files.exists(Paths.get(ref))) "fatal: Failed to resolve 'HEAD' as a valid ref."
      else if (Files.exists(Paths.get(tagPath))) "fatal: tag '" + tag + "' already exists"  
      else {
        val hash = ref
          .toFile
          .contentAsString
          .split(" ")
          .last
        Tools.createDirOrFile(false, tagPath)
        Tools.writeFile(tagPath, hash)
        ""
      }
    }
  }

}