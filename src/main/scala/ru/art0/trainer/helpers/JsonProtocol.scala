package ru.art0.trainer.helpers

import ru.art0.trainer.models.{StudyMethod, WordStudy}
import ru.art0.trainer.services.TrainingService.{TrainingAnswer, TrainingStats, TrainingResult}
import ru.art0.trainer.services.WordsService.AddWordData
import spray.json._

object JsonProtocol extends DefaultJsonProtocol {
  implicit val studyMethodDataFormat = jsonEnum(StudyMethod)
  implicit val wordStudyDataFormat = jsonFormat6(WordStudy)
  implicit val addWordDataFormat = jsonFormat2(AddWordData)
  implicit val trainingAnswerFormat = jsonFormat2(TrainingAnswer)
  implicit val trainingResultFormat = jsonFormat1(TrainingResult)
  implicit val trainingStatsFormat = jsonFormat3(TrainingStats)

  def jsonEnum[T <: Enumeration](enu: T) = new JsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)

    def read(json: JsValue) = json match {
      case JsString(txt) => enu.withName(txt)
      case something => throw new DeserializationException(s"Expected a value from enum $enu instead of $something")
    }
  }
}
