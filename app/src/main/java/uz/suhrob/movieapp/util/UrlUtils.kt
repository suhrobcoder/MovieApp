package uz.suhrob.movieapp.util

class UrlUtils {
    companion object {
        private const val IMAGE_URL = "https://image.tmdb.org/t/p/"
        private const val YOUTUBE_URL = "vnd.youtube://"

        fun buildImageUrl(path: String?, quality: Int = 3): String {
            return "${IMAGE_URL}w${quality}00$path"
        }

        fun buildYoutubeUrl(path: String): String = "$YOUTUBE_URL$path"
    }
}
