# Jisp

Jisp is a project I (Rocketnia) made in a few days in March 2008, at a time when I was reading about Lisp and excited by how simple it looked to make my own implementation.

Some aspects of what I read about lisp dialects seemed needlessly inconsistent, such as the non-function-like behavior of `quote` and the non-`quote`-like syntax of string literals.

I'd been designing my own programming languages in paper notebooks for years before this point, and something as simple as Lisp seemed like a good place to start building and experimenting.

Unlike most Lisp dialects, Jisp has macros that take unparsed strings as input, rather than s-expressions. Here's an example, which you can test for yourself by running `com.rocketnia.hacks.jisp.Jisp` with a compiled version of the Proj/src/ directory in your Java classpath:

```
> (if (eq 'apple (cdr (cons 'pine 'apple))) 'success 'failure)
result: success
> something-we-havent-defined
result: ()
> (function-we-havent-defined 'symbol)
result: (() (quote symbol))
> "(   this  is a     string )
result: "(   this  is a     string )"
> (doublequote      so   is   this   )
result: "so   is   this"
> "and-this
result: "and-this"
> (if (eq 'string (doublequote string)) (exit) 'whoops)
result: whoops
> (if (eq "string (doublequote string)) (exit) 'whoops)
[The REPL terminates.]
```

Jisp has unified semantics for functions, special forms, macros, and string literals, making it easier to implement them all at once. The evaluation model similar to an fexpr-based Lisp dialect like Kernel, but I didn't know that at the time. Perhaps due to the string-based syntax, an even closer comparison would be Tcl.

My tinkering with Jisp came to an end shortly after I got through the initial challenge of writing the parser. The implementation of `lambda` is incomplete, and there are no meaningful I/O operations.

Rather than building upon the Jisp codebase, I rebuilt Jisp's style of Lisp-inspired but string-based syntax from scratch in later projects like [Blade](https://github.com/rocketnia/blade), [Penknife Mk. I](https://github.com/rocketnia/penknife), and Chops (part of [Lathe Comforts for JS](https://github.com/lathe/lathe-comforts-for-js)).


## Installation and use

To run Jisp, first install a distribution of the JDK. We've used Eclipse Temurin 17.0.1+12 from Adoptium, but Jisp should work even on much older JVM versions. (At the time of development in 2008, I was developing in Java 6 on the Sun Microsystems JDK.)

Then, run a build with the Gradle wrapper, and run the main class with an appropriate classpath:

```bash
./gradlew build
java -classpath lib/build/classes/java/main com.rocketnia.hacks.jisp.Jisp
```
