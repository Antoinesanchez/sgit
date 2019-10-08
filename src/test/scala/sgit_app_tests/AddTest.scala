package sgit_app_tests

import org.scalatest._
import better.files._
import java.nio.file.{Files, Paths}
import sgit_app.io.Tools
import sgit_app.sgit_processes._

class AddTest extends FunSpec with BeforeAndAfter {

  before {
    Init.sgit_init()
    Tools.createDirOrFile(true, "test")
    Tools.createDirOrFile(false,"test/a.txt")
    Tools.createDirOrFile(false,"test/a.c")
    Tools.writeFile("test/a.txt", "This is a test in a text file")
    Tools.writeFile("test/a.c", "This is a test in a c file")
  }

  after {
    Tools.delete(".sgit")
    Tools.delete("test")
  }

  describe("If I add files with .") {
    it("Should add all files in the index file") {
      Add.sgit_add(Seq("."))
      assert(Files.readString(".sgit/index".toFile).contains("test/a.txt"))
      assert(Files.readString(".sgit/index".toFile).contains("test/a.c"))
    }
  }

  describe("If I add files with a regex") {
    it("Should add all files matching the regex in the index file") {
      Add.sgit_add(Seq("*.c"))
      assert(!Files.readString(".sgit/index".toFile).contains("test/a.txt"))
      assert(Files.readString(".sgit/index".toFile).contains("test/a.c"))
    }
  }

  describe("If I add files with their names") {
    it("Should add all matching files in the index file") {
      Add.sgit_add(Seq("a.c"))
      assert(!Files.readString(".sgit/index".toFile).contains("test/a.txt"))
      assert(Files.readString(".sgit/index".toFile).contains("test/a.c"))
    }
  }

}