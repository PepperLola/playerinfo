# playerinfo
<p align="center">
  <a href="https://playerinfo-frontend.vercel.app" target="_blank"><img src="img/playerinfo_icon.png" width=256/></a>
</p>

[![Build Status](https://travis-ci.org/PepperLola/playerinfo.svg?branch=master)](https://travis-ci.org/PepperLola/playerinfo)
[![GitHub issues](https://img.shields.io/github/issues/PepperLola/playerinfo)](https://github.com/PepperLola/playerinfo/issues)
[![GitHub stars](https://img.shields.io/github/stars/PepperLola/playerinfo)](https://github.com/PepperLola/playerinfo/stargazers)
[![GitHub All Releases](https://img.shields.io/github/downloads/PepperLola/playerinfo/total)](https://github.com/PepperLola/playerinfo/releases/latest)
![Visits](https://badges.pufler.dev/visits/PepperLola/playerinfo)
[![GitHub followers](https://img.shields.io/github/followers/PepperLola?label=Follow%20Me%21&style=social)](https://github.com/PepperLola)
### A Minecraft mod that does some cool stuff.

<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#features">Features</a></li>
  </ol>
</details>

---

### Features

* Scoreboard Mod
* Lifx Mod
  * Allows you to configure a LIFX light in-game.
* Background Blur
* Display Tweaks
  * Remove water overlay. 
  * Remove pumpkin overlay, and replace it with a pumpkin icon somewhere on the screen.
  * Lowers fire animation. 
* Note Block Helper
  * Shows the tuning of note blocks in chat when you change it. 
* Coordinate HUD
  * Shows current coordinates on screen.
* Movement
  * Toggle Sprint
  * Toggle Crouch
* Bedwars Resources
  * Shows how much of each resource you have in your inventory.
* CPS Display
* FPS Display
* Reach Display
  * Shows how far away the player is that you hit.
* Hypixel Events
  * Shows a notification and plays a sound in-game when something happens on Hypixel.
  * Currently only supports a friend joining or leaving, but posts an event to the event bus when that happens.
* Discord RPC
  * Shows the game and mode you're playing on Hypixel.
* Ping Display
  * Only works in lobbies.
* Particle Mod
  * Allows you to change which particles are creates as crit particles.
* Armor HUD
  * Shows what type of armor and durability of armor on screen.
* Perspective Mod
  * Allows you to change the camera angle around you like in F5 but without moving your head. 
* Time Changer
  * Changes the time in game. 
  * Supports fast mode, which will make time go by faster.
* Old Animations
  * Uses old 1.7 blockhit, bow, rod, eating, sword, and held item animations. 
* Custom Main Menu
  * Configurable to be either black and white or chroma.
* Super Zoom
  * Zooms in farther than Optifine zoom, but Optifine is still highly recommended.
* Calculator</li>
  * Parses expression into a tree, then evaluates recursively.</li>
* Player Information
  * Gets information about a Minecraft player.
  * Namesake and initial function of the mod.
* [Macros](https://github.com/PepperLola/playerinfo/wiki/Macros)
  * Lets the player write small scripts that are bound to an event, and run when that event is triggered.

### Prerequisites
1. Make sure you've installed [Minecraft Forge](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.8.html). **Make sure you're using the latest version instead of the recommended version or it will crash.**
2. You'll probably want to create a separate game directory for playerinfo to run in. More info <a href="#directory">here</a>.
3. Run this version of Forge at least once in order to create the mods folder.

### Getting Started
1. Go to the [playerinfo website](https://playerinfo-frontend.vercel.app).
2. Click download. It should automatically take you to the latest release of playerinfo on GitHub. You can also click <a href="https://github.com/PepperLola/playerinfo/releases/latest">here</a>.
3. Download the release. Make sure you download `playerinfo-XX.XX.XX.jar` and **not** `playerinfo-XX.XX.XX-sources.jar`. You don't want the sources jar.
4. Put the jar you downloaded into your mods folder and start Forge.
5. Report any issues <a href="https://github.com/PepperLola/playerinfo/issues">here</a>, as well as any feature requests. Be sure to either include `[Bug]/[Feature Request]` in the title, depending on the reason behind the issue.

### <a name="directory"></a>Game Directory
1. Create a new folder in your `.minecraft` directory. Name it something along the lines of `Forge playerinfo`. You can name it whatever you want, but descriptive names are better so you'll remember what the folder was for in the future.
2. Edit the installation of Forge in the Minecraft launcher by clicking `Installations`, scrolling to the one for Forge 1.8.9 that you're using, click the 3 dots and choose `Edit`. Under `Game Directory`, click browse and navigate to the folder you created in the previous step.
3. Choose that folder, and save the settings for the installation.

### Note
This project was created for fun, and I don't expect anyone but myself (and maybe some friends) to actually use it. I would recommend instead using a more well-known, well-made and well-maintained PvP client such as LunarClient. I would recommend Badlion but ewww.
