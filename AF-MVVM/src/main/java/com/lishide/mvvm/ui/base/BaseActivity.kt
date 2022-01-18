package com.lishide.mvvm.ui.base

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.lishide.mvvm.api.error.ErrorResult
import com.lishide.mvvm.event.EventBusManager
import com.lishide.mvvm.ext.toast
import com.lishide.mvvm.ui.dialog.LoadingDialog
import com.lishide.mvvm.util.GenericParadigmUtils
import com.lishide.mvvm.util.Logg

abstract class BaseActivity<VM : BaseViewModel<VB>, VB : ViewBinding> : AppCompatActivity(), IView {

    lateinit var mContext: FragmentActivity
    lateinit var vm: VM
    lateinit var vb: VB

    private val mLoading: LoadingDialog by lazy { LoadingDialog(mContext) }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResources()
        var pathfinders = ArrayList<GenericParadigmUtils.Pathfinder>()
        pathfinders.add(GenericParadigmUtils.Pathfinder(0, 0))
        val clazzVM = GenericParadigmUtils.parseGenericParadigm(javaClass, pathfinders) as Class<VM>
        vm = ViewModelProvider(this).get(clazzVM)

        pathfinders = ArrayList()
        pathfinders.add(GenericParadigmUtils.Pathfinder(0, 1))
        val clazzVB = GenericParadigmUtils.parseGenericParadigm(javaClass, pathfinders)
        val method = clazzVB.getMethod("inflate", LayoutInflater::class.java)
        vb = method.invoke(null, layoutInflater) as VB

        vm.binding(vb)
        vm.observe(this, this)

        setContentView(vb.root)

        mContext = this
        Logg.i(getClassName())
        init()
        initView()
        initClick()
        initData()
    }

    /**
     * 防止系统字体影响到app的字体
     *
     * @return
     */
    open fun initResources(): Resources? {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusManager.unregister(this)
    }

    open fun getClassName(): String {
        val className = "BaseActivity"
        try {
            return javaClass.name
        } catch (e: Exception) {
        }
        return className
    }

    abstract fun initView()

    abstract fun initClick()

    abstract fun initData()

    private fun init() {
        EventBusManager.register(this)
        // loading
        (vm as BaseViewModel<*>).isShowLoading.observe(this, {
            if (it) showLoading() else dismissLoading()
        })
        // 错误信息
        (vm as BaseViewModel<*>).errorData.observe(this, {
            if (it.show) showMessage(it.errMsg)
            errorResult(it)
        })
    }

    override fun showLoading() {
        mLoading.showLoading()
    }

    override fun dismissLoading() {
        mLoading.dismiss()
    }

    override fun showMessage(message: String?) {
        mContext.toast(message)
    }

    override fun close() {
        finish()
    }

    override fun onStop() {
        super.onStop()
        dismissLoading()
    }

    /**
     * 接口请求错误回调
     */
    open fun errorResult(errorResult: ErrorResult) {}

}