### SASS Stylesheets for MathSpring Website
- To compile, run ```mvn sass:update-stylesheets```
- To watch sass task, run ```mvn sass:watch```
- If you are using **Intellij**, go to ```View -> Tools Windows ->
Maven Projects -> WoServer -> sass```

#### Project Structure
```
+-- 0_lib
|   +-- bootstrap
|   +-- normalize
+-- 1_base
|   +-- _vars
+-- 2_layout
|   +-- _page_name.sass
|   +-- _page_name.sass
|   +-- _page_name.sass
+-- 3_module
|   +-- _page_name
|   |   +-- _Block.sass
|   +-- _page_name
|   |   +-- _Block.sass
|   +-- _page_name
|   |   +-- _Block.sass
```

- 0_lib contains third-party CSS libraries
- 1_base contains base files such as reset, normalize, sass variables or mixins
- 2_layout contains styles for basic layout of web pages
- 3_module contains styles for different components of web pages

All sass files in the directory above should prepend _(underscore) to avoid being compiled by sass maven plugin

The stylesheets and HTML markup are adopting BEM methodology. 
For more information, visit [BEM - Block Element Modifier](http://getbem.com/introduction/)

The MIT License (MIT)
=====================

Copyright © `2017` `Huy Tran`

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the “Software”), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

