package com.alex.print.repository.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.alex.print.repository.db.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Insert
    void insert(List<Product> productList);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product")
    LiveData<List<Product>> getProductListLiveData();
}
