package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class CartDataItem (
    @SerializedName("id"                  ) var id                 : String? = null,
    @SerializedName("customer_id"         ) var customerId         : String? = null,
    @SerializedName("product_id"          ) var productId          : String? = null,
    @SerializedName("quantity"            ) var quantity           : String? = null,
    @SerializedName("kgs"                 ) var kgs                : String? = null,
    @SerializedName("quintals"            ) var quintals           : String? = null,
    @SerializedName("cart_quantity"       ) var cartQuantity       : String? = null,
    @SerializedName("products_id"         ) var productsId         : String? = null,
    @SerializedName("categories_id"       ) var categoriesId       : String? = null,
    @SerializedName("district_id"         ) var districtId         : String? = null,
    @SerializedName("product_num"         ) var productNum         : String? = null,
    @SerializedName("product_name"        ) var productName        : String? = null,
    @SerializedName("product_title"       ) var productTitle       : String? = null,
    @SerializedName("product_information" ) var productInformation : String? = null,
    @SerializedName("product_image"       ) var productImage       : String? = null,
    @SerializedName("max_order_quantity"  ) var maxOrderQuantity   : String? = null,
    @SerializedName("sales_price"         ) var salesPrice         : String? = null,
    @SerializedName("stock"               ) var stock              : String? = null,
    @SerializedName("status"              ) var status             : String? = null,
    @SerializedName("tax_number"          ) var taxNumber          : String? = null,
    @SerializedName("item_name"           ) var itemName           : String? = null,
    @SerializedName("batch_name"          ) var batchName          : String? = null,
    @SerializedName("pack_size"           ) var packSize           : String? = null,
    @SerializedName("unit_of_measurement" ) var unitOfMeasurement  : String? = null,
    @SerializedName("master_packing_size" ) var masterPackingSize  : String? = null,
    @SerializedName("bag_contain_units"   ) var bagContainUnits    : String? = null,
    @SerializedName("bags_for_quintals"   ) var bagsForQuintals    : String? = null,
    @SerializedName("created_date"        ) var createdDate        : String? = null,
    @SerializedName("updated_date"        ) var updatedDate        : String? = null,
    @SerializedName("product_quantity"    ) var productQuantity    : String? = null,
    @SerializedName("distributor_price"   ) var distributorPrice   : String? = null,
    @SerializedName("general_trade_price" ) var generalTradePrice  : String? = null
)