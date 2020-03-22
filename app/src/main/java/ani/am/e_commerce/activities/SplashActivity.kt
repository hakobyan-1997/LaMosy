package ani.am.e_commerce.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ani.am.e_commerce.R
import kotlinx.android.synthetic.main.activity_splash.*
import android.view.animation.AlphaAnimation

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 4000
        logo.startAnimation(anim)
        appName.startAnimation(anim)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 5000)
    }
}
