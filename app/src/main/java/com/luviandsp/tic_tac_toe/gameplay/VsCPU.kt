package com.luviandsp.tic_tac_toe.gameplay

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.luviandsp.tic_tac_toe.R
import com.luviandsp.tic_tac_toe.data.DifficultyLevel
import com.luviandsp.tic_tac_toe.databinding.ActivityVsCpuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * [VsCPU] adalah Activity yang mengelola logika permainan tic-tac-toe untuk mode "Vs CPU".
 * Mengimplementasikan algoritma hibrida (minimax dan langkah acak) untuk AI.
 */
class VsCPU : AppCompatActivity() {

    private lateinit var binding: ActivityVsCpuBinding

    private var playerTurn: Boolean = true
    private var moveCount: Int = 0
    private var difficultyLevel: String = ""
    private var playerScore: Int = 0
    private var cpuScore: Int = 0

    companion object {
        private const val TAG = "VsCPU"
        const val DIFFICULTY_LEVEL = "difficulty_level"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVsCpuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mengambil tingkat kesulitan dari Intent
        difficultyLevel = intent.getStringExtra(DIFFICULTY_LEVEL).toString()
        Log.d(TAG, "Difficulty Level: $difficultyLevel")

        initViews()
    }

    /**
     * Menginisialisasi semua tampilan dan mengatur listener yang diperlukan.
     */
    private fun initViews() {
        with(binding) {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            buttons.forEach { it.setOnClickListener { onClick(it as MaterialButton) } }

            updateScoreUI()

            toolbar.setNavigationOnClickListener { finish() }
            btnReset.setOnClickListener { resetGame() }
        }
    }

    /**
     * Menangani logika saat tombol papan permainan diklik.
     *
     * @param button Tombol [MaterialButton] yang diklik oleh pemain.
     */
    private fun onClick(button: MaterialButton) {
        if (button.text.isEmpty()) {
            button.text = "X"
            binding.tvTurn.text = getString(R.string.cpu_turn)
            moveCount++

            if (checkWinner()) {
                return
            }

            if (moveCount < 9) {
                cpuTurn()
            }
        }
    }

    /**
     * Menangani giliran CPU.
     * Menggunakan coroutine untuk menjalankan algoritma minimax di thread terpisah.
     */
    private fun cpuTurn() {
        // Menonaktifkan tombol untuk mencegah spam klik
        disableButtons()
        binding.btnReset.isEnabled = false

        // Mengambil status papan permainan
        val boardState = getBoardState()

        GlobalScope.launch(Dispatchers.Default) {
            // Simulasi delay untuk efek "berpikir"
            delay(500)

            val move: Int
            val randomMoveProbability = when (difficultyLevel) {
                DifficultyLevel.EASY.name -> 0.50f
                DifficultyLevel.NORMAL.name -> 0.15f
                DifficultyLevel.HARD.name -> 0.025f
                else -> 0.20f
            }

            if (Random.nextFloat() < randomMoveProbability) {
                Log.d(TAG, "Random Move Triggered")
                move = findRandomMove(boardState)
            } else {
                move = findBestMove(boardState)
            }

            withContext(Dispatchers.Main) {
                if (move != -1) {
                    val buttons = arrayOf(binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9)
                    val buttonToMove = buttons[move]

                    if (buttonToMove.text.isEmpty()) {
                        buttonToMove.text = "O"
                        binding.tvTurn.text = getString(R.string.player_turn)
                        moveCount++
                        val isGameOver = checkWinner()
                        if (!isGameOver) {
                            enableButtonsAndReset()
                        }
                    }
                }
            }
        }
    }

