package com.privacy.browser

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.privacy.browser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val launcherArray: Array<String> = arrayOf("MainActivity", "Calcuator", "Calendar", "Weather")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() {
        Log.d("TAG", "initListener: ")
        binding.btnReset.setOnClickListener {
            changeLogo(launcherArray[0])
        }
        binding.btnCalcuator.setOnClickListener {
            changeLogo(launcherArray[1])
        }
        binding.btnCalendar.setOnClickListener {
            changeLogo(launcherArray[2])
        }
        binding.btnWeather.setOnClickListener {
            changeLogo(launcherArray[3])
        }
        binding.btnBrowser.setOnClickListener {
            startActivity(Intent(this@MainActivity, BrowserActivity::class.java))
            Logger.d("test")
        }
    }

    private fun changeLogo(name: String) {
        val packageManager = applicationContext.packageManager
        Log.d("TAG", "componentName: $componentName")
        // 去掉旧图标
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        // 显示新图标
        packageManager.setComponentEnabledSetting(
            ComponentName(baseContext, "$packageName.$name"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

    }

    override fun onDestroy() {
//        changeLogo(launcherArray[1])
        super.onDestroy()
    }
}
