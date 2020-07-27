package com.alex.print.repository.db;

import com.alex.print.repository.db.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {

    public static List<Product> generateNewProductList() {
        Product product = new Product();
        product.setName("Pair grey bedside tables bedroom furniture storage cabinet modern vintage decor");
        product.setGuid("A267865FB90000012");
        product.setOrder("OOL_2451");
        product.setCourier("");
        product.setSupplier("LLC Vintage Furniture");
        product.setRoute("1099345");

        Product product2 = new Product();
        product2.setName("2x Feet Table Legs Metal-Metal Table Steel Legs-Pied de table");
        product2.setGuid("A267865FB90000012");
        product2.setOrder("OOL_2451");
        product2.setCourier("");
        product2.setSupplier("LLC Vintage Furniture");
        product2.setRoute("1099345");

        Product product3 = new Product();
        product3.setName("Grey 2 drawer bedside chest bedroom side table living room storage furniture");
        product3.setGuid("2346898B90000012");
        product3.setOrder("OOL_2451");
        product3.setCourier("");
        product3.setSupplier("LLC Vintage Furniture");
        product3.setRoute("1099345");

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);
        productList.add(product3);

        return productList;
    }

    public static Product generateNewProduct() {
        Product product = new Product();
        product.setName("Pair grey bedside tables bedroom furniture storage cabinet modern vintage decor");
        product.setGuid("A267865FB90000012");
        product.setOrder("OOL_2451");
        product.setCourier("");
        product.setSupplier("LLC Vintage Furniture");
        product.setRoute("1099345");
        return product;
    }
}
