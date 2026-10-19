# Setup: HW1 bonus (Interval Analysis)

The workshop uses [Tai-e](https://github.com/pascal-lab/Tai-e), a static analysis framework for Java.
You only need a **JDK 17 or newer**: Gradle and Tai-e are downloaded automatically the first time you
build the project. It works the same on Windows, macOS and Linux.

## What to install (once)

1. **A JDK 17 or newer**, for example [Eclipse Temurin](https://adoptium.net/) 17 or 21.
   Check it in a terminal with `java -version`.
2. **An editor**, for example VS Code with the **Extension Pack for Java**
   (`vscjava.vscode-java-pack`), or IntelliJ IDEA.

## Opening the project

Clone your workshop repo and open the folder in VS Code (or IntelliJ). The Java extension detects the
Gradle project and downloads the dependencies; the first time takes a few minutes.

## Running tests and the analyses

In a terminal, from the project folder (on Windows, use `gradlew.bat` instead of `./gradlew`):

```bash
./gradlew test                                                                  # all tests
./gradlew intervalAnalysis -PtargetClass=seminar.dataflow.intervaltargets.IntervalAnalysis1 -Pmode=WIDENING
```

`intervalAnalysis` prints Tai-e's IR of each method together with the state your analysis computed for each
statement: a good way to understand what the analysis sees.

In VS Code you can also use **Terminal → Run Task...**: "Tests (gradlew test)" and "Interval
analysis on a class".

The test report ends up in `build/reports/tests/test/index.html`.

## Common problems

- **"Gradle requires JVM 17 or later to run"**: the JDK in use is older than 17. Check `java -version`
  (and `JAVA_HOME`, if set); in VS Code, check the JDK the Java extension uses ("Java: Configure Java Runtime").
- **Downloads are very slow or time out** (e.g. in mainland China): uncomment the mirror line in the
  `repositories` block of `build.gradle`. If the Gradle download itself is the slow part, change
  `distributionUrl` in `gradle/wrapper/gradle-wrapper.properties` to
  `https\://mirrors.cloud.tencent.com/gradle/gradle-9.8.1-bin.zip`.
- **`./gradlew: Permission denied`** (macOS/Linux): run `sh ./gradlew test`, or `chmod +x gradlew` once.
