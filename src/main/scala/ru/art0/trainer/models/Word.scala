package ru.art0.trainer.models

case class Word(id: Int, spelling: String, translation: String, transcription: Option[String] = None)

trait WordModel extends AbstractModel {

  import driverProfile._

  class WordTable(tag: Tag) extends Table[Word](tag, "word") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def spelling = column[String]("spelling")
    def translation = column[String]("translation")
    def transcription = column[Option[String]]("transcription")

    def * = (id, spelling, translation, transcription) <> (Word.tupled, Word.unapply)

    def spellingIdx = index("idx_word__spelling", spelling, unique = false)
  }

  val words = TableQuery[WordTable]
}



