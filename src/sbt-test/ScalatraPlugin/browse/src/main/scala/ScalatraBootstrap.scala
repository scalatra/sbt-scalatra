import org.scalatra._

import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {

    val app = new ScalatraServlet {

      get("/") {
        "hey"
      }

    }

    context.mount(app, "/*")

  }

}
