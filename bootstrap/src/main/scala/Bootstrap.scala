import java.io.File
import java.net.URLClassLoader
import java.nio.file.Paths

/**
  * The bootstrap launcher downloads all required libraries and starts chat overflow with the correct parameters.
  */
object Bootstrap {

  // Working directory of the bootstrap launcher
  val currentFolderPath: String = Paths.get("").toAbsolutePath.toString

  // Java home path (jre installation folder)
  val javaHomePath: String = System.getProperty("java.home")

  // Chat Overflow Launcher / Main class (should not change anymore)
  val chatOverflowMainClass = "org.codeoverflow.chatoverflow.Launcher"

  val classloader = new URLClassLoader(
    new File("bin").listFiles().filter(_.getName.endsWith(".jar")).map(_.toURI.toURL)
  )

  /**
    * Software entry point
    *
    * @param args arguments for the launcher
    */
  def main(args: Array[String]): Unit = {
    println("ChatOverflow Bootstrap Launcher.")

    if (testValidity()) {
      println("Valid ChatOverflow installation. Checking libraries...")

      val deps = DependencyDownloader.fetchDependencies().map(u => new File(u.getFile))
      if (deps.nonEmpty) {
        val javaPath = createJavaPath()
        if (javaPath.isDefined) {
          println("Found java installation. Starting ChatOverflow...")

          // Start chat overflow!
          val command = List(javaPath.get, "-cp", s"bin/*${deps.mkString(File.pathSeparator, File.pathSeparator, "")}", chatOverflowMainClass) ++ args
          val process = new java.lang.ProcessBuilder(command: _*)
            .inheritIO().start()

          val exitCode = process.waitFor()
          println(s"ChatOverflow stopped with exit code: $exitCode")
        } else {
          println("Unable to find java installation. Unable to start.")
        }
      } else {
        println("Error: Problem with libraries. Unable to start.")
      }
    } else {
      println("Error: Invalid ChatOverflow installation. Please extract all provided files properly. Unable to start.")
    }
  }

  /**
    * Takes the java home path of the launcher and tries to find the java(.exe)
    *
    * @return the path to the java runtime or none, if the file was not found
    */
  private def createJavaPath(): Option[String] = {

    // Check validity of java.home path first
    if (!new File(javaHomePath).exists()) {
      None
    } else {

      // Check for windows and unix java versions
      // This should work on current and older java JRE/JDK installations,
      // see: https://stackoverflow.com/questions/52584888/how-to-use-jdk-without-jre-in-java-11
      val javaExePath = s"$javaHomePath/bin/java.exe"
      val javaPath = s"$javaHomePath/bin/java"

      if (new File(javaExePath).exists()) {
        Some(javaExePath)
      } else if (new File(javaPath).exists()) {
        Some(javaPath)
      } else {
        None
      }
    }

  }

  /**
    * Checks, if the installation is valid
    */
  private def testValidity(): Boolean = {
    // The only validity check for now is the existence of a bin folder
    new File(currentFolderPath + "/bin").exists()
  }
}
