Cloudpod is a Play framework-based Scala application.  It uses Akka to handle
the state of each Jam session and Web sockets to push changes to each client.

Formatting
==========

  * Keep lines under 80 characters long.
  * indent code with 4 spaces, no tabs.
  * closure parameter lists on same line if under 80 chars.
  * When closing multi-line braces, parens and quotes use a separate line.
    * Otherwise follow the official Scala style guide[1]
  
Other Guidelines
================

  * Prefer private methods to limit surface area.
  * Use package private for private methods that need to be tested.
  * ScalaDoc comments required for all Public classes/methods.
  * Stick to domain vocabulary as much as possible.
  * Commit messages should be in present tense.
  * Pull requests should either pass all tests or have a commit that updates
    them until they do with a comment explaining why it was necessary.

Vocabulary
==========

  * Jam / Jam session
    A room where people can join in to listen to music.
  * Spectator
    The users joined to a Jam.
  * Player
    A player refers to the HTML5 audio element on each Spectator's browser. The 
    player can either be playing the current song of the jam or stopped.
  * Playlist
    The queue of songs that will be played in the jam session.
  * Library
    The global repository of uploaded songs.
  
[1]: http://docs.scala-lang.org/style/
