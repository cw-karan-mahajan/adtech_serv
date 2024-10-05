package tv.cloudwalker.cwnxt.launcher.models

data class MovieResponse(
    val rowCount: Int,
    val rows: List<MovieRow>
)