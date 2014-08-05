package models.actors

import akka.actor._
import api.JamHubApi
import play.api.Logger

/**
 * Directory and supervisor of jam sessions.
 */
class JamHubActor extends Actor {
    import JamHubApi._

    private var jams: Map[String, ActorRef] = Map[String, ActorRef]()
    private var spectators: Map[ActorRef, List[ActorRef]] =
        Map[ActorRef, List[ActorRef]]()

    private def onApiMessage(message: JamHubMessage) = {
        case CreateJamCommand(name) => {}
    }

    override def receive: Receive = {
        case CreateJamCommand(name) => {
            if (!jams.contains(name)) {
                val jam = context.system.actorOf(Props(new JamActor(name)))
                jams += name -> jam
            }

            sender ! jams(name)
        }
        case DeleteJamCommand(name) => {
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
        case ListJamsRequest() => {
            sender ! ListJamsResponse(jams.values.toList)
        }
        case JoinJamCommand(name, socket) => {
            if (jams.contains(name)) {
                val jam = jams(name)
                val sockets = spectators(jam)
                spectators += jam -> (socket :: sockets)
                jam ! JamActor.Join(socket)
            }
        }
        case LeaveJamCommand(name, socket) => {
            if (jams.contains(name)) {
                val jam = jams(name)
                val sockets = spectators(jam)

                spectators += jam -> sockets.filter(_ != socket)
                jam ! JamActor.Part(socket)
            }
        }
        case Terminated(actor) => {
            // TODO: Clean up Jam sockets.
        }
        case msg => Logger.warn(s"JamHubActor Received unknown message $msg")
    }
}
