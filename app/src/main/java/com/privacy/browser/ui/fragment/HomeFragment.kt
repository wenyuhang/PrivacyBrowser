package com.privacy.browser.ui.fragment

import android.os.Bundle
import com.privacy.browser.R
import com.privacy.browser.databinding.FragmentHomeBinding
import com.wlwork.libframe.base.BaseFragment
import com.wlwork.libframe.base.BaseViewModel

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/28 15:01
 * version : 1.0.0
 * desc    :
 **/

class HomeFragment : BaseFragment<BaseViewModel,FragmentHomeBinding>(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initData(savedInstanceState: Bundle?) {
    }


}

//    private lateinit var binding: ActivityMainBinding

//    val launcherArray: Array<String> = arrayOf("MainActivity", "Calcuator", "Calendar", "Weather")
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initListener()
//    }
//
//    private fun initListener() {
//        binding.btnReset.setOnClickListener {
//            changeLogo(launcherArray[0])
//        }
//        binding.btnCalcuator.setOnClickListener {
//            changeLogo(launcherArray[1])
//        }
//        binding.btnCalendar.setOnClickListener {
//            changeLogo(launcherArray[2])
//        }
//        binding.btnWeather.setOnClickListener {
//            changeLogo(launcherArray[3])
//        }
//        binding.btnBrowser.setOnClickListener {
//            startActivity(Intent(this@MainActivity, BrowserActivity::class.java))
//            Logger.d("test")
//        }
//        startActivity(Intent(this@MainActivity, BrowserRouterActivity::class.java))
//    }
//
//    private fun changeLogo(name: String) {
//        val packageManager = applicationContext.packageManager
//        Logger.d("componentName: $componentName")
//        // 去掉旧图标
//        packageManager.setComponentEnabledSetting(
//            componentName,
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP
//        )
//        // 显示新图标
//        packageManager.setComponentEnabledSetting(
//            ComponentName(baseContext, "$packageName.$name"),
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//            PackageManager.DONT_KILL_APP
//        )
//
//    }
//
//    override fun onDestroy() {
////        changeLogo(launcherArray[1])
//        super.onDestroy()
//    }