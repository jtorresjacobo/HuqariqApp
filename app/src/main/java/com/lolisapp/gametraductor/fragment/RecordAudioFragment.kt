package com.lolisapp.gametraductor.fragment

import android.Manifest
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.lolisapp.gametraductor.R
import com.lolisapp.gametraductor.asynctask.SyncronizeData
import com.lolisapp.gametraductor.bean.User
import com.lolisapp.gametraductor.database.DataBaseService
import com.lolisapp.gametraductor.player.MediaPlayerHolder
import com.lolisapp.gametraductor.player.MediaRecordHolder
import com.lolisapp.gametraductor.util.Util
import com.lolisapp.gametraductor.util.session.SessionManager
import kotlinx.android.synthetic.main.fragment_record_audio.*
import kotlinx.android.synthetic.main.fragment_record_audio.view.*
import java.io.File


class RecordAudioFragment : Fragment(), MediaPlayerHolder.EventMediaPlayer ,MediaRecordHolder.EventMediaRecordHolder{
    override fun reinitAudio() {
        showHidenButtonControlMedia(view!!,false)
    }


    val REQUEST_PERMISSION_CODE = 1

    private var mPlayerAdapter: MediaPlayerHolder? = null
    private var mediaPlayerHolderForRecord: MediaPlayerHolder? = null

