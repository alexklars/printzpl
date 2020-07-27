package com.alex.print.printer;

public interface IProgressable<R> {
    void onPreProgress();

    void onPostProgress(R result);
}
