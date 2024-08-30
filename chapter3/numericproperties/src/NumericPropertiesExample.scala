package proscalafx.chapter3

import scalafx.beans.property.{DoubleProperty, FloatProperty, IntegerProperty, LongProperty}

object NumericPropertiesExample extends App:
  val i = new IntegerProperty(null, "i", 1024)
  val l = new LongProperty(null, "l", 0L)
  val f = new FloatProperty(null, "f", 0.0f)
  val d = new DoubleProperty(null, "d", 0.0)
  println("Constructed numerical properties i, l, f, d.")

  println(s"i = ${i()}")
  println(s"l = ${l()}")
  println(s"f = ${f()}")
  println(s"d = ${d()}")

  l <== i
  f <== l
  d <== f
  println("Bound l to i, f to l, d to f.")

  println(s"i = ${i()}")
  println(s"l = ${l()}")
  println(s"f = ${f()}")
  println(s"d = ${d()}")

  println("Calling i.set(2048)")
  i() = 2048

  println(s"i = ${i()}")
  println(s"l = ${l()}")
  println(s"f = ${f()}")
  println(s"d = ${d()}")

  d.unbind()
  f.unbind()
  l.unbind()
  println("Unbound l to i, f to l, d to f.")

  f <== d
  l <== f
  i <== l
  println("Bound f to d, l to f, i to l.")

  println("Calling d.set(10000000000L).")
  d() = 1000000000L

  println(s"d = ${d()}")
  println(s"f = ${f()}")
  println(s"l = ${l()}")
  println(s"i = ${i()}")
