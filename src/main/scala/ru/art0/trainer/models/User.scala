package ru.art0.trainer.models

case class User(id: Int, email: String, passwordHash: String, isActive: Boolean)

trait UserModel extends AbstractModel {

  import driverProfile._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")
    def passwordHash = column[String]("passwordHash")
    def isActive = column[Boolean]("is_active")

    def * = (id, email, passwordHash, isActive) <> (User.tupled, User.unapply)

    def emailIdx = index("idx_user__email", email, unique = true)
  }

  val users = TableQuery[UserTable]
}
