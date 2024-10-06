package com.royalit.sreebell.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.royalit.sreebell.models.PriceList;

import java.util.List;

@Entity(tableName = "cart_items")
public class CartItems {

    @SerializedName("id")
    public int cartID;


    @PrimaryKey(autoGenerate = true)
    public int _id;

    @SerializedName("customer_id")
    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "deviceID")
    public String deviceID;

    @ColumnInfo(name = "typeOfCart")
    public String typeOfCart;

    @SerializedName( "stock")
    @ColumnInfo(name = "stock")
    public String stock;


    @SerializedName("item_name")
    @ColumnInfo(name = "itemName")
    public String itemName;

    @SerializedName("product_name")
    @ColumnInfo(name = "product_name")
    public String product_name;

    @SerializedName( "product_title")
    @ColumnInfo(name = "product_title")
    public String product_title;

    @SerializedName("product_image")
    @ColumnInfo(name = "product_image")
    public String itemImage;



    @SerializedName("cart_quantity")
    @ColumnInfo(name = "cartQty")
    public String cartQty;



//    @ColumnInfo(name = "final_price")
//    public String final_price;


    @SerializedName("product_id")
    @ColumnInfo(name = "product_id")
    public String product_id;


    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public String createdDate;


    @ColumnInfo(name = "store_id")
    public String store_id;

    @ColumnInfo(name = "weight")
    public String weight;

    @ColumnInfo(name = "max_order_quantity")
    @SerializedName( "max_order_quantity")
    public String max_order_quantity;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("kgs")
    public String kgs ;
    @SerializedName("quintals")
    public String quintals ;
    @SerializedName("district_id")
    public String district_id ;
    @SerializedName("pack_size")
    public String pack_size ;
    @SerializedName("master_packing_size")
    public String master_packing_size ;
    @SerializedName("bags_for_quintals")
    public String bags_for_quintals ;
    @SerializedName("bag_contain_units")
    public String bag_contain_units ;
    @SerializedName("unit_of_measurement")
    public String unit_of_measurement ;

    public String current_bags ;
    public String current_quitals ;

    @SerializedName("products_id")
    public String products_id;

    @SerializedName("categories_id")
    public String categories_id;
    @SerializedName("product_num")
    public String product_num;

    @ColumnInfo(name = "sales_price")
    @SerializedName("sales_price")
    public String sales_price;

    @ColumnInfo(name = "offer_price")
    @SerializedName("offer_price")
    public String offer_price;

    @ColumnInfo(name = "category_2_price")
    @SerializedName("category_2_price")
    public String category_2_price;


    @SerializedName("order_by")
    public String order_by;

    @SerializedName("tax_number")
    public String tax_number;

    @ColumnInfo(name = "product_quantity")
    @SerializedName("product_quantity")
    public String product_quantity;

    @ColumnInfo(name = "distributor_price")
    @SerializedName("distributor_price")
    public String distributor_price;

    @ColumnInfo(name = "general_trade_price")
    @SerializedName("general_trade_price")
    public String general_trade_price;


    /*@SerializedName("prices")
    public List<PriceList> prices;*/




    /* @SerializedName("id"                  ) var id                 : String? = null,
    @SerializedName("customer_id"         ) var customerId         : String? = null,
    @SerializedName("product_id"          ) var productId          : String? = null,
    @SerializedName("quantity"            ) var quantity           : String? = null,
    @SerializedName("cart_quantity"       ) var cartQuantity       : String? = null,
    @SerializedName("products_id"         ) var productsId         : String? = null,
    @SerializedName("categories_id"       ) var categoriesId       : String? = null,
    @SerializedName("product_num"         ) var productNum         : String? = null,
    @SerializedName("product_name"        ) var productName        : String? = null,
    @SerializedName("product_title"       ) var productTitle       : String? = null,
    @SerializedName("product_information" ) var productInformation : String? = null,
    @SerializedName("product_image"       ) var productImage       : String? = null,
    @SerializedName("max_order_quantity"  ) var maxOrderQuantity   : String? = null,
    @SerializedName("sales_price"         ) var salesPrice         : String? = null,
    @SerializedName("offer_price"         ) var offerPrice         : String? = null,
    @SerializedName("category_2_price"    ) var category2Price     : String? = null,
    @SerializedName("stock"               ) var stock              : String? = null,
    @SerializedName("status"              ) var status             : String? = null,
    @SerializedName("order_by"            ) var orderBy            : String? = null,
    @SerializedName("tax_number"          ) var taxNumber          : String? = null,
    @SerializedName("item_name"           ) var itemName           : String? = null,
    @SerializedName("batch_name"          ) var batchName          : String? = null,
    @SerializedName("created_date"        ) var createdDate        : String? = null,
    @SerializedName("updated_date"        ) var updatedDate        : String? = null,
    @SerializedName("product_quantity"    ) var productQuantity    : String? = null
    */
    public CartItems(
            int _id,
            int cartID, String user_id, String deviceID, String typeOfCart,
                     String itemName, String itemImage, String cartQty, String sales_price,
                     String offer_price,  String product_id, String stock, String max_order_quantity,
                     String category_2_price,String product_name,String product_title,String product_quantity,String distributor_price,String general_trade_price) {
        this._id = _id;
        this.cartID = cartID;
        this.user_id = user_id;
        this.deviceID = deviceID;
        this.typeOfCart = typeOfCart;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.cartQty = cartQty;
        this.sales_price = sales_price;
        this.product_id = product_id;
        this.offer_price = offer_price;
        this.stock = stock;
        this.max_order_quantity = max_order_quantity;
        this.category_2_price = category_2_price;
        this.product_name = product_name;
        this.product_title = product_title;
        this.product_quantity = product_quantity;
        this.distributor_price = distributor_price;
        this.general_trade_price = general_trade_price;

    }

    public String getDistributor_price() {
        return distributor_price;
    }

    public void setDistributor_price(String distributor_price) {
        this.distributor_price = distributor_price;
    }

    public String getGeneral_trade_price() {
        return general_trade_price;
    }

    public void setGeneral_trade_price(String general_trade_price) {
        this.general_trade_price = general_trade_price;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getTypeOfCart() {
        return typeOfCart;
    }

    public void setTypeOfCart(String typeOfCart) {
        this.typeOfCart = typeOfCart;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getCartQty() {
        return cartQty;
    }

    public void setCartQty(String cartQty) {
        this.cartQty = cartQty;
    }


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }


    public String getMax_order_quantity() {
        return max_order_quantity;
    }

    public void setMax_order_quantity(String max_order_quantity) {
        this.max_order_quantity = max_order_quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProducts_id() {
        return products_id;
    }

    public void setProducts_id(String products_id) {
        this.products_id = products_id;
    }

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getCategory_2_price() {
        return category_2_price;
    }

    public void setCategory_2_price(String category_2_price) {
        this.category_2_price = category_2_price;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getTax_number() {
        return tax_number;
    }

    public void setTax_number(String tax_number) {
        this.tax_number = tax_number;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}

