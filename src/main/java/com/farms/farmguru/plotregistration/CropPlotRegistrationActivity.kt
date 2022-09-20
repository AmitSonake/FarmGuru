package com.farms.farmguru.plotregistration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.ActivityRegisterCropsBinding
import com.farms.farmguru.login.LoginActivity
import com.farms.farmguru.model.*
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.splash.TestActivity
import com.farms.farmguru.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CropPlotRegistrationActivity : AppCompatActivity() {
    private var selectedCropVarietyId: Int = 0
    private var selectedIntentionId: Int = 0
    private var selectedSoilTypeId: Int = 0
    private var isPaid:Boolean =false
    private var isTrial:Boolean = true
    private val intentionList =ArrayList<CropPurpose>()
    private var intentionData = ArrayList<String>()
    //private lateinit var binding:ActivityCropPlotRegistrationBinding
    val cropVarietyList =ArrayList<CropVariety>()
    val soilTypeList =ArrayList<SoilType>()
    private var soilTypeData = ArrayList<String>()
    var irrigationSourceList =ArrayList<IrrigationSource>()
    private var irrigationSourceData = ArrayList<String>()
    private var cropVarietyData = ArrayList<String>()
    private var irrigataionSourcesIdList = ArrayList<String>()
    private var arraydata= arrayOf<String>()
    private var arrayOfIrriagtionId=arrayOf<String>()
    private var yearData= arrayOf<String>()
    private var mApiService: ApiServiceInterface?= null
    private var maxYearDiffrence=15
    private var appbarTitle :String? = null
    private var appPlotId:String? = null
    private var sourceId:Int = 0
    private lateinit var binding: ActivityRegisterCropsBinding
    var cal = Calendar.getInstance()
    var userLanguageID:Int = 1

   /* override fun onResume() {
        super.onResume()
       *//* SharedPreferencesHelper.invoke(this).getSelectedLanguage()?.let {
            com.farms.farmguru.utilities.LocaleHelper.setLocale(this!!,
                it
            )
        }*//*
        loadlabels()
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCropsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle :Bundle ?=intent.extras
        println( " user language: ${SharedPreferencesHelper.invoke(this).getSelectedLanguage()}")
        /*SharedPreferencesHelper.invoke(this).getSelectedLanguage()?.let {
            com.farms.farmguru.utilities.LocaleHelper.setLocale(this!!,
                it
            )
        }*/
        if (bundle!=null){
            appPlotId = bundle.getString("cropId")
            println("Crop Id = $appPlotId")
            appbarTitle =bundle.getString("cropName")
        }
        binding.toolbar.setTitle("$appbarTitle")
        binding.cropName.setText("$appbarTitle")
        binding.toolbar.textAlignment= View.TEXT_ALIGNMENT_TEXT_END
        loadlabels()
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
            fetchCropVariety()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }


        binding.insertSource.setOnClickListener {
            withMultiChoiceList(it)
        }
        binding.waterSource.setOnClickListener {
            withMultiChoiceList(it)
        }
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        binding.selectDate.setOnClickListener {
            DatePickerDialog(this@CropPlotRegistrationActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.intentionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedIntentionId = intentionList[position].CropPurposeId.toInt()
                println("selectedSoil type =$selectedIntentionId")
            }

        }

        binding.soilTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSoilTypeId = soilTypeList[position].SoilTypeId.toInt()
                println("selectedSoil type =$selectedSoilTypeId")
            }

        }

        binding.cropVarietySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { 
                selectedCropVarietyId = cropVarietyList[position].CropVarietyId.toInt()
                println("selectedSoil Variety id =$selectedCropVarietyId")
            }

        }

        binding.registerPlotButton.setOnClickListener {
           val isAllFieldFilled :Boolean =checkFieldValidation()
            println("All field flag = $isAllFieldFilled")
            if(isAllFieldFilled){
                requestRegisterPlot()
            }else{
                showAlertDialog("Please fill all fields and complete your plot registration ")
            }

        }
    }

    private fun loadlabels() {

        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            binding.labelFname.text="ರೈತರ ಹೆಸರು:"
            binding.labelFaddress.text="ರೈತರ ವಿಳಾಸ:"
            binding.labelFtaluqa.text="ರೈತರು ತಾಲೂಕು:"
            binding.labelFdistrict.text="ರೈತರ ಜಿಲ್ಲೆ:"
            binding.labelFstate.text="ರೈತರ ರಾಜ್ಯ:"
            binding.labelCname.text="ಬೆಳೆ ಹೆಸರು:"
            binding.labelCseason.text="ಬೆಳೆ ಋತು:"
            binding.labelCvariety.text="ಬೆಳೆ ವೈವಿಧ್ಯ:"

            binding.labelDate.text="ಸಮರುವಿಕೆಯ ದಿನಾಂಕ:"
            binding.labelWsource.text="ನೀರಿನ ಮೂಲ:"
            binding.labelStype.text="ಮಣ್ಣಿನ ವಿಧ:"
            binding.labelIntention.text="ಉದ್ದೇಶ:"
            binding.labelCdistance.text="ಬೆಳೆ ದೂರ:"
            binding.labelAge.text="ಸಸ್ಯದ ವಯಸ್ಸು:"
            binding.labelArea.text="ಒಟ್ಟು ಪ್ರದೇಶ (ಎಕರೆಗಳು):"

            binding.cropPruningDate.hint="ದಿನಾಂಕವನ್ನು ಆರಿಸಿ:"
            binding.cropDistanceArea.hint="ಬೆಳೆ ದೂರ:"
            binding.cropDistanceLength.hint="ಬೆಳೆ ದೂರ:"
            binding.totalArea.hint="ಒಟ್ಟು ಪ್ರದೇಶ (ಎಕರೆಗಳು):"
            binding.registerPlotButton.text="ಸಲ್ಲಿಸು"
            binding.waterSource.hint="ನೀರಿನ ಮೂಲ:"
       }
        else{
            binding.labelFname.text="Farmers Name:"
            binding.labelFaddress.text="Farmers Address:"
            binding.labelFtaluqa.text="Farmers Taluqa:"
            binding.labelFdistrict.text="Farmers District:"
            binding.labelFstate.text="Farmers State:"
            binding.labelCname.text="Crop Name:"
            binding.labelCseason.text="Crop Season:"
            binding.labelCvariety.text="Crop Variety:"

            binding.labelDate.text="Pruning Date:"
            binding.labelWsource.text="Water Source:"
            binding.labelStype.text="Soil Type:"
            binding.labelIntention.text="Intention:"
            binding.labelCdistance.text="Crop Distance:"
            binding.labelAge.text="Age of the plant:"
            binding.labelArea.text="Total Area(Acres):"

            binding.cropPruningDate.hint="Choose Date:"
            binding.cropDistanceArea.hint="Crop Distance:"
            binding.cropDistanceLength.hint="Crop Distance:"
            binding.totalArea.hint="Total Area(Acres):"
            binding.registerPlotButton.text="Submit"
            binding.waterSource.hint="Water Source:"

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

    private fun navigateLogin(){
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun showSuccessDialog(infoMessage:String?){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(infoMessage)
            .setCancelable(false)
            .setPositiveButton("OK"){dialog,_->dialog.cancel()
            finish()
            val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        val alert =dialogBuilder.create()
        alert.setTitle("Information")
        alert.show()
    }


    private fun checkFieldValidation(): Boolean {
        return !(binding.totalArea.text.toString().isNullOrEmpty()||
                binding.ageOfPlant.text.toString().isNullOrEmpty()||
                binding.ageOfPlant.text.toString().isNullOrEmpty()||
                binding.cropDistanceArea.text.toString().isNullOrEmpty() ||
                binding.cropDistanceLength.text.toString().isNullOrEmpty()||
                binding.farmersName.text.toString().isNullOrEmpty()||
                binding.farmersAddress.text.toString().isNullOrEmpty()||
                binding.farmersTaluqa.text.toString().isNullOrEmpty()||
                binding.farmersDistrict.text.toString().isNullOrEmpty()||
                binding.farmersState.text.toString().isNullOrEmpty()||
                binding.cropPruningDate.text.toString().isNullOrEmpty())
    }

    private fun requestRegisterPlot() {
        binding.progressbar.visibility = View.VISIBLE
        val plotAreaData = binding.totalArea.text.toString()
        var plotArea:Int =Integer.parseInt(plotAreaData)
        val plotAgeData = binding.ageOfPlant.text.toString()
        var plotAge:Int =Integer.parseInt(plotAgeData)
        var plotDistanceArea:String? =binding.cropDistanceArea.text.toString()
        var plotDistanceLength:String? =binding.cropDistanceLength.text.toString()
        var cropTotalDistance:String? ="$plotDistanceArea"+"x"+"$plotDistanceLength"
        var farmerName:String? =binding.farmersName.text.toString()
        var farmersAddress:String? =binding.farmersAddress.text.toString()
        var farmersTaluqa:String? =binding.farmersTaluqa.text.toString()
        var farmersDistrict:String? =binding.farmersDistrict.text.toString()

        var farmersState:String? =binding.farmersState.text.toString()
        var cropSeason:String? =binding.cropSeason.text.toString()
        var cropPruningDate:String? =binding.cropPruningDate.text.toString()
        println("Prunning Date =$cropPruningDate")

        val inputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(cropPruningDate)
        val outputDateStr: String = outputFormat.format(date)
        println("Prunning Date =$outputDateStr")
        var cropId:Int = Integer.parseInt(appPlotId)

        mApiService!!.registerPlot(1,"1",farmerName!!,
           farmersAddress!!,farmersTaluqa!!,
            farmersDistrict!!,farmersState!!,cropId,
            selectedCropVarietyId,cropSeason!!,outputDateStr,
            1,selectedSoilTypeId,selectedIntentionId,cropTotalDistance!!,
            plotAge,plotArea,
            isPaid,isTrial).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200 ||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    showSuccessDialog("User Plot Registered Successfully!!")
                }else{
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@CropPlotRegistrationActivity)
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        var result= sdf.format(cal.time)
        binding.cropPruningDate.setText(result)
    }

    private fun fetchCropVariety(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getCropVariety(appPlotId!!.toInt()).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    stringResponse?.let { parseCropVarietyJson(it.toString()) }!!
                }
                fetchSoilType()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }
    private fun fetchSoilType(){
        //mApiService!!.getSoilType().enqueue(object:
        userLanguageID = Integer.parseInt(SharedPreferencesHelper.invoke(this).getUserLanguage())
        mApiService!!.getSoilTypeByLangId(userLanguageID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    stringResponse?.let { parseSoilTypeJson(it) }
                }
                fetchIrrigationSource()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun fetchIrrigationSource(){
        //binding.progressbar.visibility = View.VISIBLE
        userLanguageID = Integer.parseInt(SharedPreferencesHelper.invoke(this).getUserLanguage())
        mApiService!!.getIrrigationSourceBylangID(userLanguageID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    stringResponse?.let { parseIrrigationSourceJson(it) }
                    //cropVarietyParsedList= stringResponse?.let { parseCropVarietyJson(it.toString()) }!!

                }
                fetchIntention()
                //binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun fetchIntention(){
        userLanguageID = Integer.parseInt(SharedPreferencesHelper.invoke(this).getUserLanguage())
        mApiService!!.getCropPurposeBylangID(userLanguageID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    stringResponse?.let { parseIntentionJson(it) }
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }
    private fun parseCropVarietyJson(jsonResponse: String){
       /* val jsonObject = JSONObject(jsonResponse)
        val cropVarityId:Int =jsonObject.optInt("CropVarietyId")
        val cropVarietyName=jsonObject.optString("CropVarietyName")
        val cropId:Int =jsonObject.optInt("CropId")
        cropVarietyList.add(CropVariety(cropVarityId,cropVarietyName,cropId))
        cropVarietyData.add(cropVarietyName)*/
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val cropVarityId:Int =jsonObj.optInt("CropVarietyId")
            val cropVarietyName=jsonObj.optString("CropVarietyName")
            val cropId:Int =jsonObj.optInt("CropId")
            cropVarietyList.add(CropVariety(cropVarityId,cropVarietyName,cropId))
            cropVarietyData.add(cropVarietyName)

        }
        if(cropVarietyList.size>0){
            println("Crop variety size = ${cropVarietyData.size}")
            val adapterData =ArrayAdapter(applicationContext, R.layout.cropvariety_spinner_item,cropVarietyData)
            binding.cropVarietySpinner.adapter=adapterData
        }
    }

    private fun parseSoilTypeJson(jsonResponse: String){
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
           // val jsonObj = JSONObject(jsonResponse)
            val soilTypeId:Int =jsonObj.optInt("SoilTypeId")
            val soilTypeName=jsonObj.optString("SoilTypeName")

            soilTypeList.add(SoilType(soilTypeId,soilTypeName))
            soilTypeData.add(soilTypeName)
       }
        if(soilTypeList.size>0){
            val adapterData =ArrayAdapter(applicationContext, R.layout.cropvariety_spinner_item,soilTypeData)
            binding.soilTypeSpinner.adapter=adapterData
        }
    }

    private fun parseIntentionJson(jsonResponse: String){
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray.length() - 1) {
            //val jsonObj = JSONObject(jsonResponse)
            val jsonObj = jsonArray.optJSONObject(i)
            val cropPurposeId =jsonObj.optString("CropPurposeId")
            val cropPurposeDesc=jsonObj.optString("CropPurposeName")

            intentionList.add(CropPurpose(cropPurposeId,cropPurposeDesc))
            intentionData.add(cropPurposeDesc)
       }
        if(intentionList.size>0){
            val adapterData =ArrayAdapter(applicationContext, R.layout.cropvariety_spinner_item,intentionData)
            binding.intentionSpinner.adapter=adapterData
        }
    }

    private fun parseIrrigationSourceJson(jsonResponse: String){
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            //val jsonObj = JSONObject(jsonResponse)
            val jsonObj = jsonArray.optJSONObject(i)
            val irrigationSourceId =jsonObj.optString("IrrigationSourceId")
            val irrigationSourceName=jsonObj.optString("IrrigationSourceName")
            irrigationSourceList.add(IrrigationSource(irrigationSourceId,irrigationSourceName))
            irrigataionSourcesIdList.add(irrigationSourceId)
            irrigationSourceData.add(irrigationSourceName)
            arraydata =irrigationSourceData.toTypedArray()
            arrayOfIrriagtionId =irrigataionSourcesIdList.toTypedArray()
        }
    }

    @SuppressLint("SetTextI18n")
    fun withMultiChoiceList(view: View) {
        val items = arraydata
        val itemsSourcesID =arrayOfIrriagtionId
        val selectedList = ArrayList<Int>()
        val selectedListID = ArrayList<Int>()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("This is list choice dialog box")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
                selectedListID.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
                selectedListID.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("DONE") { dialogInterface, i ->
            val selectedStrings = ArrayList<String>()
            val selectedIdStrings = ArrayList<String>()
            val buffer = StringBuffer()
            val bufferId = StringBuffer()
            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])
                selectedIdStrings.add(itemsSourcesID[j])
                buffer.append(items[selectedList[j]])
                bufferId.append(itemsSourcesID[selectedListID[j]])
                if(j<selectedList.size-1) {
                    buffer.append(",")
                    bufferId.append(",")
                }
            }
            val output=buffer
            sourceId =1
            println("Source ID = $sourceId")
            binding.waterSource.setText(output)
        }

        builder.show()

    }
}