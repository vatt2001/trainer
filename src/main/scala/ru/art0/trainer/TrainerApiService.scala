package ru.art0.trainer

import akka.actor.Actor
import akka.event.Logging
import ru.art0.trainer.ComponentWiring.{TrainingServiceComponentImpl, WordsServiceComponentImpl}
import ru.art0.trainer.components.ExecutionContextComponentImpl
import ru.art0.trainer.controllers.{TrainingController, WordsController}
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
      with ExecutionContextComponentImpl
      with ActorContextHolderImpl

  val trainingController =
    new TrainingController
      with HttpService
      with TrainingServiceComponentImpl
      with ExecutionContextComponentImpl
      with ActorContextHolderImpl


  val route = logRequestResponse("TRAINER", Logging.InfoLevel) {
    respondWithMediaType(`application/json`) {
      pathPrefix("api") {
        pathPrefix("user") {
          complete("Not implemented")
        } ~
        wordsController.route ~
        trainingController.route
      }
    } ~
      pathEndOrSingleSlash {
        getFromFile("src/main/public/index.html")
      } ~
      getFromResourceDirectory("../public") ~
      getFromDirectory("src/main/public") // TODO: move to config
  }
}