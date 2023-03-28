package com.farms.krushisanjivini

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.ActivityMainBinding
import com.farms.krushisanjivini.model.PlotListing
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.notification.NotificationListActivity
import com.farms.krushisanjivini.ui.myplots.MyPlotActivity
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.farms.krushisanjivini.utilities.util.closeAppAlert
import com.google.android.material.navigation.NavigationView
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private val recordAudioRequestCode = 101
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var languageCode:String? ="EN"
    private var mApiService: ApiServiceInterface?= null
    private val plotList = ArrayList<PlotListing>()
    private var plotParsedList = ArrayList<PlotListing>()
    private var userLanguageID:Int=1


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermissionToRecordAudio()

        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }

        languageCode= SharedPreferencesHelper.invoke(this).getSelectedLanguage()

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)


        var menu:Menu= navView.menu
        var homeMenu= menu.findItem(R.id.nav_home)
        var languageMenu= menu.findItem(R.id.nav_language)
        var callHelplineMenu= menu.findItem(R.id.nav_executive)
        var aboutUsMenu= menu.findItem(R.id.nav_aboutus)
        var privacyPolicyMenu= menu.findItem(R.id.nav_privacypolicy)
        var logoutMenu= menu.findItem(R.id.nav_logout)
        var headerView : View =navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.labelAppTitle).text=SharedPreferencesHelper.invoke(this).getUserName().toString()
        headerView.findViewById<TextView>(R.id.labelUserEmailId).text=SharedPreferencesHelper.invoke(this).getUserEmail().toString()
      //  var userTitle= binding.navView.menu.findItem(R.id.labelAppTitle)
       // var userEmail= navView.navView.menu.findItem(R.id.labelUserEmailId)
       // userTitle.setTitle("krushi sanjiveeni")


        binding.navView.findViewById<TextView>(R.id.versionInfo).text="App Version 1.0"//${BuildConfig.VERSION_NAME}"
        if(languageCode.equals("kn")){
            binding.appBarMain.toolbar.title="ಮುಖ ಪುಟ"
            supportActionBar?.title="ಮುಖ ಪುಟ"
            homeMenu.setTitle("ಮುಖ ಪುಟ")
            languageMenu.setTitle("ಭಾಷೆಯನ್ನು ಆರಿಸಿ")
            callHelplineMenu.setTitle("ಸಹಾಯವಾಣಿಗೆ ಕರೆ ಮಾಡಿ")
            aboutUsMenu.setTitle("ನಮ್ಮ ಬಗ್ಗೆ")
            privacyPolicyMenu.setTitle("ಗೌಪ್ಯತಾ ನೀತಿ")
            logoutMenu.setTitle("ಲಾಗ್ ಔಟ್")
        }else{
            binding.appBarMain.toolbar.title="Home"
            supportActionBar?.title="Home"
            homeMenu.setTitle("Home")
            languageMenu.setTitle("Choose Languge")
            callHelplineMenu.setTitle("Call Helpline")
            aboutUsMenu.setTitle("About Us")
            privacyPolicyMenu.setTitle("Privacy Policy")
            logoutMenu.setTitle("Logout")
        }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, /*R.id.nav_gallery, R.id.nav_slideshow,*/R.id.nav_language,
                R.id.nav_executive,R.id.nav_aboutus,R.id.nav_privacypolicy,R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(
            ApiServiceInterface::class.java)

        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
            getPlotList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> {
                startActivity(Intent(this, NotificationListActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid checking the build version since Context.checkSelfPermission(...) is only available in Marshmallow
        // 2) Always check for permission (even if permission has already been granted) since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), recordAudioRequestCode)
        }
    }

    override fun onBackPressed() {
        if(languageCode.equals("kn"))
            closeAppAlert(this,"ನೀವು ಅಪ್ಲಿಕೇಶನ್ ಅನ್ನು ಮುಚ್ಚಲು ಬಯಸುವಿರಾ?")
        else
            closeAppAlert(this,"Do you want to close the app?")
    }

    override fun onResume() {
        super.onResume()
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")) {
            supportActionBar?.title = "ಮುಖ ಪುಟ"
        }else{
            supportActionBar?.title = "Home"
        }

    }

    private fun getPlotList(){
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
        }else {
            userLanguageID =1
        }
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered(userLanguageID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    plotParsedList= stringResponse?.let { parseJson(it) }!!
                    if(plotParsedList.size>0){
                        SharedPreferencesHelper.invoke(this@MainActivity).saveIsUserHavePlot(true)
                        for(i in plotParsedList.indices) {
                            if (plotParsedList[i].IsPaid) {
                                SharedPreferencesHelper.invoke(this@MainActivity).saveIsUserPlotActive(true)
                                break
                            }

                        }
                        //binding.plotsListRecyclerView.adapter = ListSchedulePlotAdapter(plotParsedList,languageCode)
                    }else{
                        SharedPreferencesHelper.invoke(this@MainActivity).saveIsUserPlotActive(false)
                        navigateToCropSelection()

                    }
                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@MainActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun navigateToCropSelection(){
        binding.progressbar.visibility = View.GONE
        val intent = Intent(Intent(this, MyPlotActivity::class.java))
        ContextCompat.startActivity(this, intent, null)

    }
    private fun parseJson(jsonResponse: String):ArrayList<PlotListing>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val regId =jsonObj.optInt("RegId")
            val plotId =jsonObj.optString("PlotId")
            val farmName =jsonObj.optString("FarmerName")
            val farmAddress =jsonObj.optString("FarmerAddress")
            val farmPruningDate =jsonObj.optString("PruningDate")
            val cropSeason =jsonObj.optString("CropSeason")

            val farmTaluqa =jsonObj.optString("FarmerTal")
            val farmDistrict =jsonObj.optString("FarmerDistrict")
            val farmState =jsonObj.optString("FarmerState")

            val crop =jsonObj.optString("Crop")
            val cropVariety =jsonObj.optString("CropVariety")

            val soilType =jsonObj.optString("SoilType")

            val cropPurpose =jsonObj.optString("CropPurpose")
            val cropDistance =jsonObj.optString("CropDistance")
            val plotAge =jsonObj.optInt("PlotAge")
            val plotArea =jsonObj.optInt("PlotArea")
            val isPaid =jsonObj.optBoolean("isPaid")
            //val isTrial =jsonObj.optBoolean("isTrial")
            val regDate =jsonObj.optString("RegDate")



            plotList.add(PlotListing(regId,plotId,farmName,farmAddress,farmTaluqa,
                farmDistrict,farmState,
                crop,cropVariety,cropSeason,farmPruningDate,soilType,cropPurpose,cropDistance,
                plotAge,plotArea,isPaid))

        }

        return plotList
    }


}