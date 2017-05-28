# Curves

## Description
My basic clone of the famous CurveFever. It allows playing for up to 6 players. It was my first Java project done for academic purpose for Warsaw University of Technology. Program uses udp multicasting. It works with no problems on Linux, but sometimes is blocked on Windows due to some security issues.

## Build and run
### Requierments 
* gradle tool
* JDK or JRE 1.8
* Linux OS (preferrable)

### Building procedure
1. Build the project using 'gradle build' command
2. Go to gradle build directory 'cd build/libs'
3. Run either server or client: 
Server by typing `java -jar Curves-1.0.jar server port_number`.
Client by typing `java -jar Curves-1.0.jar client port_number`.
Port_number can be any free udp port apart from 9877

## Game

### Server
1. Using the slider choose desired number of players
2. Start the server by pressing 'start'. Anything else goes on automatically
⋅* Server waits for players to log in.
⋅* As soon as the desired number of players is logged, server starts the game. One signle round finishes when all the players are out of the game.
⋅* New round is automatically started
User can stop the game at any moment by pressing "stop". All the players listening to server are informed about that fact and the game stops. It can be then restarted, but the connection must be established again. The game also stops when all the players leaves the game. 

  
### Client
1. Connection can be established by clicking "Join" button.
Program accepts ip address only in "0.0.0.0" format. 
If the server is unreachable a suitable message is shown in the dialog. 
If server responds, information "waiting for other players" appears until the game starts. 
2. During the game any player can leave at any moment and the server will be informed about the fact. Leaving is possible by clicking "abbandon" or closing the window.
3. Move your curve by pressing LEFT_ARROW and RIGHT_ARROW


