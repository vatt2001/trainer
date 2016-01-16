package ru.art0.trainer.controllers

import ru.art0.trainer.ServiceLocator
import ru.art0.trainer.services.TrainingService.TrainingResult
import spray.routing.HttpService
import scala.concurrent.ExecutionContext.Implicits.global
import ru.art0.trainer.helpers.JsonProtocol._

trait TrainingController extends BaseController {

  this: HttpService =>

  val trainingRoute =
    pathPrefix("training") {
      pathEndOrSingleSlash {
        get {
          onSuccess(ServiceLocator.getTrainingService.getNew(DefaultUserId)) {
            complete(_)
          }
        } ~
          post {
            entity(as[TrainingResult]) { data =>
              onSuccess(ServiceLocator.getTrainingService.submit(DefaultUserId, data)) {
                complete(_)
              }
            }
          }
      }
      path("stats") {
        get {
          onSuccess(ServiceLocator.getTrainingService.getStats(DefaultUserId)) {
            complete(_)
          }
        }
      }
    }
}
