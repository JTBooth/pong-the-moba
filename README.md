pong-the-moba
=============

The eventual structure of pong-the-moba (though not the current one) will go something like this:
First, start a Pong. Pong has a physics (jbox2d) representation of the board, a kryo server, and functions
to generate the graphics objects (slick2d) that will be served to the clients. To this end, a Pong holds
onto a PongServer (kryo). Pong itself extends BasicGame (slick2d), and is the home of the game loop. 

Every frame, the Pong will check its ServerListener and get a queue (currently, int[], using Keyboard.KEY_?) of
comands entered by each player that frame. It will then execute them all, advance the physics one step, and 
then serve the resulting graphics to all players.

Serving graphics:
Graphics are served in a DisplayUpdate, which contains the time at which it was sent (because UDP isn't guaranteed
to show up in order) or some proxy for it, currently System.nanoTime(), and a list of GamePieces. A GamePiece is
a slick2d shape object and a slick2d color. Because of Kryo's specialness, everything sent over the connection
must have a zero-argument constructor. So, SerializableCircle and SerializableColor have been created, extending
slick2d's classes. This is gross. We will deal. DisplayUpdate may need to be expanded to ship things like the
current mana/score/whatever. We should rename it if we do that.

Mapping Physics to Graphics:
Physics objects have to be about 1 long (0.1 to 10). Graphics objects have to be about 100 pixels long. So,
to get from physics scale to graphics scale, just multiply by 100. Physics has (0,0) in the bottom left. Graphics
has it in the top left. This is gross but not that hard to deal with. Watch for it. 
