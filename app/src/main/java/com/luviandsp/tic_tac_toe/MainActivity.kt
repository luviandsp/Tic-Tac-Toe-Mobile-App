package com.luviandsp.tic_tac_toe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luviandsp.tic_tac_toe.databinding.ActivityMainBinding
import com.luviandsp.tic_tac_toe.gameplay.VsCPU
import com.luviandsp.tic_tac_toe.gameplay.VsPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var gameMode: String = "Normal"

    override fun onCreate(savedInstanceState: Bundle?) {
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
            btnPlayer.setOnClickListener {
                val intent = Intent(this@MainActivity, VsPlayer::class.java).also {
                    startActivity(it)
                }
            }

            btnCpu.setOnClickListener {
                val intent = Intent(this@MainActivity, VsCPU::class.java).also {
                    startActivity(it)
                }
            }

            fabBack.setOnClickListener { changeGameMode(fabBack) }
            fabNext.setOnClickListener { changeGameMode(fabNext) }

        }
    }

    private fun changeGameMode(button: FloatingActionButton) {
        with(binding) {
            if (gameMode == "Normal") {
                if (button == fabNext) {
                    gameMode = "Time Attack"
                    tvGameMode.text = gameMode
                }
            } else {
                if (button == fabBack) {
                    gameMode = "Normal"
                    tvGameMode.text = gameMode
                }
            }
        }
    }
}