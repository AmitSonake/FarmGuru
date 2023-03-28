package com.farms.krushisanjivini.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.BuildConfig
import com.farms.krushisanjivini.MainActivity
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.HomeItemsAdapter
import com.farms.krushisanjivini.adapters.HomeSliderImageAdapter
import com.farms.krushisanjivini.adapters.NotificationListAdapter
import com.farms.krushisanjivini.databinding.FragmentDashbordBinding
import com.farms.krushisanjivini.databinding.NewNotificationListingBinding
import com.farms.krushisanjivini.model.AddImageUrls
import com.farms.krushisanjivini.model.AppCurrentVersion
import com.farms.krushisanjivini.model.HomeMenuItems
import com.farms.krushisanjivini.model.Notes
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.farms.krushisanjivini.utilities.util.showSubscriptionAlert
import com.google.gson.Gson
import com.smarteist.autoimageslider.SliderView
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //private var _binding: FragmentHomeBinding? = null
    private var _binding: FragmentDashbordBinding? = null
    private val homeMenuList: ArrayList<HomeMenuItems> = ArrayList()
    private var noteParsedList = ArrayList<Notes>()
    private val noteList = ArrayList<Notes>()
    private var userLanguageID: Int =1
    private var languageCode:String = "EN"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mApiService: ApiServiceInterface?= null
    private var currentAppVersion:String = "1"
    private var listIntValue=ArrayList<Int>()
    private var notesNewID:Int = 0
    private val list: ArrayList<AddImageUrls>? =ArrayList<AddImageUrls>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

       // _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding = FragmentDashbordBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loadlabels(root.context)
        ///val baseUrl: String?="https://newsapi.org/v2/"
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(root.context).getToken())!!.create(ApiServiceInterface::class.java)
        //var appVersion:String? = fetchAppVersion()
        /*val imageList: ArrayList<Int> = ArrayList()
        imageList.add(R.drawable.g)
       // imageList.add(R.drawable.p)
        imageList.add(R.drawable.s)
      //  imageList.add(R.drawable.f)
        setImageInSlider(imageList, binding.imageSlider)
*/
        if(SharedPreferencesHelper.invoke(root.context).getSelectedLanguage().equals("kn")) {
            (activity as MainActivity).supportActionBar?.title = "ಮುಖ ಪುಟ"
            homeMenuList.add(HomeMenuItems("ನನ್ನ ತೋಟದ ಇವತ್ತಿನ ವೇಳಾಪಟ್ಟಿ"))
            homeMenuList.add(HomeMenuItems("ನಿಮ್ಮ ಪ್ರಶ್ನೆ ನಮ್ಮ ಉತ್ತರ"))
            homeMenuList.add(HomeMenuItems("ನನ್ನ ತೋಟದ ವಿವರಗಳು"))
            homeMenuList.add(HomeMenuItems("ನನ್ನ ಡೈರಿ"))
            homeMenuList.add(HomeMenuItems("ನಿಮ್ಮ ಪ್ಲಾಟನ್ನು ಇಲ್ಲಿ ನೊಂದಾಯಿಸಿ"))
            homeMenuList.add(HomeMenuItems( "ಮಾಹಿತಿ ಬಂಡಾರ"))
           // homeMenuList.add(HomeMenuItems("ಪ್ರಸ್ತುತ ಬೆಳೆ ಅವಧಿ"))
            homeMenuList.add(HomeMenuItems("ರಸ ಗೊಬ್ಬರ ಪೊರೈಕೆದಾರರು"))
            homeMenuList.add(HomeMenuItems("ಜಾಹೀರಾತು"))
            homeMenuList.add(HomeMenuItems("ವೆಬಿನಾರ"))



            homeMenuList.add(HomeMenuItems("ಸೇವೆ ಒದಗಿಸುವವರು"))
            homeMenuList.add(HomeMenuItems("ವಿಡಿಯೋಗಳು ನಿಮಗಾಗಿ"))
            homeMenuList.add(HomeMenuItems("ಹವಾಮಾನ ವರದಿ"))

            userLanguageID=2
            languageCode="kn"

        }else{
            (activity as MainActivity).supportActionBar?.title = "Home"
            homeMenuList.add(HomeMenuItems("My Plot Today’s Schedule"))
            homeMenuList.add(HomeMenuItems("Your Question Our Answer"))
            homeMenuList.add(HomeMenuItems("My Plot Details"))
            homeMenuList.add(HomeMenuItems("My Diary"))
            homeMenuList.add(HomeMenuItems("Register Your Plot"))
            homeMenuList.add(HomeMenuItems("Knowledge Bank"))
           // homeMenuList.add(HomeMenuItems("Present Crop Period"))
            homeMenuList.add(HomeMenuItems("Agri Input Dealer"))
            homeMenuList.add(HomeMenuItems("Advertisement"))
            //homeMenuList.add(HomeMenuItems("Weather Report"))
            homeMenuList.add(HomeMenuItems("Webinar"))
            homeMenuList.add(HomeMenuItems("Service Provider"))
            homeMenuList.add(HomeMenuItems("Videos For You"))
            homeMenuList.add(HomeMenuItems("Weather Report"))

            userLanguageID=1
            languageCode="EN"
        }
        GridLayoutManager(
            root.context, // context
            2,
            // span count
            RecyclerView.VERTICAL, // orientation
            false // reverse layout
        ).apply {
            // specify the layout manager for recycler view
            binding.recyclerView.layoutManager=this
        }
        var intArrayList: ArrayList<Int> = arrayListOf<Int>(R.drawable.sch,R.drawable.qa,
            R.drawable.cropss,R.drawable.my_diary,
            R.drawable.register_plot,R.drawable.knowledge_bank,
            R.drawable.agree_input,R.drawable.test,
            R.drawable.webinar,R.drawable.service,
           /* R.drawable.service,*/R.drawable.videos,R.drawable.weathr)

        binding.recyclerView.adapter= HomeItemsAdapter(homeMenuList,intArrayList)
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(root.context)
        if(networkStatus){
            fetchAppVersion()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),root.context)
        }

        /*binding.myPlotSchedule.setOnClickListener {
            startActivity(Intent(context, PlotListingActivity::class.java))
        }
        binding.myDiaryButton.setOnClickListener {
            startActivity(Intent(context, DiaryPlotListingActivity::class.java))
        }
        binding.weatherReport.setOnClickListener {
            //startActivity(Intent(context,WeatherForecastActivity::class.java))
        }
        binding.webinarButton.setOnClickListener {
        //    startActivity(Intent(context,WeatherActivity::class.java))
        }
        binding.registerAnotherPlotButton.setOnClickListener {
            startActivity(Intent(context,MyPlotActivity::class.java))
        }
        binding.videosButton.setOnClickListener {
          //  startActivity(Intent(context,WeatherActivity::class.java))
        }
        binding.askQuestionButton.setOnClickListener {
            startActivity(Intent(context,QuestionHomeActivity::class.java))
        }
        binding.myPlotDetailButton.setOnClickListener {
            startActivity(Intent(context,MyPlotListingActivity::class.java))
        }
        binding.agreeDealerButton.setOnClickListener {

        }
*/
        return root
    }

    @SuppressLint("SetTextI18n")
    private fun loadlabels(context: Context) {
        binding.versionInfo.text="Version ${BuildConfig.VERSION_NAME}"
        if(SharedPreferencesHelper.invoke(context).getSelectedLanguage().equals("kn")){
            binding.presentCropPeriod.text="ಪ್ರಸ್ತುತ ಬೆಳೆ ಅವಧಿ"
            binding.agreeDealerButton.text="ಅಗ್ರಿ ಇನ್ಪುಟ್ ಡೀಲರ್"
            binding.askQuestionButton.text="ನಿಮ್ಮ ಪ್ರಶ್ನೆ ನಮ್ಮ ಉತ್ತರ"
            binding.weatherReport.text="ಹವಾಮಾನ ವರದಿ"
            binding.myPlotSchedule.text="ನನ್ನ ಕಥಾವಸ್ತು ಇಂದಿನ ವೇಳಾಪಟ್ಟಿ"
            binding.knowledgeBankButton.text="ಜ್ಞಾನ ಬ್ಯಾಂಕ್"
            binding.webinarButton.text="ವೆಬ್ನಾರ್"
            binding.videosButton.text="ನಿಮಗಾಗಿ ವೀಡಿಯೊಗಳು"
            binding.serviceProviderButton.text="ಸೇವೆ ಒದಗಿಸುವವರು"
            binding.myDiaryButton.text="ನನ್ನ ಡೈರಿ"
            binding.myPlotDetailButton.text="ನನ್ನ ಕಥಾವಸ್ತುವಿನ ವಿವರಗಳು"
            binding.registerAnotherPlotButton.text="ನಿಮ್ಮ ಇನ್ನೊಂದು ಕಥಾವಸ್ತುವನ್ನು ನೋಂದಾಯಿಸಿ"

        }else{
            binding.presentCropPeriod.text="Present Crop Period"
            binding.agreeDealerButton.text="Agri Input Dealer"
            binding.askQuestionButton.text="Your Question our Answer"
            binding.weatherReport.text="Weather Report"
            binding.myPlotSchedule.text="My plot Today’s Schedule"
            binding.knowledgeBankButton.text="Knowledge Bank"
            binding.webinarButton.text="Webinar"
            binding.videosButton.text="Videos for You"
            binding.serviceProviderButton.text="Service Provider"
            binding.myDiaryButton.text="My Diary"
            binding.myPlotDetailButton.text="My plot Details"
            binding.registerAnotherPlotButton.text="Register your Another Plot"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setImageInSlider(images: ArrayList<Int>, imageSlider: SliderView) {
        val adapter = HomeSliderImageAdapter(requireActivity().applicationContext)
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }


    private fun fetchAppVersion(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getAppCurrentVersion(
            ).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    val topic = Gson().fromJson(stringResponse, AppCurrentVersion::class.java)
                    currentAppVersion = topic.VersionNumber.toString()

                    if(BuildConfig.VERSION_NAME.equals(currentAppVersion)){
                        getNotesList()
                        //showSubscriptionAlert(activity as MainActivity,"Application update required")
                    }else{
                        showSubscriptionAlert(activity as MainActivity,"Application update is required!! " +
                                "Please install new update from google play store")
                    }

                }else if(response.code()==401){
                   CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",activity as MainActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)

            }
        })
    }

    private fun getNotesList(){
        if(SharedPreferencesHelper.invoke(activity as MainActivity).getNewNoteID()!=null){
            notesNewID = SharedPreferencesHelper.invoke(activity as MainActivity).getNewNoteID()!!
        }else
            notesNewID=0
        println("Notes NewID= $notesNewID")
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getNewNotesBylangID(userLanguageID,notesNewID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    noteParsedList= stringResponse?.let { parseJson(it) }!!
                   /* noteParsedList.add(Notes(1,1,1,"Test"))
                    if (list != null) {
                        list.add(AddImageUrls("https://firebasestorage.googleapis.com/v0/b/krushisanjivini-96d72.appspot.com/o/adds%2Fadd.jpeg?alt=media&token=28eb6753-828c-4b9c-9e2a-ad29fa27b2b7"))
                    }*/
                    if(noteParsedList.size>0)
                    showNotifications(activity as MainActivity,noteParsedList)

                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",activity as MainActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun parseJson(jsonResponse: String):ArrayList<Notes>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val transId =jsonObj.optInt("TransId")
            val noteId =jsonObj.optInt("NoteId")
            val langId =jsonObj.optInt("LangId")
            val noteText =jsonObj.optString("NoteText")
            val imageUrl =jsonObj.optString("ImageUrl")
            noteList.add(Notes(transId,noteId,langId,noteText,imageUrl))
            listIntValue .add(noteId)
        }

        return noteList
    }

    private fun showNotifications(
        context: Context,
        noteParsedList: ArrayList<Notes>
    ){
        val maxReadId: Int = listIntValue.maxOrNull() ?: 0
        val dialogBuilder = context?.let { Dialog(it) }
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = NewNotificationListingBinding.inflate(inflater)
        dialogBuilder.setContentView(binding.root)
        if(userLanguageID==2){
            binding.readAcceptButton.text="ನಾನು ಇದನ್ನು ಓದಿ ಅರ್ಥಮಾಡಿಕೊಂಡಿದ್ದೆನೆ "
        }else{
            binding.readAcceptButton.text="Yes I Have Read"
        }
       // if(noteParsedList.size>0){
        binding.notesListRecyclerView.adapter = NotificationListAdapter(noteParsedList,languageCode)
        //loadImages(binding.notesListRecyclerView)

        //}
        binding.readAcceptButton.setOnClickListener {
            SharedPreferencesHelper.invoke(it.context).saveNewNoteID(maxReadId)
            dialogBuilder.dismiss()
        }
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()

    }


}


