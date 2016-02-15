package ru.art0.trainer.components

import ru.art0.trainer.models._

import scala.concurrent.Future

trait DaoComponent {

  def dao: Dao
}

trait Dao {

  def getWords: Future[Seq[Word]]
  def getWordById(id: Int): Future[Option[Word]]
  def upsertWord(word: Word): Future[Option[Int]]
  def deleteWord(wordId: Int): Future[Boolean]

  def getWordStudiesByUserId(userId: Int): Future[Seq[WordStudy]]
  def upsertWordStudy(wordStudy: WordStudy): Future[Option[Int]]
}

class DaoImpl extends Dao {

  this: DatabaseComponent
    with ExecutionContextHolderComponent
    with WordModel
    with UserModel
    with WordStudyModel =>

  import driverProfile._

  implicit val ec = executionContextHolder.context

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

  override def getWordStudiesByUserId(userId: Int): Future[Seq[WordStudy]] = {
    database.run(
      wordStudies.filter(_.userId === userId).result
    )
  }

  override def upsertWordStudy(wordStudy: WordStudy): Future[Option[Int]] = {
    database.run(
      (wordStudies returning wordStudies.map(_.id)).insertOrUpdate(wordStudy)
    )
  }
}
