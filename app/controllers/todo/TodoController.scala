package controllers.todo

import model.{Category, ViewValueHome}

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.I18nSupport
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}




case class CategoryFormData(content: String)

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController with I18nSupport {

  val vvError = ViewValueHome(
    title  = "404 Not Found",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )

  def home() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.Home(vv))
  }

  val categories = scala.collection.mutable.ArrayBuffer((1L to 10L).map(i => Category(Some(i), s"test category${i.toString}")): _*)

  def categoryList() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Category List",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.categoryList(categories.toSeq, vv))
  }

  def categoryShow(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Category Detail",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    categories.find(_.id.exists(_ == id)) match {
      case Some(category) => Ok(views.html.categoryShow(category, vv))
      case None           => NotFound(views.html.page404(vvError))
    }
  }


  val categoryForm = Form(
    mapping(
      "content" -> nonEmptyText(maxLength = 140)
    )(CategoryFormData.apply)(CategoryFormData.unapply)
  )

  def categoryRegister() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Register Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.categoryStore(categoryForm, vv))
  }

  def categoryStore() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Register Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    categoryForm.bindFromRequest().fold(
      (formWithErrors: Form[CategoryFormData]) => {
        BadRequest(views.html.categoryStore(formWithErrors, vv))
      },

      (categoryFormData: CategoryFormData) =>{
        categories += Category(Some(categories.size + 1L), categoryFormData.content)
        Redirect(("/categoryList"))
      }
    )
  }

  def categoryEdit(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv =  ViewValueHome(
      title  = "Category Edit Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    categories.find(_.id.exists(_ == id)) match {
      case Some(category) =>
        Ok(views.html.categoryEdit(
          id, // データを識別するためのidを渡す
          categoryForm.fill(CategoryFormData(category.content)),
          vv// fillでformに値を詰める
        ))
      case None        =>
        NotFound(views.html.page404(vvError))
    }
  }

  /**
   * 対象のツイートを更新する
   */
  def categoryUpdate(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv =  ViewValueHome(
      title  = "Category Edit Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    categoryForm.bindFromRequest().fold(
      (formWithErrors: Form[CategoryFormData]) => {
        BadRequest(views.html.categoryEdit(id, formWithErrors,vv))
      },
      (data: CategoryFormData) => {
        categories.find(_.id.exists(_ == id)) match {
          case Some(tweet) =>
            // indexは0からのため-1
            categories.update(id.toInt - 1, tweet.copy(content = data.content))
            Redirect(routes.TodoController.categoryList())
          case None        =>
            NotFound(views.html.page404(vvError))
        }
      }
    )
  }
}

