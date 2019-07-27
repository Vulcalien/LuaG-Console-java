# LuaG Console
LuaG is a virtual console that allows you to build a game very quickly using lua.  
The console comes with an integrated game editor.

## Getting Started
Just [download](https://github.com/Vulcalien/LuaG-Console/wiki/Download) a release and start programming your game!  
Releases contain blank game files, so you can start programming without caring about setting up files and folders.

## Running
When you run the console, you have to choose what mode you want it to run.  
There are `dev` mode and `user` mode.  
User mode is **default**, so just double-click the jar and it will open the game.  
But if you are creating/editing a game, you have to pass the argument `-dev` like in the example.

```batch
java -jar console-{version}.jar -dev
```

Dev mode allows you to use the game editor and restart your game without closing the console.  
If you open the console in this mode its cmd will show up. There you can write commands such as `run` and `edit`. (Use `help` for a list of commands)

## How to create a Game
Your game files are all stored inside `console-userdata` folder. You can read about [Files and Folders](https://github.com/Vulcalien/LuaG-Console/wiki/Files-and-Folders) and [Game Lua Script](https://github.com/Vulcalien/LuaG-Console/wiki/Lua-Script) in the [wiki](https://github.com/Vulcalien/LuaG-Console/wiki).

## Game Deployment
To deploy a game you can pack the console and your game's files inside a *.zip file* and send it.

## License
The Console is released under Apache 2.0 license. See [License](LICENSE).

## Built With
- [Bitmap Utility](https://github.com/Vulcalien/Bitmap-Utility) by [Vulcalien](https://github.com/Vulcalien/)
- [Gson](https://github.com/google/gson) by [Google](https://github.com/google)
- [LuaJ](http://www.luaj.org/luaj.html) by [LuaJ](http://www.luaj.org/)

## More Details
[Click here](https://github.com/Vulcalien/LuaG-Console/wiki) to see the wiki.
