package com.github.tomdom.play.multi.skeleton.client

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object DataRows extends js.JSApp {
  @JSExport
  def main(): Unit = showDataRows()

  @JSExport
  def showDataRows(): Unit = {
    dom.window.alert("scalajs")
  }
}
