package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Branch {

  /**
  * List or create branches
  */
  def sgit_branch(branchName: String = "", option: String = "", target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))
    || !Files.exists(Paths.get(target + ".sgit/HEAD"))) return "fatal: not a sgit repository"
    if((branchName == "" && option == "")
    || (branchName != "" && option != "")
    || (option != "" && option != "av")) return "sgit branch\nusage:\nsgit branch <branch name>\nsgit branch -av"
    val branch = (target + ".sgit/HEAD")
      .toFile
      .contentAsString
      .split("/")
      .last
    val ref = target + ".sgit/refs/heads/" + branch
    if(!Files.exists(Paths.get(ref))) return "fatal: Not a valid object name: '" + branch + "'."
    if (branchName != "") {
      val branchFile = target + ".sgit/refs/heads/" + branchName
      if(Files.exists(Paths.get(branchFile))) return "fatal: A branch named '" + branchName + "' already exists"
      val parentHash = ref
        .toFile
        .contentAsString
        .split(" ")
        .last
      val commit = "0000000000000000000000000000000000000000 " + parentHash + "\n"
      Tools.createDirOrFile(false, branchFile)
      Tools.writeFile(branchFile, commit)
      return ""
    } else {
      val branchList = (target + ".sgit/refs/heads")
        .toFile
        .children
        .toList
        .sortWith(_.name < _.name)
        .map(child => {
          if (child.name == branch) "* " + child.name
          else "  " + child.name
        })
        .foldLeft("branches:\n")((res, line) => res + line + "\n")
      val tagList = (target + ".sgit/refs/tags")
        .toFile
        .children
        .toList
        .sortWith(_.name < _.name)
        .map(child => "  " + child.name)
        .foldLeft("tags:\n")((res, line) => res + line + "\n")
      return branchList + tagList
    }

  }

}