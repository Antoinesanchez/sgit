package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes._

class StatusTest extends FunSpec with BeforeAndAfter {

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

  describe("sgit status") {
    describe("With no commit yet") {

      it("should display files as untracked if not staged yet") {
        val output = Status.sgit_status("test/")
        assert(output.contains("Untracked Files:\n\n"))
        assert(output.split("Untracked Files:").last.contains("test/a.txt"))
        assert(output.split("Untracked Files:").last.contains("test/a.c"))
      }

      it("should display files as new files if added to the index and no longer as untracked files") {
        Add.sgit_add(Seq("."), "test/")
        assert(Status.sgit_status("test/").contains("new file: test/a.txt"))
        assert(Status.sgit_status("test/").contains("new file: test/a.c"))
        assert(!Status.sgit_status("test/").contains("Untracked Files"))
      }

      it("should display files as modified if modified") {
        Add.sgit_add(Seq("."), "test/")
        Tools.writeFile("test/a.txt", "here I am with a status test")
        assert(Status.sgit_status("test/").contains("new file: test/a.txt"))
        assert(Status.sgit_status("test/").contains("new file: test/a.c"))
        assert(Status.sgit_status("test/").contains("modified: test/a.txt"))
      }

      it("should display files as deleted if deleted") {
        Add.sgit_add(Seq("."), "test/")
        Tools.delete("test/a.txt")
        assert(Status.sgit_status("test/").contains("deleted: test/a.txt"))
      }

    }

    describe("With an existing commit") {
      //This test is the origin of my version of status
      it("should display all files as deleted in changes to commit and all files as untracked if I delete the index") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("","","test/")
        Tools.delete("test/.sgit/index")
        val output = Status.sgit_status("test/")
        assert(output.contains("Changes to be committed:"))
        assert(output.contains("deleted: test/a.txt"))
        assert(output.contains("deleted: test/a.c"))
        assert(output.contains("Untracked Files:"))
        assert(output.split("Untracked Files:").last.contains("test/a.txt"))
        assert(output.split("Untracked Files:").last.contains("test/a.c"))
      }

    }

  }
}