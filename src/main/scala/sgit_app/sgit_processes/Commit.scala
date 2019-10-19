package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Commit {

  /**
  * Record changes to the repository
  */
  def sgit_commit(target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit")) || !Files.exists(Paths.get(target + ".sgit/objects"))) "fatal: not a sgit repository"
    val branch = {
      val fHEAD = target + ".sgit/HEAD"
      if (Files.exists(Paths.get(fHEAD))) 
        Files
          .readString(Paths.get(fHEAD))
          .split("/")
          .last
      else {
        Tools.createDirOrFile(false, fHEAD)
        Tools.writeFile(fHEAD, "ref: refs/heads/master")
        "master"
      }
    }
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
    val newObjects = index
      .split("\n")
      .map(line => line.split(" ").head)
      .filterNot(l => l == "")
    val name = target + ".sgit/objects/" + hash
    if (objects.length == 0) {
      //Initial commit
      if (index.replaceAll(" ", "").replaceAll("\n", "").nonEmpty) {
        //index is not empty, changes to commit
        val commit = "0000000000000000000000000000000000000000 " + hash + "\n"
        Tools.createDirOrFile(true, name)
        Tools.createDirOrFile(false, name + "/" + hash)
        Tools.createDirOrFile(false, ref)
        newObjects.foreach(o => {
          val newObject = name + "/" + o
          val newContent = o.toFile.contentAsString
          Tools.createDirOrFile(false, newObject)
          Tools.writeFile(newObject, newContent)
        })
        Tools.writeFile(ref, commit)
        Tools.writeFile(name + "/" + hash, index)
        "changes are committed"
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
      Tools.createDirOrFile(true, name)
      Tools.createDirOrFile(false, name + "/" + hash)
      newObjects.foreach(o => {
        val newObject = name + "/" + o
        val newContent = o.toFile.contentAsString
        Tools.createDirOrFile(false, newObject)
        Tools.writeFile(newObject, newContent)
      })
      Tools.writeFile(ref, commit)
      Tools.writeFile(name + "/" + hash, index)
      "changes are commited"
    }
  }
}