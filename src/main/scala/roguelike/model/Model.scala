package roguelike.model

import indigo.*
import roguelike.model.Direction.{DOWN, LEFT, RIGHT, UP}

final case class Model(screen: Size, player: Player)
case class Fruit(position: Point)
object Fruit {
  def randomFruit(screenSize: Point): Fruit = ???
}

object Model:
  def initial(screenSize: Size): Model =
    Model(screenSize, Player.initial(screenSize))

final case class Player(screenSize: Size, position: Point, direction: Direction):
  def moveUp: Player =
    this.copy(position = newPositionModulo(position, Point(0, -2)), direction = UP)
  def moveDown: Player =
    this.copy(position = newPositionModulo(position, Point(0, 2)), direction = DOWN)
  def moveLeft: Player =
    this.copy(position = newPositionModulo(position, Point(-2, 0)), direction = LEFT)
  def moveRight: Player =
    this.copy(position = newPositionModulo(position, Point(2, 0)), direction = RIGHT)

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


  def continue: Player =
    this.direction match
      case Direction.UP => moveUp
      case Direction.DOWN => moveDown
      case Direction.LEFT => moveLeft
      case Direction.RIGHT => moveRight


object Player:
  def initial(screenSize: Size): Player =
    Player(screenSize, screenSize.toPoint / 2, DOWN)

enum Direction:
  case UP, DOWN, LEFT, RIGHT