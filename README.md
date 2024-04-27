# Cartways
A Minecraft server plugin for Minecraft 1.20+ which enables you create stations and speed signals for minecarts and run minecarts without powered rails or furnace carts giving your server functional railways as an alternative to teleporting.

## Features
- Stateless by design: just place signs and when carts detect stations or signals they will behave as instructed.
- Define stations for carts where they will automatically stop periodically and then continue or turn around if the station is the end of the line.
- Carts spawned by the plugin do not require powered rails.
- Control the speed of carts with signals.

## Installation
- Download the JAR file from the [releases](https://github.com/edbrn/Cartways/releases/) tab.
- Place the JAR file in your server's `plugins` folder and restart your server.

## Usage
In this early stage only oak wall signs with specific placement will work and carts will only run without power on iron rails.

### Stations
Place an oak wall sign at the end of a railway or to the side on top of a three-block high pole on the side above the rail.

The sign must have the first line `[STATION]`.

The rest of the lines you can use for whatever you like.

When trains reach these signs they will stop for a few seconds then continue.

### Speed signals
You can change the speed of a minecart as it is passed by placing a sign on the side of a block on the left side of the railway.

The sign must have the first line `[SPEED SIGNAL]`
The sign must have a valid number between 0 and 1 on the second line with a maximum of 2 decimal places. For example: `0`, `1`, `0.2`, `0.45`.

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
