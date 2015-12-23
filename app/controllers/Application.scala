package controllers

import models.Widget

import play.api._
import play.api.data._
import play.api.data.Forms._
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

  def listWidgets = Action { implicit request =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets.toSeq, Application.createWidgetForm))
  }

  // This will be the action that handles our form post
  def createWidget = TODO

}

object Application {

  /** The form definition for the "create a widget" form.
   *  It specifies the form fields and their types,
   *  as well as how to convert from a Widget to form data and vice versa.
   */
  val createWidgetForm = Form(
    mapping(
      "name" -> text,
      "price" -> number
    )(Widget.apply)(Widget.unapply)

)

}