    /**
     * Menemukan langkah acak yang tersedia di papan.
     *
     * @param boardState Representasi papan saat ini sebagai array string.
     * @return Indeks langkah acak atau -1 jika tidak ada.
     */
    private fun findRandomMove(boardState: Array<Array<String>>): Int {
        val emptyCells = mutableListOf<Int>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (boardState[i][j].isEmpty()) {
                    emptyCells.add(i * 3 + j)
                }
            }
        }
        return emptyCells.randomOrNull() ?: -1
    }

    /**
     * Memeriksa kondisi pemenang atau seri setelah setiap giliran.
     * Memperbarui skor dan tampilan UI.
     *
     * @return true jika permainan berakhir (ada pemenang atau seri), false jika tidak.
     */
    private fun checkWinner(): Boolean {
        with(binding) {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            val winningConditions = arrayOf(
                intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
                intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
                intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
            )

            for (condition in winningConditions) {
                val b1 = buttons[condition[0]]
                val b2 = buttons[condition[1]]
                val b3 = buttons[condition[2]]

                if (b1.text.isNotEmpty() && b1.text == b2.text && b2.text == b3.text) {
                    val winner = b1.text.toString()
                    if (winner == "X") {
                        playerScore++
                        tvTurn.text = getString(R.string.player_win)
                    } else {
                        cpuScore++
                        tvTurn.text = getString(R.string.cpu_win)
                    }

                    b1.setTextColor(getColor(R.color.green))
                    b2.setTextColor(getColor(R.color.green))
                    b3.setTextColor(getColor(R.color.green))

                    updateScoreUI()
                    disableButtons()
                    binding.btnReset.isEnabled = true
                    return true
                }
            }

            if (moveCount == 9) {
                tvTurn.text = getString(R.string.draw)
                disableButtons()
                binding.btnReset.isEnabled = true
                return true
            }
        }
        return false
    }

    /**
     * Menonaktifkan semua tombol papan.
     */
    private fun disableButtons() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = false
            }
        }
    }

    /**
     * Mengaktifkan semua tombol papan dan tombol reset.
     */
    private fun enableButtonsAndReset() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = true
            }
            btnReset.isEnabled = true
        }
    }

    /**
     * Mengambil status papan permainan dari UI.
     *
     * @return Representasi papan sebagai array string 2D.
     */
    private fun getBoardState(): Array<Array<String>> {
        return arrayOf(
            arrayOf(binding.btn1.text.toString(), binding.btn2.text.toString(), binding.btn3.text.toString()),
            arrayOf(binding.btn4.text.toString(), binding.btn5.text.toString(), binding.btn6.text.toString()),
            arrayOf(binding.btn7.text.toString(), binding.btn8.text.toString(), binding.btn9.text.toString())
        )
    }

    /**
     * Mengimplementasikan algoritma minimax untuk mencari langkah terbaik.
     *
     * @param board Papan permainan saat ini.
     * @return Indeks langkah terbaik.
     */
    private fun findBestMove(board: Array<Array<String>>): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "O"
                    val score = minimax(board, 0, false)
                    board[i][j] = ""
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = i * 3 + j
                    }
                }
            }
        }
        return bestMove
    }

    /**
     * Fungsi rekursif untuk algoritma minimax.
     *
     * @param board Papan permainan saat ini.
     * @param depth Kedalaman pencarian.
     * @param isMaximizing true jika giliran pemain yang memaksimalkan skor (CPU).
     * @return Skor evaluasi papan.
     */
    private fun minimax(board: Array<Array<String>>, depth: Int, isMaximizing: Boolean): Int {
        val score = evaluate(board)
        if (score == 10) return score - depth
        if (score == -10) return score + depth
        if (isBoardFull(board)) return 0

        if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = "O"
                        best = best.coerceAtLeast(minimax(board, depth + 1, !isMaximizing))
                        board[i][j] = ""
                    }
                }
            }
            return best
        } else {
            var best = Int.MAX_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j].isEmpty()) {
                        board[i][j] = "X"
                        best = best.coerceAtMost(minimax(board, depth + 1, !isMaximizing))
                        board[i][j] = ""
                    }
                }
            }
            return best
        }
    }

    /**
     * Mengevaluasi kondisi papan permainan.
     *
     * @param board Papan permainan saat ini.
     * @return 10 jika CPU menang, -10 jika pemain menang, 0 jika tidak ada pemenang.
     */
    private fun evaluate(board: Array<Array<String>>): Int {
        // ... (kode yang sudah ada)
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == "O") return 10
                if (board[i][0] == "X") return -10
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == "O") return 10
                if (board[0][i] == "X") return -10
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == "O") return 10
            if (board[0][0] == "X") return -10
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == "O") return 10
            if (board[0][2] == "X") return -10
        }
        return 0
    }

    /**
     * Memeriksa apakah papan sudah penuh.
     *
     * @param board Papan permainan saat ini.
     * @return true jika papan penuh, false jika tidak.
     */
    private fun isBoardFull(board: Array<Array<String>>): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Mereset papan permainan ke keadaan awal.
     */
    private fun resetGame() {
        with(binding) {
            val textColor = if (isDarkMode()) getColor(R.color.white) else getColor(R.color.black)
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            buttons.forEach {
                it.text = ""
                it.setTextColor(textColor)
            }

            moveCount = 0
            playerTurn = true

            binding.tvTurn.text = getString(R.string.player_turn)
            enableButtonsAndReset()
        }
    }

    /**
     * Memeriksa apakah perangkat sedang dalam mode gelap.
     */
    private fun isDarkMode(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Memperbarui tampilan skor di UI.
     */
    private fun updateScoreUI() {
        binding.tvPlayerScore.text = getString(R.string.player_win_count, playerScore)
        binding.tvCpuScore.text = getString(R.string.cpu_win_count, difficultyLevel, cpuScore)
    }
}