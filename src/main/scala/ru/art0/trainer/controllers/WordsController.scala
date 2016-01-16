package ru.art0.trainer.controllers

import ru.art0.trainer.ServiceLocator
import ru.art0.trainer.services.WordsService.AddWordData
import spray.http.MediaTypes._
import spray.routing.HttpService
import scala.concurrent.ExecutionContext.Implicits.global
import ru.art0.trainer.helpers.JsonProtocol._
import spray.routing.PathMatchers.IntNumber

trait WordsController extends BaseController {

  this: HttpService =>

  val wordsRoute =
    pathPrefix("words") {
      get {
        onSuccess(ServiceLocator.getWordService.getWords(DefaultUserId)) {
          complete(_)
        }
      } ~
      post {
        entity(as[AddWordData]) { data =>
          onSuccess(ServiceLocator.getWordService.addWord(DefaultUserId, data)) {
            complete(_)
          }
        }
      } ~
      path(IntNumber) { wordStudyId =>
        put {
          onSuccess(ServiceLocator.getWordService.repeatWord(wordStudyId)) {
            complete(_)
          }
        } ~
          delete {
            onSuccess(ServiceLocator.getWordService.deleteWord(wordStudyId)) {
              complete(_)
            }
          }
      }
    }
}
