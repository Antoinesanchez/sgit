package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects.Staged

object Commit {

  /**
  * Record changes to the repository
  */
  def sgit_commit(target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit")) || !Files.exists(Paths.get(target + ".sgit/objects"))) "fatal: not a sgit repository"
    val branch = "master" //TODO change when possible
    val ref = target + ".sgit/refs/heads/" + branch
    val objects = (target+".sgit/objects")
      .toFile
      .children
      .toList
    var index = ""
    if (Files.exists(Paths.get(target+".sgit/index"))) {
      index += (target+".sgit/index")
        .toFile
        .contentAsString
    }
    val hash = (target+".sgit/index")
      .toFile
      .sha1
      .toString
    val name = target + ".sgit/objects/" + hash
    if (objects.length == 0) {
      //Initial commit
      if (index.replaceAll(" ", "").replaceAll("\n", "").nonEmpty) {
        //index is not empty, changes to commit
        val commit = "0000000000000000000000000000000000000000 " + hash
        Tools.createDirOrFile(false, name)
        Tools.createDirOrFile(false, ref)
        Tools.writeFile(name, index)
        Tools.writeFile(ref, commit)
        "changes are commited"
      } else {
        //No commit and no index or nothing in it
        "Nothing to commit"
      }
    } else {
      val parentCommit = ref
        .toFile
        .contentAsString
        .split("\n")
        .last
        .split(" ")
        .last
      val commit = parentCommit + " " + hash
      Tools.createDirOrFile(false, name)
      Tools.writeFile(name, index)
      Tools.writeFile(ref, commit)
      "changes are commited"
    }
  }
}