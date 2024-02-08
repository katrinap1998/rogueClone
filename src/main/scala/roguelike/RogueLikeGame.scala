package roguelike

import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel
import roguelike.model.Model

@JSExportTopLevel("IndigoGame")
object RogueLikeGame extends IndigoSandbox[Unit, Model]:

  val screenSize: Size = Size(450, 450)

  val assetName = AssetName("dots")
  def animations: Set[Animation] = Set()
  def assets: Set[AssetType] = Set(AssetType.Image(assetName, AssetPath("assets/dots.png")))
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
    case FrameTick => Outcome(model.copy(player = model.player.continue))
    case _ => Outcome(model)
  }

  def present(context: FrameContext[Unit], model: Model): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        Graphic(
          Rectangle(0, 0, 32, 32), 1, Material.Bitmap(RogueLikeGame.assetName)
        ).withCrop(Rectangle(16, 0, 16, 16)).moveTo(model.player.position)
      )
    )
