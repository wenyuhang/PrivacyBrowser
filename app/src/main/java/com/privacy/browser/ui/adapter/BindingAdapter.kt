package com.privacy.browser.ui.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.king.base.adapter.BaseRecyclerAdapter
import com.orhanobut.logger.Logger
import com.privacy.browser.BR

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 9:50
 * version : 1.0.0
 * desc    :
 **/

class BindingAdapter<T> : BaseRecyclerAdapter<T, BindingHolder<ViewDataBinding>> {

    constructor(context: Context, layoutId: Int) : super(context, layoutId)

    constructor(context: Context, listData: MutableList<T>?, layoutId: Int) : super(
        context, listData, layoutId
    )

    override fun bindViewDatas(holder: BindingHolder<ViewDataBinding>, item: T, position: Int) {
        holder.binding?.also {
            it.setVariable(BR.data, item)
            it.executePendingBindings()
            it.root.setOnLongClickListener {
                onItemLongClickListener?.let { operation ->
                    Logger.e("执行了")
                    operation(item,position)
                }
                true
            }
        }
        // child 添加点击回调
        childIdList?.let {
            if (it.isNotEmpty()){
                it.forEach {id ->
                    holder.addOnClickListener(id)
                }
            }
        }

    }

    private var childIdList: List<Int>? = null
    private var onItemLongClickListener: ((T, Int) -> Unit)? = null
    fun setOnItemLongClickListener(operation: (T, Int) -> Unit) {
        onItemLongClickListener = operation
    }

    fun getItem(position: Int): T? {
        return listData.getOrNull(position)
    }

    fun addData(list: List<T>?) {
        list?.let {
            val allListData = listData
            allListData.addAll(it)
            listData = allListData
            notifyDataSetChanged()
        }
    }

    fun refreshData(list: List<T>?) {
        if (list != null) {
            setListData(list)
        } else {
            listData.clear()
        }
        notifyDataSetChanged()
    }

    fun setChildIdList(listOf: List<Int>) {
        childIdList = listOf
    }
}