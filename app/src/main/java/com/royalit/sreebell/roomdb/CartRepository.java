package com.royalit.sreebell.roomdb;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.royalit.sreebell.utils.Utilities;

import java.util.List;

public class CartRepository {

    static Activity activity;
    private final CartDAO dao;
    private LiveData<List<CartItems>> getUserCart;


    public CartRepository(Activity application) {
        activity = application;
        MotherChoiceDB database = MotherChoiceDB.getInstance(application);
        dao = database.cartDAO();
        getCartData();

    }

    public CartRepository(Activity application, boolean type) {
        activity = application;
        MotherChoiceDB database = MotherChoiceDB.getInstance(application);
        dao = database.cartDAO();
        getCartData(type);

    }

    public LiveData<List<CartItems>> getCartCount(String deviceID) {
        getUserCart = dao.getCartCount(deviceID, AppConstants.PRODUCTS_CART);


        return getUserCart;
    }

    public LiveData<List<CartItems>> productCartDetails(String deviceID, String productID) {
        getUserCart = dao.getProductCartQty(deviceID, AppConstants.PRODUCTS_CART, productID);

        return getUserCart;
    }

    public void getCartData(boolean food) {

        getUserCart = dao.getUserCart(Utilities.getDeviceID(activity), AppConstants.PRODUCTS_CART);


    }


    public void getCartData() {


        getUserCart = dao.getUserCart(Utilities.getDeviceID(activity), AppConstants.PRODUCTS_CART);


    }

    // creating a method to insert the data to our database.
    public void insert(CartItems model) {

        new InsertCourseAsyncTask(dao).execute(model);


    }

    // below method is to read all the courses.
    public LiveData<List<CartItems>> getUserCart() {
        return getUserCart;
    }

    // creating a method to delete the data in our database.
    public void delete(CartItems model, boolean allData, boolean deleteFromTable) {
        new DeleteCourseAsyncTask(dao, allData, deleteFromTable).execute(model);
    }
    public void deleteSingle(CartItems model) {
        new DeleteSingleCartAsyncTask(dao).execute(model);
    }
    public void deleteAll() {
        new DeleteCartTask(dao).execute();
    }

    public LiveData<List<CartItems>> getTypeCartCount(String deviceID, String type) {
        getUserCart = dao.getCartCountBasedOnType(deviceID, type);
        return getUserCart;
    }


    // we are creating a async task method to insert new course.
    private static class InsertCourseAsyncTask extends AsyncTask<CartItems, Void, Void> {
        private final CartDAO dao;


        private InsertCourseAsyncTask(CartDAO dao) {
            this.dao = dao;

        }

        @Override
        protected Void doInBackground(CartItems... model) {
            // below line is use to insert our modal in dao.

            if (!dao.checkCartProduct(model[0].product_id)) {

               /* if (model[0].typeOfCart.equals(AppConstants.PRODUCTS_REST)) {
                    if (!dao.checkSameRestaurantOrNot(model[0].user_id, model[0].store_id, model[0].typeOfCart)) {
                        dao.insert(model[0]);
                    } else {
                        activity.runOnUiThread(() -> {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                            dialog.setMessage("In your cart you have another items in another restaurant. Click yes to remove that item in cart and add this item");


                            dialog.setPositiveButton("Yes", (dialogInterface, i1) -> {
                                CartViewModel viewModel = new CartViewModel(activity);
                                viewModel.delete(model[0], true, true);
                                viewModel.insert(model[0]);
                            });

                            dialog.setNegativeButton("No", null);
                            dialog.create().show();
                        });
                    }
                } else {*/
                    dao.insert(model[0]);
                    Log.e("insert_data==>", "" + model[0].cartQty + " , " + model[0].product_id);

               // }
            } else {
                dao.updateCartQty(model[0].cartQty, String.valueOf(model[0].cartID),model[0].product_id);
                Log.e("update_data==>", "" + model[0].cartQty + " , " + model[0].product_id);
                //activity.runOnUiThread(()->Utilities.showToast(activity,"Item quantity updated..!"));
            }

            return null;
        }
    }

    // we are creating a async task method to delete course.
    // we are creating a async task method to delete course.
    private static class DeleteCourseAsyncTask extends AsyncTask<CartItems, Void, Void> {
        private final CartDAO dao;
        public Boolean allData;
        public Boolean deleteFromTable;

        private DeleteCourseAsyncTask(CartDAO dao, boolean allData, Boolean deleteFromTable) {
            this.dao = dao;
            this.allData = allData;
            this.deleteFromTable = deleteFromTable;
        }


        @Override
        protected Void doInBackground(CartItems... models) {
            // below line is use to delete
            // our course modal in dao.
            //dao.delete(models[0]);


            if (deleteFromTable) {

                dao.deleteTable();

            } else {
                dao.delete(models[0]);
            }


            return null;
        }
    }
    private static class DeleteCartTask extends AsyncTask<CartItems, Void, Void> {
        private final CartDAO dao;


        private DeleteCartTask(CartDAO dao) {
            this.dao = dao;

        }

        @Override
        protected Void doInBackground(CartItems... models) {
            // below line is use to delete
            // our course modal in dao.
            //dao.delete(models[0]);




                dao.deleteTable();




            return null;
        }
    }

    private static class DeleteSingleCartAsyncTask extends AsyncTask<CartItems, Void, Void> {
        private final CartDAO dao;


        private DeleteSingleCartAsyncTask(CartDAO dao) {
            this.dao = dao;

        }

        @Override
        protected Void doInBackground(CartItems... models) {
            // below line is use to delete
            // our course modal in dao.
            //dao.delete(models[0]);




                dao.deleteSingleCart(models[0].product_id);




            return null;
        }
    }

}
