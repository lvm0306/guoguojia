package com.lovesosoi.kotlin_shop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread({
            Thread.sleep(3000)
            startActivity(Intent(this,MainActivity::class.java))
            this.finish()
        }).start()
    }
}
