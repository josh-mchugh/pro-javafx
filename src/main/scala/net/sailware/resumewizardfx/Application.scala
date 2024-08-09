package net.sailware.resumewizardfx

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import io.quarkiverse.fx.FxPostStartupEvent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.layout.Priority
import javafx.scene.web.WebView

@ApplicationScoped
class Application:

  def start(@Observes event: FxPostStartupEvent): Unit =
    val webView = WebView()
    val webEngine = webView.getEngine()
    webEngine.load("http://localhost:8080/wizard/detail")
    VBox.setVgrow(webView, Priority.ALWAYS)
    val root = VBox()
    root.getChildren().add(webView)
    val scene = Scene(root)
    val stage = event.getPrimaryStage()
    stage.setScene(scene)
    stage.setResizable(true)
    stage.show()
    
