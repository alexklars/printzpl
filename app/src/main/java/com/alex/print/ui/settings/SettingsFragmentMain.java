package com.alex.print.ui.settings;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.alex.print.R;


public class SettingsFragmentMain extends PreferenceFragmentCompat
        implements
        Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        findPreference(getString(R.string.pr_printers)).setOnPreferenceClickListener(this);
        findPreference(getString(R.string.pr_about_app)).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.pr_printers))) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((SettingsActivity) requireActivity()).openPrinterSettings(true);
            }
            return true;
        }
        if (key.equals(getString(R.string.pr_about_app))) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((SettingsActivity) requireActivity()).openAboutAppSettings();
            }
            return true;
        }
        return false;
    }
}
