package com.ahmed.english_pl.utils.utilities;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalizationManager
{
    Context context;
    public LocalizationManager(Context context)
    {
        this.context = context;
    }
    public void update_locale(String selected_language)
    {
        Locale locale = new Locale(selected_language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}
