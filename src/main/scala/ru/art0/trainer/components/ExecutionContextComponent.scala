package ru.art0.trainer.components

import scala.concurrent.ExecutionContext

trait ExecutionContextComponent {
  implicit def executionContext: ExecutionContext
}

trait ExecutionContextComponentImpl extends ExecutionContextComponent {
  override def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}