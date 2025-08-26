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
import com.luviandsp.tic_tac_toe.databinding.ActivityVsCpuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VsCPU : AppCompatActivity() {

    private lateinit var binding: ActivityVsCpuBinding
    private var playerTurn: Boolean = true
    private var moveCount = 0
    private var gameMode = ""

    companion object {
        private const val TAG = "VsCPU"
        const val GAME_MODE = ""
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

        gameMode = intent.getStringExtra(GAME_MODE).toString()
        Log.d(TAG, "Game Mode: $gameMode")

        initViews()
    }

    private fun initViews() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach { it ->
                it.setOnClickListener { onClick(it as MaterialButton) }
            }

            btnReset.setOnClickListener { resetGame() }
        }
    }

    private fun onClick(button: MaterialButton) {
        if (button.text.isEmpty()) {
            if (playerTurn) {
                button.text = "X"

                binding.tvTurn.text = "CPU Turn"
                playerTurn = false
                moveCount++
            }

            if (checkWinner()) {
                return
            }

            if (moveCount < 9) {
                cpuTurn()
            }
        }
    }

    private fun cpuTurn() {

        // Mencegah pemain untuk mengklik ketika CPU memilih langkah
        disableButtons()

        val boardState = getBoardState()

        GlobalScope.launch(Dispatchers.Default) {

            // Simulasi delay untuk komputer memilih langkah
            delay(500)

            // Dapatkan langkah terbaik dari algoritma minimax
            val bestMove = findBestMove(boardState)

            withContext(Dispatchers.Main) {
                val row = bestMove / 3
                val col = bestMove % 3

                // Pastikan tombol masih kosong sebelum CPU bergerak
                if (boardState[row][col].isEmpty()) {
                    val buttonToMove = when (bestMove) {
                        0 -> binding.btn1
                        1 -> binding.btn2
                        2 -> binding.btn3
                        3 -> binding.btn4
                        4 -> binding.btn5
                        5 -> binding.btn6
                        6 -> binding.btn7
                        7 -> binding.btn8
                        else -> binding.btn9
                    }

                    buttonToMove.text = "O"

                    binding.tvTurn.text = "Player Turn"
                    playerTurn = true

                    moveCount++
                    checkWinner()
                }

                // Aktifkan tombol-tombol kembali
                enableButtons()
            }
        }
    }

    private fun getBoardState(): Array<Array<String>> {
        with(binding) {
            return arrayOf(
                arrayOf(btn1.text.toString(), btn2.text.toString(), btn3.text.toString()),
                arrayOf(btn4.text.toString(), btn5.text.toString(), btn6.text.toString()),
                arrayOf(btn7.text.toString(), btn8.text.toString(), btn9.text.toString())
            )
        }
    }

    // Fungsi bantu untuk menemukan langkah terbaik berdasarkan algoritma minimax
    private fun findBestMove(board: Array<Array<String>>): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    // Simulasikan langkah CPU (pemain 'O')
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

    // Implementasi algoritma minimax
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

    // Fungsi bantu untuk memeriksa apakah papan penuh (untuk seri)
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

    private fun evaluate(board: Array<Array<String>>): Int {
        // Mengecek baris, kolom, dan diagonal
        for (i in 0..2) {
            // Cek baris
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == "O") return 10
                if (board[i][0] == "X") return -10
            }

            // Cek kolom
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == "O") return 10
                if (board[0][i] == "X") return -10
            }
        }

        // Cek diagonal
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == "O") return 10
            if (board[0][0] == "X") return -10
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == "O") return 10
            if (board[0][2] == "X") return -10
        }

        return 0 // Tidak ada pemenang
    }

    private fun checkWinner(): Boolean {
        with(binding) {
            val winningCondition = arrayOf(
                arrayOf(btn1, btn2, btn3),
                arrayOf(btn4, btn5, btn6),
                arrayOf(btn7, btn8, btn9),
                arrayOf(btn1, btn4, btn7),
                arrayOf(btn2, btn5, btn8),
                arrayOf(btn3, btn6, btn9),
                arrayOf(btn1, btn5, btn9),
                arrayOf(btn3, btn5, btn7)
            )

            for (condition in winningCondition) {
                if (condition[0].text.isNotEmpty() &&
                    condition[0].text == condition[1].text &&
                    condition[0].text == condition[2].text
                ) {
                    // Atur teks dan warna jika ada pemenang
                    tvTurn.text = if (condition[0].text == "X") "Player Win!" else "CPU Win!"
                    condition[0].setTextColor(getColor(R.color.green))
                    condition[1].setTextColor(getColor(R.color.green))
                    condition[2].setTextColor(getColor(R.color.green))
                    disableButtons()
                    return true // Pemenang ditemukan
                }
            }

            // Cek jika seri
            if (moveCount == 9) {
                tvTurn.text = "Draw!"
                disableButtons()
                return true // Seri
            }
        }

        return false
    }

    private fun disableButtons() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = false
            }
        }
    }

    private fun enableButtons() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = true
            }
        }
    }

    private fun isDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun resetGame() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.text = ""
                if (isDarkMode()) it.setTextColor(getColor(R.color.white)) else it.setTextColor(getColor(R.color.black))
            }

            moveCount = 0
            playerTurn = true

            binding.tvTurn.text = "Player Turn"
            enableButtons()
        }
    }
}