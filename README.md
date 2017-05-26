# Curves

My basic clone of the famous CurveFever. Game consists of two seperate apps: Server and Client. Server already compiled is included in the repo as .jar file. To run the client you have to build the project using gradle build command.

Server: ServerApp has a basic gui which allows a user to choose a desired number of players. By pressing start the server initializes and waits for players to log in. As soon as the desired number of players is logged, server starts the game. One signle round finishes when all the players are out of the game. Then a new round is started. User can stop the game at any moment by pressing "stop". All the players listening to server are informed about that fact and the game stops. It can be then restarted but the connection must be established again. The game also stops when all the players leaves the game. Server runs on udp port 9876.

Client: Client can establish connection to a server by clicking "Join" button. Program accepts ip address only in "0.0.0.0" format. The server port number is 9876 and client uses ports 9877 and 9878. If the server is unreachable a suitable message is shown in the dialog. If server responds, information "waiting for other players" appears until the game starts. During the game any player can leave at any moment and the server will be informed about the fact. Leaving is possible by clicking "abbandon" or closing the window.

Technical details: Program uses udp multicasting. It works with no problems on Linux, but sometimes is blocked on Windows due to some security issues.
