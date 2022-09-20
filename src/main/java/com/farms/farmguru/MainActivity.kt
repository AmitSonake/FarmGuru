package com.farms.farmguru

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.farms.farmguru.databinding.ActivityMainBinding
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.dogs.util.SharedPreferencesHelper
import java.util.*


class MainActivity : AppCompatActivity() {
    private val recordAudioRequestCode = 101
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var languageCode:String? ="EN"
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
       // languageCode = intent.getStringExtra("LanguageCode").toString()


// Use conf.locale = new Locale(...) if targeting lower versions
// Use conf.locale = new Locale(...) if targeting lower versions

        setSupportActionBar(binding.appBarMain.toolbar)
        /*binding.appBarMain.fab.visibility= View.GONE
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        var menu:Menu= navView.menu
        var homeMenu= menu.findItem(R.id.nav_home)
        var languageMenu= menu.findItem(R.id.nav_language)
        var callHelplineMenu= menu.findItem(R.id.nav_executive)
        var aboutUsMenu= menu.findItem(R.id.nav_aboutus)
        var logoutMenu= menu.findItem(R.id.nav_logout)

        if(languageCode.equals("kn")){
            homeMenu.setTitle("ಮುಖಪುಟ")
            languageMenu.setTitle("ಭಾಷೆಯನ್ನು ಆರಿಸಿ")
            callHelplineMenu.setTitle("ಸಹಾಯವಾಣಿಗೆ ಕರೆ ಮಾಡಿ")
            aboutUsMenu.setTitle("ನಮ್ಮ ಬಗ್ಗೆ")
            logoutMenu.setTitle("ಲಾಗ್ ಔಟ್")
        }else{
            homeMenu.setTitle("Home")
            languageMenu.setTitle("Choose Languge")
            callHelplineMenu.setTitle("Call Helpline")
            aboutUsMenu.setTitle("About Us")
            logoutMenu.setTitle("Logout")
        }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, /*R.id.nav_gallery, R.id.nav_slideshow,*/R.id.nav_language,
                R.id.nav_executive,R.id.nav_aboutus,R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        val res: Resources = this.getResources()
// Change locale settings in the app.
// Change locale settings in the app.
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.setLocale(Locale(languageCode.toString().toLowerCase())) // API 17+ only.
        res.updateConfiguration(conf, dm)
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


}