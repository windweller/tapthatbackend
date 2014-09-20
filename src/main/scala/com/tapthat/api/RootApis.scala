package com.tapthat.api


import akka.actor.{Props, ActorSystem}
import akka.event.Logging.InfoLevel
import spray.http.HttpRequest
import spray.http.StatusCodes.{MovedPermanently, NotFound }
import spray.httpx.encoding.Gzip
import spray.routing.PathMatchers.Rest
import spray.routing.{Directives, RouteConcatenation}
import spray.routing.directives.LogEntry
import java.io.File
import spray.http.MediaTypes._

trait AbstractSystem {
  implicit def system: ActorSystem
}

//the ReactiveApi trait, which defines the REST endpoints. In keeping with the structure exposed above,
//each endpoint has been kept in its own class. The ReactiveApi trait constructs the classes for these
// endpoints and then concatenates their respective routes.
trait RootApis extends RouteConcatenation with StaticRoute with AbstractSystem {
  this: MainActors =>

  val rootService = system.actorOf(Props(new RootService(routes)))

  lazy val routes = logRequest(showReq _) {
      staticRoute
  }

  private def showReq(req : HttpRequest) = LogEntry(req.uri, InfoLevel)
}

trait StaticRoute extends Directives {
  this: AbstractSystem =>

  lazy val staticRoute =
    pathEndOrSingleSlash {
      getFromFile(new File("views/home.html"), `text/html`)
    } ~
      path("css" / Rest) {fileName =>
        compressResponse(Gzip) {
          getFromFile(new File("views/css/"+fileName), `text/css`)
        }
      } ~
      path("js" / Rest) {fileName =>
        compressResponse(Gzip) {
          getFromFile(new File("views/js/" + fileName), `application/javascript`)
        }
      } ~
      path("img" / Rest) {fileName =>
        getFromFile(new File("views/img/"+fileName))
      } ~ complete(NotFound)
}