# Cartways
A Minecraft server plugin for Minecraft 1.20+ which enables you create stations for minecarts and run minecarts without powered rails or furnace carts giving your server functional railways as an alternative to teleporting.

## Features
- Define stations for carts where they will automatically stop periodically and then continue or turn around if the station is the end of the line.
- Carts will run between stations without powered rails or a furnace cart.

## Installation
- Download the JAR file from the [releases](https://github.com/edbrn/Cartways/releases/) tab.
- Place the JAR file in your server's `plugins` folder and restart your server.

## Commands
### `/cartways create`

## Files managed by Cartways
`cartways.json` - TBC

## Developing
### Automated tests
#### Unit tests
Run `mvn test` to run the unit tests.

#### Formatting checks
Formatting of files is checked as part of the CI build using `mvn fmt:check`. You can automatically format files using: `mvn fmt:format`.

#### Linting
There are no automated linting rules at the moment. Code cleanup like removing unused imports relies on the developer.

### Building a development server
You need to make a server JAR to run your server.

- Make a temporary directory for the build and a directory for your development server:
  - `mkdir spigot-build`
  - `mkdir mc-server-dev`
  - `cd spigot-build`
- Obtain `BuildTools.jar` from the Spigot website (https://www.spigotmc.org/wiki/buildtools/) and place it in this folder
- Run `java -jar BuildTools.jar`
- A `spigot-{version}.jar` file will be made. Copy this into your development server folder `cp spigot-{version}.jar ../mc-server-dev`
- Run your server: `cd mc-server-dev` then `java -jar spigot-{version}.jar`.
- After accepting the EULA and running a second time, a `plugins` folder now exists.

### Installing the plugin during development
- Write code as needed
- Run `cd /path/to/Cartways/ && mvn package && cd /path/to/mc-server-dev/ && cp /path/to/Cartways/target/Cartways-{version}.jar plugins/ && java -jar {serverjar}.jar nogui` - for the version to expect, see `pom.xml`
