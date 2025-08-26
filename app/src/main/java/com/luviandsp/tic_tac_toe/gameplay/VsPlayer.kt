package com.luviandsp.tic_tac_toe.gameplay

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.luviandsp.tic_tac_toe.R
import com.luviandsp.tic_tac_toe.databinding.ActivityVsPlayerBinding

/**
 * [VsPlayer] adalah Activity yang mengelola logika permainan tic-tac-toe untuk mode
 * "VS Player" (dua pemain).
 */
class VsPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityVsPlayerBinding

    private var playerTurn: Boolean = true
    private var moveCount: Int = 0
    private var playerXScore: Int = 0
    private var playerOScore: Int = 0

    companion object {
        private const val TAG = "VsPlayer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVsPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
    }

    /**
     * Menginisialisasi tampilan dan mengatur listener untuk interaksi pengguna.
     */
    private fun initViews() {
        with(binding) {
            val buttons = arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            buttons.forEach { it.setOnClickListener { onClick(it as MaterialButton) } }

            updateUiScores()
            updateTurnText()

            toolbar.setNavigationOnClickListener { finish() }
            btnReset.setOnClickListener { resetGame() }
        }
    }

    /**
     * Menangani logika saat salah satu tombol papan diklik.
     *
     * @param button Tombol [MaterialButton] yang diklik oleh pemain.
     */
    private fun onClick(button: MaterialButton) {
        if (button.text.isEmpty()) {
            val playerMark = if (playerTurn) "X" else "O"
            button.text = playerMark

            playerTurn = !playerTurn
            moveCount++
            updateTurnText()
            checkWinner()
        }
    }

    /**
     * Memeriksa kondisi pemenang atau seri setelah setiap giliran.
     * Logika ini juga memperbarui skor dan tampilan UI.
     */
    private fun checkWinner() {
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
                        playerXScore++
                        tvTurn.text = getString(R.string.player_x_win)
                    } else {
                        playerOScore++
                        tvTurn.text = getString(R.string.player_o_win)
                    }

                    b1.setTextColor(getColor(R.color.green))
                    b2.setTextColor(getColor(R.color.green))
                    b3.setTextColor(getColor(R.color.green))

                    updateUiScores()
                    disableButtons()
                    return
                }
            }

            if (moveCount == 9) {
                tvTurn.text = getString(R.string.draw)
                disableButtons()
                return
            }
        }
    }

    /**
     * Menonaktifkan semua tombol papan untuk mengakhiri permainan.
     */
    private fun disableButtons() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = false
            }
        }
    }

    /**
     * Mengaktifkan semua tombol papan untuk memulai atau melanjutkan permainan.
     */
    private fun enableButtons() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.isEnabled = true
            }
        }
    }

    /**
     * Mereset papan permainan ke keadaan awal.
     */
    private fun resetGame() {
        with(binding) {
            val textColor = if (isDarkMode()) getColor(R.color.white) else getColor(R.color.black)
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.text = ""
                it.setTextColor(textColor)
            }

            playerTurn = true
            moveCount = 0

            updateTurnText()
            enableButtons()
        }
    }

    /**
     * Memeriksa apakah perangkat sedang dalam mode gelap (dark mode).
     *
     * @return true jika mode gelap aktif, false jika tidak.
     */
    private fun isDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Memperbarui teks pada [tvTurn] berdasarkan giliran pemain.
     */
    private fun updateTurnText() {
        binding.tvTurn.text = if (playerTurn) getString(R.string.player_x_turn) else getString(R.string.player_o_turn)
    }

    /**
     * Memperbarui tampilan skor di UI.
     */
    private fun updateUiScores() {
        binding.tvPlayerXScore.text = getString(R.string.player_x_win_count, playerXScore)
        binding.tvPlayerOScore.text = getString(R.string.player_o_win_count, playerOScore)
    }
}