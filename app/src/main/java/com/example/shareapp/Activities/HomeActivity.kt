package com.example.shareapp.Activities

import android.content.Intent
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Adapters.ProductAdapter
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Product
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), ProductAdapter.OneItemClickListener {

    lateinit var products: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //no dark
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // api call when application started
        apiCall()

        show_profile_button.setOnClickListener {
            startActivity(Intent(applicationContext, ProfileActivity::class.java));
        }

        add_product.setOnClickListener {
            startActivity(Intent(applicationContext, AddProductActivity::class.java))
        }

        show_messages.setOnClickListener {
            startActivity(Intent(applicationContext, ShowMessagesActivity::class.java))
        }

        search_button.setOnClickListener {
            apiCall()
        }
        newest_radio.setOnClickListener{
            apiCall()
        }
        closest_radio.setOnClickListener{
            apiCall()
        }
        asc_radio.setOnClickListener{
            apiCall()
        }
        desc_radio.setOnClickListener{
            apiCall()
        }

        km_input.doAfterTextChanged {
            if(km_input.text.toString() != "") {
                apiCall()
            }
        }


    }
    private fun apiCall(){
        val search_keyword = search_input.text.toString().trim()
        val filter_1 = "others"
        var filter_2 = "distance"
        var filter_3 = "ASC"
        val max_distance = km_input.text.toString().trim().toInt()

        if (newest_radio.isChecked) {
            filter_2 = "upload_time"
        }
        if (desc_radio.isChecked) {
            filter_3 = "DESC"
        }

        // inicialize api interface
        val apiInterface = APIClient().getClient()?.create(APIService::class.java)
        apiInterface!!.allProduct(
             Token.getAccessToken(this),
            search_keyword,
            filter_1,
            filter_2,
            filter_3,
            max_distance
        ).enqueue(object : Callback<List<Product>> {

            // if api call was succesfull
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.code() == 200) {
                    products = response.body()!!
                    showData(response.body()!!)
                } else {
                    Toast.makeText(this@HomeActivity, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            // if api call failed
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "fail: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showData(products: List<Product>) {
        rview_myProducts.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = ProductAdapter(this@HomeActivity, products, this@HomeActivity)
        }
    }

    // on list item was clicked
    override fun onItemClick(position: Int) {
        val clickedItem: Product = products[position]
        val intent = Intent(applicationContext, ProductDetailsActivity::class.java);
        intent.putExtra("donator", clickedItem.getDonator().toString())
        intent.putExtra("distance", clickedItem.getDistance().toString())
        intent.putExtra("description", clickedItem.getDescription().toString())
        intent.putExtra("amount", clickedItem.getAmount().toString())
        intent.putExtra("expiration", clickedItem.getExpiration().toString())
        intent.putExtra("adress", clickedItem.getAdress().toString())
        intent.putExtra("image", clickedItem.getImage().toString())
        intent.putExtra("product", clickedItem.getProduct().toString())
        intent.putExtra("user_id", clickedItem.getUser_id().toString())
        intent.putExtra("id", clickedItem.getId().toString())
        startActivity(intent);
    }

    override fun onBackPressed() {
        // do nothing, if not go back to login page..
    }
}