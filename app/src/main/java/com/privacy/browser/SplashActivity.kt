package com.privacy.browser

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.privacy.browser.ui.BrowserActivity
import com.wlwork.libframe.base.BaseActivity
import com.wlwork.libframe.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 17:51
 * version : 1.0.0
 * desc    : 启动页
 **/

@AndroidEntryPoint
class SplashActivity : BaseActivity<BaseViewModel,ViewDataBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun isBinding(): Boolean {
        //不覆写此方法时，默认返回true，这里返回false表示不使用DataBinding，因为当前界面比较简单，完全没有必要用
        return false
    }

    override fun initData(savedInstanceState: Bundle?) {
        startActivity(Intent(this@SplashActivity, BrowserActivity::class.java))
        finish()
    }
}