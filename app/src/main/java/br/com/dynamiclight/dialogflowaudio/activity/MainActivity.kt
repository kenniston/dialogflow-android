package br.com.dynamiclight.dialogflowaudio.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.dynamiclight.dialogflowaudio.R
import br.com.dynamiclight.dialogflowaudio.model.DialogflowResult
import br.com.dynamiclight.dialogflowaudio.repository.DialogflowRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DialogflowRepository(this).sentTextMessage(
            "Olá", "kenniston@gmail.com",
            UUID.randomUUID().toString(),
            object : Callback<DialogflowResult>
            {
                override fun onResponse(call: Call<DialogflowResult>, response: Response<DialogflowResult>) {
                    Log.d("DIALOGFLOW", response.body().toString())
                }

                override fun onFailure(call: Call<DialogflowResult>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
    }
}
