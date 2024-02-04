package roguelike

import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel
import roguelike.model.Model
import roguelikestarterkit.TerminalText
import roguelikestarterkit.tiles.RoguelikeTiles

@JSExportTopLevel("IndigoGame")
object RogueLikeGame extends IndigoSandbox[Size, Model]:

  val screenSize: Size = Size(80, 50)
  val charSize: Size = Size(10, 10)

  def animations: Set[indigo.Animation] = ???
  def assets: Set[indigo.AssetType] = ???
  def initialScene(bootData: Unit): Option[SceneName] =
    None
  def config: indigo.GameConfig = ???
  def fonts: Set[indigo.FontInfo] = ???

  def initialModel(startupData: indigo.Size): indigo.shared.Outcome[roguelike.model.Model] = ???

  def present(context: indigo.shared.FrameContext[indigo.Size], model:roguelike.model.Model):
  indigo.shared.Outcome[indigo.SceneUpdateFragment] = ???
  def setup(assetCollection: indigo.AssetCollection, dice: indigo.Dice):indigo.shared.Outcome[indigo.shared.Startup[indigo.Size]] = ???
  def shaders: Set[indigo.Shader] = ???
  
  def scenes(bootData: Unit): NonEmptyList[Scene[Unit, Model, Unit]] =
    NonEmptyList(GameScene)

  val eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit]] =
    Outcome(
      BootResult
        .noData(
          GameConfig.default
            .withMagnification(1)
            .withFrameRateLimit(30)
            .withViewport(screenSize.width * charSize.width, screenSize.height * charSize.height)
        )
        .withFonts(RoguelikeTiles.Size10x10.Fonts.fontInfo)
        .withAssets(Assets.assets)
        .withShaders(
          TerminalText.standardShader
        )
    )

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial(screenSize))

  def initialViewModel(startupData: Unit, model: Model): Outcome[Unit] =
    Outcome(())

  def setup(bootData: Unit, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def updateModel(context: FrameContext[Size], model: Model): GlobalEvent => Outcome[Model] =
    _ => Outcome(model)

  def updateViewModel(context: FrameContext[Unit], model: Model, viewModel: Unit): GlobalEvent => Outcome[Unit] =
    _ => Outcome(viewModel)

  def present(context: FrameContext[Unit], model: Model, viewModel: Unit): Outcome[SceneUpdateFragment] =
    Outcome(SceneUpdateFragment.empty)
