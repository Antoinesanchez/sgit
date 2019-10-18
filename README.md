## SGIT

### Git, but it's in scala
This project is an exercise in which I try to implement some features of git in scala. The main goal is to practice using Functional Programming.

### How to use it
Simply clone the project and while in the project folder, type

`sbt assembly`
 
Doing so will compile the code, run the tests and create a jar. Please copy the path to the jar and add the following command to your `.bashrc` file:

`alias sgit='java -jar /path/to/the/generated.jar'`

This will let you use sgit as a command in your shell.
