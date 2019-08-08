package br.com.dynamiclight.dialogflowaudio.repository

import android.content.Context
import br.com.bb.valentino.repository.DialogflowRetrofit
import br.com.dynamiclight.dialogflowaudio.model.DialogflowRequest
import br.com.dynamiclight.dialogflowaudio.model.DialogflowResult
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*
import java.io.File


interface DialogflowService {

    @POST("message/text/send")
    @Headers("Content-Type: application/json")
    fun sendTextMessage(@Body request: DialogflowRequest): Call<DialogflowResult>

    @Multipart
    @POST("message/audio/send")
    fun sendAudioMessage(@Part("json") request: RequestBody, @Part file: MultipartBody.Part): Call<DialogflowResult>
}

class DialogflowRepository(context: Context) : DialogflowRetrofit(context) {
    private val service = retrofit.create(DialogflowService::class.java)

    fun sentTextMessage(text: String, email: String, sessionId: String, callback: Callback<DialogflowResult>) {
        val request = DialogflowRequest(text, email, sessionId)
        service.sendTextMessage(request).enqueue(callback)
    }

    fun sentAudioMessage(filePath: String, email: String, sessionId: String, callback: Callback<DialogflowResult>) {
        val request = DialogflowRequest("", email, sessionId)
        val data = RequestBody.create(MediaType.parse("application/json"), toJson(request))

        val file = File(filePath)
        val fileData = RequestBody.create(MediaType.parse("audio/3gpp"), file)

        val part = MultipartBody.Part.createFormData("audioFile", file.name, fileData)

        service.sendAudioMessage(data, part).enqueue(callback)
    }
}