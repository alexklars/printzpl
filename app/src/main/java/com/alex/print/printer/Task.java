package com.alex.print.printer;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class Task {
    public static abstract class PrinterTask<I, O> extends AsyncTask<I, Void, O> {
        protected final PrinterAdapter printer;
        protected final WeakReference<IProgressable<O>> progressable;

        public PrinterTask(PrinterAdapter printer, IProgressable<O> progressable) {
            this.printer = printer;
            this.progressable = progressable == null ? null : new WeakReference<>(progressable);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressable != null) {
                IProgressable progressable = this.progressable.get();
                if (progressable != null)
                    progressable.onPreProgress();
            }
        }

        @Override
        protected void onPostExecute(O o) {
            super.onPostExecute(o);
            if (progressable != null) {
                IProgressable progressable = this.progressable.get();
                if (progressable != null)
                    progressable.onPostProgress(o);
            }
        }
    }

    public static class ConnectTask extends PrinterTask<BluetoothDevice, Boolean> {
        public ConnectTask(PrinterAdapter printer, IProgressable<Boolean> progressable) {
            super(printer, progressable);
        }

        @Override
        protected Boolean doInBackground(BluetoothDevice... strings) {
            return printer.connect(strings[0]);
        }
    }

    public static class DisconnectTask extends PrinterTask<Void, Void> {
        public DisconnectTask(PrinterAdapter printer, IProgressable<Void> progressable) {
            super(printer, progressable);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            printer.disconnect();
            return null;
        }
    }

    public static class PrintTask extends PrinterTask<String, Boolean> {
        public PrintTask(PrinterAdapter printer, IProgressable<Boolean> progressable) {
            super(printer, progressable);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return printer.printZPL(strings[0]);
        }
    }

//    public static class TestTask extends PrinterTask<String, PrinterResponse> {
//
//        public TestTask(XprinterP323Adapter printer, IProgressable<PrinterResponse> progressable, Context context) {
//            super(printer, progressable);
//        }
//
//        private boolean checkResponse(PrinterResponse response) {
//            return response != null && response.getErrorCode() == 0;
//        }
//
//        @Override
//        protected PrinterResponse doInBackground(String... params) {
//            String text = params[0];
//            String qr = null;
//            try {
//                qr = params[1];
//            } catch (Exception ignore) {
//            }
//
//
//            PrinterResponse response = new PrinterResponse();
//            try {
//                response = printer.printText(text, IPrinterAdapter.TextAlignment.CENTER);
//                if (checkResponse(response) && qr != null)
//                    response = printer.printBarcode(IPrinterAdapter.Barcode.QR, qr);
//                if (checkResponse(response)) response = printer.scroll(1);
//            } catch (PrinterException e) {
//                e.printStackTrace();
//                return null;
//            }
//            return response;
//        }
//    }
}
