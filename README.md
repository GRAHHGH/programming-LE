CREDITS: 
Visual Assets: from "Pixel Frog"
Character & Enemy Sprites: Pirate and Crabby animations.
Environment Tileset: "Outside" and "Design" spritesheets used for level building.
User Interface: Buttons, sliders, and menu backgrounds designed for a nautical aesthetic.
Background Art: Big clouds, small clouds, and playing background layers.

Audio and Sound: from "Nico Staf, Sunny Travel" 
Music Composition: Level and Menu themes.
Sound Effects: Jump, attack, and environmental interaction sounds.

MEMBERS AND CONTRIBUTIONS
John David Jaca - Lead Developer, visual assets, basically overall
Francis Paul Sison - visual assests
Jaryl Pelayre Bancil - debugger, tester
Gerose Cubio - debugger, tester, UI

Tools and Assitance:
Ai Gemini Pro - use for debugging and finding some bugs 
Vscode - main IDE for coding
pixil - used for art 

HOW TO PLAY!!!!
MOVE LEFT - A 
MOVE RIGHT - D
Jump - SPACE
ATTACK - Mouse click (left click)
Pause Game - ESC button
Debug hitboxes - F5

Core Mechanics
Attacking: Use your sword to defeat Crabbies. They will strike you back
Health Bar: Keep an eye on your HP in the top-left corner. If it hits zero, it's Game Over.
Death & Respawn: If you die, you can choose to restart the level from the beginning.
Crabbies: They patrol platforms. If they see you, they will turn and move toward you to attack.
Cannons: Stationary turrets that fire Cannonballs if you cross their line of sight. You must time your jumps or movement to avoid the projectiles.

GOAL!!!!!
Your objective is to reach the end of the map by clearing the enemies and navigating the platforms. Once all enemies are defeated or the goal is reached, the Level Completed screen will appear, allowing you to move to the next challenge.


📁 Package Documentation
🔊 audio
Manages all sound-related functionality.
Key File: AudioPlayer.java handles the loading and playback of background music (songs) and sound effects (effects) using the Java Sound API.
👾 entities
Contains all "living" actors within the game world.
Expect to find: The base Entity class, the Player logic, and specific enemy types like Crabby. This package also handles EnemyManager for spawning and updating AI groups.
🎮 gamestates
Implements a State Machine pattern to transition between game scenes.
Expect to find: Separate classes for the main Menu, the Playing state (active gameplay), and GameOptions.
⌨️ inputs
The bridge between the user and the game.
Expect to find: Listeners for both KeyboardInputs and MouseInputs, which capture raw hardware signals and convert them into game actions.
🗺️ levels
Handles the structure and data of the game world.
Expect to find: Level.java (stores tile and object locations) and LevelManager.java (renders the levels and handles level switching).
🏗️ main
The entry point and primary game loop.
Expect to find: MainGame (starts the application) and Game.java (the central class that connects all other packages and handles the core update/render thread).
📦 objects
Contains interactive, non-human actors in the level.
Expect to find: Classes for Potion, GameContainer (boxes/barrels), Spike traps, and Cannon.
🎨 res (Resource Folder)
The directory for all non-code assets.
audio/: Contains .wav files for music and sound effects.
lvls/: Contains numerical .png images used to map out level layouts via color channels.
Top-level: Contains all sprite atlases for characters, objects, and tilemaps.
🖼️ UI
Expect to find: Visual overlays such as the PauseOverlay, GameOverOverlay, and various interactive UI components like VolumeButton and UrmButton.
🛠️ utilz (Utilities)
Contains static helper classes used globally across the project.
Expect to find: Constants.java (defining animation speeds and physics), HelpMethods.java (calculating collisions), and LoadSave.java (logic for importing external files).