package com.lolisapp.gametraductor.player

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import com.lolisapp.gametraductor.database.DataBaseService
import com.lolisapp.gametraductor.util.Constants
import com.lolisapp.gametraductor.util.Util
import com.lolisapp.gametraductor.util.session.SessionManager
import java.io.File
import java.io.File.separator
import java.text.SimpleDateFormat
import java.util.*




class MediaRecordHolder (event: EventMediaRecordHolder){



    private var mediaRecorder: MediaRecorder? = null
    private var lastAudioRecord: String? = null
    private var eventMediaRecordHolder: EventMediaRecordHolder=event




    private fun getMediaRecorderReady(dni:String,index: Int,context: Context): MediaRecorder {
        createDirectory()
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder.setAudioChannels(1)
        mediaRecorder.setAudioSamplingRate(16000)
        lastAudioRecord = getPathToAudio(dni,index,context)
        mediaRecorder.setOutputFile(lastAudioRecord)
        return mediaRecorder
    }


    private fun createDirectory() {
        val folder = File(getExternalStorageDirectory().absolutePath + separator + "game_catolic_quechua")
        if (!folder.exists()) {
            folder.mkdirs()
        }

    }

    private fun getPathToAudio(dni:String,index:Int,context: Context): String {
        val path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/game_catolic_quechua/" + getCode(dni,index,context)
        return path
    }

    private fun getCode(dni:String,index:Int,context:Context): String {
        val email=SessionManager.getInstance(context).userLogged.email
        val user=DataBaseService.getInstance(context).getUser(email)
        val dateText= Util.getStringDate();
        var code = "${dni}_${dateText}_${index+1}-331.wav"
        if (user.isMember==Constants.IS_MEMBER)code="${user.region}_${user.institution}_$code"
        return code
    }


    fun stopRecord(){
        mediaRecorder?.stop()
        eventMediaRecordHolder.finishRecord(lastAudioRecord!!)
    }


    fun initRecord(dni:String,index:Int,context: Context) {
            mediaRecorder = getMediaRecorderReady(dni,index,context)
            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            } catch (e: Exception) {
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
                e.printStackTrace()
            }
    }








    interface EventMediaRecordHolder {
        fun finishRecord(nameAudio:String)

    }



}