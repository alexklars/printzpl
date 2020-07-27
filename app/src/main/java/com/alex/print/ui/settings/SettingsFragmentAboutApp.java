package com.alex.print.ui.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alex.print.R;
import com.alex.print.databinding.FragmentAboutAppBinding;

public class SettingsFragmentAboutApp extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAboutAppBinding binding = FragmentAboutAppBinding.inflate(inflater);

        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            binding.tvAppVer.setText(getString(R.string.app_version, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return binding.getRoot();
    }
}
