package com.royalit.sreebell.roomdb;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.royalit.sreebell.utils.Utilities;
import com.royalit.sreebell.utils.Utilities;

import java.util.List;

public class CartViewModel extends ViewModel {

    CartRepository repository;
    Activity activity;
    private LiveData<List<CartItems>> getCartItems;
    private LiveData<List<CartItems>> cartCount;
    private LiveData<List<CartItems>> typeBasedCartCount;
    private LiveData<List<CartItems>> productDetails;

    public CartViewModel(Activity activity) {
        this.activity = activity;
        repository = new CartRepository(activity);

    }


    public CartViewModel(Activity activity, boolean type) {
        repository = new CartRepository(activity, type);
    }

    public LiveData<List<CartItems>> getTypeBasedCartCount() {
        return typeBasedCartCount;
    }

    public void typeBasedCartCount(String deviceID, String type) {
        typeBasedCartCount = repository.getTypeCartCount(deviceID, type);
    }

    public LiveData<List<CartItems>> getCartCount() {
        return cartCount;
    }

    public void cartCount() {
        cartCount = repository.getCartCount(Utilities.getDeviceID(activity));
    }

    public void cartData() {
        getCartItems = repository.getUserCart();
    }

    // below method is use to insert the data to our repository.
    public void insert(CartItems model) {
        repository.insert(model);
    }

    // below line is to delete the data in our repository.
    public void delete(CartItems model, boolean allData, boolean deleteFromTable) {
        repository.delete(model, allData, deleteFromTable);
    }
    public void deleteSingle(CartItems model) {
        repository.deleteSingle(model);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<CartItems>> getGetCartItems() {
        return getCartItems;
    }

    public LiveData<List<CartItems>> getProductDetails() {
        return productDetails;
    }

    public void getProductCartDetails(String productID, Activity activity) {
        productDetails = repository.productCartDetails(Utilities.getDeviceID(activity), productID);
    }
}