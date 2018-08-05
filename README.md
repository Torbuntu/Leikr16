# Leikr16
A Java(libgdx) based Fantasy Computer(Console) that uses groovy scripted game files.

# Current features
- Terminal Emulator. Not very many commands yet.
- Groovysh repl by prefixing commands with gv.
- Small groovy games can be loaded and played by typing `load GameNameHere` then `run` games loaded from the ChipSpace directory.
- Exit a game with ESC
- 256 8x8 sprites loaded from GameName.png in the same folder as the GameName.groovy file.

# Planned Features
- 16 color palette (WIP)
- Sprite Editor(WIP)
- 'large' sprites by combining 2 or 4 sprite ID's (WIP)
- Tiled Maps (256x256 map file)
- Music
- SFX
- Online repository of game packages. example: `lkpm install Game.lkr`
- More advanced terminal/console
- Text Editor
- SFX Editor
- Music Editor

![Alt text](console.png?raw=true "Console")

![Alt text](loadgame.png?raw=true "loadGame")

- The method `load` now only takes the name of the game package. not .groovy extension.

![Alt text](pong.png?raw=true "Pong")

# Demo file showing a Sprite being drawn to the screen. The Sprite is 8x8 pixels but it is stretched to 32x32. In the demo it bounces around the screen like a sort of primitive screen saver.
![Alt text](BounceDemo.png?raw=true "Bounce demo")

# Work in progress sprite editor.
![Alt text](spriteEditor.png?raw=true "spriteEditor")
