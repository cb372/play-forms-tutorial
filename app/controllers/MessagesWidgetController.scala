package controllers

import models._

import javax.inject.Inject

import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * MessagesWidgetController is like WidgetController, but does not use I18nSupport.
 *
 * Instead, it uses a [[MessagesActionBuilder]] under the hood, which provides a
 * [[play.api.mvc.MessagesRequest]] that is a [[play.api.i18n.MessagesProvider]].
 */
class MessagesWidgetController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import WidgetForm._
  import play.api.data.Form

  private val widgets = scala.collection.mutable.ArrayBuffer(
    Widget("Widget 1", 123),
    Widget("Widget 2", 456),
    Widget("Widget 3", 789)
  )

  def index = Action {
    Ok(views.html.index())
  }

  def listWidgets = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets, widgetForm))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Widget] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets, formWithErrors))
    }

    val successFunction = { widget: Widget =>
      // This is the good case, where the form was successfully parsed as a Widget.
      widgets.append(widget)
      Redirect(routes.MessagesWidgetController.listWidgets())
    }

    val formValidationResult = widgetForm.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

}

/**
 * Abstract Controller that is like [[play.api.mvc.AbstractController]]
 * but uses "def Action: ActionBuilder[MessagesRequest, AnyContent]".
 */
abstract class MessagesAbstractController @Inject()(
  protected val controllerComponents: MessagesControllerComponents
) extends BaseControllerWithoutAction {

  def Action: ActionBuilder[MessagesRequest, AnyContent] = {
    controllerComponents.messagesActionBuilder.compose(controllerComponents.actionBuilder)
  }
}

/**
 * Trait that is like [[play.api.mvc.BaseController]]
 * but does not add "def Action: ActionBuilder[Request, AnyContent]".
 */
trait BaseControllerWithoutAction extends ControllerHelpers {

  protected def controllerComponents: ControllerComponents

  def parse: PlayBodyParsers = controllerComponents.parsers

  def defaultExecutionContext: ExecutionContext = controllerComponents.executionContext

  implicit def messagesApi: MessagesApi = controllerComponents.messagesApi

  implicit def supportedLangs: Langs = controllerComponents.langs

  implicit def fileMimeTypes: FileMimeTypes = controllerComponents.fileMimeTypes
}

case class MessagesControllerComponents @Inject()(
  messagesActionBuilder: MessagesActionBuilder,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents


