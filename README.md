Project demonstrating a strange interpretation of the JSpecify `@NullMarked` annotation by `kotlinc`.

The upstream project, written in Java, is using JSpecify's `@NullMarked` to indicate that the type parameter of
`Supertype<T>` and `Subtype<T>`, as well as `Supertype#get()`, cannot be nullable types or return `null`.

The downstream `lib` project, written in Kotlin, does not use any JSpecify annotations, but this shouldn't be needed
as Kotlin provides built-in nullability annotations.

However, for some strange reason, `kotlinc` thinks that the extension function in `SubtypeExt.kt` has a receiver type
which is **both** non-null and nullable, which is very strange. It should obviously be non-null, as there is no `?`
attached to it. If you try to compile this project with `gradle build`, it will fail:
```kotlin
e: warnings found and -Werror specified
w: file://[redacted]/kotlin-jspecify-oddity/lib/src/main/kotlin/sample/jspecify/oddity/different/SubtypeExt.kt:3:71 Type mismatch: inferred type is Subtype<CapturedType(out T)> but Supertype<out Nothing> was expected
```
Or, if you supply `this.` in front of the `get()`:
```kotlin
e: warnings found and -Werror specified
w: file://[redacted]/kotlin-jspecify-oddity/lib/src/main/kotlin/sample/jspecify/oddity/different/SubtypeExt.kt:5:71 Unsafe use of a nullable receiver of type Subtype<CapturedType(out T)>
```
Which is plainly wrong. And finally, using `this?.` to make it safe produces:
```kotlin
e: warnings found and -Werror specified
w: file://[redacted]/kotlin-jspecify-oddity/lib/src/main/kotlin/sample/jspecify/oddity/different/SubtypeExt.kt:5:75 Unnecessary safe call on a non-null receiver of type Subtype<out T>
```
This is a correct warning, but it's strange that it disagrees with the previous invocation.

This behavior does not reproduce without the `@NullMarked` in the upstream project. A workaround is to suppress
`NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS`.
