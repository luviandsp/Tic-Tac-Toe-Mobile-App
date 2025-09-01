# Tic-Tac-Toe Mobile App

A classic Tic-Tac-Toe game developed natively for Android. This application allows players to enjoy the game in two modes: against another player or against a smart AI with adjustable difficulty levels.

## Technologies Used
* **Kotlin**: The primary programming language for Android development.
* **Android Studio**: The official IDE for Android development.
* **XML**: Used for designing and creating the application's user interface layouts.
* **Coroutines**: Used for asynchronous and non-blocking operations to ensure the UI remains responsive during AI calculations.
* **Gradle**: Powerful build system for dependency management.

## Features
* **Two Game Modes**: Play against another player on the same device or challenge an AI opponent.
* **Adjustable AI Difficulty**: The AI opponent has three difficulty levels:
  *  **Easy**: The AI makes random moves, providing a casual and simple challenge.
  * **Normal**: The AI uses a mix of random and strategic moves, offering a moderate challenge.
  * **Hard**: The AI employs a strategic algorithm to always make the best possible move.
* **Score Tracker**: Keeps track of wins for both players or the player and the CPU.
* **Responsive UI**: The AI's turn is simulated with a brief delay, and the UI remains active and responsive throughout the gameplay.
* **Dark Mode Support**: The app automatically adjusts its color scheme based on the system's theme settings.

## Setup Instructions
To run this project on your local machine, follow these simple steps:
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/luviandsp/Tic-Tac-Toe-Mobile-App.git
    ```

2. **Open in Android Studio:**
    * Launch Android Studio.
    * Select `Open an existing Android Studio project`.
    * Navigate to the cloned repository and select the root directory.
      
4. Run the application:
    * Connect an Android device to your computer via USB, or set up an Android Emulator.
    * Click the Run button (a green triangle ▶️) in the toolbar.
    * Select your device or emulator from the target list and wait for the app to install and launch.

## AI Support in Development
This project was built with the assistance of AI to optimize the development process. The AI provided help in several key areas:
* **Logic for the CPU Opponent**: The AI helped in refining and implementing the complex Minimax algorithm to create a challenging opponent.
* **Code Refactoring**: The AI assisted in restructuring code to be more efficient, readable, and maintainable.
* **Documentation**: The AI generated detailed KDoc documentation for the code, making it easier to understand and maintain.
* **Project Documentation**: The AI created this README file to provide a clear overview of the project's features and technical details.

