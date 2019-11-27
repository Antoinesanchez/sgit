package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes._

class LogTest extends FunSpec with BeforeAndAfter {

  before {
    Tools.createDirOrFile(true, "test") 
    Init.sgit_init("test/")
    Tools.createDirOrFile(false,"test/a.txt")
    Tools.createDirOrFile(false,"test/a.c")
    Tools.writeFile("test/a.txt", "This is a test in a text file")
    Tools.writeFile("test/a.c", "This is a test in a c file")
    Add.sgit_add(Seq("."), "test/")
    Commit.sgit_commit("","","test/")
    Tools.delete("test/a.txt")
    Tools.createDirOrFile(false,"test/a.txt")
    Tools.writeFile("test/a.txt", "This is a sgit log test")
    Add.sgit_add(Seq("."), "test/")    
    Commit.sgit_commit("","","test/")
  }

  after {
    Tools.delete("test")
  }

  describe("sgit log") {

    it("Should display the name of commits in the right order if no option") {
      val commitList = Files
        .readString(Paths.get("test/.sgit/refs/heads/master"))
        .split("\n")
        .filterNot(line => line == "")
      val index1 = commitList
        .head
        .split(" ")
        .last
      val index2 = commitList
        .last
        .split(" ")
        .last
      val output = Log.sgit_log("", "test/")
      assert(output.contains("commit: " + index2))
      assert(output.split("commit: " + index2).last.contains("commit: " + index1))
    }

    it("Should display the name of commits as well as diffs if flag p is used") {
      assert(Log.sgit_log("p", "test/").contains("+ This is a sgit log test"))
      assert(Log.sgit_log("p", "test/").contains("- This is a test in a text file"))
    }

    // it("Should display the name of commits as well as stats if option stat is used") {
    //   assert(Log.sgit_log("stat", "test/").contains("test/a.txt"))
    //   assert(Log.sgit_log("stat", "test/").contains("1 file(s) changed, 1 insertion(s), 1 deletion(s)"))
    // }

  }

}