package proscalafx.chapter4.borderlayout

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.Observable
import scalafx.beans.binding.*
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Pos
import scalafx.scene.effect.{DropShadow, InnerShadow}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Ellipse
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.scene.{Node, Scene}

import scala.collection.mutable.ListBuffer

object BorderLayoutExample extends JFXApp3:

  override def start(): Unit =
    val borderPane = new BorderPane:
      top = createTitle
      center = createBackground
      bottom = createScoreBoxes

    stage = new PrimaryStage:
      scene = new Scene(600, 400):
        root = borderPane
      
  private def createTitle = new TilePane:
    snapToPixel = false
    children = List(
      new StackPane {
        style = "-fx-background-color: black"
        children = new Text("ScalaFX") {
          font = Font.font(null, FontWeight.Bold, 18)
          fill = Color.White
          alignmentInParent = Pos.CenterRight
        }
      },
      new Text("Reversi") {
        font = Font.font(null, FontWeight.Bold, 18)
        alignmentInParent = Pos.CenterLeft
      }
    )
    prefTileHeight = 40
    prefTileWidth <== parent.selectDouble("width") / 2


  private def createBackground = new Region:
    style = "-fx-background-color: radial-gradient(radius 100%, white, gray)"    
  
  private def createScoreBoxes = new TilePane:
    snapToPixel = false
    prefColumns = 2
    children = List(
      createScore(Black),
      createScore(White)
    )
    prefTileWidth <== parent.selectDouble("width") / 2

  private def createScore(owner: Owner): Node =
    val innerShadow = new InnerShadow:
      color = Color.DodgerBlue
      choke = 0.5

    val backgroundRegion = new Region:
      style = s"-fx-background-color: ${owner.opposite.colorStyle}"
      if Black == owner then effect = innerShadow

    val dropShadow = new DropShadow:
      color = Color.DodgerBlue
      spread = 0.2

    val piece = new Ellipse:
      radiusX = 32
      radiusY = 20
      fill = owner.color
      if Black == owner then effect = dropShadow

    val score = new Text:
      font = Font.font(null, FontWeight.Bold, 100)
      fill = owner.color
      text <== ReversiModel.score(owner).asString

    val remaining = new Text:
      font = Font.font(null, FontWeight.Bold, 12)
      fill = owner.color
      text <== ReversiModel.turnsRemaining(owner).asString() + " turns remaining"

    new StackPane:
      children = List(
        backgroundRegion,
        new FlowPane {
          hgap = 20
          vgap = 10
          alignment = Pos.Center
          children = List(
            score,
            new VBox {
              alignment = Pos.Center
              spacing = 10
              children = List(
                piece,
                remaining
              )
            }
          )
        }
      )
    
sealed case class Owner(color: Color, colorStyle: String):
  def opposite: Owner = this match
    case White => Black
    case Black => White
    case _ => NONE

object NONE extends Owner(Color.Transparent, "")
object White extends Owner(Color.White, "white")
object Black extends Owner(Color.Black, "black")

object ReversiModel:
  val BOARD_SIZE = 8
  val turn = ObjectProperty[Owner](Black)
  val board = Array.tabulate(BOARD_SIZE, BOARD_SIZE)((_,_) => ObjectProperty[Owner](NONE))

  initBoard()

  private def initBoard(): Unit =
    val center1 = BOARD_SIZE / 2 - 1
    val center2 = BOARD_SIZE / 2
    board(center1)(center1)() = White
    board(center1)(center2)() = Black
    board(center2)(center2)() = White
    board(center2)(center2)() = Black

  def restart(): Unit =
    board.flatten.foreach(_() = NONE)
    initBoard()
    turn() = Black

  def score(owner: Owner): NumberExpression =
    board.flatten.map(p => when(p === owner) choose 1 otherwise 0).reduce(_ + _)

  def turnsRemaining(owner: Owner): NumberBinding =
    val emptyCellCount = score(NONE)
    when(turn === owner) choose ((emptyCellCount + 1) / 2) otherwise (emptyCellCount / 2)

  def legalMove(x: Int, y: Int): BooleanBinding =
    (board(x)(y) === NONE) && (
      canFlip(x, y, 0, -1, turn) ||
        canFlip(x, y, -1, -1, turn) ||
        canFlip(x, y, -1, 0, turn) ||
        canFlip(x, y, -1, 1, turn) ||
        canFlip(x, y, 0, 1, turn) ||
        canFlip(x, y, 1, 1, turn) ||
        canFlip(x, y, 1, 0, turn) ||
        canFlip(x, y, 1, -1, turn)
    )

  private def canFlip(
    cellX: Int,
    cellY: Int,
    directionX: Int,
    directionY: Int,
    turn: ObjectProperty[Owner]
  ): BooleanBinding =
    val dependencies = ListBuffer.empty[Observable]
    var x = cellX + directionX
    var y = cellY + directionY
    while x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE
    do
      dependencies += board(x)(y)
      x += directionX
      y += directionY
    dependencies += turn

    Bindings.createBooleanBinding(
      () => {
        val turnVal = turn.get
        var x = cellX + directionX
        var y = cellY + directionY
        var first = true
        var result: Option[Boolean] = None
        while result.isEmpty && x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board(x)(y).get != NONE
        do
          if board(x)(y).get == turnVal then result = Option(!first)
          first = false
          x += directionX
          y += directionY
        result.getOrElse(false)
      },
      dependencies.toSeq*
    )

  def play(cellX: Int, cellY: Int): Unit =
    if legalMove(cellX, cellY).get then
      board(cellX)(cellY)() = turn()
      flip(cellX, cellY, 0, -1, turn)
      flip(cellX, cellY, -1, -1, turn)
      flip(cellX, cellY, -1, 0, turn)
      flip(cellX, cellY, -1, 1, turn)
      flip(cellX, cellY, 0, 1, turn)
      flip(cellX, cellY, 1, 1, turn)
      flip(cellX, cellY, 1, 0, turn)
      flip(cellX, cellY, 1, -1, turn)
      turn.value = turn.value.opposite

  def flip(cellX: Int, cellY: Int, directionX: Int, directionY: Int, turn: ObjectProperty[Owner]): Unit =
    if canFlip(cellX, cellY, directionX, directionY, turn).get then
      var x = cellX + directionX
      var y = cellY + directionY
      while x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board(x)(y)() != turn()
      do
        board(x)(y)() = turn()
        x += directionX
        y += directionY
































