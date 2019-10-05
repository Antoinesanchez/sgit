package sgit_app.commands

import java.io.File

case class Config(
    command: String = "",
    option: String = "",
    branch: String = "",
    checkout: String = "",
    tag: String = "",
    files: Seq[String] = Seq()){}