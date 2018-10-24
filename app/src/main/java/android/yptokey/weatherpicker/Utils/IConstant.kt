package android.yptokey.weatherpicker.Utils

interface IConstant {
    val API_KEY: String
        get() = "&APPID=84bcbd37630c5c65ff14e32cda028d51"
    val BASE_URL: String
        get() = "https://api.openweathermap.org/data/2.5/forecast?q="
    val REQUEST_UNIT: String
        get() = "&units=metric"
}