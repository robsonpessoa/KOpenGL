# KOpenGL
Exercises of OpenGL implemented with Kotlin.

This project was built with Gradle version 5.2.1 and Java 11.

### Build
To create the jar, use:
```sh
./gradlew clean build shadowJar -Dorg.gradle.java.home="${JAVA_11_HOME}"
```

### Run
```sh
java -jar build/libs/KOpenGL-1.0-SNAPSHOT-all.jar
```
