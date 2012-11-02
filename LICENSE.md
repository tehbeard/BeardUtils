Copyright (C) 2012 Tehbeard

Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
and associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT 
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT 
OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Usage restrictions:

When using this software within the context of a bukkit plugin (herein referred to as the plugin), 
the developer must include the library using maven's shade plugin with said bukkit plugin, or by 
a similar mechanism that relocates the included parts of the library to a namespace used by the 
plugin. So as to avoid conflicts between plugins using incompatible versions of the library. 

i.e when compiled, the BeardUtils code MUST NOT reside within me.tehbeard.utils inside the plugin's
jar, it must instead reside under a package unique to that plugin to prevent issues arising from
differences in library versions (e.g. com.domain.bukkitPlugin.BeardUtils is an acceptable package
name for relocation).
  
Should no automated mechanism to relocate included library parts be available, source files may be 
copied directly into the project instead of shaded, so long as restrictions on package name stated
above are followed.
