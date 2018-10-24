package android.yptokey.weatherpicker

import android.graphics.Movie
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.yptokey.weatherpicker.Data.DataModelWeather
import android.yptokey.weatherpicker.Utils.IConstant
import android.yptokey.weatherpicker.Utils.RecyclerAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), IConstant {

    var enteredCity: String = ""
    private lateinit var recyclerView: RecyclerView
    private val weatherList: ArrayList<DataModelWeather> = ArrayList()
    private val mAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configSearchView(ETCity)

        recyclerView = findViewById(R.id.RVResults)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    fun showWeather(view: View) {
        enteredCity = ETCity.text.toString()
        requestWeatherATM(enteredCity)
    }

    private fun configSearchView(searchView: EditText) {
        searchView.requestFocusFromTouch()
        searchView.setOnEditorActionListener { _: TextView, actionId: Int, keyEvent: KeyEvent? ->
            if (keyEvent != null) {
                if (actionId != EditorInfo.IME_ACTION_SEARCH || keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    enteredCity = searchView.text.toString()
                    requestWeatherATM(enteredCity)
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    private fun requestWeatherATM(city: String) {
        if (enteredCity != "" && enteredCity != " ") {
            var url = BASE_URL
            url = url + city + REQUEST_UNIT + API_KEY
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)

            // Request a string response from the provided URL.
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        handleNetworkResponse(response)
                    },
                    Response.ErrorListener { error ->
                        Log.e("Network Error", error.toString())
                    }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } else {
            ETCity.error = "This is empty"
        }
    }

    private fun handleNetworkResponse(response: JSONObject) {

        var time: String
        var currentTemp: String
        var overallWeather: String
        var weatherDescription: String


        //Parse Response JSON
        val listOfDays: JSONArray = response.getJSONArray("list")
        for (i in 0..(listOfDays.length() - 1)) {
            val item = listOfDays.getJSONObject(i)
            val allData: ArrayList<JSONObject> = ArrayList()
            allData.add(item)
            for (j in 0..(allData.size - 1)) {
                val oneDate: JSONObject = allData[j]
                time = oneDate.getString("dt_txt")
                val mainTemp: JSONObject = oneDate.get("main") as JSONObject
                currentTemp = mainTemp.getString("temp")
                val weather: JSONArray = oneDate.get("weather") as JSONArray
                for (k in 0 until weather.length()) {
                    val mainWeather = weather.getJSONObject(k)
                    val allWeather: ArrayList<JSONObject> = ArrayList()
                    allWeather.add(mainWeather)
                    for (m in 0 until allWeather.size) {
                        val oneWeather: JSONObject = allWeather[m]
                        overallWeather = oneWeather.getString("main")
                        weatherDescription = oneWeather.getString("description")

                        var weatherByDay: HashMap<String, String> = HashMap()
                        weatherByDay.put("day", time)
                        weatherByDay.put("temp", currentTemp)
                        weatherByDay.put("weather", overallWeather)
                        weatherByDay.put("weatherDescription", weatherDescription)

                        val weatherAllDay: ArrayList<HashMap<String, String>> = ArrayList()
                        weatherAllDay.add(weatherByDay)

                        prepareMovieData(time, currentTemp, overallWeather, weatherDescription)

                        Log.e("WEather", "Am " + time + " werden es " + currentTemp + " und wir rechnen mit " + overallWeather + " " + weatherDescription)
                    }
                }
            }
        }

        val adapter = RecyclerAdapter(weatherList)
        recyclerView.adapter = adapter
    }

    private fun filterDays(weather: ArrayList<HashMap<String, String>>) {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val today = calendar.get(Calendar.MILLISECOND).toLong()
    }

    private fun prepareMovieData(time: String, temp: String, overallWeather: String, weatherDescription: String) {
        weatherList.add(DataModelWeather(time, temp, overallWeather, weatherDescription))
    }
}
