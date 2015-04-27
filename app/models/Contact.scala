package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Contact(id: Long, name: String, phone: String)

object Contact {

  val contact = {
    get[Long]("id") ~
    get[String]("name") ~
    get[String]("phone") map {
      case id ~ name ~ phone => Contact(id, name, phone)
    }
  }

  def all(): List[Contact] = {
    DB.withConnection { implicit c =>
      SQL("select * from contacts")
        .as(contact *)
    }
  }

  def findByName(name: String): List[Contact] = {
    DB.withConnection { implicit c =>
      SQL("select * from contacts where LOWER(name) LIKE LOWER('%" + name + "%')")
        .as(contact *)
    }
  }

  def create(name: String, phone: String): Unit = {
    DB.withConnection { implicit c =>
      SQL("insert into contacts (name, phone) values ({name}, {phone})")
        .on('name -> name, 'phone -> phone)
        .executeUpdate()
    }
  }

  def delete(id: Long): Unit = {
    DB.withConnection {implicit c =>
      SQL("delete from contacts where id = {id}")
      .on('id -> id)
      .executeUpdate()
    }
  }

  def exists(name: String, phone: String): Boolean = {
    DB.withConnection { implicit c =>
      SQL("select top 1 * from contacts where LOWER(name) = LOWER({name}) and LOWER(phone) = LOWER({phone})")
        .on('name -> name, 'phone -> phone)
        .as(contact *)
        .size > 0
    }
  }

}
