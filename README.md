SGIT
===

Git, but it's in scala
---

This project is an exercise in which I try to implement some features of git in scala. The main goal is to practice using Functional Programming.

How to use it
---

Before doing anything make sure you're using **JDK11** or **JDK12** or **JDK13**. This is due to the fact some methods were not available before JDK11.

Simply clone the project and while in the project folder, type

`sbt assembly`
 
Doing so will compile the code, run the tests and create a jar. Please copy the path to the jar and add the following command to your `.bashrc` file:

`alias sgit='java -jar /path/to/the/generated.jar'`

This will let you use sgit as a command in your shell when you log back in, but you can run `source ~/.bashrc` to make it work instantly.

Alternatively, you can just run `alias sgit='java -jar /path/to/the/generated.jar'` and it will run on your current shell.

