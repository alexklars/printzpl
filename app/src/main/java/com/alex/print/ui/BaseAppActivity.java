package com.alex.print.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.alex.print.databinding.DialogProgressBinding;
import com.alex.print.databinding.ToastErrorLayoutBinding;
import com.alex.print.databinding.ToastSuccessLayoutBinding;

@SuppressLint("Registered")
public class BaseAppActivity extends AppCompatActivity {

    AlertDialog dialog;
    DialogProgressBinding progressBinding;

    public void showSuccessMessage(String text) {
        ToastSuccessLayoutBinding binding = ToastSuccessLayoutBinding.inflate(getLayoutInflater());
        binding.text.setText(text);
        Toast toast = new Toast(this);
        toast.setView(binding.getRoot());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void showErrorMessage(String text) {
        showErrorMessage(text, Toast.LENGTH_SHORT);
    }

    public void showErrorMessage(String text, int duration) {
        ToastErrorLayoutBinding binding = ToastErrorLayoutBinding.inflate(getLayoutInflater());
        binding.text.setText(text);
        Toast toast = new Toast(this);
        toast.setView(binding.getRoot());
        toast.setDuration(duration);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void showProgress() {
        showProgress("");
    }

    public void showProgress(String message) {
        showProgress(message, true);
    }

    public void showProgress(String message, boolean isCancelable) {
        if (dialog == null || progressBinding == null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            progressBinding = DialogProgressBinding.inflate(getLayoutInflater());
            dialog = new AlertDialog.Builder(this).setView(progressBinding.getRoot()).create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressBinding.message.setText(message);
        dialog.setCancelable(isCancelable);
        dialog.show();
    }

    public void hideProgress() {
        if (dialog != null && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            dialog.dismiss();
        }
    }
}
