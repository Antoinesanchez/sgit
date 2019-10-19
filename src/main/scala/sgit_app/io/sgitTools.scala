package sgit_app.io

import better.files._
import sgit_app.sgit_objects.Staged
import java.nio.file.{Files, Paths}

object sgitTools {

  /**
  * Returns status between the working directory and staged files
  */
  def workingDirectoryStagedDeltas(target: String = ""): String = {
    var untracked: Seq[String] = Seq()
    var modified: Seq[String] = Seq()
    var deleted: Seq[String] = Seq()
    val wd = Tools
      .yieldAllFiles(target)
      .map(f => createStaged(f))
      .toList
    val index: List[String] = yieldIndexIfExists(target)
    wd.foreach(file => {
      if (index.filter(f => f.contains(file.fileName)).size == 0) {
        //Not contained in index => untracked
        untracked = untracked :+ file.fileName
      } else if (index.filter(f => f.contains(file.contentHash)).size == 0) {
        //Contained in index but different hash => modified
        modified = modified :+ file.fileName
      }
    })
    index.foreach(line => {
      val fileName = line.split(" ").head
      if (wd.filter(f => f.fileName.contains(fileName)).size == 0) {
        //File no longer in working directory => deleted
        deleted = deleted :+ fileName
      }
    })
    var res = ""
    modified
      .map(f => "modified: " + f + "\n")
      .foreach(f => res += f)
    deleted
      .map(f => "deleted: " + f + "\n")
      .foreach(f => res += f)
    if(untracked.size > 0) res += "\nUntracked Files:\n\n"
    untracked
      .map(f => f + "\n")
      .foreach(f => res += f)
    res
  }

  /**
  * Returns deltas between the index and the last commit
  */
  def stagedCommitDeltas(target: String = ""): String = {
    var newFiles: Seq[String] = Seq()
    var modified: Seq[String] = Seq()
    var deleted: Seq[String] = Seq()
    val index: List[String] = yieldIndexIfExists(target) 
    val commit: List[String] = yieldLastCommitIfExists(target)
    index.foreach(line => {
      val file = line.split(" ").head
      val hash = line.split(" ").last
      if (commit.filter(f => f.contains(file)).size == 0) {
        //new file
        newFiles = newFiles :+ file
      } else if (commit.filter(f => f.contains(hash)).size == 0) {
        //modified file
        modified = modified :+ file
      }
    })
    commit.foreach((line: String) => {
      val file = line.split(" ").head
      if(index.filter(f => f.contains(file)).size == 0) {
        //deleted file
        deleted = deleted :+ file
      }
    })
    var res = ""
    newFiles
      .map(f => "new file: " + f + "\n")
      .foreach(f => res += f)
    modified
      .map(f => "modified: " + f + "\n")
      .foreach(f => res += f)
    deleted
      .map(f => "deleted: " + f + "\n")
      .foreach(f => res += f)
    res
  }

  /**
  * Yields the list of file names in which changes have been detected since last commit
  */
  def workingDirectoryCommitDeltas(target: String = ""): List[String] = {
    var res = List("")
    val lastCommit = yieldLastCommitIfExists(target)
    val wd = Tools
      .yieldAllFiles(target)
      .map(f => createStaged(f))
      .toList
    if (lastCommit == List("")) lastCommit
    else { 
      lastCommit.foreach(f => {
        val file = f.split(" ").head
        val hash = f.split(" ").last
        if(wd.filter(f => f.fileName.contains(file)).size == 0) {
          //File in last commit was deleted
          res = res :+ file
        }
        else if (wd.filter(f => f.contentHash == hash).size == 0) {
          //File in working directory is different
          res = res :+ file
        }
      })
      res.filterNot(f => f == "")
    }
  }

  /**
  * Yield different files from 2 commits
  */
  def commitCommitDeltas(hash1: String, hash2: String, target: String = ""): List[String] = {
    val path1 = target + ".sgit/objects/" + hash1 + "/" + hash1
    val path2 = target + ".sgit/objects/" + hash2 + "/" + hash2
    if(!Files.exists(Paths.get(path1)) && !Files.exists(Paths.get(path2))) Nil
    else {
      val files1 = path1
        .toFile
        .contentAsString
        .split("\n")
        .filterNot(line => line == "")
        .map(line => line.split(" ").head)
        .toSeq
      if (hash2 == "0000000000000000000000000000000000000000") files1.toList
      else {
        val files2 = path2
          .toFile
          .contentAsString
          .split("\n")
          .filterNot(line => line == "")
          .map(line => line.split(" ").head)
          .toSeq
        val seq1 = files1
          .diff(files2)
          .toList
        val seq2 = files2
          .diff(files1)
          .toList
        (seq1 ++ seq2)
      }
    }
  }

