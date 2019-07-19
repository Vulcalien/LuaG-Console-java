# LuaG Console
LuaG is a virtual console that allows you to build a game very quickly using lua.<br>
It works on 3 modes:
- cmd
- run game
- edit

The editor view contains a **map editor**.

## Getting Started
You can:
- clone and compile
- [download](https://github.com/Vulcalien/LuaG-Console/wiki/Download) a release

**I strongly recommend you to download a release** that will also contain blank '*console-userdata*' files.

**But** if you wish so, you can clone the repository and receive a copy of my test '*console-userdata*'.<br>
Just clone into your favourite Java IDE and compile it (don't forget to include the libraries in `lib` folder)

## Running
When you run the console, you have to choose what mode you want it to run.<br>

You have to pass an argument when calling the jar file:

| mode     | argument |
| -------- | -------- |
| cmd      | (none)   |
| run game | -run     |
| edit     | -edit    |

```batch
java -jar {console-file}.jar [argument]
```

If you don't pass any argument (maybe you just opened it without a terminal), the console will run in **cmd** mode.<br>
There you can write commands such as `run` and `edit`.

## How to create a Game
Your game files are all stored inside `console-userdata` folder. Please read about [Files and Folders](https://github.com/Vulcalien/LuaG-Console/wiki/Files-and-Folders) and [Game Lua Script](https://github.com/Vulcalien/LuaG-Console/wiki/Lua-Script) in the [wiki](https://github.com/Vulcalien/LuaG-Console/wiki).

## Game Deployment
At the moment, to deploy a game made with LuaG you have to **send to the final user both the console and your game files**. I suggest you to **pack them** into a *.zip* file.<br>
The final user will then open the console and type **run** in the internal CMD panel.

Please note that the console is still under development and a easier deployment system will probably come in the future.

## License
The Console is released under Apache 2.0 license. See [License](LICENSE).

## Built With
- [Bitmap Utility](https://github.com/Vulcalien/Bitmap-Utility) by [Vulcalien](https://github.com/Vulcalien/)
- [Gson](https://github.com/google/gson) by [Google](https://github.com/google)
- [LuaJ](http://www.luaj.org/luaj.html) by [LuaJ](http://www.luaj.org/)

## More Details
See the [wiki](https://github.com/Vulcalien/LuaG-Console/wiki).
