package com.alex.print.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.alex.print.R;
import com.alex.print.databinding.FragmentPrintersSettingsBinding;
import com.alex.print.printer.BluetoothUtil;
import com.alex.print.printer.IProgressable;
import com.alex.print.printer.PrinterAdapter;
import com.alex.print.printer.Task;
import com.alex.print.ui.BaseAppActivity;

import java.util.LinkedHashMap;

import static android.app.Activity.RESULT_OK;
import static com.alex.print.ui.settings.SettingsActivity.ACTION_PRINTER;

public class SettingsFragmentPrinter extends Fragment {

    private FragmentPrintersSettingsBinding binding;
    private PrinterAdapter printer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        printer = PrinterAdapter.getInstance();
        binding = FragmentPrintersSettingsBinding.inflate(inflater);

        initUI();

        if (requireActivity().getIntent() != null && ACTION_PRINTER.equals(requireActivity().getIntent().getAction())) {
            if (!printer.isConnected()) {
                showChooseDeviceDialog();
            }
        }

        return binding.getRoot();
    }

    private void initUI() {
        binding.connectPrinter.setOnClickListener(view -> {
            if (printer.isConnected()) {
                disconnect();
            } else {
                showChooseDeviceDialog();
            }
        });
        updateUI();
    }

    private void updateUI() {
        if (printer != null && printer.isConnected()) {
            binding.printerStatus.setText(R.string.printer_connected);
            binding.printerInfo.setText(printer.getName());
            binding.printerInfo.setVisibility(View.VISIBLE);
        } else {
            binding.printerStatus.setText(R.string.printer_disconnected);
            binding.printerInfo.setVisibility(View.GONE);
        }
    }

    private void showChooseDeviceDialog() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            BluetoothUtil.turnOnBluetooth(requireContext());
            return;
        }

        LinkedHashMap<String, BluetoothDevice> devices = BluetoothUtil.getBluetoothDeviceMap(bluetoothAdapter);

        if (devices.isEmpty()) {
            new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.no_paired_devices)
                    .setNegativeButton(R.string.string_ok, (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                devices.keySet().toArray(new String[]{}));
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_device)
                .setNegativeButton(R.string.string_cancel, (dialog, which) -> dialog.dismiss())
                .setAdapter(arrayAdapter, (dialog, which) -> {
                    connect(devices.get(arrayAdapter.getItem(which)));
                })
                .show();
    }


    private void connect(BluetoothDevice bluetoothDevice) {
        if (printer != null) {
            new Task.ConnectTask(printer, connectProgress).execute(bluetoothDevice);
        }
    }

    private void disconnect() {
        if (printer != null) {
            new Task.DisconnectTask(printer, disconnectProgress).execute();
        }
    }

    private IProgressable<Boolean> connectProgress = new IProgressable<Boolean>() {
        @Override
        public void onPreProgress() {
            ((BaseAppActivity) requireActivity()).showProgress(getString(R.string.connecting));
        }

        @Override
        public void onPostProgress(Boolean success) {
            if (getActivity() != null) {
                ((BaseAppActivity) requireActivity()).hideProgress();
                updateUI();
                if (success) {
                    BluetoothUtil.setLastConnectedBluetoothDeviceAddress(requireContext(), printer.getAddress());
                    if (requireActivity().getIntent() != null && ACTION_PRINTER.equals(requireActivity().getIntent().getAction())) {
                        requireActivity().setResult(RESULT_OK);
                        requireActivity().finish();
                    }
                } else {
                    BluetoothUtil.setLastConnectedBluetoothDeviceAddress(requireContext(), "");
                    ((BaseAppActivity) requireActivity()).showErrorMessage(getString(R.string.can_not_connect_printer));
                }
            }
        }
    };

    private IProgressable<Void> disconnectProgress = new IProgressable<Void>() {
        @Override
        public void onPreProgress() {
            ((BaseAppActivity) requireActivity()).showProgress(getString(R.string.disconnecting));
        }

        @Override
        public void onPostProgress(Void result) {
            if (getActivity() != null) {
                ((BaseAppActivity) requireActivity()).hideProgress();
                updateUI();
            }
        }
    };
}
