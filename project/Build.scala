import sbt._
import Keys._
import PlayProject._

import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {

    val appName         = "Reference database"
    val appVersion      = "1.0"
      
      lazy val s = Defaults.defaultSettings ++ Seq(jacoco.settings:_*)

    val appDependencies = Seq(
      // Add your project dependencies here,
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA, settings = s).settings(
    		// Add your own project settings here      
    		parallelExecution in jacoco.Config := false // ,
    		// jacoco.includes in jacoco.Config := Seq("*"),
    		// jacoco.excludes in jacoco.Config := Seq("*")
    )

}
