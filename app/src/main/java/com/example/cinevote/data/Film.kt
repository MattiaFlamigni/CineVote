package com.example.cinevote.data

enum class Genre(val id:Int){
    Action(28),
    Adventure(12),
    Animation(16),
    Comedy(35),
    Drama(18),
    Horror(27),

}



data class Film(
    val id : Int,
    val title: String,
    val posterPath: String,
    val plot: String,
    val voteAverage: Float,
    val releaseDate: String,
    val genreIDs: List<Int>

){
    val posterUrl: String
        get() = "https://image.tmdb.org/t/p/w500$posterPath"

    companion object {
        // Mappa per memorizzare il mapping degli ID dei generi ai loro nomi
        private val genreIdToNameMap: Map<Int, String> = mapOf(
            // Inserisci qui il mapping degli ID dei generi ai loro nomi corrispondenti
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            // Aggiungi altri generi...
        )
    }

    // Metodo per ottenere i nomi dei generi corrispondenti agli ID dei generi
    fun getGenres(): List<String> {
        return genreIDs.mapNotNull { genreIdToNameMap[it] }
    }
}







