package model

import java.time.LocalDateTime

// case classについての説明は省略
// 参考: https://docs.scala-lang.org/ja/tour/case-classes.html
case class Todo(
  id:          Option[Long],
  category_id: Option[Long],
  title:       String,
  body:        String,
  state:       Option[Int],
  postedAt:  LocalDateTime = LocalDateTime.now,
  createdAt: LocalDateTime = LocalDateTime.now
)
