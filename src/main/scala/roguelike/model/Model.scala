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

object Model:
  def initial(screenSize: Size): Model =
    Model(screenSize, Player.initial(screenSize), Fruit.randomGenFruit(Point(screenSize.toPoint.x, screenSize.toPoint.y)))

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