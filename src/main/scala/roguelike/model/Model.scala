package roguelike.model

import indigo.*
import roguelike.model.Direction.{DOWN, LEFT, RIGHT, UP}

import scala.util.Random

final case class Model(screen: Size, player: Player, fruit: Fruit)
case class Fruit(position: Point)
object Fruit {
  def randomGenFruit(screenSize: Point): Fruit = {
    val validPos = for {
      x <- 0 until screenSize.x
      y <- 0 until screenSize.y
    } yield Point(x, y)
    val randomPos = Random.nextInt(validPos.length)
    Fruit(validPos(randomPos))
  }
}

final case class PlayerContinueOutcome(player: Player, hasHitWall: Boolean)

object Model:
  val wallThickness = 12

  def initial(screenSize: Size): Model =
    Model(screenSize, Player.initial(screenSize, wallThickness), Fruit.randomGenFruit(Point(screenSize.toPoint.x, screenSize.toPoint.y)))

final case class Player(screenSize: Size, wallThickness: Int, position: Point, direction: Direction):
  def moveUp: Player =
    this.copy(position = position + Point(0, -2), direction = UP)
  def moveDown: Player =
    this.copy(position = position + Point(0, 2), direction = DOWN)
  def moveLeft: Player =
    this.copy(position = position + Point(-2, 0), direction = LEFT)
  def moveRight: Player =
    this.copy(position = position + Point(2, 0), direction = RIGHT)

  // modulo is no longer required as wall stops the snake
  @Deprecated
  def newPositionModulo(currPos: Point, delta: Point): Point = {
    val newPos = currPos + delta
    // which of the new positions became negative?
    if (newPos.x < 0) {
      Point(screenSize.toPoint.x, newPos.y)
    } else if (newPos.y < 0) {
      Point(newPos.x, screenSize.toPoint.y)
    } else {
      Point(newPos.x % screenSize.toPoint.x, newPos.y % screenSize.toPoint.y)
    }
  }

  def continue: PlayerContinueOutcome = {
    if (hasHitWall)
      PlayerContinueOutcome(Player.initial(screenSize, Model.wallThickness), true)
    else
      this.direction match
        case Direction.UP => PlayerContinueOutcome(moveUp, false)
        case Direction.DOWN => PlayerContinueOutcome(moveDown, false)
        case Direction.LEFT => PlayerContinueOutcome(moveLeft, false)
        case Direction.RIGHT => PlayerContinueOutcome(moveRight, false)
  }

  private def hasHitWall: Boolean = {
    val leftWallBarrierX = wallThickness
    val topWallBarrierY  = wallThickness
    // cannot tell why I need 2 * wallThickness :( but with just wallThickness player goes inside the wall
    val rightWallBarrierX  = screenSize.toPoint.x - (2 * wallThickness)
    val bottomWallBarrierY = screenSize.toPoint.y - (2 * wallThickness)

    // check if the position of the player is at the walls
    val playerOnWall = position.x <= leftWallBarrierX || position.y <= topWallBarrierY ||
      position.x >= rightWallBarrierX || position.y >= bottomWallBarrierY

    playerOnWall
  }

object Player:
  def initial(screenSize: Size, wallThickness: Int): Player =
    Player(screenSize, wallThickness, screenSize.toPoint / 2, DOWN)

enum Direction:
  case UP, DOWN, LEFT, RIGHT