package models.actors

import akka.actor.{ActorRef, Actor}
import play.api.Logger

object JamActor {
    case class Join(socket: ActorRef)
    case class Part(socket: ActorRef)
}

/**
 * Actor that holds the player state.
 */
class JamActor(name: String) extends Actor {
    override def receive: Receive = {
        case msg => Logger.warn(s"JamActor received unknown message: $msg")
    }
}
