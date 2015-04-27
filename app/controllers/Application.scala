package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models.Contact

object Application extends Controller {
  
  def index = Action {
    Redirect(routes.Application.list)
  }

  def list = Action {
    val contacts = Contact.all();
    Ok(views.html.list(contacts, findForm))
  }

  def find = Action { implicit request =>
    findForm.bindFromRequest.fold(
      errors => BadRequest(views.html.list(Nil, errors)),
      value => value match { case (name) => {
        val contacts = Contact.findByName(name)
        Ok(views.html.findResult(contacts, findForm))
      }}
    )
  }

  def newContact = Action {
    Ok(views.html.newContact(newContactForm))
  }

  def save = Action { implicit request =>
    newContactForm.bindFromRequest.fold(
      errors => BadRequest(views.html.newContact(errors)),
      value => value match { case (name, phone) => {
        Contact.create(name, phone)
        Redirect(routes.Application.list)
      }}
    )
  }

  def delete(id: Long) = Action {
    Contact.delete(id)
    Redirect(routes.Application.list())
  }

  val newContactForm = Form (
    tuple(
      "name" -> nonEmptyText(1, 200),
      "phone" -> nonEmptyText(1, 40)
    ) verifying("Контакт уже есть в справочнике!", f => f match {
      case (name, phone) => !Contact.exists(name, phone)
    })
  )

  val findForm = Form (
    "name" -> nonEmptyText
  )
}