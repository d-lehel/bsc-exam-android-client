package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Adapters.MessageAdapter
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Message
import com.example.shareapp.ResponseModels.Product
import kotlinx.android.synthetic.main.activity_show_messages.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowMessagesActivity : AppCompatActivity(), MessageAdapter.OneItemClickListener{

    lateinit var messages: List<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_messages)


        val apiInterface = APIClient().getClient()?.create(APIService::class.java)
        apiInterface!!.allInboxMessages("Bearer " + Token.getAccessToken(this)).enqueue(object : Callback<List<Message>> {

            // if api call was succesfull
            override fun onResponse(
                call: Call<List<Message>>,
                response: Response<List<Message>>
            ) {
                if (response.code() == 200) {
                    messages = response.body()!!
                    showData(response.body()!!)
                } else {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            // if api call failed
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Toast.makeText(applicationContext, "fail: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showData(messages: List<Message>) {
        rview_messages.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = MessageAdapter(applicationContext,messages,this@ShowMessagesActivity)
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem: Message = messages[position]
        val intent = Intent(applicationContext, MessageDetailsActivity::class.java);
        intent.putExtra("sender_id",clickedItem.getSenderId().toString())
        intent.putExtra("sender_name",clickedItem.getSender_name().toString())
        intent.putExtra("recipient_id",clickedItem.getRecipient_id().toString())
        intent.putExtra("product_id",clickedItem.getProduct_id().toString())
        intent.putExtra("is_accepted",clickedItem.isAccepted().toString())
        intent.putExtra("subject",clickedItem.getSubject().toString())
        intent.putExtra("message",clickedItem.getMessage().toString())
        startActivity(intent);
    }
    override fun onBackPressed() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }
}