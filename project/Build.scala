import sbt._
import Keys._
import play.Project._

import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {

    val appName         = "ReferenceDatabase"
    val appVersion      = "1.0"
      
      lazy val s = Defaults.defaultSettings ++ Seq(jacoco.settings:_*)

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore, javaJdbc, javaEbean, "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
    )

    val main = play.Project(appName, appVersion, appDependencies, settings = s).settings(
    		// Add your own project settings here      
    		parallelExecution in jacoco.Config := false // ,
    		// jacoco.includes in jacoco.Config := Seq("*"),
    		// jacoco.excludes in jacoco.Config := Seq("*")
    )

}
