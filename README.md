# Fox in a Hole Game Simulation

## Overview

This project is a game theory implementation that simulates the "Fox in a Hole" game, a classic pursuit-evasion problem. The simulation models interactions between a farmer (pursuer) and a fox (evader), each employing different strategic approaches to achieve their objectives.

This implementation was developed as coursework to explore game theory concepts and strategy optimization in adversarial scenarios.

## Project Description

The Fox in a Hole simulation demonstrates various player strategies and their effectiveness through multiple simulation runs. The program supports:

- **Farmer Strategies:**
  - Optimal: Uses computed optimal play strategy
  - Random: Makes random moves

- **Fox Strategies:**
  - Random: Moves randomly without pattern
  - RandomWithStay: Random movement with the option to remain in current position

## Requirements

- **Java Development Kit (JDK):** Version 21 or earlier
- **Build Tool:** Gradle (included via Gradle wrapper)
- **IDE Extensions (for VS Code):**
  - Extension Pack for Java
  - Kotlin Language Support
  - Related language extensions

## Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Ryan7od/GameTheoryCW.git
cd GameTheoryCW
```

### 2. Install Java JDK
Ensure you have Java JDK 21 or earlier installed on your system. You can download it from [oracle.com](https://www.oracle.com/java/technologies/downloads/) or use a package manager.

### 3. Verify Java Installation
```bash
java -version
```

### 4. (Optional) VS Code Setup
If using VS Code, install the recommended extensions:
- Extension Pack for Java (Microsoft)
- Kotlin (fwcd)
- Gradle for Java (Microsoft)

## Building the Project

The project uses Gradle for dependency management and compilation.

### Build the Project
```bash
./gradlew build
```

On Windows:
```cmd
gradlew.bat build
```

### Run the Application
```bash
./gradlew run
```

On Windows:
```cmd
gradlew.bat run
```

## Usage

### Running Simulations

The main function provides access to the `runMany` method, which executes multiple simulation runs with specified strategies and print the average iterations it took for the farmer to catch the fox:

```kotlin
runMany(
    num: Int,                       // # of threads to launch
    iterations: Int,                // # of iterations to run on each thread
    n: Int,                         // # of holes in the game
    farmerFactory: FarmerFactory,
    foxFactory: FoxFactory,
)
```

For each Factory, you must build it using n, the number of holes.

The function used by `runMany` is `runSim`, which executes one simulation run over a specified number of iterations, returning the average iterations it took for the farmer to catch the fox and is run over `num` threads by runMany:

```kotlin
fun runSim(
    iterations: Int,
    n: Int,
    farmerFactory: FarmerFactory,
    foxFactory: FoxFactory,
)
```

You may create new functions, similar to `runSim` and the respective `runMany`, in order to compute different statistics about the game.

### Example Usage

The following main function runs 100,000,000 iterations of the game, split over 10 threads, using the Optimal farmer and RandomWithStay fox on a game size of 8 holes:

```kotlin
fun main() = runBlocking {
    val n = 8
    val result = runMany(10, 10000000, n, OptimalFarmerFactory(n), RandomWithStayFoxFactory(n))
    println(result)
}
```

To run a simulation comparing different strategy combinations, modify the main function calls in `Main.kt` and execute the application.

## Project Structure

```
GameTheoryCW/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── Main.kt          # Entry point and simulation runner
│   │   └── resources/
│   └── test/
│       └── kotlin/
├── build.gradle.kts              # Gradle build configuration
├── settings.gradle.kts            # Gradle settings
└── README.md                       # This file
```

## Technical Stack

- **Language:** Kotlin
- **Build System:** Gradle
- **JVM Version:** Java 21 or earlier

## Compilation & Execution

The project is configured to compile Kotlin source code to JVM bytecode. The executable JAR is generated during the build process and can be found in the `build/libs/` directory.

## Notes

- Ensure your Java version matches the requirement (JDK 21 or earlier)
- The Gradle wrapper handles dependency downloads automatically
- VS Code extensions are recommended for improved development experience but are not required

## License

This project is submitted as coursework.

## Author

Ryan O'Donnell

---

For issues or questions regarding this implementation, please refer to the project repository.
