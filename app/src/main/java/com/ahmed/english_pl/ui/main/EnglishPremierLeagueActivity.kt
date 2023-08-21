package com.ahmed.english_pl.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import com.ahmed.english_pl.databinding.ActivityEnglishPremierLeagueBinding
import com.ahmed.english_pl.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnglishPremierLeagueActivity : BaseActivity<ActivityEnglishPremierLeagueBinding>() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
    }

    override fun initializeViews() {
    }

    override fun setListeners() {
    }

    override val viewBindingInflater: (LayoutInflater) -> ActivityEnglishPremierLeagueBinding
        get() = ActivityEnglishPremierLeagueBinding::inflate

}