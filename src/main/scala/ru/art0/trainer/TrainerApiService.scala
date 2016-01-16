package ru.art0.trainer

import akka.actor.Actor
import ru.art0.trainer.controllers.{TrainingController, WordsController}
import ru.art0.trainer.services.WordsService.AddWordData
import spray.routing._
import spray.http._
import MediaTypes._


class TrainerApiActor extends Actor with TrainerApiService {

  def actorRefFactory = context

  def receive = runRoute(myRoute)
}


trait TrainerApiService extends HttpService with WordsController with TrainingController {

  val myRoute =
    respondWithMediaType(`application/json`) {
      pathEndOrSingleSlash {
        get {
          complete("OK")
        }
      } ~
      pathPrefix("api") {
        pathPrefix("user") {
          complete("Not implemented")
        } ~
        pathPrefix("words") {
          wordsRoute
        } ~
        pathPrefix("training") {
          trainingRoute
        }
      } ~
      getFromResourceDirectory("../public")
    }
}