package ru.art0.trainer.controllers

import ru.art0.trainer.services.WordsService.{AddWordData}
import ru.art0.trainer.services.WordsServiceComponent
import spray.http.MediaTypes._
import spray.routing.HttpService
import scala.concurrent.ExecutionContext.Implicits.global
import spray.httpx.SprayJsonSupport._
import ru.art0.trainer.helpers.JsonProtocol._
import spray.routing.PathMatchers.IntNumber

trait WordsController extends BaseController {

  this: HttpService with WordsServiceComponent =>

  val route =
    pathPrefix("words") {
      get {
        onSuccess(wordsService.getWords(DefaultUserId)) { data =>
          complete(data)
        }
      } ~
      post {
        entity(as[AddWordData]) { data =>
          onSuccess(wordsService.addWord(DefaultUserId, data)) { result =>
            complete(IdResponse(result))
          }
        }
      } ~
      path(IntNumber) { wordStudyId =>
        put {
          onSuccess(wordsService.repeatWord(wordStudyId)) { result =>
            complete("OK")
          }
        } ~
        delete {
          onSuccess(wordsService.deleteWord(wordStudyId)) { result =>
            complete("OK")
          }
        }
      }
    }
}
