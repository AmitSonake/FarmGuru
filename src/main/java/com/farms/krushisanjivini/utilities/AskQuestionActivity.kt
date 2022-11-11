package com.farms.krushisanjivini.utilities

import android.R
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Chronometer
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.farms.krushisanjivini.databinding.ActivityAskQuestionBinding
import java.io.File
import java.io.IOException


class AskQuestionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAskQuestionBinding
    private var mUri: Uri? = null
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null

    //Our constants
    private val capturePhoto = 1
    private val choosePhoto = 2


    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val recordAudioRequestCode = 101
    private var isPlaying = false

    private val RECORD_AUDIO_REQUEST_CODE = 123

    private val chronometer: Chronometer? = null

    private val seekBar: SeekBar? = null


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permissions,0)
        } else {
           // startRecording()
        }
*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissionToRecordAudio()
        }

        binding.imageViewRecord.setOnClickListener {
            prepareforRecording();
            startRecording();
        }
        binding.imageViewStop.setOnClickListener {
            prepareforStop();
            stopRecording();
        }
        binding.imageViewPlay.setOnClickListener {
            if( !isPlaying && fileName != null ){
                isPlaying = true;
                startPlaying();
            }else{
                isPlaying = false;
                stopPlaying();
            }
        }
    }
    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer!!.setDataSource(fileName)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }
        //making the imageview pause button
        binding.imageViewPlay.setImageResource(R.drawable.ic_media_pause)
        seekBar!!.progress = lastProgress
        mPlayer!!.seekTo(lastProgress)
        seekBar.max = mPlayer!!.duration
        seekUpdation()
        chronometer!!.start()
        /** once the audio is complete, timer is stopped here */
        mPlayer!!.setOnCompletionListener {
            binding.imageViewPlay.setImageResource(R.drawable.ic_media_play)
            isPlaying = false
            chronometer.stop()
        }
        /** moving the track as per the seekBar's position */
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mPlayer != null && fromUser) {
                    //here the track's progress is being changed as per the progress bar
                    mPlayer!!.seekTo(progress)
                    //timer is being updated as per the progress of the seekbar
                    chronometer.base = SystemClock.elapsedRealtime() - mPlayer!!.currentPosition
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    var runnable = Runnable { seekUpdation() }

    private fun seekUpdation() {
        if (mPlayer != null) {
            val mCurrentPosition = mPlayer!!.currentPosition
            seekBar!!.progress = mCurrentPosition
            lastProgress = mCurrentPosition
        }
        mHandler.postDelayed(runnable, 100)
    }

    private fun prepareforStop() {
        TransitionManager.beginDelayedTransition( binding.linearLayoutRecorder)
        binding.imageViewRecord.setVisibility(View.VISIBLE)
        binding.imageViewStop.setVisibility(View.GONE)
        binding.linearLayoutPlay.setVisibility(View.VISIBLE)
    }

    private fun stopRecording() {
        try {
            mRecorder!!.stop()
            mRecorder!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mRecorder = null
        //starting the chronometer
        chronometer!!.stop()
        chronometer.base = SystemClock.elapsedRealtime()
        //showing the play button
        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                RECORD_AUDIO_REQUEST_CODE
            )
        }
    }
    private fun prepareforRecording() {
        TransitionManager.beginDelayedTransition(binding.linearLayoutRecorder)
        binding.imageViewRecord.setVisibility(View.GONE)
        binding.imageViewStop.setVisibility(View.VISIBLE)
        binding.linearLayoutPlay.setVisibility(View.GONE)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = MediaRecorder(this)
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder  */
        val root: File = Environment.getExternalStorageDirectory()
        val file = File(root.getAbsolutePath().toString() + "/VoiceRecorderSimplifiedCoding/Audios")
        if (!file.exists()) {
            file.mkdirs()
        }
        fileName = root.getAbsolutePath()
            .toString() + "/VoiceRecorderSimplifiedCoding/Audios/" + (System.currentTimeMillis()
            .toString() + ".mp3")
        Log.d("filename", fileName!!)
        mRecorder!!.setOutputFile(fileName)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        lastProgress = 0
        seekBar!!.progress = 0
        stopPlaying()
        //starting the chronometer
        chronometer!!.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }


    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPlayer = null
        //showing the play button
        binding.imageViewPlay.setImageResource(R.drawable.ic_media_play)
        chronometer!!.stop()
    }
    // Callback with the request from calling requestPermissions(...)
    @SuppressLint("MissingSuperCall")
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                    this,
                    "You must give permissions to use this app. App is exiting.",
                    Toast.LENGTH_SHORT
                ).show()
                //finishAffinity()
            }
        }
    }

}