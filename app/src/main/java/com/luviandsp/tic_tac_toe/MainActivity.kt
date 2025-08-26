package com.luviandsp.tic_tac_toe

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luviandsp.tic_tac_toe.databinding.ActivityMainBinding
import com.luviandsp.tic_tac_toe.gameplay.VsCPU
import com.luviandsp.tic_tac_toe.gameplay.VsPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var gameMode: String = "Normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
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

            tvGameMode.text = gameMode

            btnPlayer.setOnClickListener {
                Intent(this@MainActivity, VsPlayer::class.java).also {
                    it.putExtra(VsPlayer.GAME_MODE, gameMode)
                    startActivity(it)
                }
            }

            btnCpu.setOnClickListener {
                Intent(this@MainActivity, VsCPU::class.java).also {
                    it.putExtra(VsCPU.GAME_MODE, gameMode)
                    startActivity(it)
                }
            }

            btnBack.setOnClickListener { changeGameMode(btnBack) }
            btnNext.setOnClickListener { changeGameMode(btnNext) }

        }
    }

    private fun changeGameMode(button: ImageButton) {
        with(binding) {
            if (gameMode == "Normal") {
                if (button == btnNext) {
                    gameMode = "Time Attack"
                    tvGameMode.text = gameMode
                }
            } else {
                if (button == btnBack) {
                    gameMode = "Normal"
                    tvGameMode.text = gameMode
                }
            }
        }
    }
}