# QuickParse

QuickParse is a "de-templating" library for fast prototyping born during solving 
tasks from Advent of Code 2021.

## QuickStart

### Introduction and templates

Let's say we have an input that is clearly derived from some kind of template, but differs with arguments:

```
target area: x=20..30, y=-10..-5
```

```
target area: x=3..12, y=-1..-52
```

QuickParse wants to make obtaining values from this format as fast as possible - from the viewpoint of you, the programmer (who likes parsing anyways?).

Firstly, we need to define the QuickParse template:

```
target area: x=$(int){x1}$..$(int){x2}$, y=-$(int){y1}$..$(int){y2}$
```

every potential object that we want to catch needs to be signified with the dollar sign symbol (``$``). 
Naming the object (using curly brackets ``{``, ``}``) and type of the object (using brackets ``(``, ``)``) is optional - 
QuickParse defaults the type to ``String``, and does not give a name to the objects.

To escape dollar sign parsing, you can replace it with the double dollar sign (``$$``). Escaping in object names and type names
is currently not supported.

Keep in mind that *complex* capture groups (those with name or type) must start and end with the dollar sign, however simple
capture groups are composed of a single dollar sign.

### Outputs

QuickParse supports three default output options:
- to class instance
- to map
- to list

Exporting parsed objects to the class instance requires you to follow the naming structure of given class - so here
using names for the objects is required. Exporting parsed objects to the list ignores their names.

### Code example

```java
public class Coords {
    private int x1;
    private int x2;
    private int y1;
    private int y2;
}
```

```java
String template = "target area: x=$(int){x1}$..$(int){x2}$, y=-$(int){y1}$..$(int){y2}$";
String input = "target area: x=11..12, y=-13..-14";
Coords coords = QuickParse.parseToObject(template, input, new Coords(), Coords.class);
coords.x1; //<-- now it's 11
coords.x2; //<-- now it's 12
coords.y1; //<-- now it's -13
coords.y2; //<-- now it's -14
```

### Optimization

QuickParse supports pattern compilation for reuse.


## Parsers

Out-of-the-box QuickParse supports following type parsers:

- String (default) - returns the input string
- int - tries to parse the int as an integer
- Integer - tries to parse the int as a null value or an integer
