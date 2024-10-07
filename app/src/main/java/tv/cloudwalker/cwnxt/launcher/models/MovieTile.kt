package tv.cloudwalker.cwnxt.launcher.models

import androidx.annotation.Keep

@Keep
data class MovieRow(
    val isAdsRow: Boolean,
    val rowAdConfig: MovieAdConfig? = null,
    val rowAutoRotate: Boolean,
    val rowAutoRotateInterval: Int,
    val rowHeader: String,
    val rowIndex: Int,
    val rowItems: List<MovieTile>,
    val rowLayout: String
)

@Keep
data class MovieAdConfig(
    val rowAdBackground: String,
    val rowAdPlayType: String,
    val rowAdType: String
)

@Keep
data class MovieTile(
    val adsVideoUrl: String?= "",
    val ads_server: String?= "",
    val alternateUrl: String,
    val background: String,
    val cast: List<String>,
    val detailPage: Boolean,
    val director: List<String>,
    val genre: List<String>,
    val `package`: String,
    val playstoreUrl: String,
    val portrait: String,
    val poster: String,
    val rating: Double,
    val runtime: String,
    val showTileInfo: String,
    val source: String,
    val startIndex: Any,
    val startTime: Int,
    val synopsis: String,
    val target: List<String>,
    val tid: String,
    val tileHeight: String,
    val tileType: String,
    val tileWidth: String,
    val title: String,
    val type: String,
    val useAlternate: Boolean,
    val video_url: String,
    val year: String,
    var rowLayout: String? = "",
    var rowHeader: String? = "",
    var rowAdConfig: MovieAdConfig? = null,
    var adImageUrl: String? = null,
    var clickTrackers: List<String>? = null,
    var impressionTrackers: List<String>? = null
)