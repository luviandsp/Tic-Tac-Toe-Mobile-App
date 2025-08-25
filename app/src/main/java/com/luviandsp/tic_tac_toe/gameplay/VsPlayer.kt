package com.luviandsp.tic_tac_toe.gameplay

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luviandsp.tic_tac_toe.R
import com.luviandsp.tic_tac_toe.databinding.ActivityVsPlayerBinding

class VsPlayer : AppCompatActivity() {

    private lateinit var binding: ActivityVsPlayerBinding
    private var playerTurn = true
    private var moveCount = 0

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

    private fun initViews() {
        with(binding) {
            btn1.setOnClickListener { onClick(btn1) }
            btn2.setOnClickListener { onClick(btn2) }
            btn3.setOnClickListener { onClick(btn3) }

            btn4.setOnClickListener { onClick(btn4) }
            btn5.setOnClickListener { onClick(btn5) }
            btn6.setOnClickListener { onClick(btn6) }

            btn7.setOnClickListener { onClick(btn7) }
            btn8.setOnClickListener { onClick(btn8) }
            btn9.setOnClickListener { onClick(btn9) }

            btnReset.setOnClickListener { resetGame() }
        }
    }

    private fun onClick(button: Button) {
        if (button.text.isEmpty()) {
            if (playerTurn) {
                button.text = "X"
                binding.tvTurn.text = "Player O Turn"
            } else {
                button.text = "O"
                binding.tvTurn.text = "Player X Turn"
            }

            playerTurn = !playerTurn
            moveCount++
            checkWinner()
        }
    }

    private fun checkWinner() {
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
                if (condition[0].text != "" &&
                    condition[0].text == condition[1].text &&
                    condition[0].text == condition[2].text
                ) {
                    tvTurn.text = "${condition[0].text} Won!"
                    condition[0].setTextColor(getColor(R.color.green))
                    condition[1].setTextColor(getColor(R.color.green))
                    condition[2].setTextColor(getColor(R.color.green))

                    disableButtons()
                    return
                }
            }

            if (moveCount == 9) {
                tvTurn.text = "Draw!"
                disableButtons()
                return
            }
        }
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

    private fun resetGame() {
        with(binding) {
            arrayOf(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9).forEach {
                it.text = ""
                it.setTextColor(getColor(R.color.black))
            }

            playerTurn = true
            moveCount = 0

            binding.tvTurn.text = "Player X Turn"
            enableButtons()
        }
    }
}