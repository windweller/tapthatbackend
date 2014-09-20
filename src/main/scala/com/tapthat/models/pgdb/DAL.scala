package com.tapthat.models.pgdb

import akka.actor.ActorSystem
import com.tapthat.Config._

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

object DAL {

  val db = (dbUser, dbPassword) match {
    case (None, None) => Database.forURL(dbConnect, driver = dbDriver)
    case _ => Database.forURL(url = dbURL, user = dbUser.get, password = dbPassword.get, driver = dbDriver)
  }

  def databaseInit()(implicit system: ActorSystem) {

    db.withSession { implicit session =>

    }
  }
}
