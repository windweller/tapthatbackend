package com.tapthat.api

import akka.actor.Props
import com.tapthat.api.AbstractSystem
import com.tapthat.tasks._

//Here is where we build up all actors
trait MainActors {
  this: AbstractSystem =>

  lazy val processActor = system.actorOf(Props[ProcessActor], "ProgressActor")

}