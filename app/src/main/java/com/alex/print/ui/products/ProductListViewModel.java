package com.alex.print.ui.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alex.print.MyApp;
import com.alex.print.repository.DataRepository;
import com.alex.print.repository.db.DataGenerator;
import com.alex.print.repository.db.entity.Product;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;
    private LiveData<List<Product>> productListLivaData;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((MyApp) application).getRepository();
        productListLivaData = mRepository.getProductListLiveData();
    }

    LiveData<List<Product>> getProductListLiveData() {
        return productListLivaData;
    }

    void addProduct() {
        mRepository.addProduct(DataGenerator.generateNewProduct());
    }
}
