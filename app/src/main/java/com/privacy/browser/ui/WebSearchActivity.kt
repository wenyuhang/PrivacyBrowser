package com.privacy.browser.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.ui.adapter.BindingAdapter
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityWebSearchBinding
import com.privacy.browser.pojo.SearchHistory
import com.privacy.browser.ui.vm.WebSearchVMImpl
import com.wlwork.libframe.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 9:37
 * version : 1.0.0
 * desc    : 搜索页面
 **/

@AndroidEntryPoint
class WebSearchActivity : BaseActivity<WebSearchVMImpl, ActivityWebSearchBinding>(),
    OnEditorActionListener, OnFocusChangeListener {

    private val mAdapter by lazy {
        BindingAdapter<SearchHistory>(getContext(), R.layout.item_search_history)
    }

    // 软键盘服务
    private val inputMethodManager by lazy {
        getSystemService<InputMethodManager>()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_web_search
    }

    private val listHistory = mutableListOf<SearchHistory>()

    override fun initData(savedInstanceState: Bundle?) {

        // 注册物理键back事件
        registerBack{
            callResultIntent(onBack = it)
        }

        binding.state = this
        binding.vm = viewModel

//        viewModel.pageWebTitle.observe(this) {
//            binding.editWebUrl.hint = it
//        }

        viewModel.getIntentParam(intent)

        // 设置软键盘监听
        binding.editWebUrl.setOnEditorActionListener(this)
        // 设置焦点监听
//        binding.editWebUrl.onFocusChangeListener = this
        // 设置输入监听
        binding.editWebUrl.addTextChangedListener {
            // 当文本变化时，将光标移动到文本末尾
            it?.let {
                Handler(Looper.getMainLooper()).post { binding.editWebUrl.setSelection(it.length) }
            }
        }


        binding.editWebUrl.post {
            // 获取焦点
            binding.editWebUrl.requestFocus()
            // 拉起软键盘
            inputMethodManager?.showSoftInput(binding.editWebUrl, InputMethodManager.SHOW_IMPLICIT)
        }



        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener { _, position ->
            val item = mAdapter.getItem(position) as SearchHistory
            // 输入 编辑框
            viewModel.editSearchContent.postValue(item.word)
        }

        lifecycleScope.launch {
            viewModel.getHistory().flowWithLifecycle(lifecycle).collect{
                Logger.d(it)
                mAdapter.refreshData(it)
            }
        }

    }


    /**
     * 软键盘回调
     */
    override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_GO ||
            actionId == EditorInfo.IME_ACTION_SEARCH ||
            actionId == EditorInfo.IME_ACTION_SEND ||
            actionId == EditorInfo.IME_ACTION_NEXT ||
            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
        ) {
            // 在这里处理回车键的逻辑
            // 例如提交数据或者关闭键盘等
            callResultIntent(linkStr = viewModel.getSearchLink())
            return true
        }
        return false
    }

    /**
     * 回调Intent
     */
    private fun callResultIntent(linkStr: String? = null,onBack:OnBackPressedCallback? = null) {
        if (linkStr.isNullOrEmpty()){
            setResult(RESULT_CANCELED)
        }else{
            val intent = Intent()
            intent.putExtra(Constants.DATA,linkStr)
            setResult(RESULT_OK,intent)
        }
        onBack?.remove()
        finish()
    }

    /**
     * 设置焦点监听
     */
    override fun onFocusChange(p0: View?, hasFocus: Boolean) {
        if (hasFocus) {
            // 当EditText获得焦点时执行的操作
            Logger.d("EditText获得了焦点")
//            startActivity(viewModel.getBuildIntent(this))
        } else {
            // 当EditText失去焦点时执行的操作
            Logger.d("EditText失去了焦点")
        }
    }

    /**
     * 点击事件  搜索
     */
    fun btnToSearch() {
        callResultIntent(linkStr = viewModel.getSearchLink())
    }


    /**
     * 复制链接
     */
    fun btnToCopyUrl() {
        val webUrl = viewModel.getWebUrl()
        val clipService = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipService.setPrimaryClip(ClipData.newPlainText("text/plain", webUrl))
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show()
    }

    /**
     * 编辑链接
     */
    fun btnToEditUrl() {
        val webUrl = viewModel.getWebUrl()
        viewModel.postEditSearchContent(webUrl)
    }

}