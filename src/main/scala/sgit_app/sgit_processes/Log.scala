package sgit_app.sgit_processes

import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io._
import sgit_app.sgit_objects._

object Log {

  /**
  * Show commit logs
  */
  def sgit_log(option: String = "", target: String = ""): String = {
    if(!Files.exists(Paths.get(target + ".sgit"))
    || !Files.exists(Paths.get(target + ".sgit/objects"))) "fatal: not a sgit repository"
    else {
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
      option match {
        case ""     => {
          if(!Files.exists(Paths.get(ref))) ""
          else {
            sgitTools
              .yieldCommits(ref)
              .map(line => "commit: " + line.split(" ").last + "\n")
              .foldLeft("")((res, line) => res + line)
          }
        }
        case "p"    => {
          if(!Files.exists(Paths.get(ref))) ""
          else {
            sgitTools
              .yieldCommits(ref)
              .map(line => {
                val hash1 = line.split(" ").last
                val hash2 = line.split(" ").head
                "commit: " + hash1 + "\n" + (sgitTools
                  .commitCommitDeltas(hash1, hash2, target)
                  .map(file => sgitTools.commitDiff(file, hash1, hash2, target))
                  .foldLeft("")((res, line) => res + line))
              })
              .foldLeft("")((res, line) => res + line)
          }
        }
        case "stat" => {
          "Not implemented yet"
        }
        case _ => "Unrecognized option, usage:\n  sgit log\n  sgit log -p\n  sgit log --stat"
      }
    }
  }

}