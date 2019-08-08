package br.com.dynamiclight.dialogflowaudio.activity

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.dynamiclight.dialogflowaudio.R
import br.com.dynamiclight.dialogflowaudio.model.DialogflowResult
import br.com.dynamiclight.dialogflowaudio.repository.DialogflowRepository
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var recording = false
    private val WRITE_REQUEST_CODE = 9998
    private val RECORD_REQUEST_CODE = 9997
    private val uuid = UUID.randomUUID().toString()

    lateinit var recorder: MediaRecorder
    lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        imgRecord.setOnClickListener { startRecorder() }
    }

    private fun startRecorder() {
        recording = !recording

        if (recording) {
            imgRecord.setImageResource(R.drawable.btn2)
        } else {
            imgRecord.setImageResource(R.drawable.btn1)
        }

        if (recording) {
            recorder = MediaRecorder()
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) //MPEG_4
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            val path = File(Environment.getExternalStorageDirectory().path)
            try {
                file = File.createTempFile("temporary", ".3gp", path)
            } catch (e: IOException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                return
            }

            recorder.setOutputFile(file.absolutePath)
            try {
                recorder.prepare()
            } catch (e: IOException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                return
            }
            recorder.start()
        } else {

            recorder.stop()

            DialogflowRepository(this).sentAudioMessage(file.absolutePath, "kenniston@gmail.com", uuid, object : Callback<DialogflowResult> {
                override fun onResponse(call: Call<DialogflowResult>, response: Response<DialogflowResult>) {
                    Toast.makeText(this@MainActivity, "SUCESSO", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<DialogflowResult>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun checkPermission() {
        var permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_REQUEST_CODE)
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this@MainActivity,
                        "A permissão de acesso ao dispositivo é necessária.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this@MainActivity,
                        "A permissão para gravação de áudio é necessária.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
