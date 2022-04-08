package model

// case classについての説明は省略
// 参考: https://docs.scala-lang.org/ja/tour/case-classes.html
case class Category(
  id:      Option[Long],
  content: String
)