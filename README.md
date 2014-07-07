Cloudpod
========

A real-time jukebox for the cloud.

Users can create a jam session and invite other people to join.

Each user can listen to the jam on their own computer.  Cloudpod tries to keep
all the players in sync.

Listeners can also:

  * Upload audio files
  * Enqueue songs to a jam's playlist
  * Vote songs off of a playlist

How it works
------------

At the moment Cloudpod simply uses websockets to synchronize html5 audio tags.
more sophisticated techniques will be tried later.

Patches welcome
---------------

Cloudpod does not at the moment support the following features but it could:

  * Uploaded file re-encoding
  * Streaming
  * Chromecast support
  
Building and Running
--------------------

Cloudpod is a Play framework 2.3-based application.  During development you can 
run it with:

    ./activator run

See the latest Play framework documentation for how to deploy it to production.

People who want to contribute should send a pull request.  See HACKING.md for
details.

License
-------

Cloudpod is released under the Apache license.  See LICENSE for details.
