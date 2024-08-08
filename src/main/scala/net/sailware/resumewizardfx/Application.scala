package net.sailware.resumewizardfx

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import io.quarkiverse.fx.FxPostStartupEvent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane

@ApplicationScoped
class Application:

  def start(@Observes event: FxPostStartupEvent): Unit =
    val javaVersion = System.getProperty("java.version")
    val javafxVersion = System.getProperty("javafx.version")
    val label = Label(s"Hello JavaFX $javafxVersion, running on Java ${javaVersion}.")
    val scene = Scene(StackPane(label), 640, 480)
    val stage = event.getPrimaryStage()
    stage.setScene(scene)
    stage.show()
    
