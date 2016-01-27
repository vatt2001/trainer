package ru.art0.trainer.models

import ru.art0.trainer.models.StudyMethod.StudyMethod

case class WordStudy(
  id: Int,
  userId: Int,
  wordId: Int,
  studyMethod: StudyMethod,
  trainingQtyTotal: Int,
  trainingQtyCorrect: Int
)

trait WordStudyModel extends AbstractModel {
  this: UserModel with WordModel =>

  import databaseProfile.simple._

  implicit lazy val employeeStatusColumnType = enumColumnType(EmployeeStatus)

  class WordStudyTable(tag: Tag) extends Table[WordStudy](tag, "word_study") {
    def id = column[Int]("id", O.PrimaryKey)
    def userId = column[Int]("user_id")
    def wordId = column[Int]("word_id")
    def studyMethod = column[StudyMethod]("study_method")
    def trainingQtyTotal = column[Int]("training_qty_total")
    def trainingQtyCorrect = column[Int]("training_qty_correct")

    def * = (id, userId, wordId, studyMethod, trainingQtyTotal, trainingQtyCorrect) <> (WordStudy.tupled, WordStudy.unapply)

    def wordStudyIdx = index("idx_word_study", (companyId, status), unique = false)
    def userIdIdx = index("idx_word_study__user_id", applicationId, unique = false)
    def wordIdIdx = index("idx_word_study__word_id", applicationId, unique = false)
    def user = foreignKey("fk_word_study__user_id", userId, users)(_.id)
    def word = foreignKey("fk_word_study__word_id", wordId, words)(_.id)
  }

  val wordStudies = TableQuery[WordStudy]
}
