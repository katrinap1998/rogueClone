package roguelike

import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel
import roguelike.model.Model

@JSExportTopLevel("IndigoGame")
object RogueLikeGame extends IndigoSandbox[Unit, Model]:

  val screenSize: Size = Size(450, 450)

  val assetName = AssetName("snake")
  def animations: Set[Animation] = Set()
  def assets: Set[AssetType] = Set(AssetType.Image(assetName, AssetPath("assets/snake.png")))
  def initialScene(bootData: Unit): Option[SceneName] = None
  def config: GameConfig = GameConfig.default.withViewport(GameViewport(450, 450))
  def fonts: Set[FontInfo] = Set()

  def setup(assetCollection: AssetCollection, dice: indigo.Dice): Outcome[Startup[Unit]] = Outcome(Startup.Success(()))
  def shaders: Set[indigo.Shader] = Set()
  
  def scenes(bootData: Unit): NonEmptyList[Scene[Unit, Model, Unit]] =
    NonEmptyList(GameScene)

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
      // If we introduce new aspects such as a wall or a fruit we must include it in the frame tick
      // Scenario 1: If the player collides with the fruit update the model with new position of the fruit, otherwise fruit does not move
      // Scenario 2: If the player hits the wall, then the game should reset
      Outcome(model.copy(player = model.player.continue))
    
    case _ => Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model): Outcome[SceneUpdateFragment] =
    val playerGraphic = Graphic(
      Rectangle(0, 0, 12, 12), 2, Material.Bitmap(RogueLikeGame.assetName)
    ).moveTo(model.player.position)

    val fruitGraphic = Graphic(Rectangle(0, 0, 12, 12), 2, Material.Bitmap(assetName))
      .withCrop(12, 0, 12, 12)
    
    Outcome(
      SceneUpdateFragment(
        Batch(playerGraphic)
      )
    )

  private def drawWalls(screenSize: Size, brickSize: Int): Batch[Graphic[_]] = {
    val brick = Graphic(
      Rectangle(0, 0, 32, 12), 1, Material.Bitmap(assetName)
    ).withCrop(Rectangle(0, 0, 12, 12))

    Batch.empty
  }