# Java

## Datatypes

- Identifiers
- Assignment
- Datatypes
    - boolean
    - byte - 8 bit
    - short - 16
    - int - 32
        - octal - starts with 0
    - long - 64L
        - hexadeximal - starts with 0x  
    - float - 32 F or f
    - double - 64
        - infinity, NAN
    - char literals - 16 (0-65536) - ASCII

### Strings

String literals - Char Sequence

- concat using +
- equals() - check object equality
-  == checks for reference equality


### String Builder

```
  public static void main(String[] args) {
  
    // Create an empty StringBuffer
    StringBuilder sb = new StringBuilder();
    printDetails(sb);
    
    // Append "good"
    sb.append("good");
    printDetails(sb);

    // Insert "Hi " in the beginning
    sb.insert(0, "Hi ");
    printDetails(sb);

    // Delete the first o
    sb.deleteCharAt(1);
    printDetails(sb);

    // Append "  be  with  you"
    sb.append(" be  with  you");
    printDetails(sb);

    // Set the length to 3
    sb.setLength(3);
    printDetails(sb);

    // Reverse the content
    sb.reverse();
    printDetails(sb);
  }

```


### Arrays

- `int[] myIntArray; or int myIntArray[];`
- `int a1[] = new int[3]; or int[] a2 = new int[3];` 
- `arrayName.length`
- `int[] primes = {2, 3, 5, 7, 11, 13, 17};    // An array of 7 elements `
- Indexing - `primes[2] // 5`
- `int twoD[][] = new int[4][5];`
    
```
for (i = 0; i < 5; i++)
    result = result + nums[i];
```
        
```
    int days[] = {1, 2, 3,};
    for(int i:days){
      System.out.println(i);
    }
```

```
int twoD[][] = new int[4][5];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 5; j++) {
        twoD[i][j] = i*j;
      }
    }
```

```
int nums[][] = new int[3][5];

    // give nums some values
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 5; j++)
        nums[i][j] = (i + 1) * (j + 1);

    // use for-each for to display and sum the values
    for (int x[] : nums) {
      for (int y : x) {
        System.out.println("Value is: " + y);
        sum += y;
      }
    }
```

```
double m[][] = { 
            { 0, 1, 2, 3 }, 
            { 0, 1, 2, 3 }, 
            { 0, 1, 2, 3 }, 
            { 0, 1, 2, 3 } 
        }; 
```

##### Array Declaration

```
// salary can  hold  multiple float  values 
float[] salary;

// name can  hold  multiple references to String objects
String[]  name;

// emp can  hold  multiple references to Employee objects
Employee[]   emp;
```

##### Array Creation

```

// Create an  array object
int[] myID = new int[5]; 
```

##### Explicit Array Initialization

```
// Initialize the array at the time of declaration 
int[] myID = {1,   2, 3, 4, 5};

// A  comma   after the   last  value 5  is valid.
int[] myID = {1, 2, 3, 4, 5, }; 

// Also valid
int[] myID = new int[]{1, 2, 3, 4, 5};

String[] names = {new String("A"), new String("B")};

String[] names = {"C",  "D"};

```

##### Length of an Array

```
int[] myID = new int[5]; // Create an  array of  length 5 
int len = myID.length;  // 5  will be  assigned to len
```

##### Copy an Array

```
public static  void  arraycopy(Object sourceArray, 
                               int  sourceStartPosition, 
                               Object destinationArray,
                               int destinationStartPosition, 
                               int  lengthToBeCopied)
                               
Called as: System.arraycopy (sourse,  0, dest, 0, noofelementsToCopy);
```



### Increment and Decrement

- `x = x + 1; or x++; `

| Initial Value of x |	Expression | Final Value of y | Final Value of x |
| --- | --- | --- | --- | 
|5|	y = x++|	5	|6 |
|5	|y = ++x|	6	| 6 |
|5	|y = x--|	5	| 4 |
|5	|y = --x|	4|	4 |

### Java Logical Operators Shortcut

The OR operator results in true when one operand is true, no matter what the second operand is. The AND operator results in false when one operand is false, no matter what the second operand is. If you use the || and &&, Java will not evaluate the right-hand operand when the outcome can be determined by the left operand alone.

### If - else

```
if(condition) 
   statement; 
else if(condition) 
   statement; 
else if(condition) 
   statement; 
. 
.
else 
   statement;
```

### Switch

```
switch (expression) { 
case value1: 
    statement sequence 
    break; 
case value2: 
    statement sequence 
    break; 
. 
. 
. 
case valueN: 
    statement sequence 
    break; 
default: 
    default statement sequence 
}
```

### Class

A class in Java may consist of five components:

* Fields
* Methods
* Constructors
* Static initializers
* Instance initializers

Fields and methods are also known as members of the class.

Classes and interfaces can also be members of a class.

A class can have zero or more class members.

Constructors are used to create objects of a class. We must have at least one constructor for a class.

Initializers are used to initialize fields of a class. We can have zero or more initializers of static or instance types.

