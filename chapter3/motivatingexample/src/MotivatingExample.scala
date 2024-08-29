package profscalafx.chapter3

import scalafx.beans.property.IntegerProperty

object MotivatingExample extends App:

  var intProperty: IntegerProperty = _

  def createProperty(): Unit =
    println()
    intProperty = IntegerProperty(1024)
    println(s"intProperty = $intProperty")
    println(s"intProperty.get = ${intProperty.get}")
    println(s"intProperty.value = ${intProperty.value}")
    println(s"intProperty() = ${intProperty()}")

  def addAndRemoveInvalidationListener(): Unit =
    println()
    val subscription = intProperty.onInvalidate {
      observable => println(s"The obsrevable has been invalidated: ${observable}.")
    }

    println("Added invalidation listener.")

    println("Calling intProperty.set(2048).")
    intProperty() = 2048

    println("Calling intProperty.setValue(3072).")
    intProperty() = Integer.valueOf(3072)

    subscription.cancel()
    println("Removed invalidation listener.")

    println("Calling intProperty.set(4096).")
    intProperty() = 4096

  def addAndRemoveChangeListener(): Unit =
    println()
    val subscription = intProperty.onChange {
      (_, oldValue, newValue) => println(s"The observable has changed: oldValue = ${oldValue}, newValue = ${newValue}")
    }
    println("Added change listener.")

    println("Calling intProperty.set(5120)")
    intProperty() = 5120

    subscription.cancel()
    println("Removed change listener.")

    println("Calling intProperty.set(6144).")
    intProperty() = 6144

  def bindAndUnbindOnePropertyToAnother(): Unit =
    println()
    val otherProperty = IntegerProperty(0)
    println(s"otherProperty() = ${otherProperty()}")

    println("Binding otherProperty to intProperty.")
    otherProperty <== intProperty
    println(s"otherProperty() = ${otherProperty()}")

    println("Calling intProperty.set(7168).")
    intProperty() = 7168
    println(s"otherProperty.get = ${otherProperty.get}")

    println("Unbinding otherProperty from intProperty.")
    otherProperty.unbind()
    println(s"otherProperty() = ${otherProperty()}")

    println("Calling intProperty.set(8192).")
    intProperty() = 8192
    println(s"otherProperty() = ${otherProperty()}")

  createProperty()
  addAndRemoveInvalidationListener()
  addAndRemoveChangeListener()
  bindAndUnbindOnePropertyToAnother()
