# Werewolves
The popular game werewolves now becomes digital. In this social deduction game, different fractions (werewolves, villagers etc.) try to seize control of the village through an interactive voting system. During the night the werewolves awake and kill one of the sleeping unknowing villagers. Furthermore hidden magical creatures like the witch, cupid or seer act in their secretive manner. During the day the villagers try to free their town by voting on who's secretly a werewolf.

An integrated voicechat allows the crucial discussions during the voting phases to take place.
## Technologies

- Gradle
- Java Spring boot
- REST Interface
- H2 in-memory db
- Agora.io voicechat

## Launch & Deployment
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
1. File -> Open... -> werewolf-server
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `werewolf-server` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Generate Jacoco Reports

```bash
./gradlew test jacocoTestReport
```

## Contributing

We welcome contributions to enhance and improve the Werewolves game. If you would like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make the necessary changes and commit them.
4. Push your branch to your forked repository.
5. Submit a pull request to the main repository.

We appreciate your contributions and will review your pull request as soon as possible.

## High-level components

### Lobby ([Lobby.java](./src/main/java/ch/uzh/ifi/hase/soprafs23/logic/lobby/Lobby.java))
The lobby aggregates players and roles and is responsible for assigning players to their respective roles.  
Because a lobby can be used for multiple games, it does not aggregate a game object.

### Game ([Game.java](./src/main/java/ch/uzh/ifi/hase/soprafs23/logic/game/Game.java))
The Game class aggregates information about the current state of the game, like the current stage or the  
results of passed polls. It also serves as the main access point to logic components for the service layer.

### Poll ([Poll.java](./src/main/java/ch/uzh/ifi/hase/soprafs23/logic/poll/Poll.java))
Voting is the main component of the game, all actions are executed via votes of one or more players.  
This complexity is modeled in the poll class (Poll instead of Vote, in order to not confuse the noun and verb vote).
The poll class aggregates its participants, its options with their corresponding actions and what happens when a poll is tied.

## Roadmap

The application was built in a modular approach to easily allow extending it.
The following features are not yet implemented but could improve the Game play:

### 1. Additional roles

Implementing further roles could make the Game more interesting. E.g.

- Wolf cub
- Jester
- "Blinzelmädchen" /-"bübchen"
- Vampire
- Guard

Also see https://werwolf.fandom.com/de/wiki/Werwolf-Rollen-Sammlung for further roles.

### 2. User Statistics

Storing Game Statistics for every user would provide interesting insights into the abilities of each user. The following benchmarks could be interesting:

- How many Games has a user played / won ?
- How often has the user played which role ? How often did the user win in those roles?
- In which day-night cycle was the user usually killed


### 3. Lobby Join System

Currently users are only able to play a game, if they have a group of friends that are currently online and exchange the join lobby code. To allow users to play the game with strangers it would make sense to develop an open lobby listing feature, where users can view and select from available lobbies. This can be implemented by providing a user interface that displays the open lobbies along with relevant details such as lobby name, number of players, and game settings. Players can then select a lobby from the list and join directly.

Additionally users that are in a lobby should be able to invite users (by their username or similar) to join the game.

## Authors and acknowledgment

The application was developed for the SOPRA 23 course by:

- Jan Lüthi ([@Dev-Lj](https://github.com/Dev-Lj))
- Michel Sabbatini ([@Atomis14](https://github.com/Atomis14))
- Marvin Wiedenkeller ([@ChlineSaurus](https://github.com/ChlineSaurus))
- Miro Vannini ([@mirovv](https://github.com/mirovv))
- David Scherrer ([@djscherrer](https://github.com/djscherrer))

A special thank you to our TA Jerome Maier for supporting us during the project.

## License
This project is licensed under the MIT License - see the [LICENSE.md](License.md) file for details