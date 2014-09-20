package com.tapthat


import akka.actor.{Props, ActorSystem}
import akka.io.{IO, Tcp}
import com.tapthat.api.{RootApis, WsServer, MainActors}
import spray.can.Http
import com.tapthat.models.pgdb._

object Boot extends App with MainActors with RootApis {

  //construct database tables; it needs improvement
  implicit lazy val system = ActorSystem("mturk-survey")

  DAL.databaseInit()

  //wss is not working for some reason
  private val ws = new WsServer(Config.portWs)
  ws.forResource("/ws/script", Some(processActor))
  ws.start()

  sys.addShutdownHook({system.shutdown(); ws.stop()})

  IO(Http) ! Http.Bind(rootService, interface = Config.host, port = Config.portHTTP)
}

object Config {
  import com.typesafe.config.ConfigFactory

  private val config = ConfigFactory.load
  config.checkValid(ConfigFactory.defaultReference)

  val host = config.getString("service.host")
  val portHTTP = config.getInt("service.port")
  val portTcp = config.getInt("service.ports.tcp")
  val portWs = config.getInt("service.ports.ws")

  //database for both deployment and development
  val extractPattern = """(\/\/)(.+?):(.+?)@""".r

  val tempConnect = config.getString("db.postgresql.connect")
  val dbConnect = if (!tempConnect.contains("postgresql"))
    "jdbc:"+tempConnect.replace("postgres", "postgresql").replace("172.17.42.1", "104.131.7.141")
  else tempConnect

  val dbURL = extractPattern.replaceFirstIn(dbConnect, "//")
  val dbUser = for (m <- extractPattern.findFirstMatchIn(dbConnect)) yield m.group(2)
  val dbPassword = for (m <- extractPattern.findFirstMatchIn(dbConnect)) yield m.group(3)
  val dbDriver = config.getString("db.postgresql.driver")

  //for mturk-comapny task: load file location parameter
  val secFileLoc = config.getString("file.sec-file-loc")
}