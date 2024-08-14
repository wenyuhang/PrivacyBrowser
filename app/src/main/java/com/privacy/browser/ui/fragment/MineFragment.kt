package com.privacy.browser.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.databinding.FragmentMineBinding
import com.wlwork.libframe.base.BaseFragment
import com.wlwork.libframe.base.BaseViewModel
import java.io.File
import java.io.FileOutputStream

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/28 15:01
 * version : 1.0.0
 * desc    :
 **/

class MineFragment : BaseFragment<BaseViewModel,FragmentMineBinding>(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    private lateinit var albumLauncher: ActivityResultLauncher<Intent>

    override fun initData(savedInstanceState: Bundle?) {
        //  TODO 判断设备版本来决定是否采用分区存储  华为设备需要单独处理
        //  TODO (1) 打开相册  (2) 复制到缓存目录  (3) 拆分文件到存储目录[这里涉及到分区处理]  (4)  合并文件生成临时文件进行展示

        registerResult()
        binding.btnOpenAlbum.setOnClickListener {
            openAlbum()
        }
    }

    private fun registerResult() {
        albumLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val data = it.data
                data?.let {
                    val imageUri = data.data
                    Logger.e("======> $imageUri")
                    convertContentUriToFile(activity,imageUri)
                    imageUri
                }
            }
        }
    }

    private fun openAlbum() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        albumLauncher.launch(intent)
    }

    @Throws(Exception::class)
    fun convertContentUriToFile(context: Context?, uri: Uri?): File? {
        context?.let {
            // 获取ContentResolver实例
            val resolver = context.contentResolver

            // 从URI获取输入流
            val inputStream = resolver.openInputStream(uri!!)

            // 创建一个临时文件
            val tempFile = createTempFile(context) // 你需要定义这个方法
            FileOutputStream(tempFile).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // 或者其他缓冲大小
                var read: Int
                while (inputStream!!.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
            }

            // 关闭输入流
            inputStream!!.close()
            return tempFile
        }
        return null
    }

    private fun createTempFile(context: Context): File? {
        // 在缓存目录下创建一个临时文件，你可以使用其他的目录
        val filesDir: File = context.cacheDir
        Logger.e("cacheDir path: ${filesDir.absolutePath}")
//        val filesDir = context.getExternalFilesDir("dts")
        return File.createTempFile("temp", ".jpg", filesDir)
    }

}