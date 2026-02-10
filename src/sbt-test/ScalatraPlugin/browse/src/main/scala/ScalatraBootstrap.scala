import org.scalatra.*

import javax.servlet.ServletContext

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
