package com.wlwork.libframe.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hjq.toast.Toaster
import com.wlwork.libframe.R
import com.wlwork.libframe.utils.JumpDebounce
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType


/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 16:18
 * version :
 * desc    : baseactivity
 **/

abstract class BaseActivity<VM : BaseViewModel, VDB : ViewDataBinding> : AppCompatActivity(),
    IView<VM> {

    lateinit var viewModel: VM
        private set

    private var viewDataBinding: VDB? = null

    val binding: VDB
        get() = viewDataBinding!!

    private var dialog: Dialog? = null

    private var progressDialog: Dialog? = null

    private val jumpDebounce by lazy {
        JumpDebounce()
    }

    // 跳转回调
    var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initViewModel()
        initData(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.unbind()

    }

    /**
     * 初始化ContentView；
     */
    open fun initContentView() {
        if (isBinding()) {
            viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
            // 数据绑定
            binding.lifecycleOwner = this
        } else {
            setContentView(getLayoutId())
        }
    }

    /**
     * 初始化[viewModel]
     */
    private fun initViewModel() {
        viewModel = createViewModel()
        lifecycleScope.launch {
            viewModel.loadingState.flowWithLifecycle(lifecycle).collect {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.messageEvent.flowWithLifecycle(lifecycle).collect {
                showToast(it)
            }
        }
    }

    /**
     * 获取泛型VM对应的类
     */
    @Suppress("UNCHECKED_CAST")
    private fun getVMClass(): Class<VM> {
        var cls: Class<*>? = javaClass
        var vmClass: Class<VM>? = null
        while (vmClass == null && cls != null) {
            vmClass = getVMClass(cls)
            cls = cls.superclass
        }
        if (vmClass == null) {
            vmClass = BaseViewModel::class.java as Class<VM>
        }
        return vmClass
    }

    /**
     * 根据传入的 cls 获取泛型VM对应的类
     */
    @Suppress("UNCHECKED_CAST")
    private fun getVMClass(cls: Class<*>): Class<VM>? {
        val type = cls.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (t in types) {
                if (t is Class<*>) {
                    if (BaseViewModel::class.java.isAssignableFrom(t)) {
                        return t as? Class<VM>
                    }
                } else if (t is ParameterizedType) {
                    val rawType = t.rawType
                    if (rawType is Class<*>) {
                        if (BaseViewModel::class.java.isAssignableFrom(rawType)) {
                            return rawType as? Class<VM>
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * 是否使用 ViewDataBinding；默认为：true
     */
    override fun isBinding(): Boolean {
        return true
    }

    override fun createViewModel(): VM {
        return obtainViewModel(getVMClass())
    }

    fun <T : ViewModel> obtainViewModel(vmClass: Class<T>): T {
        return ViewModelProvider(
            viewModelStore,
            defaultViewModelProviderFactory,
            defaultViewModelCreationExtras
        )[vmClass]
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        dismissProgressDialog()
    }

    override fun showToast(text: CharSequence) {
        Toaster.show(text)
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(stringResIdToString(resId))
    }

    /**
     * 显示进度对话框
     */
    fun showProgressDialog(
        layoutId: Int = R.layout.mvvmframe_progress_dialog,
        cancel: Boolean = false
    ) {
        dismissProgressDialog()
        progressDialog = BaseProgressDialog(this).also {
            it.setContentView(layoutId)
            it.setCanceledOnTouchOutside(cancel)
            it.show()
        }
    }

    /**
     * 显示对话框
     * @param contentView 弹框内容视图
     * @param styleId Dialog样式
     * @param gravity Dialog的对齐方式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param x x轴偏移量，需与 gravity 结合使用
     * @param y y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin 垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight 垂直方向权重
     * @param backCancel 返回键是否可取消（默认为true，false则拦截back键）
     */
    fun showDialog(
        contentView: View,
        @StyleRes styleId: Int = R.style.mvvmframe_dialog,
        gravity: Int = Gravity.NO_GRAVITY,
        widthRatio: Float = 0.85f,
        x: Int = 0,
        y: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f,
        horizontalWeight: Float = 0f,
        verticalWeight: Float = 0f,
        backCancel: Boolean = true
    ) {
        dismissDialog()
        dialog = Dialog(this, styleId).also {
            it.setContentView(contentView)
            it.setCanceledOnTouchOutside(false)
            it.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (backCancel) {
                        dismissDialog()
                    }
                    return@OnKeyListener true
                }
                false
            })
            setWindow(
                it.window,
                gravity,
                widthRatio,
                x,
                y,
                horizontalMargin,
                verticalMargin,
                horizontalWeight,
                verticalWeight
            )
            it.show()
        }

    }

    /**
     * 设置 Window 布局相关参数
     * @param window [Window]
     * @param gravity Dialog的对齐方式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param x x轴偏移量，需与 gravity 结合使用
     * @param y y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin 垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight 垂直方向权重
     */
    open fun setWindow(
        window: Window?,
        gravity: Int = Gravity.NO_GRAVITY,
        widthRatio: Float = 0.85f,
        x: Int = 0,
        y: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f,
        horizontalWeight: Float = 0f,
        verticalWeight: Float = 0f
    ) {
        window?.attributes?.let {
            it.width = (resources.displayMetrics.widthPixels * widthRatio).toInt()
            it.gravity = gravity
            it.x = x
            it.y = y
            it.horizontalMargin = horizontalMargin
            it.verticalMargin = verticalMargin
            it.horizontalWeight = horizontalWeight
            it.verticalWeight = verticalWeight
            window.attributes = it
        }
    }

    /**
     * 关闭进度对话框
     */
    fun dismissProgressDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }

    /**
     * 关闭对话框
     */
    fun dismissDialog(dialog: Dialog? = this.dialog) {
        dialog?.dismiss()
    }

    @Suppress("DEPRECATION")
    @Deprecated("DeprecatedIsStillUsed")
    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        if (jumpDebounce.isIgnoreJump(intent)) {
            return
        }
        super.startActivityForResult(intent, requestCode, options)
    }

    /**
     * 获取[Context]
     */
    fun getContext() = this


    /**
     * 注册物理回退键 回调
     */
    open fun registerBack(operation: ((OnBackPressedCallback) -> Unit)? = null) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 如果不处理，则移除当前回调并执行默认的返回行为
                if (operation != null) {
                    // runBlocking 函数可以创建了一个阻塞的协程作用域，在这个作用域内运行的协程会阻止程序结束，直到所有的子协程都完成
                    operation(this)
                } else {
                    remove()
                    finish()
                }
            }
        })
    }

    /**
     * 注册跳转回调 StartActivityForResult()
     */
    open fun registerLauncher(operation: (ActivityResult) -> Unit) {
        // 搜索回调
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                operation(it)
            }
    }

}