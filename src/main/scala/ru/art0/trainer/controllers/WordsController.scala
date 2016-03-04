package ru.art0.trainer.controllers

import ru.art0.trainer.components.ExecutionContextComponent
import ru.art0.trainer.services.WordsService.AddWordData
import ru.art0.trainer.services.WordsServiceComponent
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport._
import ru.art0.trainer.helpers.JsonProtocol._

trait WordsController extends BaseController {

  this: HttpService with WordsServiceComponent with ExecutionContextComponent =>

  val route =
    pathPrefix("words") {
      get {
        onSuccess(wordsService.getWords(DefaultUserId)) {
          complete(_)
        }
      } ~
      post {
        entity(as[AddWordData]) { data =>
          onSuccess(wordsService.addWord(DefaultUserId, data)) {
            complete(_)
          }
        }
      } ~
      path(IntNumber) { wordStudyId =>
        put {
          onSuccess(wordsService.repeatWord(wordStudyId)) {
            complete(_)
          }
        } ~
        delete {
          onSuccess(wordsService.deleteWord(wordStudyId)) {
            complete(_)
          }
        }
      }
    }
}
