package ru.art0.trainer.components

import scala.concurrent.ExecutionContext

trait ExecutionContextHolderComponent {
  def executionContextHolder: ExecutionContextHolder
}

trait ExecutionContextHolder {
  def context: ExecutionContext
}

class SimpleExecutionContextHolder extends ExecutionContextHolder {
  override def context: ExecutionContext = scala.concurrent.ExecutionContext.global
}