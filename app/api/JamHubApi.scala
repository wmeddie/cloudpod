package api

import akka.actor.ActorRef

/**
 * Contains application messages for JamHubActor
 */
object JamHubApi {

    /**
     * Trait for JamHub messages.
     */
    sealed trait JamHubMessage

    /**
     * Trait for outgoing JamHub messages.
     */
    sealed trait JamHubProtocol

    /**
     * Command to create a Jam.
     * @param name Name of the Jam to create
     */
    case class CreateJamCommand(name: String) extends JamHubMessage

    /**
     * Command to delete a Jam.
     * @param name Name of the Jam to delete
     */
    case class DeleteJamCommand(name: String) extends JamHubMessage

    /**
     * Query to get the list of Jams.
     */
    case class ListJamsRequest() extends JamHubMessage

    /**
     * Joins the socket to the Jam.
     * @param name Name of the Jam to join.
     * @param socket Reference to the socket that wants to join.
     */
    case class JoinJamCommand(name: String, socket: ActorRef)
            extends JamHubMessage

    /**
     * Leaves the socket from the Jam.
     * @param name Name of the Jam to leave
     * @param socket Reference to the socket that wants to leave.
     */
    case class LeaveJamCommand(name: String, socket: ActorRef)
            extends JamHubMessage

    /**
     * Response to the ListJamsRequest query.
     * @param jams The current list of Jams.
     */
    case class ListJamsResponse(jams: List[ActorRef]) extends JamHubProtocol

}
