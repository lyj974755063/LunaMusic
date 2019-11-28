package com.example.lunamusic.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.example.lunamusic.R;

public class SettingFragment extends PreferenceFragmentCompat {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_setting,rootKey);
    }

}
