package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes.{Tag => STag, _}

class TagTest extends FunSpec with BeforeAndAfter {

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

  describe("sgit tag") {

    describe("before a commit") {

      it("Should throw an error") {
        assert(STag.sgit_tag("tag", "test/") == "fatal: Failed to resolve 'HEAD' as a valid ref.")
      }
    
    }

    describe("after a commit") {

      it("Should create the tag succesfully if not already used") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        STag.sgit_tag("tag", "test/")
        val lastCommitHash = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
          .split(" ")
          .last
        assert(Files.exists(Paths.get("test/.sgit/refs/tags/tag")))
        assert("test/.sgit/refs/tags/tag".toFile.contentAsString == lastCommitHash)
      }

      it("Shouldn't allow a user to create the same tag twice") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        assert(STag.sgit_tag("tag", "test/") == "")
        assert(STag.sgit_tag("tag", "test/") == "fatal: tag 'tag' already exists")
      }

    }
  
  }
      
}