### Static and Instance methods

```
// A  static or  class method 
static void  aClassMethod()  {
    
}

Called as: Classname.aClassMethod()

// A  non-static or  instance method 
void  anInstanceMethod()  {

}

Called as: Instancename.anInstanceMethod()
```

### This

Java has a keyword called this. It is a reference to the current instance of a class.

It can be used only in the context of an instance.

### Initialization Block

- An instance initialization block is used to initialize objects of a class. - Once for every instance

- A static initialization block is also known as a static initializer. It is similar to an instance initialization block. It is used to initialize a class. An instance initializer is executed once per object whereas a static initializer is executed only once for a class when the class definition is loaded into JVM.

```
public class Main {
  private static int num;
  
  // An instance initializer
  {
    System.out.println("Inside instance initializer.");
  }
  
  // A static initializer. Note the use of the keyword static below.
  static {
    num = 2014;
    System.out.println("Inside static initializer.");
  }

  // Constructor
  public Main() {
    System.out.println("Inside constructor.");
  }
}

```


### Immutable objects

An object whose state cannot be changed after it is created is called an immutable object.

A class whose objects are immutable is called an immutable class.

An immutable object can be shared by different areas of a program without worrying about its state changes.

An immutable object is inherently thread-safe.

```
// Immutable class for int
public  class  IntWrapper {
    private  final  int  value;
    
    public IntWrapper(int value) {
        this.value = value;
    }
    public int  getValue() {
        return value;
    }
}

>> IntWrapper wrapper  = new IntWrapper(101);

```


```
class Box {
  int width;
  int height;
  int depth;
}
public class Main {
  public static void main(String args[]) {
    Box myBox = new Box();
    myBox.width = 10;

    System.out.println("myBox.width:"+myBox.width);
  }
}
```
- object instanceof class


### Methods and constructors

```
class Box {
  int width;
  int height;
  int depth;
  
  Box(Box ob) { // pass object to constructor
    width = ob.width;
    height = ob.height;
  }

  Box(int w, int h) {
    width = w;
    height = h;
  }

  // constructor used when no dimensions specified
  Box() {
    width = -1; // use -1 to indicate
    height = -1; // an uninitialized
  }
  
  // initialize a and b to the same value
  Box(int i) {
    this(i, i); // invokes Box(i, i)
  }

  // constructor used when cube is created
  Box(int len) {
    width = height = len;
  }
  
  void setDim(int w, int h) { // Method with parameters
    width = w;
    height = h;
  }
  
  int calculateVolume() {
    return width * height * depth;
  }
}

public class Main {

  public static void main(String args[]) {
    Box mybox1 = new Box();
    mybox1.width = 10;
    mybox1.height = 20;
    mybox1.depth = 15;

    int vol = mybox1.calculateVolume();

  }
}
```

### Testing

Test suite knows more about the code than us in the long run

#### 10 rules for writing great testcases

<img src=http://i.imgur.com/Fvv9Zw9.png width="300">

### Annotations

A retention policy determines at what point an annotation is discarded.

Java defines three such policies:SOURCE, CLASS, and RUNTIME.

SOURCE is retained only in the source file and is discarded during compilation.
CLASS is stored in the .class file during compilation. It is not available through the JVM during run time.
RUNTIME is stored in the .class file and is available through the JVM during run time.

**Built-In Annotations**

Java defines many built-in annotations. Most are specialized, but seven are general purpose.

* @Retention
* @Documented
* @Target
* @Inherited
* @Override
* @Deprecated
* @SuppressWarnings


### Collection

```

public class CollectionsDemo {

    public static void main(String[] args) {

        List<Integer> arrayList = new ArrayList<>();

        List<String> linkedList = new LinkedList<>();

        Iterator<Integer> alIterator = arrayList.iterator();

        ListIterator<String> stringIterator = linkedList.listIterator();

        HashSet<Integer> integerHashSet = new HashSet<>();

        HashSet<Integer> linkedHashSet = new LinkedHashSet<>();

        TreeSet<String> stringTreeSet = new TreeSet<>();

        Queue<Long> queue = new LinkedList<>();

        Queue<String> priorityQueue = new PriorityQueue<String>();

        Deque<Byte> deque = new ArrayDeque<>();

        // one null keys, multiple null vals
        HashMap<String, String> stringHashMap = new HashMap<>();

        // one null keys, multiple null vals
        HashMap<Integer, String> intHashMap = new LinkedHashMap<>();

        // No null keys
        TreeMap<Integer, Integer> intTreeMap = new TreeMap<>();

        stringHashMap.put("Naren", "Manoharan");
        stringHashMap.put("Shri", "Manoharan");
        stringHashMap.put(null, "HEllo");

        stringHashMap.entrySet()
                .stream()
                .forEach(entry -> System.out.println("Key: " + entry.getKey() + " " + "Val: " + entry.getValue()));


        // No null keys or vals
        Hashtable<Integer,String> hashtable = new Hashtable<Integer,String>();

    }

}

```

