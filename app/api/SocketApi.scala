package api

import models.entities.Song

/**
 * Contains application messages for the SocketActor.
 */
object SocketApi {

    /**
     * Trait for messages a SocketActor receives.
     */
    sealed trait SocketMessage

    /**
     * Trait for messages the SocketActor sends to the browser.
     */
    sealed trait SocketProtocol

    // Initial Behavior

    // Receive From JS

    /**
     * Request to refresh the list of jams.
     */
    case class JamListRequest() extends SocketMessage

    /**
     * Request from the browser to join a Jam Session.
     * @param jamName Name from JamListResponse
     */
    case class JoinJamCommand(jamName: String) extends SocketMessage

    // Send To JS

    /**
     * Message sent to browser when Jam was successfully joined.
     * @param jamName Name of the Jam
     */
    case class JamJoinedEvent(jamName: String) extends SocketProtocol

    /**
     * Summary for a Jam.
     * @param name Name of the jam.
     * @param spectators Count of people currently joined to the jam.
     */
    case class JamInfo(name: String, spectators: Int)

    /**
     * Response for JamListRequest.
     * @param jams List of Jam names and spectator counts
     */
    case class JamListResponse(jams: List[JamInfo]) extends SocketProtocol

    // In Jam Behavior

    // Receive From JS

    /**
     * Command from the browser to put the Jam into the playing state.
     */
    case class StartPlayerCommand() extends SocketMessage

    /**
     * Command from the browser to skip the song.
     */
    case class SongSkipVoteCommand() extends SocketMessage

    /**
     * Command from the browser to leave the Jam.  The Socket should return
     * to the Initial behavior afterwards.
     */
    case class LeaveJamCommand() extends SocketMessage

    /**
     * Query from the browser for the current status of the Jam's player.
     */
    case class PlayerStatusRequest() extends SocketMessage


    // Receive From Jam -> Forward to JS

    /**
     * Received from Jam when the Player started playing.
     * This message is forwarded to the browser.
     */
    case class PlayerStartedEvent() extends SocketMessage
        with SocketProtocol

    /**
     * Received from the Jam when there are no more songs to play.
     * The message is forwarded to the browser.
     */
    case class PlayerStoppedEvent() extends SocketMessage with SocketProtocol

    /**
     * Received from the Jam when the song skip vote was successful.
     * This message is forwarded to the browser.
     */
    case class SongSkippedEvent() extends SocketMessage with SocketProtocol

    /**
     * Received from the Jam when a new file has be successfully uploaded.
     * The message is forwarded to the browser.
     * @param song Metadata of the enqueued song.
     */
    case class SongEnqueuedEvent(song: Song) extends SocketMessage
        with SocketProtocol

    /**
     * Response for a PlayerStatusRequest query.
     * This message is forwarded to the browser.
     * @param current The currently playing song.
     * @param position The current position in seconds.
     * @param playlist The list of enqueued songs.
     */
    case class PlayerStatusResponse(
        current: Song,
        position: Int,
        playlist: List[Song]
   ) extends SocketMessage with SocketProtocol

    // Send to JS

    /**
     * Event sent to the browser indicating that someone wants to skip the song.
     */
    case class SongSkipVoteStartedEvent() extends SocketProtocol
}
