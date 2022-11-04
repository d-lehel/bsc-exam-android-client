package com.example.shareapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shareapp.APIClient
import com.example.shareapp.APIService
import com.example.shareapp.Helpers.Token
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Message
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_message_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_details)

        // get token from local storage
        val token: String? = Token.getAccessToken(this)

        val sender_id: String? = intent.getStringExtra("sender_id")
        val sender_name: String? = intent.getStringExtra("sender_name")
        val recipient_id: Int = intent.getIntExtra("recipient_id", 0)
        val product_id: String? = intent.getStringExtra("product_id")
        val is_accepted: Int = intent.getIntExtra("is_accepted", 0)
        val subject: String? = intent.getStringExtra("subject")
        val message: String? = intent.getStringExtra("message")

        message_from.setText(sender_name)
        message_subject.setText(subject)
        message_message.setText(message)

        send_response_button.setOnClickListener {
            // todo id?, product_id?, subject?
            val apiInterface = APIClient().getClient()?.create(APIService::class.java)
            apiInterface!!.sendResponse(
                "Bearer $token",
                sender_id.toString(),
                product_id.toString(),
                "Response",
                response_message.getText().toString(),
                "1"
            ).enqueue(
                object : Callback<Message> {
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    applicationContext,
                                    "Üzenet elküldve!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG)
                                    .show()
                            }

                        } else { //todo hibakzeles specifikus
                            Toast.makeText(
                                applicationContext,
                                response.toString(),
                                Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, ShowMessagesActivity::class.java))
    }
}