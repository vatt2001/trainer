package ru.art0.trainer.models

case class Word(id: Int, spelling: String, translation: String, transcription: String)

trait WordModel extends AbstractModel {

  import databaseProfile.simple._

  class WordTable(tag: Tag) extends Table[Word](tag, "word") {
    def id = column[Int]("id", O.PrimaryKey)
    def spelling = column[String]("spelling")
    def translation = column[String]("translation")
    def transcription = column[String]("transcription")

    def * = (id, spelling, translation, transcription) <> (Word.tupled, Word.unapply)

    def spellingIdx = index("idx_word__spelling", spelling, unique = false)
  }

  val words = TableQuery[Word]
}



