package com.example.cinevote.screens.cinema


import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder

const val DISTANCE = 15000
data class CinemaStatus(
    val userPosition : LatLng,
    val cinemaList : List<Cinema> = emptyList()
)

data class Cinema(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address:String,
    val city:String
)


interface CinemaAction{
    fun setUserPosition(latLng: LatLng)
    fun getCinema(coordinates:LatLng)
}

class cinemaVm : ViewModel() {
    private val _state= MutableStateFlow(CinemaStatus(LatLng(44.4949, 11.3426 )))
    val state = _state.asStateFlow()








    val actions = object : CinemaAction {
        override fun setUserPosition(latLng: LatLng) {

            _state.update { it.copy(userPosition = latLng) }

        }

        override fun getCinema(coordinates:LatLng) {
            //val userPosition = _state.value.userPosition

            Log.d("getcinemacoord", (coordinates.latitude).toString())
            // Esegui la query Overpass API per recuperare i cinema vicini alla posizione dell'utente
            val query = """
                [out:json];
                node["amenity"="cinema"](around:${DISTANCE},${coordinates.latitude},${coordinates.longitude});
                out;
            """.trimIndent()

            // Effettua la richiesta HTTP per eseguire la query
            val url = "https://overpass-api.de/api/interpreter?data=${URLEncoder.encode(query, "UTF-8")}"

            Log.d("URL", url)

            //val url="https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=cinema](around:100000,40.7128,-74.00);out;"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Gestisci il fallimento della richiesta
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {
                        val body = response.body
                        if (body != null) {
                            val jsonData = body.string()
                            // Analizza i dati JSON ricevuti e aggiorna lo stato
                            try {
                                // Esempio di analisi dei dati JSON (dipende dalla struttura dei dati)
                                val cinemaList = parseCinemaData(jsonData)
                                _state.update { it.copy(cinemaList = cinemaList) }
                            } catch (e: Exception) {
                                // Gestisci eventuali errori nell'analisi dei dati JSON
                            }
                        } else {
                            // Il corpo della risposta è vuoto
                            // Gestisci questa situazione di conseguenza
                        }
                    } else {
                        // La richiesta non è stata eseguita con successo
                        // Gestisci questa situazione di conseguenza
                    }
                }
            })
        }
}


private fun parseCinemaData(jsonData: String): List<Cinema> {
    val cinemaList = mutableListOf<Cinema>()
    try {

        val jsonObject = JSONObject(jsonData)
        val elements = jsonObject.getJSONArray("elements")
        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val tags = element.getJSONObject("tags")
            if (tags.has("name")) {
                val id = element.getString("id")
                val name = tags.getString("name")
                val latitude = element.getDouble("lat")
                val longitude = element.getDouble("lon")
                val housenumber = tags.optString("addr:housenumber", "")
                val street = tags.optString("addr:street","")
                val city = tags.optString("addr:city","")
                val address = "$street $housenumber"

                val cinema = Cinema(id, name, latitude, longitude, address, city)
                cinemaList.add(cinema)
            }
        }
    } catch (e: JSONException) {
        // Gestisci l'eccezione se ci sono problemi nell'analisi dei dati JSON
    }
    return cinemaList
}}