  def commitDiff(fileName: String, hash1: String, hash2: String, target: String = ""): String = {
    val path1 = target + ".sgit/objects/" + hash1 + "/" + fileName
    val path2 = target + ".sgit/objects/" + hash2 + "/" + fileName
    if(!Files.exists(Paths.get(path1)) && !Files.exists(Paths.get(path2))) fileName + ": an error as occured"
    else {
      val file1 = {
        if (Files.exists(Paths.get(path1))) {
          path1
            .toFile
            .contentAsString
            .split("\n")
            .toSeq
        } else Seq()
      }
      val file2 = {
        if(Files.exists(Paths.get(path2)))
          path2
            .toFile
            .contentAsString
            .split("\n")
            .toSeq
        else Seq()
      }
      val diff1 = file1
        .diff(file2)
        .map(line => Tools.greenWrapper("+ " + line + "\n"))
        .toList
      val diff2 = file2
        .diff(file1)
        .map(line => Tools.redWrapper("- " + line + "\n"))
        .toList
      val res = (diff1 ++ diff2)
        .map(line => line.toString)
        .foldLeft("")((text, line) => text + line)
      fileName + ":\n" + res
    }
  }

  def fileDiff(fileName: String, target: String = ""): List[String] = {
    val objectPath = target + ".sgit/objects/" + yieldLastCommitHashIfExists(target) + "/" + fileName
    if (!Files.exists(Paths.get(fileName))) {
      //file no longer in working directory
      if (!Files.exists(Paths.get(objectPath))) {
        List("Error, this isn't supposed to happen")
      } else {
        val flag: String = "Deleted: " + fileName + "\n"
        val res: List[String] = Files
          .readString(Paths.get(objectPath))
          .split("\n")
          .map(line => Tools.redWrapper("- " + line + "\n"))
          .map(line => line.toString) //Somehow, the compilator doesn't believe me when I tell it that the elements of this List are String
          .toList
        flag +: res
      }
    } else {
      //file in directory, we need to calculate the diff
      var res: List[String] = List()
      val currentFile = Files
        .readString(Paths.get(fileName))
        .split("\n")
        .toSeq
      val commitFile = Files
        .readString(Paths.get(objectPath))
        .split("\n")
        .toSeq
      val currentDiffCommit = currentFile
        .diff(commitFile)
        .map(line => Tools.greenWrapper("+ " + line + "\n"))
        .toList
      val commitDiffCurrent = commitFile
        .diff(currentFile)
        .map(line => Tools.redWrapper("- " + line + "\n"))
        .toList
      res = res ++ currentDiffCommit ++ commitDiffCurrent
      val flag: String = "Updated: " + fileName + "\n"
      (flag +: res)
        .map(line => line.toString) //Somehow, the compilator doesn't believe me when I tell it that the elements of this List are String
        .toList
    }
  }

  /**
  * Return list of commits on branch
  */
  def yieldCommits(ref: String): List[String] = {
    Files
      .readString(Paths.get(ref))
      .split("\n")
      .toList
      .reverse
  }

  /**
  * Return list of lines in .sgit/index if it exists
  */
  def yieldIndexIfExists(target: String = ""): List[String] = {
    if(Files.exists(Paths.get(target + ".sgit/index"))) {
      Files.readString(Paths.get(target + ".sgit/index"))
      .replaceAll("\r", "")
      .split("\n")
      .toList
    } else List("")
  }


  def yieldLastCommitHashIfExists(target: String = ""): String = {
    if (Files.exists(Paths.get(target + ".sgit/refs/heads/master"))) {
      Files.readString(Paths.get(target + ".sgit/refs/heads/master"))
        .replaceAll("\r", "")
        .split("\n")
        .last
        .split(" ")
        .last
    } else ""
  }

  /**
  * Return list of lines in the last commit's copy of index if it exists
  */
  def yieldLastCommitIfExists(target: String = ""): List[String] = {
    if (Files.exists(Paths.get(target + ".sgit/refs/heads/master"))) {
      val commitHash = yieldLastCommitHashIfExists(target)
      Files.readString(Paths.get(target + ".sgit/objects/" + commitHash + "/" + commitHash))
        .replaceAll("\r", "")
        .split("\n")
        .toList
    } else List("")
  }

  /**
  * Create and return a Staged object encapsulating a hash of the content of the file as well as its name
  * @param file : given file
  */
  def createStaged(file: File): Staged = {
    Staged(
      file.sha1.toString, file.pathAsString.replaceAll(Tools.workingDirectoryPath, ""))
  }

}