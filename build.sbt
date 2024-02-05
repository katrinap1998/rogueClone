import scala.sys.process.*
import scala.language.postfixOps
import indigoplugin.{IndigoOptions, *}
import sbtindigo.SbtIndigo.autoImport.indigoOptions

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val roguelike =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin, SbtIndigo)
    .settings(
      indigoOptions :=
        IndigoOptions.defaults
          .withTitle("Roguelike")
          .withWindowSize(720, 480),
    )
    .settings(
      name         := "roguelike",
      version      := "0.0.1",
      scalaVersion := "3.3.1",
      organization := "roguelike",
      libraryDependencies ++= Seq(
        "org.scalameta" %%% "munit" % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
//      showCursor          := true,
//      title               := "Indigo Roguelike!",
//      gameAssetsDirectory := "assets",
//      windowStartWidth    := 80 * 10,
//      windowStartHeight   := 50 * 10,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo-json-circe"    % "0.16.0",
        "io.indigoengine" %%% "indigo"               % "0.16.0",
        "io.indigoengine" %%% "indigo-extras"        % "0.16.0",
        "io.indigoengine" %%% "roguelike-starterkit" % "0.5.1-SNAPSHOT"
      )
      // scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) } // required for parcel, but will break indigoRun & indigoBuild
    )
    .settings(
      code := { "code ." ! }
    )


// To use indigoBuild or indigoRun, first comment out the line above that says: `scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }`
addCommandAlias("runGame", ";compile;fastOptJS;indigoRun")
addCommandAlias("buildGame", ";compile;fastOptJS;indigoBuild")

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")
