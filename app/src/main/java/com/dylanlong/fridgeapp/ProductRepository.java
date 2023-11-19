package com.dylanlong.fridgeapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ProductRepository {
    ProductDAO productDAO;

    private static class InsertProductAsync extends AsyncTask<Product, Void, Void> {
        private ProductDAO productDAO;

        private InsertProductAsync(ProductDAO productDAO) {
            this.productDAO = productDAO;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDAO.insert(products[0]);
            return null;
        }
    }

    public ProductRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        this.productDAO = productDAO;
    }

    public void insert(Product product)
    {
        new InsertProductAsync(productDAO).execute(product);
    }

    public LiveData<Product> getFoodItem(String code) {
        return productDAO.getFoodItem(code);
    }
}
