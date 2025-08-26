package com.luviandsp.tic_tac_toe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luviandsp.tic_tac_toe.data.DifficultyLevel
import com.luviandsp.tic_tac_toe.databinding.ActivityMainBinding
import com.luviandsp.tic_tac_toe.gameplay.VsCPU
import com.luviandsp.tic_tac_toe.gameplay.VsPlayer

/**
 * [MainActivity] adalah layar utama aplikasi yang berfungsi sebagai menu utama.
 * Pengguna dapat memilih mode permainan (Vs Player atau Vs CPU) dan
 * mengatur tingkat kesulitan untuk mode Vs CPU.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Objek binding untuk mengakses View di layout activity_main.xml.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Menyimpan tingkat kesulitan CPU yang dipilih saat ini.
     * Nilai defaultnya adalah [DifficultyLevel.EASY].
     */
    private var difficultyLevel: DifficultyLevel = DifficultyLevel.EASY

    /**
     * Dipanggil saat Activity pertama kali dibuat.
     *
     * @param savedInstanceState Jika Activity sedang dibuat ulang setelah sebelumnya dihancurkan,
     * Bundle ini berisi data yang paling baru disediakannya dalam
     * [onSaveInstanceState]. Jika tidak, nilainya adalah null.
     */
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

    /**
     * Menginisialisasi semua elemen UI dan mengatur listener yang diperlukan.
     */
    private fun initViews() {
        with(binding) {
            tvCpuDifficulty.text = difficultyLevel.name

            btnPlayer.setOnClickListener {
                startActivity(Intent(this@MainActivity, VsPlayer::class.java))
            }

            btnCpu.setOnClickListener {
                Intent(this@MainActivity, VsCPU::class.java).apply {
                    putExtra(VsCPU.DIFFICULTY_LEVEL, difficultyLevel.name)
                    startActivity(this)
                }
            }

            btnPrevDiff.setOnClickListener { changeDifficultyLevel(isNext = false) }
            btnNextDiff.setOnClickListener { changeDifficultyLevel(isNext = true) }
        }
    }

    /**
     * Mengubah tingkat kesulitan CPU.
     * Logika ini berputar antara EASY, NORMAL, dan HARD.
     *
     * @param isNext Boolean yang menentukan apakah akan pindah ke tingkat kesulitan berikutnya (true)
     * atau sebelumnya (false).
     */
    private fun changeDifficultyLevel(isNext: Boolean) {
        difficultyLevel = if (isNext) {
            // Menggunakan when untuk menentukan tingkat kesulitan berikutnya
            when (difficultyLevel) {
                DifficultyLevel.EASY -> DifficultyLevel.NORMAL
                DifficultyLevel.NORMAL -> DifficultyLevel.HARD
                DifficultyLevel.HARD -> DifficultyLevel.HARD // Tetap Hard jika sudah di Hard
            }
        } else {
            // Menggunakan when untuk menentukan tingkat kesulitan sebelumnya
            when (difficultyLevel) {
                DifficultyLevel.EASY -> DifficultyLevel.EASY // Tetap Easy jika sudah di Easy
                DifficultyLevel.NORMAL -> DifficultyLevel.EASY
                DifficultyLevel.HARD -> DifficultyLevel.NORMAL
            }
        }
        binding.tvCpuDifficulty.text = difficultyLevel.name
    }
}