package ru.art0.trainer.models

case class User(id: Int, email: String, passwordHash: String, isActive: Boolean)

trait UserModel extends AbstractModel {

  import databaseProfile.simple._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey)
    def email = column[Int]("email")
    def passwordHash = column[Int]("passwordHash")
    def isActive = column[Boolean]("is_active")

    def * = (id, email, passwordHash, isActive) <> (User.tupled, User.unapply)

    def emailIdx = index("idx_user__email", email, unique = true)
  }

  val users = TableQuery[User]
}
