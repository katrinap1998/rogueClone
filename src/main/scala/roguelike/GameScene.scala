package roguelike

import indigo.*
import indigo.scenes.*
import roguelike.model.Model
import roguelikestarterkit.{MapTile, Tile}
import roguelikestarterkit.TerminalEmulator

object GameScene extends Scene[Unit, Model, Unit]:

  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName =
    SceneName("game scene")

  val modelLens: Lens[Model, Model] =
    Lens.keepLatest

  val viewModelLens: Lens[Unit, Unit] =
    Lens.keepLatest

  val eventFilters: EventFilters =
    EventFilters.Permissive

  val subSystems: Set[SubSystem] =
    Set()

  def updateModel(context: indigo.scenes.SceneContext[Unit], model: roguelike.GameScene.SceneModel): indigo.shared.events.GlobalEvent => indigo.shared.Outcome[roguelike.GameScene.SceneModel] =
    case KeyboardEvent.KeyUp(Key.UP_ARROW) =>
      Outcome(model.copy(player = model.player.moveUp))

    case KeyboardEvent.KeyUp(Key.DOWN_ARROW) =>
      Outcome(model.copy(player = model.player.moveDown))

    case KeyboardEvent.KeyUp(Key.LEFT_ARROW) =>
      Outcome(model.copy(player = model.player.moveLeft))

    case KeyboardEvent.KeyUp(Key.RIGHT_ARROW) =>
      Outcome(model.copy(player = model.player.moveRight))

    case _ =>
      Outcome(model)

  def updateViewModel(
      context: indigo.scenes.SceneContext[Unit],
      model: roguelike.GameScene.SceneModel,
      viewModel: roguelike.GameScene.SceneViewModel): indigo.shared.events.GlobalEvent => indigo.shared.Outcome[roguelike.GameScene.SceneViewModel] =
      _ => Outcome(viewModel)

  val terminal: TerminalEmulator =
    TerminalEmulator(RogueLikeGame.screenSize)

  def present(context: indigo.scenes.SceneContext[Unit], model: roguelike.GameScene.SceneModel, viewModel: roguelike.GameScene.SceneViewModel): indigo.shared.Outcome[indigo.shared.scenegraph.SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        Layer(
          BindingKey("game"),
          TextBox("No level", 100, 30)
            .withColor(RGBA.White)
            .withFontFamily(FontFamily.monospace)
        )
//        terminal
//          .put(model.player.position, Tile.`@`)
//          .put(RogueLikeGame.charSize, MapTile(Tile.SPACE), 4000)
//          .draw(Assets.tileMap, RogueLikeGame.charSize, MapTile(Tile.SPACE), 4000)
      )
    )
