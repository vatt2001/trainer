package ru.art0.trainer.services

class AuthService {
  def login(username: String, password: String): Option[String] = ???

  def logout(sessionId: String): Boolean = ???
}
