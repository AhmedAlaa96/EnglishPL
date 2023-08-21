package com.ahmed.english_pl.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.ahmed.english_pl.databinding.ActivityEnglishPremierLeagueBinding
import com.ahmed.english_pl.ui.base.BaseActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnglishPremierLeagueActivity : BaseActivity<ActivityEnglishPremierLeagueBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        AppCenter.start(
            application, "9107de5f-830a-4780-b26b-64dbab397ea4",
            Analytics::class.java, Crashes::class.java
        )
    }

    override fun initializeViews() {
    }

    override fun setListeners() {
    }

    override val viewBindingInflater: (LayoutInflater) -> ActivityEnglishPremierLeagueBinding
        get() = ActivityEnglishPremierLeagueBinding::inflate

}