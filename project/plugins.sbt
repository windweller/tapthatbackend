resolvers += Classpaths.typesafeResolver

resolvers +=  "sbt-idea" at "http://mpeltonen.github.com/maven"

addSbtPlugin( "com.typesafe.sbt" % "sbt-start-script" % "0.10.0")
//addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")
