# BeardUtils 
BeardUtils is a utility package that provides reusable code for bukkit plugins.

## Compiling
mvn install

## License
BeardUtils is licensed under a modified version of MIT. The modification is as follows:

  When using this software within the context of a bukkit plugin, the user must bundle the library
  using maven's shade plugin with said bukkit plugin, so that plugin code and BeardUtils code are within the same jar,
  with relocation of the BeardUtils package set so as to avoid conflicts between plugins using incompatible versions. 
  i.e when compiled, the BeardUtils code MUST NOT reside within me.tehbeard.utils inside the plugin jar, but com.herpaderp.bukkitPlugin.beardutils is acceptable
  
  Alternatively, source files may be copied directly into the project instead of shaded, so long as package name is changed
  to prevent conflicts between plugins using incompatible versions.
  
## What's inside (packages list)
* Addons      - classes related to loading external class files.
* commands    - classes for parsing command input, and an alternative to the CommandExecutor structure of bukkit.
* cuboid      - Classes for handling abstract regions and determining if an entity is inside one or more of them.
* expressions - Classes for parsing infix expressions and processing them.
* factory     - Classes for providing objects at runtime on demand, such as Data Providers.
* session     - Class for storing temporary data on a player.
* syringe     - Classes that use reflection to modify fields of other classes

## Packages in detail
### Addons
Addons contains AddonLoader, which is a class that can load .jar files and initialise classes inside them. It acts like a simplified version
of Bukkit's plugin loader. This is for plugins that need to load external optional code, that could have been written by users.
The best usecase is BeardAch. BeardAch uses AddonLoader to allow server admins and plugin authors to write custom triggers and rewards for achievements.
These can be dropped into plugins/BeardAch/addons

###commands
Commands consists of two main components, ArguementPack and CommandHandler.

ArgumentPack is a helper class that parses an argument list to provide boolean and option (string) flags. Leaving the programmer to worry about the action of 
a command instead of parsing flags.

CommandHandler and the annotations in the package provide an alternative to bukkit's CommandExecutor system that works alongside it. Enhancements include:
* Built-in processing of arguemnts to ArgumentPack.
* Handles permissions
* Clarity of code, without overkill (one method per command instead of one class per command/s) 

With CommandHandler, each command is handled by a static methods that take ArgumentPack as a parameter and return a boolean.
`
@CommandDescriptor(label="foobar")
public static boolean commandFooBar(ArgumentPack pack){
  pack.getSender().sendMessage("hello");
}
`

###cuboid
Need to map data to a spatial region? Don't want to depend on a protection plugin like WorldGuard? cuboid has you covered.
Cuboid provides a way to define an area and check if an entity is inside it.
ChunkCache provides a way to index data by cuboid, and search for it. the result is a list of CuboidEntry's, providing the cuboid and data associated with it.
CuboidSelector is a helper method that provides code for getting a cuboid object from a player, defined using a configurable "tool" item.

###expressions
expressions a mechanism for processing an infix expression, it supports +-*/() and powers (^), as well as variables and functions (definable by programmer). 
It can take a string such as @avg(20,9*(3-$foo)) and evaluate it, producing the value 23 if $foo is 0.
It includes a number of functions like average,min,max,mod

###factory
Factory makes it simple to automate runtime choices, such as data provider, class, spell etc. 
Add an annotation to a class, add it to the factory, and you can then pull objects out of it using a string value.

###Session
One class file that lets you store data associated with a player. Register the session object with bukkit's event system, and the session is cleared automatically on logout.

###Syringe
This is mainly for more advanced programmers, such as those using the addons and factory packages. It allows you to modify fields in a class based on annotation.
Uses for this would be to set the field to the value of another addon (see: http://www.sk89q.com/2011/10/how-i-stay-sane-while-updating-my-minecraft-server/ for a better example), or
even as a way to set configuration values, like so:
`
@Config("path.to.config.value");//programmer defined annotation
private int value = 0;//default value set

....
`
where an injector would take such an annotation, parse the config path and then set the field value to the value found.