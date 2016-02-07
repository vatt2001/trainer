organization  := "ru.art0"

version       := "1.0"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.12"
  val sprayV = "1.3.3"
  val sprayJsonV = "1.3.2"
  Seq(
    "com.typesafe"        % "config"         % "1.3.0",
    "io.spray"            %% "spray-can"     % sprayV,
    "io.spray"            %% "spray-routing" % sprayV,
    "io.spray"            %% "spray-json"    % sprayJsonV,
    "io.spray"            %% "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %% "akka-actor"    % akkaV,
    "com.typesafe.akka"   %% "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %% "specs2-core"   % "2.3.11" % "test",
    "com.typesafe.slick"  %% "slick"         % "3.1.1",
    "org.slf4j"           % "slf4j-nop"      % "1.6.4"
  )
}

Revolver.settings