    private var mediaRecordHolder: MediaRecordHolder? = null
    private var mUserIsSeeking = false
    private var mUserRecordIsSeeking = false
    private lateinit var seekbarExample:SeekBar
    private lateinit var seekbarRecord:SeekBar
    private var index=0
    private  var totalAudio:Int=0;
    lateinit var fileName:String;
    var frameAnimation: AnimationDrawable? = null
     lateinit var user: User;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_record_audio, container, false)
        user=SessionManager.getInstance(activity).userLogged
        user=DataBaseService.getInstance(activity).getUser(user.email)
        fileName=Util.getFileName(user.codeDepartamento)
        totalAudio=Util.getTotalAudio(fileName)

        initializeSeekbar(view)
        initializeSeekbarRecord(view)
        initializePlaybackController()
        verifiyPermisied()
        index=user.avance
        view.tvAvance.setText("${index+1}/$totalAudio")
        mPlayerAdapter?.loadMedia("$fileName/${Util.getAudioName(index,"$fileName.txt",context)}")
        initButton(view)
        view.audioRecord.visibility=View.GONE
        return view
    }


    private fun initButton(view:View){

        view.btnNextAudio.setOnClickListener({nextAudio()})

        view.ibPlay.setOnClickListener({ showHidenButtonControlMedia(view, true);mPlayerAdapter?.play() })
        view.ibPause.setOnClickListener({ showHidenButtonControlMedia(view, false); mPlayerAdapter?.pause() })

        view.ibPlayRecord.setOnClickListener({ showHidenButtonControlMediaRecord(view, true);mediaPlayerHolderForRecord?.play() })
        view.ibPauseRecord.setOnClickListener({ showHidenButtonControlMediaRecord(view, false); mediaPlayerHolderForRecord?.pause() })
        view.ivCloseRecord.setOnClickListener({dialogClose()})

        view.btnRecord.setOnClickListener({recordAudio()})
        view.btnPauseRecord.setOnClickListener({mediaRecordHolder?.stopRecord();btnRecord.isEnabled=true})
    }

    private fun showHidenButtonControlMedia(view: View, isPlaying: Boolean) {
        view.ibPause.visibility = if (isPlaying) View.VISIBLE else View.GONE
        view.ibPlay.visibility = if (isPlaying) View.GONE else View.VISIBLE
    }

    private fun showHidenButtonControlMediaRecord(view: View, isPlaying: Boolean) {
        view.ibPauseRecord.visibility = if (isPlaying) View.VISIBLE else View.GONE
        view.ibPlayRecord.visibility = if (isPlaying) View.GONE else View.VISIBLE
    }



    override fun changePositionSeek(pos: Int) {

        if (mUserIsSeeking) return
        seekbarExample.setProgress(pos)

    }


    private fun initializePlaybackController() {
        val mMediaPlayerHolder = MediaPlayerHolder(this!!.activity, this)
        mPlayerAdapter = mMediaPlayerHolder
        mediaRecordHolder=MediaRecordHolder(this)
        initMediaPlayerForRecord()

    }




    private fun initMediaPlayerForRecord(){
        mediaPlayerHolderForRecord= MediaPlayerHolder(this!!.activity!!, object : MediaPlayerHolder.EventMediaPlayer {
            override fun reinitAudio() {
                showHidenButtonControlMediaRecord(view!!,false)
            }

            override fun changePositionSeek(pos: Int) { if (mUserRecordIsSeeking) return;seekbarRecord.setProgress(pos)}

            override fun onPositionChanged(positon: Int) {if (!mUserRecordIsSeeking) seekbarRecord.setProgress(positon) ;showHidenButtonControlMedia(view!!,false) }

            override fun onDurationChanged(duration: Int) {seekbarAudioRecord.setMax(duration) }


        }


        )

    }






    private fun initializeSeekbar(view:View) {
        seekbarExample=view.seekbarAudioExample
        view.seekbarAudioExample.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    internal var userSelectedPosition = 0

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        mUserIsSeeking = true
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser)
                            userSelectedPosition = progress
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        mUserIsSeeking = false
                        mPlayerAdapter?.seekTo(userSelectedPosition)
                    }
                })
    }


    private fun initializeSeekbarRecord(view:View) {
        seekbarRecord=view.seekbarAudioRecord
        view.seekbarAudioRecord.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    internal var userSelectedPosition = 0

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        mUserRecordIsSeeking = true
                    }

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser)
                            userSelectedPosition = progress
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        mUserRecordIsSeeking = false
                        mediaPlayerHolderForRecord?.seekTo(userSelectedPosition)
                    }
                })
    }





    override fun onPositionChanged(position: Int) {
        if (!mUserIsSeeking) seekbarExample.setProgress(position)
        if (view!=null) showHidenButtonControlMediaRecord(view!!,false)
    }

    override fun onDurationChanged(duration: Int) {

        seekbarExample.setMax(duration)
    }

    fun recordAudio(){
        btnRecord.isEnabled=false
        btnNextAudio.isEnabled=false;
        initAnimation()
        if (checkPermission())mediaRecordHolder?.initRecord(SessionManager.getInstance(activity)
                .userLogged.dni,index,context!!) else verifiyPermisied()
    }

    private fun dialogClose(){

        val builder1 =  AlertDialog.Builder(this!!.context!!)
        builder1.setMessage("¿Desea eliminar la grabación?")
        builder1.setCancelable(true)
        builder1.setPositiveButton("Yes", {dialog,id->deleteRecord()})
        builder1.setNegativeButton("No",{dialog,id->dialog.cancel()})
        val alert11 = builder1.create()
        alert11.show()

    }

    private fun deleteRecord(){
        btnNextAudio.isEnabled=false
        audioRecord.visibility=View.GONE
        val dataBaseService=DataBaseService.getInstance(activity)
        dataBaseService.deleteAudio(user.email,index)


    }



    fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity!!.getApplicationContext(),
                WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(activity!!.getApplicationContext(),
                RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    private fun verifiyPermisied() {
        if (enablePermise(Manifest.permission.RECORD_AUDIO)|| enablePermise(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                enablePermise(Manifest.permission.CAPTURE_AUDIO_OUTPUT)|| enablePermise(Manifest.permission.READ_SMS) ) {
            if (Build.VERSION.SDK_INT >= 23) {

                ActivityCompat.requestPermissions(this!!.activity!!, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAPTURE_AUDIO_OUTPUT, Manifest.permission.READ_SMS), REQUEST_PERMISSION_CODE)
            }

        }

    }


    private fun enablePermise(permission:String):Boolean{
        return ContextCompat.checkSelfPermission(this!!.context!!, permission) != PackageManager.PERMISSION_GRANTED
    }

    override fun finishRecord(pathAudio: String) {
        stopAnimation()
        audioRecord.visibility=View.VISIBLE
        val fileName=pathAudio.replace(Environment.getExternalStorageDirectory().getAbsolutePath() + "/game_catolic_quechua/","")
        val audioFile = File(pathAudio)
        Util.copyFileUsingStream(audioFile,context)
        mediaPlayerHolderForRecord?.loadMediaFromPath(pathAudio)
        val dataBaseService=DataBaseService.getInstance(activity)
        dataBaseService.insertAudio(user.email,index,fileName)


    }

    private fun nextAudio(){
        btnNextAudio.isEnabled=false
        btnRecord.isEnabled=true
        index++
        mPlayerAdapter?.loadMedia("$fileName/${Util.getAudioName(index,"$fileName.txt",context)}")
        tvAvance.setText("${index+1}/$totalAudio")
        val database=DataBaseService.getInstance(activity)
        database.editAvance(index,user.email)

        audioRecord.visibility=View.GONE

        val sincro=SyncronizeData(activity!!.baseContext)
    }

    private fun initAnimation() {
        audioRecord.visibility=View.GONE
        btnPauseRecord.isEnabled=true
       // llOptionMiddel.setVisibility(View.GONE)
        //llTextTraduccion.setVisibility(View.GONE)
        cardAnimation.visibility=View.VISIBLE
        ivAnimation.setVisibility(View.VISIBLE)
        ivAnimation.setBackgroundResource(R.drawable.animsound)
        frameAnimation = ivAnimation.getBackground() as AnimationDrawable
        frameAnimation?.start()
    }

    private fun stopAnimation() {
       // btnStart.setVisibility(View.VISIBLE)
        //btnStop.setVisibility(View.GONE)
        //llOptionMiddel.setVisibility(View.VISIBLE)
        cardAnimation.visibility=View.GONE

        btnNextAudio.isEnabled=true
        btnPauseRecord.isEnabled=false
        frameAnimation?.stop()
        ivAnimation.setVisibility(View.GONE)
    }

}
