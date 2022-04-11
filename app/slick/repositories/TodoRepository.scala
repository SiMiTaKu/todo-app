package slick.repositories

import java.time.LocalDateTime
import play.api.db.slick.{HasDatabaseConfigProvider,DatabaseConfigProvider}
import javax.inject.{Inject, Singleton}
import slick.jdbc.{JdbcProfile, GetResult}
import scala.concurrent.{Future, ExecutionContext}
import slick.model.Todo

@Singleton
class TodoRepository @Inject()(
                                 protected val dbConfigProvider: DatabaseConfigProvider
                               )(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val query = new TableQuery(tag => new TodoTable(tag))

  // ########## [DBIO Methods] ##########

  /**
   * tweetを全件取得
   */
  def all(): Future[Seq[Todo]] = db.run(query.result)

  // ########## [Table Mapping] ##########
  private class TodoTable(_tableTag: Tag) extends Table[Todo](_tableTag, Some("to_do"), "to_do") {
    // Tableとのカラムマッピング
    val id:          Rep[Long]          = column[Long]("id", O.AutoInc, O.PrimaryKey)
    val category_Id: Rep[Long]          = column[Long]("category_Id", O.AutoInc)
    val title:       Rep[String]        = column[String]("title", O.Length(120,varying=true))
    val body:        Rep[String]        = column[String]("body",  O.Length(120,varying=true))
    val state:       Rep[Int]           = column[Int]("state", O.AutoInc)
    val createdAt:   Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    val updatedAt:   Rep[LocalDateTime] = column[LocalDateTime]("created_at")

    // Plain SQLでデータ取得を行う用のマッピング
    implicit def GetResultTodo(implicit e0: GetResult[Long], e1: GetResult[String], e2: GetResult[LocalDateTime]): GetResult[Todo] = GetResult{
      prs => import prs._
        Todo.tupled((Some(<<[Long]), Some(<<[Long]),  <<[String], <<[String], Some(<<[Int]), <<[LocalDateTime], <<[LocalDateTime]))
    }

    // model -> db用タプル, dbからのデータ -> modelの変換を記述する処理
    // O.PrimaryKeyはColumnOptionTypeとなるためid.?でidをOptionとして取り扱い可能
    def * = (id.?, category_Id.?, title, body, state.?, createdAt, updatedAt) <> (Todo.tupled, Todo.unapply)

    def ? = (
      Rep.Some(id),
      Rep.Some(category_Id),
      Rep.Some(title),
      Rep.Some(body),
      Rep.Some(state),
      Rep.Some(createdAt),
      Rep.Some(updatedAt)).shaped.<>(
      { r => import r._;
        _1.map(_=>
          Todo.tupled((
            Option(_1.get),
            Option(_2.get),
            _3.get,
            _4.get,
            Option(_5.get),
            _6.get,
            _7.get
          ))
        )},
      (_:Any) =>
        throw new Exception("Inserting into ? projection not supported.")
    )

  }
}