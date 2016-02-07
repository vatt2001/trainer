package ru.art0.trainer.controllers

import ru.art0.trainer.services.TrainingService.TrainingResult
import ru.art0.trainer.services.TrainingServiceComponent
import spray.routing.HttpService
import scala.concurrent.ExecutionContext.Implicits.global
import spray.httpx.SprayJsonSupport._
import ru.art0.trainer.helpers.JsonProtocol._

trait TrainingController extends BaseController {

  this: HttpService with TrainingServiceComponent =>

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
              onSuccess(trainingService.submit(DefaultUserId, data)) { result =>
                complete("OK")
              }
            }
          }
      }
      path("stats") {
        get {
          onSuccess(trainingService.getStats(DefaultUserId)) {
            complete(_)
          }
        }
      }
    }
}
