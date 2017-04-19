package controllers

import javax.inject.Inject

import models.Widget
import play.api.data._

class WidgetController @Inject()(cc: MyControllerComponents) extends MyAbstractController(cc) {
  import WidgetController._

  private val widgets = scala.collection.mutable.ArrayBuffer(
    Widget("Widget 1", 123),
    Widget("Widget 2", 456),
    Widget("Widget 3", 789)
  )

  def index = Action {
    Ok(views.html.index())
  }

  def listWidgets = Action { implicit request =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets, widgetForm))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request =>
    val errorFunction = { formWithErrors: Form[Widget] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets, formWithErrors))
    }

    val successFunction = { widget: Widget =>
      // This is the good case, where the form was successfully parsed as a Widget.
      widgets.append(widget)
      Redirect(routes.WidgetController.listWidgets())
    }

    val formValidationResult = widgetForm.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

}

object WidgetController {
  import play.api.data.Forms._

  /**
   * The form definition for the "create a widget" form.
   * It specifies the form fields and their types,
   * as well as how to convert from a Widget to form data and vice versa.
   */
  val widgetForm = Form(
    mapping(
      "name" -> text,
      "price" -> number
    )(Widget.apply)(Widget.unapply)
  )

}
