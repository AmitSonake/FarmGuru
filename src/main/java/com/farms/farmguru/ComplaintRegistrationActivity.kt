package com.farms.farmguru

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.DATA
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.databinding.ActivityComplaintRegistrationBinding
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.questions.MyQuestionListingActivity
import com.farms.farmguru.utilities.CheckInternetConnection
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class ComplaintRegistrationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityComplaintRegistrationBinding
    private var mUri: Uri? = null
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var imageDownloadUrl: String? =null

    //Our constants
    private val capturePhoto = 1
    private val choosePhoto = 2
    private var imageUrl :String? =null
    private var audioUrl :String? =null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileNames: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val recordAudioRequestCode = 101
    private var isPlaying = false
    private var mApiService: ApiServiceInterface?= null
    private var currentDateTime:String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityComplaintRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(
            ApiServiceInterface::class.java)
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    output = Environment.getStorageDirectory().absolutePath + "/recording.mp3"
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    mediaRecorder = MediaRecorder(this)
                }

                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mediaRecorder?.setOutputFile(output)*/
        binding.btnCapture.setOnClickListener {
            capturePhoto()
        }
                binding.btnChoose.setOnClickListener{
                    //check permission at runtime
                    val checkSelfPermission = ContextCompat.checkSelfPermission(this,
                        WRITE_EXTERNAL_STORAGE
                    )
                    if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                        //Requests permissions to be granted to this application at runtime
                        ActivityCompat.requestPermissions(this,
                            arrayOf(WRITE_EXTERNAL_STORAGE), 1)
                    }
                    else{
                        openGallery()
                    }
                }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //getPermissionToRecordAudio()
        }
                binding.imgBtRecord.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getPermissionToRecordAudio()
                    }
                    prepareRecording()
                    startRecording()
                    binding.imgViewPlay.visibility = View.GONE
                    binding.labelPlayRecording.visibility= View.GONE

                    binding.imgBtStop.visibility = View.VISIBLE
                    binding.labelStopRecording.visibility= View.VISIBLE
                    binding.imgBtRecord.visibility=View.GONE
                    binding.labelStartRecording.visibility=View.GONE
                    binding.headerTxt.visibility = View.VISIBLE
                    countdown()
                }
                 binding.imgBtStop.setOnClickListener {
                     prepareStop()
                     stopRecording()
                     binding.imgBtRecord.visibility=View.VISIBLE
                     binding.labelStartRecording.visibility=View.VISIBLE
                     binding.headerTxt.visibility = View.GONE
                     binding.imgBtStop.visibility = View.GONE
                     binding.labelStopRecording.visibility= View.GONE

                     binding.imgBtRecord.visibility=View.VISIBLE
                     binding.labelStartRecording.visibility=View.VISIBLE
                }
                binding.imgViewPlay.setOnClickListener {
                    if (!isPlaying) {
                        isPlaying = true
                        startPlaying()
                    } else {
                        isPlaying = false
                        stopPlaying()
                    }
                }

                binding.btnRemove.setOnClickListener {
                    binding.mImageView.setImageBitmap(null)
                }

        binding.submitButton.setOnClickListener {
            val localtime = LocalDateTime.now().toString()
            currentDateTime =localtime
            val isAllFieldFilled :Boolean =checkFieldValidation()
            println("Current Date = $currentDateTime")
            var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
            if(networkStatus){
                if(isAllFieldFilled)
                registerCompliant()
                else
                    showAlertDialog("Please choose all fields")
            }else{
                CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
                finish()
            }
        }
    }
    private fun showAlertDialog(infoMessage:String?){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->dialog.cancel()}
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }

    private fun checkFieldValidation(): Boolean {
        return !(binding.commentsText.text.toString().isNullOrEmpty()||
                (imageUrl.isNullOrEmpty()||
                audioUrl.isNullOrEmpty())
                )
    }

    private fun registerCompliant() {
        binding.progressbar.visibility = View.VISIBLE
       var questionText:String? = binding.commentsText.text.toString()
        mApiService!!.registerQuestion(1,"1",currentDateTime!!,questionText!!,imageUrl!!,audioUrl!!,"","New")
            .enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200 ||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    showSuccessDialog("Your request has been registered successfully!!")
                }else{
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@ComplaintRegistrationActivity)
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun showSuccessDialog(infoMessage:String?){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->dialog.cancel()
                finish()
                val intent = Intent(this, MyQuestionListingActivity::class.java)
               // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }
    private fun countdown(){
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.headerTxt.text = (millisUntilFinished / 1000).toString()
                println("Done recording time")
            }
            override fun onFinish() {
                // do something after countdown is done ie. enable button, change color
                binding.headerTxt.text="Done"
                prepareStop()
                stopRecording()
                println("Done recording time")
                binding.headerTxt.visibility = View.GONE
            }
        }.start()
    }

    private fun show(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun capturePhoto(){
        val capturedImage = File(externalCacheDir, "My_Captured_Photo.jpg")
        if(capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if(Build.VERSION.SDK_INT >= 24){
            FileProvider.getUriForFile(this, "com.farms.farmguru.fileprovider",
                capturedImage)
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, capturePhoto)
    }

    private fun openGallery(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, choosePhoto)
    }

    private fun renderImage(imagePath: String?){
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.mImageView.setImageBitmap(bitmap)
        }
        else {
            show("ImagePath is null")
        }
    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = uri?.let { contentResolver.query(it, null, selection, null, null ) }
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(DATA))
            }
            cursor.close()
        }
        return path!!
    }

    @TargetApi(19)
    fun handleImageOnKitkat(data: Intent?) {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(this, uri)){
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority){
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selsetion)
            }
            else if ("com.android.providers.downloads.documents" == uri?.authority){
                val contentUri = ContentUris.withAppendedId(Uri.parse(
                    "content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        }
        else if ("content".equals(uri?.scheme, ignoreCase = true)){
            imagePath = getImagePath(uri, null)
        }
        else if ("file".equals(uri?.scheme, ignoreCase = true)){
            imagePath = uri?.path
        }
        uploadImgageToFireStore(uri)

        renderImage(imagePath)
    }

    private fun uploadImgageToFireStore(uri: Uri?) {
        var storage= FirebaseStorage.getInstance()
        var storageRef = storage.reference
        val fileName = UUID.randomUUID().toString() +".jpg"
        val mountainImagesRef = storageRef.child("images/$fileName")
        mountainImagesRef.putFile(uri!!)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        println("Image Url = $imageUrl")
                    }
                })

            ?.addOnFailureListener(OnFailureListener { e ->
                print(e.message)
            })
    }

    private fun uploadAudioToFireStore(filePath: String?) {
        var storage= FirebaseStorage.getInstance()
        var storageRef = storage.reference
        val fileName = UUID.randomUUID().toString() +".mp3"
        val metadata = StorageMetadata.Builder().setContentType("audio/mpeg").build()
        val mountainImagesRef = storageRef.child("audios/$fileName")
        val uri = Uri.fromFile(File(filePath))
        println("Audio uri =$uri")
        mountainImagesRef.putFile(uri)
            .addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        audioUrl = it.toString()
                        println("Audio Url = $audioUrl")
                    }
                })

            ?.addOnFailureListener(OnFailureListener { e ->
                print(e.message)
            })
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>
                                            , grantedResults: IntArray) {
        if (requestCode == recordAudioRequestCode) {
            if (grantedResults.size == 3 && grantedResults[0] ==
                PackageManager.PERMISSION_GRANTED && grantedResults[1] ==
                PackageManager.PERMISSION_GRANTED && grantedResults[2] ==
                PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show()
               // finishAffinity()
            }
        }
        when(requestCode){
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }

        }
        if (!permissionToRecordAccepted) finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            capturePhoto ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        mUri?.let { contentResolver.openInputStream(it) })
                    binding.mImageView.setImageBitmap(bitmap)
                    uploadImgageToFireStore(mUri)
                }
            choosePhoto ->
                if (resultCode == Activity.RESULT_OK) {
                    handleImageOnKitkat(data)
                }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid checking the build version since Context.checkSelfPermission(...) is only available in Marshmallow
        // 2) Always check for permission (even if permission has already been granted) since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE), recordAudioRequestCode)
        }
    }

    private fun prepareStop() {
        //TransitionManager.beginDelayedTransition( binding.llRecorder)
        binding.imgBtRecord.visibility = View.VISIBLE
        binding.imgBtStop.visibility = View.GONE
        binding.labelStopRecording.visibility=View.GONE
        binding.imgViewPlay.visibility = View.VISIBLE
        binding.labelPlayRecording.visibility=View.VISIBLE
    }

    private fun prepareRecording() {
        //TransitionManager.beginDelayedTransition( binding.llRecorder)
        binding.imgBtRecord.visibility = View.GONE
        binding.imgBtStop.visibility = View.VISIBLE
        binding.labelStopRecording.visibility=View.VISIBLE
        binding.imgViewPlay.visibility = View.GONE
        binding.labelPlayRecording.visibility=View.GONE
    }

    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPlayer = null
        //showing the play button
        binding.imgViewPlay.setImageResource(R.drawable.ic_play_circle)
      //  binding.labelPlayRecording.visibility=View.VISIBLE
        binding.chronometer.stop()
    }

    private fun getAbsolutePath(): File {
        return File(externalCacheDir, "amit")
    }

    private fun startRecording() {
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setMaxDuration(60000)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        val root =getAbsolutePath()
        val file = File(root.absolutePath + "/Audios")
        if (!file.exists()) {
            file.mkdirs()
        }

        fileNames = root.absolutePath + "/Audios/" + (System.currentTimeMillis().toString() + ".3gp")
        Log.d("filename", fileNames!!)
        mRecorder!!.setOutputFile(fileNames)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        lastProgress = 0
        binding.seekBar.progress = 0
        stopPlaying()
        // making the imageView a stop button starting the chronometer
        binding.chronometer.base =  SystemClock.elapsedRealtime()
        binding.chronometer.start()
    }

    private fun stopRecording() {
        try {
            mRecorder!!.stop()
            mRecorder!!.release()
            binding.imgViewPlay.visibility=View.VISIBLE
            binding.labelPlayRecording.visibility=View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mRecorder = null
        //starting the chronometer
        binding.chronometer.stop()
        binding.chronometer.base = SystemClock.elapsedRealtime()
        //showing the play button

        Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show()
        println("Audio File URi = ${fileNames!!.toUri()}")
        uploadAudioToFireStore(fileNames)
    }

    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer!!.setDataSource(fileNames)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }

        //making the imageView pause button
        binding.imgViewPlay.setImageResource(R.drawable.ic_pause_circle)

        binding.seekBar.progress = lastProgress
        mPlayer!!.seekTo(lastProgress)
        binding.seekBar.max = mPlayer!!.duration
        seekBarUpdate()
        binding.chronometer.start()

        mPlayer!!.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            binding.imgViewPlay.setImageResource(R.drawable.ic_play_circle)
            isPlaying = false
            binding.chronometer.stop()
            binding.chronometer.base = SystemClock.elapsedRealtime()
            mPlayer!!.seekTo(0)
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mPlayer != null && fromUser) {
                    mPlayer!!.seekTo(progress)
                    binding.chronometer.base = SystemClock.elapsedRealtime() - mPlayer!!.currentPosition
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
    private var runnable: Runnable = Runnable { seekBarUpdate() }
    private fun seekBarUpdate() {
        if (mPlayer != null) {
            val mCurrentPosition = mPlayer!!.currentPosition
            binding.seekBar.progress = mCurrentPosition
            lastProgress = mCurrentPosition
        }
        mHandler.postDelayed(runnable, 100)
    }

}