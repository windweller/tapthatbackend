package com.tapthat.tasks

import akka.actor.{ActorLogging, Actor}
import com.tapthat.api.WsServerMsg._

import org.java_websocket.WebSocket

import scala.collection.mutable.ListBuffer

class ProcessActor extends Actor with ActorLogging {

  import ProcessActorMsg._

  val clients = ListBuffer[WebSocket]()

  def receive = {
    case Open(ws, hs) =>
      clients += ws

    case Message(ws, msg) =>
      ws.send("got message!");

    case Close(ws, code, reason, ext) => self ! Unregister(ws)
    case Error(ws, ex) => self ! Unregister(ws)

    case Unregister(ws) =>
      if (null != ws) {
        log.debug("unregister monitor")
        clients -= ws
      }

  }
}

object ProcessActorMsg {
  case class Unregister(ws : WebSocket)
}