package sgit_app

import scopt.OParser
import sgit_app.commands._
import sgit_processes.init._
import sgit_processes.status._
import sgit_processes.diff._
import sgit_processes.add._
import sgit_processes.commit._
import sgit_processes.log._
import sgit_processes.branch._
import sgit_processes.checkout._
import sgit_processes.tag._
import sgit_processes.merge._
import sgit_processes.rebase._


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
      case "init"     => println(Init.sgit_init())
      case "status"   => println(Status.sgit_status())
      case "diff"     => println(Diff.sgit_diff())
      case "add"      => println(Add.sgit_add(config.files))
      case "commit"   => println(Commit.sgit_commit())
      case "log"      => println(Log.sgit_log(config.option))
      case "branch"   => println(Branch.sgit_branch(config.branch, config.option))
      case "checkout" => println(Checkout.sgit_checkout(config.checkout))
      case "tag"      => println(Tag.sgit_tag(config.tag))
      case "merge"    => println(Merge.sgit_merge(config.branch))
      case "rebase"   => println(Rebase.sgit_rebase(config.branch, config.option))
      case _          => println("sgit: '" + config.command + "'is not a sgit command")
    }
  }
}