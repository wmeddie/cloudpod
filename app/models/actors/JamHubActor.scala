package models.actors

import akka.actor._
import models.actors.JamHubActor._
import play.api.Logger

object JamHubActor {
    case class CreateJam(name: String)
    case class DeleteJam(name: String)
    case class ListJams()
    case class JoinJam(name: String, socket: ActorRef)
    case class PartJam(name: String, socket: ActorRef)
    case class SpectatorTerminated(jam: ActorRef, socket: ActorRef)
}

/**
 * Directory and supervisor of jam sessions.
 */
class JamHubActor extends Actor {
    private var jams: Map[String, ActorRef] = Map[String, ActorRef]()
    private var spectators: Map[ActorRef, List[ActorRef]] =
        Map[ActorRef, List[ActorRef]]()

    override def receive: Receive = {
        case CreateJam(name) => {
            if (!jams.contains(name)) {
                val jam = context.system.actorOf(Props(new JamActor(name)))
                jams += name -> jam
            }

            sender ! jams(name)
        }
        case DeleteJam(name) => {
            if (jams.contains(name)) {
                val jam = jams(name)

                if (spectators(jam).isEmpty) {
                    jam ! PoisonPill
                    spectators -= jam
                    jams -= name
                } else {
                    // TODO: send can't delete non-empty jam error.
                }
            }
        }
        case ListJams() => {
            sender ! jams.keySet
        }
        case JoinJam(name, socket) => {
            if (jams.contains(name)) {
                val jam = jams(name)
                val sockets = spectators(jam)
                spectators += jam -> (socket :: sockets)
                jam ! JamActor.Join(socket)
            }
        }
        case PartJam(name, socket) => {
            if (jams.contains(name)) {
                val jam = jams(name)
                val sockets = spectators(jam)

                spectators += jam -> sockets.filter(_ != socket)
                jam ! JamActor.Part(socket)
            }
        }
        case SpectatorTerminated(jam, socket) => {
            val sockets = spectators(jam)
            spectators += jam -> sockets.filter(_ != socket)
        }
        case Terminated(actor) => {
            // TODO: Clean up Jam sockets.
        }
        case msg => Logger.warn(s"JamHubActor Received unknown message $msg")
    }
}
