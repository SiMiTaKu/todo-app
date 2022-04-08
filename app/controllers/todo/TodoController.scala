package controllers.todo

import model.{Category, ViewValueHome, Todo}

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.i18n.I18nSupport
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}


case class CategoryFormData(content: String)
case class TodoFormData(content: String)

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

  val categories = scala.collection.mutable.ArrayBuffer((1L to 10L).map(i => Category(Some(i), s"test category${i.toString}", "あ", Some(1))): _*)

  def categoryList() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Category List",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.category.categoryList(categories.toSeq, vv))
  }

  def categoryShow(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Category Detail",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    categories.find(_.id.exists(_ == id)) match {
      case Some(category) => Ok(views.html.category.categoryShow(category, vv))
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
    Ok(views.html.category.categoryStore(categoryForm, vv))
  }

  def categoryStore() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Category Register Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    categoryForm.bindFromRequest().fold(
      (formWithErrors: Form[CategoryFormData]) => {
        BadRequest(views.html.category.categoryStore(formWithErrors, vv))
      },

      (categoryFormData: CategoryFormData) =>{
        categories += Category(Some(categories.size + 1L), categoryFormData.content, "あ", Some(1))
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
        Ok(views.html.category.categoryEdit(
          id, // データを識別するためのidを渡す
          categoryForm.fill(CategoryFormData(category.name)),
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
        BadRequest(views.html.category.categoryEdit(id, formWithErrors,vv))
      },
      (data: CategoryFormData) => {
        categories.find(_.id.exists(_ == id)) match {
          case Some(category) =>
            categories.update(id.toInt - 1, category.copy(slug = data.content))
            Redirect(routes.TodoController.categoryList())
          case None        =>
            NotFound(views.html.page404(vvError))
        }
      }
    )
  }

  def categoryDelete() = Action {implicit request: Request[AnyContent] =>
    val idOpt = request.body.asFormUrlEncoded.get("id").headOption
    categories.find(_.id.map(_.toString) == idOpt) match {
      case Some(category) =>
        categories -= category
        Redirect(routes.TodoController.categoryList())
      case None           =>
        NotFound(views.html.page404(vvError))
    }
  }



  val todos = scala.collection.mutable.ArrayBuffer((1L to 10L).map(i => Todo(Some(i), Some(i), s"test todo${i.toString}", "do what", Some(1))): _*)

  def todoList() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Todo List",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.todoList(todos.toSeq, vv))
  }

  def todoShow(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Todo Detail",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    todos.find(_.id.exists(_ == id)) match {
      case Some(todo) => Ok(views.html.todo.todoShow(todo, vv))
      case None           => NotFound(views.html.page404(vvError))
    }
  }


  val todoForm = Form(
    mapping(
      "content" -> nonEmptyText(maxLength = 140)
    )(TodoFormData.apply)(TodoFormData.unapply)
  )

  def todoRegister() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Todo Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.todo.todoStore(todoForm, vv))
  }

  def todoStore() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Todo Register Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    todoForm.bindFromRequest().fold(
      (formWithErrors: Form[TodoFormData]) => {
        BadRequest(views.html.todo.todoStore(formWithErrors, vv))
      },

      (todoFormData: TodoFormData) =>{
        todos += Todo(Some(categories.size + 1L), Some(categories.size + 1L), todoFormData.content, "do what", Some(1))
        Redirect(("/todoList"))
      }
    )
  }

  def todoEdit(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv =  ViewValueHome(
      title  = "Todo Edit Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    todos.find(_.id.exists(_ == id)) match {
      case Some(todo) =>
        Ok(views.html.todo.todoEdit(
          id, // データを識別するためのidを渡す
          todoForm.fill(TodoFormData(todo.title)),
          vv// fillでformに値を詰める
        ))
      case None        =>
        NotFound(views.html.page404(vvError))
    }
  }

  /**
   * 対象のツイートを更新する
   */
  def todoUpdate(id: Long) = Action { implicit request: Request[AnyContent] =>
    val vv =  ViewValueHome(
      title  = "Todo Edit Form",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    todoForm.bindFromRequest().fold(
      (formWithErrors: Form[TodoFormData]) => {
        BadRequest(views.html.todo.todoEdit(id, formWithErrors,vv))
      },
      (data: TodoFormData) => {
        todos.find(_.id.exists(_ == id)) match {
          case Some(todo) =>
            todos.update(id.toInt - 1, todo.copy(title = data.content))
            Redirect(routes.TodoController.todoList())
          case None        =>
            NotFound(views.html.page404(vvError))
        }
      }
    )
  }

  def todoDelete() = Action {implicit request: Request[AnyContent] =>
    val idOpt = request.body.asFormUrlEncoded.get("id").headOption
    todos.find(_.id.map(_.toString) == idOpt) match {
      case Some(todo) =>
        todos -= todo
        Redirect(routes.TodoController.todoList())
      case None           =>
        NotFound(views.html.page404(vvError))
    }
  }
}

