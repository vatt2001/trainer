package ru.art0.trainer.models

import ru.art0.trainer.models.StudyMethod.StudyMethod

case class WordStudy(
  id: Int,
  userId: Int,
  wordId: Int,
  studyMethod: StudyMethod,
  trainingQtyTotal: Int = 0,
  trainingQtyCorrect: Int = 0
)

trait WordStudyModel extends AbstractModel {
  this: UserModel with WordModel =>

  import driverProfile._

  class WordStudyTable(tag: Tag) extends Table[WordStudy](tag, "word_study") {
    def id = column[Int]("id", O.PrimaryKey)
    def userId = column[Int]("user_id")
    def wordId = column[Int]("word_id")
    def studyMethodId = column[StudyMethod]("study_method_id")
    def trainingQtyTotal = column[Int]("training_qty_total")
    def trainingQtyCorrect = column[Int]("training_qty_correct")

    def * = (id, userId, wordId, studyMethodId, trainingQtyTotal, trainingQtyCorrect) <> (WordStudy.tupled, WordStudy.unapply)

    def wordStudyIdx = index("idx_word_study", studyMethodId, unique = false)
    def userIdIdx = index("idx_word_study__user_id", userId, unique = false)
    def wordIdIdx = index("idx_word_study__word_id", wordId, unique = false)
    def user = foreignKey("fk_word_study__user_id", userId, users)(_.id)
    def word = foreignKey("fk_word_study__word_id", wordId, words)(_.id)
  }

  val wordStudies = TableQuery[WordStudyTable]
}
