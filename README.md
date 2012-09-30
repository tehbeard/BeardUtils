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