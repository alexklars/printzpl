package com.alex.print.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.alex.print.repository.db.AppDatabase;
import com.alex.print.repository.db.entity.Product;

import java.util.List;

public class DataRepository {

    private static DataRepository INSTANCE;
    private final AppDatabase mDatabase;
    private final AppExecutors mExecutors;
    private final Context mAppContext;

    private DataRepository(final AppDatabase appDatabase, AppExecutors executors, Context appContext) {
        mDatabase = appDatabase;
        mExecutors = executors;
        mAppContext = appContext;
    }

    public static DataRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(
                            AppDatabase.getInstance(context),
                            AppExecutors.getInstance(),
                            context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Product>> getProductListLiveData() {
        return mDatabase.productDao().getProductListLiveData();
    }

    public void addProduct(Product product) {
        mExecutors.diskIO().execute(() ->
                mDatabase.productDao().insert(product));
    }
}
