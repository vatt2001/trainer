package ru.art0.trainer.controllers

import ru.art0.trainer.components.ExecutionContextComponent
import ru.art0.trainer.services.TrainingService.TrainingResult
import ru.art0.trainer.services.TrainingServiceComponent
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import ru.art0.trainer.helpers.JsonProtocol._

trait TrainingController extends BaseController {

  this: HttpService with TrainingServiceComponent with ExecutionContextComponent =>

  val route =
    pathPrefix("training") {
      pathEndOrSingleSlash {
        get {
          onSuccess(trainingService.getNew(DefaultUserId)) {
            complete(_)
          }
        } ~
          post {
            entity(as[TrainingResult]) { data =>
              onSuccess(trainingService.submit(DefaultUserId, data)) {
                complete(_)
              }
            }
          }
      } ~
      path("stats") {
        get {
          onSuccess(trainingService.getStats(DefaultUserId)) {
            complete(_)
          }
        }
      }
    }
}
