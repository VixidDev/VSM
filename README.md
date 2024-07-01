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

VSM is a personal Skyblock[^1] Minecraft mod of mine with the purpose of adding QOL features that I
want, that don't really fit in any widely distributed mod.

This mod was not made with the intention of other people using it, and its existence
on GitHub is solely for hosting the project somewhere and using version control. 
However, if you find this mod adds a feature / features that you also want from a mod,
you are welcome to download and use the mod yourself.

Currently, there exists a 1.8.9 Forge version and a 1.20.6 Fabric version. The same
features should exist in both version unless some limitation arises that causes a
feature to not be worth implementing in one of the versions.

> The mod is considered early development and features will be added as and when I want to add something

[^1]: Currently there are no Skyblock specific features, but since there possibly may be some Skyblock 
features in the future (as I only really play Hypixel Skyblock), and because of the mod name, I will
still refer to it as a 'skyblock' mod.

## Features

<details>
<summary>Spotify Display</summary>
    
Works by reading the window title of the Spotify desktop client
* Shows the current song artist and title in game
* Can control the song using keybinds set in the config
* Posts a chat message whenever a song changes
> Only the Windows JNA method for getting the window title is implemented at the moment, this feature
> will not work with other platforms yet.
</details>
