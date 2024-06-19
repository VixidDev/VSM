<h1 align="center">VSM</h1>
<h3 align="center">Vixid's Skyblock Mod</h3>

<div align="center">
    <a href="https://github.com/VixidDev/VSM/releases/latest">
        <img src="https://img.shields.io/github/v/release/VixidDev/VSM?color=informational&include_prereleases&label=release&logo=github&logoColor=white" alt="release">
    </a>
    <a href="LICENSE" target="_blank">
        <img src="https://img.shields.io/github/license/VixidDev/VSM?color=informational" alt="license">
    </a>
</div>

VSM is a personal Minecraft mod of mine with the purpose of adding QOL features that I
want, that don't really fit in any widely distributed mod.

This mod was not made with the intention of other people using it, and its existence
on GitHub is solely for hosting the project somewhere and using version control.
However, if you find this mod adds a feature / features that you also want from a mod,
you are welcome to download and use the mod yourself.

Currently, there exists a 1.8.9 Forge version and a 1.20.6 Fabric version. The same
features should exist in both version unless some limitation arises that causes a
feature to not be worth implementing in one of the versions.

## Requirements

Requires the following libraries to work (only for the Fabric 1.20.6 version):

- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin/releases/)

## Features

<details>
<summary>Spotify Display</summary>

Works by reading the window title of the Spotify desktop client
* Shows the current song artist and title in game
* Can control the song using keybinds set in the config
* Posts a chat message whenever a song changes
> Currently uses powershell commands to query the window title so this will only work
on Windows devices
</details>