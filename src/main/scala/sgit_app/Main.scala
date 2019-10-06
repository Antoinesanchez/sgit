package sgit_app

import scopt.OParser
import sgit_app.commands._
import sgit_processes.init._

object Main extends App {

  /**
  * Parse the input from the user
  */
  OParser.parse(Parser.parser, args, Config()) match {
    case Some(config) => configParse(config)
    case None => println("Something went wrong, try again")
  }

  /**
  * Check the input and redirect to the corresponding command
  * @param config : object encapsulating the config for next command
  */
  def configParse(config: Config): Unit = {
    config.command match {
      case "init"     => Init.sgit_init()
      // case "status"   => Status.sgit_status()
      // case "diff"     => Diff.sgit_diff()
      // case "add"      => Add.sgit_add(config.files)
      // case "commit"   => Commit.sgit_commit()
      // case "log"      => Log.sgit_log(config.option)
      // case "branch"   => Branch.sgit_branch(config.branch, config.option)
      // case "checkout" => Checkout.sgit_checkout(config.checkout)
      // case "tag"      => Tag.sgit_tag(config.tag)
      // case "merge"    => Merge.sgit_merge(config.branch)
      // case "rebase"   => Rebase.sgit_rebase(config.branch, config.option)
      case _          => println("sgit: '" + config.command + "'is not a sgit command")
    }
  }
}