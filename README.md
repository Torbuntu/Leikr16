# Leikr16
A Java Fantasy Computer.
- Written in Java libgdx and Groovy.
- Supports games written in Java and Groovy (using the Leikr Engine)

# Specs
- Resolution 256x240
- 256 8x8 Sprites
- 640x120 Tiled Map (each tile is 8x8). (4 screens tall and 20 wide)

# Current features
- Terminal Emulator. Not many commands yet.
- Groovysh repl by prefixing commands with `gv`.
- Small groovy games can be loaded and played by typing `load GameNameHere` then `run` games loaded from the ChipSpace directory.
- Exit to Terminal with ESC
- Sprite Editor. (functional, but needs improvement)

# Potential/Planned Features
- System Config file to set defaults to the system before system boot. Such as default terminal background and font color, or to boot into a graphical environment.
- Customizable Graphical interface instead of terminal (after setting up a System Config that points to a graphical environment on boot. API for this to be created )
- Map Editor
- Music
- SFX
- Online repository of game packages. example: `lkpm install Game.lkr`
- More advanced terminal/console
- Code Editor
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
