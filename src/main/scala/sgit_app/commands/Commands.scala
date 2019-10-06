package sgit_app.commands

import scopt.OParser

object Parser {
  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("sgit"),
      head("sgit"),
      cmd("init")
        .action((_, c) => c.copy(command = "init"))
        .text("Create an empty sGit repository or reinitialise an existing one"),
      cmd("status")
        .action((_, c) => c.copy(command = "status"))
        .text("Show the working tree status"),
      cmd("diff")
        .action((_, c) => c.copy(command = "diff"))
        .text("Show changes between commits, commit and working tree, etc"),
      cmd("add")
        .action((_, c) => c.copy(command = "add"))
        .text("Add file contents to the index")
        .children(
          arg[String]("<file>... or regex")
            .unbounded()
            .action((x, c) => c.copy(files = c.files :+ x))
            .text("Several files or a regex")
        ),
      cmd("commit")
        .action((_, c) => c.copy(command = "commit"))
        .text("Record changes to the repository"),
      cmd("log")
        .action((_, c) => c.copy(command = "log"))
        .text("Show commit logs")
        .children(
          opt[Unit]("p")
            .action((_, c) => c.copy(option = "p"))
            .text("Show changes overtime"),
          opt[Unit]("stat")
            .action((_, c) => c.copy(option = "stat"))
            .text("Show stats about changes overtime")
        ),
      cmd("branch")
        .action((_, c) => c.copy(command = "branch"))
        .text("Create a new branch")
        .children(
          arg[String]("<branch name>")
            .required()
            .action((x, c) => c.copy(branch = x))
            .text("Branch to be created"),
          opt[String]("")
            .abbr("av")
            .action((x, c) => c.copy(option = "av"))
            .text("List all existing branches and tags")
        ),
      cmd("checkout")
        .action((_, c) => c.copy(command = "checkout"))
        .text("Switch branches or restore working tree files")
        .children(
          arg[String]("<branch or tag or commit hash>")
            .required()
            .action((x, c) => c.copy(checkout = x))
            .text("Branch, tag or commit hash to switch to")
        ),
      cmd("tag")
        .action((_, c) => c.copy(command = "tag"))
        .text("Create a tag object signed with GPG")
        .children(
          arg[String]("<tag name>")
            .required()
            .action((x, c) => c.copy(tag = x))
            .text("Name of the tag to be created")
        ),
      cmd("merge")
        .action((_, c) => c.copy(command = "merge"))
        .text("Join two or more development histories together")
        .children(
          arg[String]("<branch>")
            .required()
            .action((x, c) => c.copy(branch = x))
            .text("Branch to merge with")
        ),
      cmd("rebase")
        .action((_, c) => c.copy(command = "rebase"))
        .text("Reapply commits on top of another base tip")
        .children(
          arg[String]("<branch>")
            .required()
            .action((x, c) => c.copy(branch = x))
            .text("Branch whose tip will have the current branch's commits reapplied"),
          opt[String]("i")
            .action((x, c) => c.copy(option = "i"))
            .text("Interactive rebase")
        ),
      checkConfig(
        c =>
          if (c.command == "") failure("You need to write a command")
          else success
      )
    )
  }
}