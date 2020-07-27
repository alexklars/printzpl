package com.alex.print.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alex.print.R;
import com.alex.print.ui.BaseAppActivity;


public class SettingsActivity extends BaseAppActivity {

    public static final String ACTION_PRINTER = "ACTION_PRINTER";
    public static final String STACK_NAME = "stack_name";

    public static Intent getPrinterSettingsIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setAction(ACTION_PRINTER);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            String action = getIntent().getAction();

            if (action == null) {
                openMainSettings();
            } else if (action.equals(ACTION_PRINTER))
                openPrinterSettings(false);
        }
    }

    public void openMainSettings() {
        replaceFragment(new SettingsFragmentMain(), false);
        setTitle(getString(R.string.action_settings));
    }

    public void openPrinterSettings(boolean addToBackStack) {
        replaceFragment(new SettingsFragmentPrinter(), addToBackStack);
        setTitle(getString(R.string.string_printer));
    }

    public void openAboutAppSettings() {
        replaceFragment(new SettingsFragmentAboutApp(), true);
        setTitle(getString(R.string.pr_title_about_app));
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsContainer, fragment)
                    .addToBackStack(STACK_NAME)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsContainer, fragment)
                    .commit();
        }
    }

    private void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!getSupportFragmentManager().popBackStackImmediate(STACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)) {
                finish();
            } else {
                setTitle(getString(R.string.action_settings));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle(getString(R.string.action_settings));
    }
}