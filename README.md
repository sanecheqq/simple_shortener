# simple_shortener
String shortener realized on the "Strategy" pattern.

Shortening means assigning a unique key to each string object, by which this string can also be easily obtained.

Shorteners are implemented on various HashMap: some are written by me on the basis of the standard HashMap, some are taken from third-party libraries (Guava, Apache Commons Collections).

Various tests have been written to compare the running time of strategies: some tests are written in a standard way, some with the help of Junit.
