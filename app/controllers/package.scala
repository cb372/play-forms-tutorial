package object controllers {

  import javax.inject.Inject

  import play.api.http.FileMimeTypes
  import play.api.i18n.{Langs, MessagesApi}
  import play.api.mvc._

  import scala.concurrent.ExecutionContext

  /**
   * Use a base controller that does not rely on I18NSupport or implicits magic
   */
  abstract class MyAbstractController @Inject()(
    protected val controllerComponents: MyControllerComponents
  ) extends ControllerHelpers {

    def Action: ActionBuilder[MessagesRequest, AnyContent] = {
      controllerComponents.messagesActionBuilder.compose(controllerComponents.actionBuilder)
    }

    def parse: PlayBodyParsers = controllerComponents.parsers

    def defaultExecutionContext: ExecutionContext = controllerComponents.executionContext

    implicit def messagesApi: MessagesApi = controllerComponents.messagesApi

    implicit def supportedLangs: Langs = controllerComponents.langs

    implicit def fileMimeTypes: FileMimeTypes = controllerComponents.fileMimeTypes
  }

  case class MyControllerComponents @Inject()(
     messagesActionBuilder: MessagesAction,
     actionBuilder: DefaultActionBuilder,
     parsers: PlayBodyParsers,
     messagesApi: MessagesApi,
     langs: Langs,
     fileMimeTypes: FileMimeTypes,
     executionContext: scala.concurrent.ExecutionContext
  ) extends ControllerComponents

}
