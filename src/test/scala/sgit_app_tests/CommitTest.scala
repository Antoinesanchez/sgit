package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes._

class CommitTest extends FunSpec with BeforeAndAfter {
  
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

  describe("With no staged files") {
    it("should return nothing to commit") {
      assert(Commit.sgit_commit("test/").contains("Nothing to commit"))
    }
  }

  describe("With staged files") {

    describe("And no initial commit") {
      it("Should create a branch named master that references the commit") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        val objects = "test/.sgit/objects"
        .toFile
        .children
        val index = "test/.sgit/index"
          .toFile
          .sha1
          .toString
        val commit = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
        assert(
          "test/.sgit/refs/heads/master"
          .toFile
          .exists
          )
        assert(
          "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
          .contains(index)
          )
      }

      it("Should create a commit in the .sgit/objects folder") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        val objects = "test/.sgit/objects"
        .toFile
        .children
        .toList
        val index = "test/.sgit/index"
          .toFile
          .sha1
          .toString
        val commit = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
        assert(objects.length == 1)
        assert(objects.filter(f => f.name == index).length == 1)
      }
    }

    describe("And an initial commit") {

      it("Should reference the previous commit as a parent of the new one") {
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        val commit = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
          .split(" ")
          .head
        Tools.createDirOrFile(false,"test/a.md")
        Tools.createDirOrFile(false,"test/a.cpp")
        Add.sgit_add(Seq("."), "test/")
        Commit.sgit_commit("test/")
        val objects = "test/.sgit/objects"
        .toFile
        .children
        val index = "test/.sgit/index"
          .toFile
          .sha1
          .toString
        assert(
          "test/.sgit/refs/heads/master"
          .toFile
          .exists
          )
        val commit2 = "test/.sgit/refs/heads/master"
          .toFile
          .contentAsString
          .split("\n")
          .last
          .split(" ")
          .head
        assert(commit2 == commit)
      }
    }
  }
}