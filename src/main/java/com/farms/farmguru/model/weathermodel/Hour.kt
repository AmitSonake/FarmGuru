/* 
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Hour (

	val time_epoch : Int,
	val time : String,
	val temp_c : Double,
	val temp_f : Double,
	val is_day : Int,
	val condition : Condition,
	val wind_mph : Double,
	val wind_kph : Double,
	val wind_degree : Int,
	val wind_dir : String,
	val pressure_mb : Int,
	val pressure_in : Double,
	val precip_mm : Int,
	val precip_in : Int,
	val humidity : Int,
	val cloud : Int,
	val feelslike_c : Double,
	val feelslike_f : Double,
	val windchill_c : Double,
	val windchill_f : Double,
	val heatindex_c : Double,
	val heatindex_f : Double,
	val dewpoint_c : Double,
	val dewpoint_f : Double,
	val will_it_rain : Int,
	val chance_of_rain : Int,
	val will_it_snow : Int,
	val chance_of_snow : Int,
	val vis_km : Int,
	val vis_miles : Int,
	val gust_mph : Double,
	val gust_kph : Double,
	val uv : Int
)