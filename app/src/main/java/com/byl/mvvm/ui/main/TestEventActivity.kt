package com.byl.mvvm.ui.main

import androidx.fragment.app.Fragment
import com.byl.mvvm.databinding.ActivityTestEventBinding
import com.lishide.mvvm.event.Event
import com.lishide.mvvm.event.EventCode
import com.lishide.mvvm.event.EventMessage
import com.lishide.mvvm.ui.base.BaseActivity
import com.lishide.mvvm.ui.base.BaseViewModel
import com.byl.mvvm.ui.main.adapter.FragmentPageAdapter
import com.lishide.mvvm.widget.clicks
import java.util.*


class TestEventActivity :
    BaseActivity<BaseViewModel<ActivityTestEventBinding>, ActivityTestEventBinding>() {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private val titles = arrayOf("最新", "热门", "我的")

    override fun initView() {

    }

    override fun initClick() {
        vb.btn.clicks {
            Event.post(EventMessage(EventCode.REFRESH))
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