Copyright (C) 2012 Tehbeard

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

When using this software within the context of a bukkit plugin, the user must bundle the library
using maven's shade plugin with said bukkit plugin, so that plugin code and BeardUtils code are within the same jar,
with relocation of the BeardUtils package set so as to avoid conflicts between plugins using incompatible versions. 
i.e when compiled, the BeardUtils code MUST NOT reside within me.tehbeard.utils inside the plugin jar, but com.herpaderp.bukkitPlugin.beardutils is acceptable
  
Alternatively, source files may be copied directly into the project instead of shaded, so long as package name is changed
to prevent conflicts between plugins using incompatible versions.