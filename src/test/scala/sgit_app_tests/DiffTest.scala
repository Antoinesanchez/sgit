package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes._

class DiffTest extends FunSpec with BeforeAndAfter {

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

  describe("sgit diff") {

    describe("Before a commit") {
      
      it("shouldn't print anything") {
        assert(Diff.sgit_diff("test/") == "")
      }

    }

    describe("After a commit") {

      it("Shouldn't print anything if I don't update anything") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        assert(Diff.sgit_diff("test/") == "")
      }

      it("Should display a diff if something is updated") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        Tools.delete("test/a.txt")
        Tools.createDirOrFile(false,"test/a.txt")
        Tools.writeFile("test/a.txt", "This is being added for the sake of this test")
        assert(Diff.sgit_diff("test/").contains("+ This is being added for the sake of this test"))
        assert(Diff.sgit_diff("test/").contains("- This is a test in a text file"))
      }

      it("Should display a diff if something is deleted") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        Tools.delete("test/a.txt")
        assert(Diff.sgit_diff("test/").contains("- This is a test in a text file"))
      }

    }

  }

}