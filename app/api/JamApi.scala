package api

import akka.actor.ActorRef
import models.entities.Song

/**
 * Contains application messages for the JamActor.
 */
object JamApi {

    /**
     * Trait for JamActor messages.
     */
    sealed trait JamMessage

    /**
     * Add the socket to the list of spectators.
     * @param socket Socket to add
     */
    case class JamJoinedEvent(socket: ActorRef) extends JamMessage

    /**
     * Remove the socket from the list of spectators.
     * @param socket Socket to remove.
     */
    case class JamLeftEvent(socket: ActorRef) extends JamMessage

    /**
     * Event received in response to a PlayerStartCommand from the browser.
     */
    case class PlayerStartEvent() extends JamMessage

    /**
     * Query for the current state of the plaer.
     */
    case class PlayerStatusRequest() extends JamMessage

    /**
     * Event received from the Application when a song is uploaded.
     * @param song Metadata of uploaded song.
     */
    case class SongUploadedEvent(song: Song) extends JamMessage

}
