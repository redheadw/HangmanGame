

import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File

class HangmanGame : Application() {
    private lateinit var wordToGuess: String
    private lateinit var hiddenWordLabel: Label
    private lateinit var incorrectGuessesLabel: Label
    private val guessedLetters = mutableSetOf<Char>()
    private val incorrectGuesses = mutableListOf<Char>()

    override fun start(primaryStage: Stage) {
        val words = File(javaClass.getResource("/words.txt").toURI())
            .readLines()
            .filter { it.length in 5..6 }

        wordToGuess = words.random().lowercase()

        hiddenWordLabel = Label(maskWord())
        incorrectGuessesLabel = Label("Incorrect guesses: ")

        val guessField = TextField()
        guessField.promptText = "Enter a letter"
        guessField.maxWidth = 100.0

        val guessButton = Button("Guess")
        guessButton.setOnAction {
            val input = guessField.text.lowercase()
            if (input.isNotEmpty()) {
                val letter = input[0]
                processGuess(letter)
                guessField.clear()
            }
        }

        val restartButton = Button("Restart")
        restartButton.setOnAction {
            restartGame(words)
        }

        val layout = VBox(10.0, hiddenWordLabel, incorrectGuessesLabel, guessField, guessButton, restartButton)
        layout.alignment = Pos.CENTER
        val scene = Scene(layout, 400.0, 300.0)

        primaryStage.title = "Hangman Game"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun maskWord(): String {
        return wordToGuess.map { if (guessedLetters.contains(it)) it else '_' }.joinToString(" ")
    }

    private fun processGuess(letter: Char) {
        if (letter in guessedLetters || letter in incorrectGuesses) return

        if (letter in wordToGuess) {
            guessedLetters.add(letter)
        } else {
            incorrectGuesses.add(letter)
        }

        hiddenWordLabel.text = maskWord()
        incorrectGuessesLabel.text = "Incorrect guesses: ${incorrectGuesses.joinToString(" ")}"

        if (wordToGuess.all { guessedLetters.contains(it) }) {
            hiddenWordLabel.text = "You WON! The word was '$wordToGuess'"
        }
    }

    private fun restartGame(words: List<String>) {
        guessedLetters.clear()
        incorrectGuesses.clear()
        wordToGuess = words.random().lowercase()
        hiddenWordLabel.text = maskWord()
        incorrectGuessesLabel.text = "Incorrect guesses: "
    }
    }
    
fun main() {
    Application.launch(HangmanGame::class.java)
}





