package com.byl.mvvm.ui.main

import androidx.fragment.app.Fragment
import com.byl.mvvm.databinding.ActivityTestEventBinding
import com.byl.mvvm.event.EventCode
import com.byl.mvvm.event.EventMessage
import com.byl.mvvm.ui.base.BaseAppActivity
import com.byl.mvvm.ui.main.adapter.FragmentPageAdapter
import com.lishide.mvvm.event.EventBusManager
import com.lishide.mvvm.ui.base.BaseViewModel
import com.lishide.mvvm.widget.clicks
import java.util.*


class TestEventActivity :
    BaseAppActivity<BaseViewModel<ActivityTestEventBinding>, ActivityTestEventBinding>() {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private val titles = arrayOf("最新", "热门", "我的")

    override fun initView() {

    }

    override fun initClick() {
        vb.btn.clicks {
            EventBusManager.post(EventMessage(EventCode.REFRESH))
        }
    }

    override fun initData() {
        for (i in titles.indices) {
            fragments.add(MainFragment.getInstance(i))
            val tab = vb.tabLayout.newTab()
            tab.text = titles[i]
            vb.tabLayout.addTab(tab)
        }
        vb.tabLayout.setupWithViewPager(vb.viewPager, false)
        var pagerAdapter = FragmentPageAdapter(mContext, supportFragmentManager, fragments, titles)
        vb.viewPager.adapter = pagerAdapter

    }

}