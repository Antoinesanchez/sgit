package sgit_app_tests

import org.scalatest._  
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes.{Tag => STag, _}

class BranchTest extends FunSpec with BeforeAndAfter {

  before {
    Tools.createDirOrFile(true, "test") 
    Init.sgit_init("test/")
    Tools.createDirOrFile(false,"test/a.txt")
    Tools.createDirOrFile(false,"test/a.c")
    Tools.writeFile("test/a.txt", "This is a test in a text file")
    Tools.writeFile("test/a.c", "This is a test in a c file")
  }

  after {
    Tools.delete("test")
  }

  describe("sgit branch") {

    describe("before a commit") {

      it("Should throw an error") {
        assert(Branch.sgit_branch("branch", "", "test/") == "fatal: Not a valid object name: 'master'.")
      }
    
    }

    describe("after a commit") {

      it("Should create the branch succesfully if not already used") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        Branch.sgit_branch("branch", "", "test/")
        val lastCommitHash = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
          .split(" ")
          .last
        assert(Files.exists(Paths.get("test/.sgit/refs/heads/branch")))
        assert("test/.sgit/refs/heads/branch".toFile.contentAsString.contains(lastCommitHash))
      }

      it("Shouldn't allow a user to create the same tag twice") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        assert(Branch.sgit_branch("branch", "", "test/") == "")
        assert(Branch.sgit_branch("branch", "", "test/") == "fatal: A branch named 'branch' already exists")
      }

      it("Should list branches and tags if the option is av") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        Branch.sgit_branch("branch", "", "test/")
        Branch.sgit_branch("zranch", "", "test/")
        STag.sgit_tag("tag", "test/")
        STag.sgit_tag("bag", "test/")
        assert(Branch.sgit_branch("", "av", "test/").contains("branches:\n  branch\n* master\n  zranch"))
        assert(Branch.sgit_branch("", "av", "test/").contains("tags:\n  bag\n  tag"))
      }

    }
  
  }
      
}