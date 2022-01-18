package com.byl.mvvm.ui.base

import androidx.viewbinding.ViewBinding
import com.byl.mvvm.event.EventMessage
import com.lishide.mvvm.api.error.ErrorResult
import com.lishide.mvvm.ui.base.BaseFragment
import com.lishide.mvvm.ui.base.BaseViewModel
import org.greenrobot.eventbus.Subscribe

/**
 * App 中的 Activity 基础类，在这里实现事件传递、通用错误处理、统计等业务需求
 *
 * @author lishide
 * @date 2022/1/18
 */
abstract class BaseAppFragment<VM : BaseViewModel<VB>, VB : ViewBinding> : BaseFragment<VM, VB>() {

    /**
     * 事件传递
     */
    @Subscribe
    fun onEventMainThread(msg: EventMessage) {
        handleEvent(msg)
    }

    /**
     * 消息、事件接收回调
     */
    open fun handleEvent(msg: EventMessage) {
    }

    override fun errorResult(errorResult: ErrorResult) {
        super.errorResult(errorResult)
    }

}