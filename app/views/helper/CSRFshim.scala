package views.html.helper

import play.api.mvc._
import play.twirl.api.{ Html, HtmlFormat }

/**
 * CSRF helper for Play calls
 *
 * This is a kludged version of https://github.com/playframework/playframework/blob/2.4.6/framework/src/play-filters-helpers/src/main/scala/views/html/helper/CSRF.scala,
 * which takes an implicit CSRFConfig to make things play nicely with compile-time DI.
 */
object CSRFshim {

  /**
   * Render a CSRF form field token
   */
  def formField(implicit request: play.api.mvc.RequestHeader, config: play.filters.csrf.CSRFConfig): Html = {
    val token = play.filters.csrf.CSRF.getToken(request, config).getOrElse(sys.error("Missing CSRF Token"))
    // probably not possible for an attacker to XSS with a CSRF token, but just to be on the safe side...
    Html(s"""<input type="hidden" name="${config.tokenName}" value="${HtmlFormat.escape(token.value)}"/>""")
  }

}
