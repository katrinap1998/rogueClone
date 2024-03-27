package roguelike

import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel
import roguelike.model.{Fruit, Model, Player, PlayerContinueOutcome}
import indigo.syntax.toBatch

@JSExportTopLevel("IndigoGame")
object RogueLikeGame extends IndigoSandbox[Unit, Model]:

  val screenSize: Size = Size(480, 480)

  val assetName = AssetName("snake")
  def animations: Set[Animation] = Set()
  def assets: Set[AssetType] = Set(AssetType.Image(assetName, AssetPath("assets/snake.png")))
  def initialScene(bootData: Unit): Option[SceneName] = None
  def config: GameConfig = GameConfig.default.withViewport(GameViewport(450, 450))
  def fonts: Set[FontInfo] = Set()

  def setup(assetCollection: AssetCollection, dice: indigo.Dice): Outcome[Startup[Unit]] = Outcome(Startup.Success(()))
  def shaders: Set[indigo.Shader] = Set()

  val eventFilters: EventFilters =
    EventFilters.Permissive

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial(screenSize))

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {
    case KeyboardEvent.KeyUp(Key.UP_ARROW) => Outcome(model.copy(player = model.player.moveUp))
    case KeyboardEvent.KeyUp(Key.DOWN_ARROW) => Outcome(model.copy(player = model.player.moveDown))
    case KeyboardEvent.KeyUp(Key.LEFT_ARROW) => Outcome(model.copy(player = model.player.moveLeft))
    case KeyboardEvent.KeyUp(Key.RIGHT_ARROW) => Outcome(model.copy(player = model.player.moveRight))
    case FrameTick =>
      model.player.continue match {
        case PlayerContinueOutcome(p, true) => Outcome(model.copy(player = p, fruit = Fruit.randomGenFruit(Point(screenSize.toPoint.x, screenSize.toPoint.y))))
        case PlayerContinueOutcome(p, false) => 
          val modelCopy = model.copy(player = p)
          Outcome(updateFruitPosition(model.fruit.position, modelCopy))
        case _ => Outcome(model)
      } 
    
    case _ => Outcome(model)
  }

  private def updateFruitPosition(fruitPos: Point, model: Model): Model = {
    if( Math.abs(model.player.position.y - fruitPos.y) <= 16 && Math.abs(model.player.position.x - fruitPos.x) <= 16) {
      model.copy(fruit = Fruit.randomGenFruit(Point(screenSize.toPoint.x, screenSize.toPoint.y)))
    }
    else model
  }

  def present(context: FrameContext[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val snakeGraphic = Graphic(
      Rectangle(0, 0, 12, 12), 2, Material.Bitmap(RogueLikeGame.assetName)
    ).moveTo(model.player.position)

    val fruitGraphic = Graphic(Rectangle(0, 0, 12, 12), 2, Material.Bitmap(assetName))
      .withCrop(12, 0, 12, 12)
      .moveTo(model.fruit.position)

    Outcome(
      SceneUpdateFragment(
        Batch(snakeGraphic) ++ Batch(fruitGraphic) ++ drawWalls(model.screen, model.player.wallThickness)
      )
    )

  private def drawWalls(screenSize: Size, brickSize: Int): Batch[Graphic[_]] = {
    val bottomRightPos = screenSize.toPoint
    val topLeft = Point(0, 0)
    val topRightCorner = Point(bottomRightPos.x, 0)
    val bottomLeftCorner = Point(0, bottomRightPos.y)

    // range to be used for left to right walls on top and bottom of the window
    // we step through the range in brick size hops to place each brick next to each other
    // should not place a brick beyond the end of the screen hence the range stops a brick size hop
    // lesser than screensize
    val rangeX = 0 to (topRightCorner.x - brickSize) by brickSize

    // rangeY is same but used to lay bricks from top to bottom of the screen
    // Since rangeX places bricks at the ends of y, we need to disregard those end positions
    // when rangeY is used
    val rangeY = brickSize to (bottomLeftCorner.y - brickSize) by brickSize

    // going from topLeft to topRight
    val b1 = rangeX.map(brickPos => brick.moveTo(Point(brickPos, 0)))

    // going from bottomLeft to bottomRight
    val b2 = rangeX.map(brickPos => brick.moveTo(brickPos, bottomRightPos.y - brickSize))

    // going from topLeft to bottomRight
    val b3 = rangeY.map(brickPos => brick.moveTo(Point(0, brickPos)))

    //going from topRight to bottomRight
    val b4 = rangeY.map(brickPos => brick.moveTo(bottomRightPos.x - brickSize, brickPos))

    (b1 ++ b2 ++ b3 ++ b4).toBatch
  }

  private val brick = Graphic(
    Rectangle(0, 0, 32, 12), 1, Material.Bitmap(assetName)
  ).withCrop(Rectangle(0, 0, 12, 12))
