package br.com.dynamiclight.dialogflowaudio.repository

import android.content.Context
import br.com.bb.valentino.repository.DialogflowRetrofit
import br.com.dynamiclight.dialogflowaudio.model.DialogflowRequest
import br.com.dynamiclight.dialogflowaudio.model.DialogflowResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DialogflowService {

    @POST("/message/text/send")
    @Headers("Content-Type: application/json")
    fun sendTextMessage(@Body request: DialogflowRequest): Call<DialogflowResult>

}

class DialogflowRepository(context: Context) : DialogflowRetrofit(context) {
    private val service = retrofit.create(DialogflowService::class.java)

    fun sentTextMessage(text: String, email: String, sessionId: String, callback: Callback<DialogflowResult>) {
        val request = DialogflowRequest(text, email, sessionId)
        service.sendTextMessage(request).enqueue(callback)
    }
}