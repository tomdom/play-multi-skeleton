package controllers

import javax.inject._

import akka.actor.ActorSystem
import com.github.tomdom.play.multi.skeleton.module.components.DataServiceComponent
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scalatags.Text.all._
import scalatags.Text.tags2.title

@Singleton
class SkeletonController @Inject()(cc: ControllerComponents,
                                   dataServiceComponent: DataServiceComponent,
                                   actorSystem: ActorSystem)(
    implicit exec: ExecutionContext,
    environment: play.api.Environment)
    extends AbstractController(cc) {

  def twirl = Action.async {
    dataServiceComponent.service
      .dataRows()
      .map(dataRows => Ok(views.html.dataRows(dataRows)))
  }

  def scalatags = Action.async {
    dataServiceComponent.service
      .dataRows()
      .map(dataRows =>
        Ok(
          html(
            head(
              title("Data"),
              link(
                rel := "stylesheet",
                media := "screen",
                href := s"${routes.Assets.versioned("lib/bootstrap/css/bootstrap.css")}"),
              link(
                rel := "stylesheet",
                media := "screen",
                href := s"${routes.Assets.versioned("stylesheets/main.css")}"),
              link(rel := "shortcut icon",
                   `type` := "image/png",
                   href := s"${routes.Assets.versioned("images/favicon.png")}")
            ),
            body(
              div(
                for (datarow <- dataRows)
                  yield
                    div(
                      s"${datarow.id} - ${datarow.name}"
                    )
              ),
              script(
                `type` := "text/javascript",
                src := s"${routes.Assets.versioned("client-fastopt.js")}"),
              script(
                `type` := "text/javascript",
                src := s"${routes.Assets.versioned("client-launcher.js")}")
            )
          ).render
        ).as("text/html"))
  }
}
