package com.royalit.sreebell.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.royalit.sreebell.R
import com.royalit.sreebell.databinding.CategoriesAdapterBinding
import com.royalit.sreebell.models.Category_ListResponse
import com.royalit.sreebell.models.Category_Response
import retrofit2.Callback

class CategoryList_Adapter(
    val context: Context,
    private var languageList: ArrayList<Category_Response>

) : RecyclerView.Adapter<CategoryList_Adapter.ViewHolder>() {
    interface CategoryClickListener {
        fun onCategoryClick(categoryId: Int)
    }
    inner class ViewHolder(val binding: CategoriesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoriesAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object;
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(languageList[position]) {
                // set name of the language from the list
//                binding.brndsTitleText.text = languageList.get(position).prodcut_name
//                binding.brandNameText.text = languageList.get(position).prodcut_desc

                Glide.with(context).load(languageList.get(position).category_image)
                    .error(R.drawable.placeholder_image)
                    .into(holder.binding.categoryImage)

                binding.categoryText.text = "" + languageList.get(position).category_name

                Log.e("category_image", "" + languageList.get(position).category_image)

                holder.itemView.setOnClickListener {

                    val sharedPreferences =
                        context.getSharedPreferences(
                            "loginprefs",
                            Context.MODE_PRIVATE)

                    val editor = sharedPreferences.edit()
                    editor.putString(
                        "categoryid",
                        languageList.get(position).categories_id.toString())
                    editor.commit()

                    val navController =
                        Navigation.findNavController(
                            context as Activity,
                            R.id.nav_host_fragment_content_home_screen)
                    val b=Bundle()
                    b.putParcelableArrayList("categories" , languageList)
                    val bundle = bundleOf("categories" to languageList )

                    navController.navigate(R.id.navigation_products,bundle)

                }

            }
        }
    }


    // return the size of languageList
    override fun getItemCount(): Int {
        return languageList.size
    }
}






