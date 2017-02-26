package com.practicingtechie.slick3

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import concurrent.Await
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration.Duration
import com.typesafe.scalalogging.Logger


object Main {
  val logger = Logger(getClass.getName)

  // Definition of the SUPPLIERS table
  class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
    def name = column[String]("SUP_NAME")
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def state = column[String]("STATE")
    def zip = column[String]("ZIP")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip)
  }
  val suppliers = TableQuery[Suppliers]

  // Definition of the COFFEES table
  class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)
    def supID = column[Int]("SUP_ID")
    def price = column[Double]("PRICE")
    def sales = column[Int]("SALES")
    def total = column[Int]("TOTAL")
    def * = (name, supID, price, sales, total)
    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id)
  }
  val coffees = TableQuery[Coffees]


  def main(args: Array[String]): Unit = {
    val db = Database.forConfig("postgres")

    Await.result({
      db.run {
        args.toList match {
          case List("create") =>
            DBIO.seq(
              suppliers.schema.create,
              coffees.schema.create
            )
          case List("query") =>
            val innerJoin = for {
              (c, s) <- coffees join suppliers on (_.supID === _.id)
            } yield (c.name, s.name)
            innerJoin.result
          case _ => DBIO.successful(logger.warn("unknown command"))
        }
      }
    }, Duration.Inf)
    db.close
  }
}