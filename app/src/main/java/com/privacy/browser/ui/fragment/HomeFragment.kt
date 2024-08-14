package com.privacy.browser.ui.fragment

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.orhanobut.logger.Logger
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

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA
    )

    private val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"

    private var imageList: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initData(savedInstanceState: Bundle?) {
       //  TODO 判断设备版本来决定是否采用分区存储  华为设备需要单独处理
       //  TODO (1) 打开相册  (2) 复制到缓存目录  (3) 拆分文件到存储目录[这里涉及到分区处理]  (4)  合并文件生成临时文件进行展示
        val isEnabled = activity?.let {
            val scopedStorageEnabled = isScopedStorageEnabled(it)
            if (scopedStorageEnabled){
                val cursor = it.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,  // Selection criteria
                    null,  // Selection arguments

                    orderBy
                ) // Order by date added (newest first)

                cursor.use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val idColumnIndex =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        val dataColumnIndex =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        do {
                            val id = cursor.getLong(idColumnIndex)
                            val path = cursor.getString(dataColumnIndex)
                            imageList.add(path)
                        } while (cursor.moveToNext())
                    }
                }


            }

            scopedStorageEnabled
        }
        Logger.e("是否支持分区存储：$isEnabled  ${imageList.size}")

    }


}

// 检查是否需要遵循分区存储
private fun isScopedStorageEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11 及更高版本强制使用分区存储
        true
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        // Android 10 允许选择是否使用分区存储
        val packageManager: PackageManager = context.packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            // 处理异常
        }
        (applicationInfo != null
                && applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q
//                && !applicationInfo.requestLegacyExternalStorage
                )
    } else {
        // Android 9 及更低版本不支持分区存储
        false
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