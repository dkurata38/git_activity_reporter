package adapter.web.controllers

import application.inputport.FindUserByTokenUseCaseInputPort
import javax.inject.{Inject, Singleton}
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, findUserByTokenUseCaseInputPort: FindUserByTokenUseCaseInputPort) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>
    request.session.get("accessToken").map(accessToken =>
      findUserByTokenUseCaseInputPort.getUserOf(accessToken) match {
        case Some(_) => Redirect(adapter.web.controllers.routes.SummaryController.index())
        case None => {
          val session = request.session - "accessToken"
          Ok(views.html.index()).withSession(session)
        }
      }
    ).getOrElse(Ok(views.html.index()))
  }
}
