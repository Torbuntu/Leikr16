# Leikr16
#### (Martyr16: the project that scope creeped itself to death to show the path to a better system)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Info
This version is deprecated and no longer supported. Now working on [ [Leikr](https://github.com/torbuntu/leikr) ] (without the 16)

A Java Fantasy Computer.
- Written in Java using libgdx and Groovy.
- Supports games written in Java, Groovy, and Jython (using the Leikr Engine)

# Contributors
- Uberoverlord [ [github](https://github.com/uberoverlord) ]
- Torbuntu
- mhbert [ [github](https://github.com/mhbert) ]

# Note
- best built with Java 10. Java 11 currently breaks.

# Specs
- Resolution 320x240
- 1024 8x8 Sprites (by means of 4 256 sprite sheets)
- 640x120 Tiled Map (each tile is 8x8). (4 screens tall and 16 wide)
- 128 WAV sound files per game [wip]

# Current features
- Terminal Emulator. 
- Groovysh repl in the terminal.
- Small games can be loaded and played by typing `load [game name] [optional: game type]` then `run` games loaded from the ChipSpace directory.
- Sprite Editor. (functional, but needs improvement)
- Github repo downloads using `setUserRepo [github username]` then `lpm install [repo name]`. Then to load the game use `mnt [game name]` which will load the game from the Download directory into ChipSpace. Then you can `load [game name]` and `run`. To set where the repository exists use the `setRepoType [repo website]` command. Options known to work are github and gitlab. To set repository user and type all at once use `setRepoSettings [repo name] [repo type]`.
- settings.properties file for customizing the colors of the background and font in terminal as well as the font itself (font name file and size in width and height).
- [WIP] Desktop Environment screen that is customizable through settings.properties and a programmed groovy file.
- Sound fx are now possible using the terminal command sfx to generate wav files into a projects audio directory. 

# Potential/Planned Features
- More advanced terminal/console commands (update games, upload games to repo, etc...)
- Code Editor
- Map Editor
- Music and editor
- SFX and editor
- Kotlin, Lua, and Scala support for game coding 

# Build instructions
- git clone repo.
- run gradle distZip.
- go to desktop/build/distribution and copy the contents of the zip to desired location (LeikrSystem)
- run desktop.jar with java -jar desktop.jar

# Progress
The trello board for tracking this project.
https://trello.com/b/EHNvZGzC/leikr16
