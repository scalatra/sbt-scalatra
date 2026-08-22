import org.scalatra.*

import jakarta.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext): Unit = {

    val app = new ScalatraServlet {

      get("/") {
        "hey"
      }

    }

    context.mount(app, "/*")

  }

}
