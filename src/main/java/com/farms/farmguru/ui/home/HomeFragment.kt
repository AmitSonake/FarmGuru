package com.farms.farmguru.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.farms.farmguru.R
import com.farms.farmguru.adapters.HomeSliderImageAdapter
import com.farms.farmguru.databinding.FragmentDashbordBinding
import com.farms.farmguru.model.NewsData
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.schedule.PlotListingActivity
import com.farms.farmguru.ui.dealers.DealerSearchActivity
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.myplots.MyPlotListingActivity
import com.farms.farmguru.ui.questions.QuestionHomeActivity
import com.farms.farmguru.ui.splash.TestActivity
import com.smarteist.autoimageslider.SliderView


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //private var _binding: FragmentHomeBinding? = null
    private var _binding: FragmentDashbordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var mApiService: ApiServiceInterface?= null
    private val newsArraylist = ArrayList<NewsData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

       // _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _binding = FragmentDashbordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val baseUrl: String?="https://newsapi.org/v2/"
       mApiService = ApiClient.getClient()!!.create(ApiServiceInterface::class.java)
        //fetchNewsListing()
        val imageList: ArrayList<Int> = ArrayList()
        imageList.add(R.drawable.g)
       // imageList.add(R.drawable.p)
        imageList.add(R.drawable.s)
      //  imageList.add(R.drawable.f)
        setImageInSlider(imageList, binding.imageSlider)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val mediaController = MediaController(context)
        mediaController.setAnchorView(binding.videoView)

        binding.videoView.setVideoPath("https://media.istockphoto.com/videos/vineyard-red-wine-grapes-on-the-vine-of-winery-video-id1044279946")
        binding.videoView.setMediaController(mediaController)
        binding.videoView.requestFocus()
        binding.videoView.start()
        binding.videoView.setOnCompletionListener {
            it.start()
        }
        binding.myPlotSchedule.setOnClickListener {
            startActivity(Intent(context, PlotListingActivity::class.java))
        }
        binding.myDiaryButton.setOnClickListener {
            //startActivity(Intent(context,DiaryNoteActivity::class.java))
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
            startActivity(Intent(context, TestActivity::class.java))

          /*  val payUPaymentParams = PayUPaymentParams.Builder()
                .setAmount("1.0")
            .setIsProduction(false)

            .setKey("gtKFFx")
            .setProductInfo("Test")
            .setPhone("9890400545")
            .setTransactionId("1111")
            .setFirstName("Test")
            .setEmail("sanjivinikrushi@gmail.com")
            .setSurl("https://www.google.com")
            .setFurl("https://www.yahoo.com")
            .setUserCredential("")
            //Optional, can contain any additional PG params
            .build()*/
        }

        return root
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

   /* private fun fetchNewsListing(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getLatestNews(
            "techcrunch","d6d847780c3f4f32a3375d047c424e17").enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    val topic = Gson().fromJson(stringResponse, NewsListing::class.java)
                    var position =0
                    while(position<topic.articles.toList().size){
                        val title=topic.articles.get(position).title
                        val description=topic.articles.get(position).description
                        newsArraylist.add(NewsData(title,description))
                        println("Author= $id")
                        position=position+1
                    }

                    if(newsArraylist.size>0){
                        //binding.recyclerViewNewsListing.adapter = NewsListingAdapter(newsArraylist)
                    }
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }*/
}


