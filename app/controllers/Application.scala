package controllers

import models.Widget

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.filters.csrf.CSRFConfig

import scala.collection.mutable.ArrayBuffer

class Application(val messagesApi: MessagesApi)(implicit csrfConfig: CSRFConfig) extends Controller with I18nSupport {

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
  def createWidget = Action { implicit request =>
    val formValidationResult = Application.createWidgetForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets.toSeq, formWithErrors))
    }, { widget =>
      // This is the good case, where the form was successfully parsed as a Widget.
      widgets.append(widget)
      Redirect(routes.Application.listWidgets)
    })
  }

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
