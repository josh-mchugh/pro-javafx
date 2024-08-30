package proscalafx.chapter3

import scalafx.beans.property.StringProperty

object BidirectionalBindingExample extends App:
  println("Constructing two StringProperty objects")
  val prop1 = new StringProperty("")
  val prop2 = new StringProperty("")

  println("Calling bidirectional (<==>).")
  prop2 <==> prop1

  println(s"prop1.isBound = ${prop1.isBound}")
  println(s"prop2.isBound = ${prop2.isBound}")

  println("""Calling prop1.set("prop1 says: Hi!")""")
  prop1() = "prop1 says: Hi!"
  println("prop2.get returned:")
  println(prop2())

  println("""Calling prop2.set(prop2.get + "\nprop2 says: Bye!")""")
  prop2() = prop2() + "\nprop2 says: Bye!"
  println("prop1.get returned:")
  println(prop1())
