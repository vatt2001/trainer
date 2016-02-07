package ru.art0.trainer

import akka.actor.{ActorContext, Actor}
import ru.art0.trainer.ComponentWiring.{TrainingServiceComponentImpl, WordsServiceComponentImpl}
import ru.art0.trainer.controllers.{TrainingController, WordsController}
import ru.art0.trainer.services.WordsService.AddWordData
import ru.art0.trainer.services.WordsServiceComponent
import spray.routing._
import spray.http._
import MediaTypes._


class TrainerApiActor extends Actor with TrainerApiService {

  def actorRefFactory = context

  def receive = runRoute(route)

}


trait TrainerApiService extends HttpService {

  val actorContext = actorRefFactory

  trait ActorContextHolderImpl {
    def actorRefFactory = actorContext
  }

  val wordsController =
    new WordsController
      with HttpService
      with WordsServiceComponentImpl
      with ActorContextHolderImpl
  
  val trainingController =
    new TrainingController
      with HttpService
      with TrainingServiceComponentImpl
      with ActorContextHolderImpl


  val route =
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
        wordsController.route ~
        trainingController.route
      } ~
      getFromResourceDirectory("../public")
    }
}