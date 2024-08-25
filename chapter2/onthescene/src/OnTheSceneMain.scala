package proscalafx.chapter2.onthescene

import javafx.scene as jfxs
import javafx.scene.{control as jfxsc, text as jfxst}
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.{DoubleProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{HPos, Insets, Orientation, VPos}
import scalafx.scene.control.*
import scalafx.scene.layout.{FlowPane, HBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.scene.{Cursor, Scene}

object OnTheSceneMain extends JFXApp3:
  override def start(): Unit =
    val fillVals = DoubleProperty(255.0)

    val cursors = ObservableBuffer[jfxs.Cursor](
      Cursor.Default,
      Cursor.Crosshair,
      Cursor.Wait,
      Cursor.Text,
      Cursor.Hand,
      Cursor.Move,
      Cursor.NResize,
      Cursor.NEResize,
      Cursor.NWResize,
      Cursor.EResize,
      Cursor.WResize,
      Cursor.SResize,
      Cursor.SEResize,
      Cursor.SWResize,
      Cursor.None
    )

    val sliderRef = new Slider:
      min = 0
      max = 255
      value = 255
      orientation = Orientation.Vertical

    val choiceRef = new ChoiceBox[jfxs.Cursor]:
      items = cursors

    val textSceneX = new Text:
      styleClass = List("emphasized-text")

    val textSceneY = new Text:
      styleClass = List("emphasized-text")

    val textSceneW = new Text:
      styleClass = List("emphasized-text")

    val textSceneH = new Text:
      styleClass = List("emphasized-text")
      id = "sceneHeightText"

    val labelStageX = new Label:
      id = "stageX"

    val labelStageY = new Label:
      id = "stageY"

    val labelStageW = new Label()
    val labelStageH = new Label()

    val toggleGrp = new ToggleGroup()

    val sceneRoot = new FlowPane:
      layoutX = 20
      layoutY = 40
      padding = Insets(0, 20, 40, 0)
      orientation = Orientation.Vertical
      vgap = 10
      hgap = 20
      columnHalignment = HPos.Left
      children = List(
        new HBox {
          spacing = 10
          children = List(sliderRef, choiceRef)
        },
        textSceneX,
        textSceneY,
        textSceneW,
        textSceneH,
        new Hyperlink {
          text = "lookup()"
          onAction = () => {
            println(s"sceneRef: ${stage.scene()}")
            val textRef = stage.scene().lookup("#sceneHeightText").asInstanceOf[jfxst.Text]
            println(textRef.text())
          }
        },
        new RadioButton {
          text = "onTheScene.css"
          toggleGroup = toggleGrp
          selected = true
        },
        new RadioButton {
          text = "changeOfScene.css"
          toggleGroup = toggleGrp
        },
        labelStageX,
        labelStageY,
        labelStageW,
        labelStageH
      )

    val sceneRef = new Scene(600, 250):
      root = sceneRoot
      stylesheets = List(this.getClass.getResource("onTheScene.css").toExternalForm)

    stage = new PrimaryStage:
      title = "On the Scene"
      scene = sceneRef

    choiceRef.selectionModel().selectFirst()

    textSceneX.text <== new StringProperty("Scene x: ") + sceneRef.x.asString
    textSceneY.text <== new StringProperty("Scene y: ") + sceneRef.y.asString
    textSceneW.text <== new StringProperty("Scene width: ") + sceneRef.width.asString
    textSceneH.text <== new StringProperty("Scene height: ") + sceneRef.height.asString
    labelStageX.text <== new StringProperty("Stage x: ") + sceneRef.window.get.x.asString
    labelStageY.text <== new StringProperty("Stage y: ") + sceneRef.window.get.y.asString
    labelStageW.text <== new StringProperty("Stage width: ") + sceneRef.window.get.width.asString
    labelStageH.text <== new StringProperty("Stage height: ") + sceneRef.window.get.height.asString
    sceneRef.cursor <== choiceRef.value
    fillVals <== sliderRef.value

    fillVals.onChange({
      println("changed")
      val fillValue: Double = fillVals() / 256.0
      sceneRef.fill = Color(fillValue, fillValue, fillValue, 1.0)
    })

    toggleGrp.selectedToggle.onChange({
      val radioButtonText = toggleGrp.selectedToggle().asInstanceOf[jfxsc.RadioButton].text()
      sceneRef.stylesheets = List(this.getClass.getResource(radioButtonText).toExternalForm)
    })

    val addedTextRef = new Text:
      layoutX = 0
      layoutY = -30
      textOrigin = VPos.Top
      fill = Color.Blue
      font = Font.font("Sans Serif", FontWeight.Bold, 16)
      managed = false
      text <== new StringProperty("Scene fill: ") + sceneRef.fill.asString

    sceneRef.content += addedTextRef























