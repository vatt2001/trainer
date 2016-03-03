package ru.art0.trainer.components

import java.time.Instant

import ru.art0.trainer.models._
import ru.art0.trainer.services.TrainingService.TrainingStats

import scala.concurrent.Future

trait DaoComponent {

  def dao: Dao
}

trait Dao {

  def getWords: Future[Seq[Word]]
  def getWordById(id: Int): Future[Option[Word]]
  def upsertWord(word: Word): Future[Option[Int]]
  def deleteWord(wordId: Int): Future[Boolean]

  def getWordStudies(ids: Seq[Int]): Future[Seq[WordStudy]]
  def getWordsForUser(userId: Int, includeDeleted: Boolean = false): Future[Seq[(Word, WordStudy)]]
  def getWordsForTraining(userId: Int, limit: Int): Future[Seq[(Word, WordStudy)]]
  def getWordsForTrainingCount(userId: Int): Future[Int]
  def upsertWordStudy(wordStudy: WordStudy): Future[Option[Int]]
  def archiveWordStudy(wordStudyId: Int): Future[Int]

  def getWordStudyLearnedQty(userId: Int): Future[Seq[(Boolean, Int)]]
}

class DaoImpl extends Dao {

  this: DatabaseComponent
    with ExecutionContextComponent
    with WordModel
    with UserModel
    with WordStudyModel =>

  import driverProfile._

  override def getWords: Future[Seq[Word]] = {
    database.run(
      words.result
    )
  }

  override def deleteWord(wordId: Int): Future[Boolean] = {
    database.run(
      words.filter(_.id === wordId).delete
    ).map(_ == 1)
  }

  override def upsertWord(word: Word): Future[Option[Int]] = {
    database.run(
      (words returning words.map(_.id)).insertOrUpdate(word)
    )
  }

  override def getWordById(id: Int): Future[Option[Word]] = {
    database.run(
      words.filter(_.id === id).result.headOption
    )
  }

  override def getWordStudies(ids: Seq[Int]): Future[Seq[WordStudy]] = {
    database.run(
      wordStudies.filter(_.id inSet ids).result
    )
  }

  override def getWordsForUser(userId: Int, includeDeleted: Boolean = false): Future[Seq[(Word, WordStudy)]] = {
    val query =
      for {
        ws <- wordStudies if ws.userId === userId && (!ws.isArchived || includeDeleted)
        w <- words if w.id === ws.wordId
      } yield (w, ws)
    database.run(query.result)
  }

  override def getWordsForTraining(userId: Int, limit: Int): Future[Seq[(Word, WordStudy)]] = {
    database.run(
      getWordsForTrainingQuery(userId).sortBy(_._2.nextTrainingAt).take(limit).result
    )
  }

  override def getWordsForTrainingCount(userId: Int): Future[Int] = {
    database.run(
      getWordsForTrainingQuery(userId).length.result
    )
  }

  override def upsertWordStudy(wordStudy: WordStudy): Future[Option[Int]] = {
    database.run(
      (wordStudies returning wordStudies.map(_.id)).insertOrUpdate(wordStudy)
    )
  }

  override def archiveWordStudy(wordStudyId: Int): Future[Int] = {
    database.run(
      wordStudies.filter(_.id === wordStudyId).map(_.isArchived).update(true)
    )
  }

  override def getWordStudyLearnedQty(userId: Int): Future[Seq[(Boolean, Int)]] = {
    database.run(
      wordStudies.filter(_.userId === userId).groupBy(_.isLearned).map(q => (q._1, q._2.length)).result
    )
  }

  private def getWordsForTrainingQuery(userId: Int) = {
      for {
        ws <- wordStudies if ws.userId === userId && !ws.isArchived && !ws.isLearned && ws.nextTrainingAt <= Instant.now()
        w <- words if w.id === ws.wordId
      } yield (w, ws)
  }
}
