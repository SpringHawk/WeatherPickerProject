package android.yptokey.weatherpicker.Utils


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.yptokey.weatherpicker.Data.DataModelWeather
import android.yptokey.weatherpicker.R

class RecyclerAdapter(val weatherList: ArrayList<DataModelWeather>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtTime.text = weatherList[p1].time
        p0.txtTemp.text = weatherList[p1].temp
        p0.txtWeather.text = weatherList[p1].weatherOverall
        p0.txtWeatherDescription.text = weatherList[p1].weatherDescription
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.recycler_row_item, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtTime: TextView = itemView.findViewById(R.id.TVtime)
        val txtTemp: TextView = itemView.findViewById(R.id.TVtemp)
        val txtWeather: TextView = itemView.findViewById(R.id.TVweatherOverall)
        val txtWeatherDescription: TextView = itemView.findViewById(R.id.TVWeatherDescription)

    }

}