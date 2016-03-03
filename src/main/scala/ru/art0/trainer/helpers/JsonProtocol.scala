package ru.art0.trainer.helpers

import ru.art0.trainer.helpers.WordToStudyHelper.{WordToStudyStatus, WordToStudy}
import ru.art0.trainer.models.StudyMethod
import ru.art0.trainer.services.TrainingService._
import ru.art0.trainer.services.WordsService.{WordsList, AddWordData}
import spray.json._


object JsonProtocol extends DefaultJsonProtocol {
  implicit val studyMethodDataFormat = jsonEnum(StudyMethod)
  implicit val wordToStudyStatusDataFormat = jsonEnum(WordToStudyStatus)
  implicit val wordResponseDataFormat = jsonFormat7(WordToStudy)
  implicit val addWordDataFormat = jsonFormat2(AddWordData)
  implicit val wordsListDataFormat = jsonFormat1(WordsList)
  implicit val trainingNewDataFormat = jsonFormat1(TrainingNew)
  implicit val trainingAnswerFormat = jsonFormat2(TrainingAnswer)
  implicit val trainingResultFormat = jsonFormat1(TrainingResult)
  implicit val trainingStatsFormat = jsonFormat4(TrainingStats)
  implicit val idResponseFormat = jsonFormat1(IdResponse)
  implicit val simpleResponseFormat = jsonFormat2(SimpleResponse)

  def jsonEnum[T <: Enumeration](enu: T) = new JsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)

    def read(json: JsValue) = json match {
      case JsString(txt) => enu.withName(txt)
      case something => throw new DeserializationException(s"Expected a value from enum $enu instead of $something")
    }
  }

  case class SimpleResponse(success: Boolean, error: Option[String] = None)

  case class IdResponse(id: Int)
}
