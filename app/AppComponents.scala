import controllers.Application
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.i18n.{DefaultLangs, DefaultMessagesApi, MessagesApi}
import play.api.routing.Router
import play.api.http.HttpFilters
import play.filters.csrf._
import play.filters.csrf.CSRF.ConfigTokenProvider
import router.Routes // this is the auto-generated Router class

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with CSRFComponents {

  override lazy val httpFilters = Seq(csrfFilter)
  val messagesApi: MessagesApi = new DefaultMessagesApi(environment, configuration, new DefaultLangs(configuration))
  val appController = new Application(messagesApi)(csrfConfig)
  val assets = new controllers.Assets(httpErrorHandler)
  val router: Router = new Routes(httpErrorHandler, appController, assets)

}
