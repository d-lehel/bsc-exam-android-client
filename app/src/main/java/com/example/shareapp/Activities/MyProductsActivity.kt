package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Adapters.ProductAdapter
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Product
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProductsActivity : AppCompatActivity(), ProductAdapter.OneItemClickListener {
    lateinit var products: List<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)

        // inicialize api interface
        val apiInterface = APIClient().getClient()?.create(APIService::class.java)
        apiInterface!!.allProduct(
            "Bearer " + Token.getAccessToken(this),
            "",
            "own",
            "upload_time",
            "DESC",
            10000
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
                    Toast.makeText(this@MyProductsActivity, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            // if api call failed
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@MyProductsActivity, "fail: " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    private fun showData(products: List<Product>) {
        rview_myProducts.apply {
            layoutManager = LinearLayoutManager(this@MyProductsActivity)
            adapter = ProductAdapter(this@MyProductsActivity, products, this@MyProductsActivity)
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem: Product = products[position]
        val intent = Intent(applicationContext, EditProductActivity::class.java);

        intent.putExtra("id", clickedItem.getId().toString())
        intent.putExtra("name", clickedItem.getProduct().toString())
        intent.putExtra("description", clickedItem.getDescription().toString())
        intent.putExtra("amount", clickedItem.getAmount().toString())
        intent.putExtra("expiration", clickedItem.getExpiration().toString())
        intent.putExtra("adress", clickedItem.getAdress().toString())
        // todo expiration, doesnt show if expired
        intent.putExtra("image", clickedItem.getImage().toString())
        startActivity(intent);
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, ProfileActivity::class.java))
    }
}