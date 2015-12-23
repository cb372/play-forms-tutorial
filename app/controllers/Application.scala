package controllers

import models.Widget

import play.api._
import play.api.mvc._

import scala.collection.mutable.ArrayBuffer

class Application extends Controller {

  private val widgets = ArrayBuffer(
    Widget("Widget 1", 123),
    Widget("Widget 2", 456),
    Widget("Widget 3", 789)
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def listWidgets = Action {
    Ok(views.html.listWidgets(widgets.toSeq))
  }

  // This will be the action that handles our form post
  def createWidget = TODO

}
