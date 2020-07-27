package com.alex.print.ui.products;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.alex.print.R;
import com.alex.print.databinding.ActivityProductListBinding;
import com.alex.print.printer.BluetoothUtil;
import com.alex.print.printer.IProgressable;
import com.alex.print.printer.PrinterAdapter;
import com.alex.print.printer.Task;
import com.alex.print.printer.ZplUtil;
import com.alex.print.repository.db.entity.Product;
import com.alex.print.ui.BaseAppActivity;
import com.alex.print.ui.settings.SettingsActivity;

import java.util.List;

public class ProductListActivity extends BaseAppActivity {

    private final int CONNECT_PRINTER_REQUEST_CODE = 123;

    private ActivityProductListBinding binding;
    private ProductListViewModel viewModel;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        initUI();
        subscribeUI();
    }

    private void initUI() {
        adapter = new ProductListAdapter(this);
        binding.productList.setAdapter(adapter);
        binding.productList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.print.setOnClickListener(v -> print());
    }

    private void subscribeUI() {
        viewModel.getProductListLiveData().observe(this, products ->
                adapter.submitList(products));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.addNewProduct:
                viewModel.addProduct();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONNECT_PRINTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            print();
        }
    }

    public void print() {
        PrinterAdapter printer = PrinterAdapter.getInstance();

        // Check products
        List<Product> productList = viewModel.getProductListLiveData().getValue();
        if (productList == null || productList.isEmpty()) {
            showErrorMessage(getString(R.string.no_products_to_print));
            return;
        }

        // Check printer connection
        if (!printer.isConnected()) {
            connectLastConnectedDevice(printer);
            return;
        }

        String productListZplStr = ZplUtil.getProductListZplStr(productList);

        new Task.PrintTask(printer, new IProgressable<Boolean>() {
            @Override
            public void onPreProgress() {
            }

            @Override
            public void onPostProgress(Boolean success) {
                if (!success) {
                    connectLastConnectedDevice(printer);
                }
            }
        }).execute(productListZplStr);
    }

    private void openPrinterSettings() {
        startActivityForResult(SettingsActivity.getPrinterSettingsIntent(this), CONNECT_PRINTER_REQUEST_CODE);
    }

    private void connectLastConnectedDevice(PrinterAdapter printer) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            BluetoothUtil.turnOnBluetooth(this);
            return;
        }

        BluetoothDevice device = BluetoothUtil.findLastConnectedBluetoothDevice(this, bluetoothAdapter);

        if (device == null) {
            openPrinterSettings();
            return;
        }

        new Task.ConnectTask(printer, connectProgress).execute(device);
    }

    private IProgressable<Boolean> connectProgress = new IProgressable<Boolean>() {
        @Override
        public void onPreProgress() {
            showProgress(getString(R.string.connecting));
        }

        @Override
        public void onPostProgress(Boolean success) {
            hideProgress();
            if (success) {
                print();
            } else {
                openPrinterSettings();
            }
        }
    };
}
