package ru.art0.trainer.services

trait AuthServiceComponent {

  def authService: AuthService

}


trait AuthService {

  def login(username: String, password: String): Option[String]

  def logout(sessionId: String): Boolean

}


class AuthServiceImpl extends AuthService {

  def login(username: String, password: String): Option[String] = throw new RuntimeException("Not implemented")

  def logout(sessionId: String): Boolean = throw new RuntimeException("Not implemented")

}