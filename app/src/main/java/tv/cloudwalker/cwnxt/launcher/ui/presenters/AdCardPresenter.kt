package tv.cloudwalker.cwnxt.launcher.ui.presenters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import tv.cloudwalker.cwnxt.launcher.R
import tv.cloudwalker.cwnxt.launcher.application.GlideApp
import tv.cloudwalker.cwnxt.launcher.models.MovieTile
import tv.cloudwalker.cwnxt.launcher.ui.viewmodel.MovieViewModel
import tv.cloudwalker.cwnxt.launcher.utils.dpToPx
import tv.cloudwalker.cwnxt.launcher.utils.makeSecure

class AdCardPresenter(private val mainViewModel: MovieViewModel) : Presenter() {

    companion object {
        val MOVIE_TILE_TAG_KEY = R.id.movie_tile_tag
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        try {
            val drawable = ContextCompat.getDrawable(parent.context, R.drawable.focus_on_select_bg)
            v.background = drawable
        } catch (e: Exception) {
            Timber.e(e)
        }

        var params = v.layoutParams
        if (params == null) {
            params = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else if (params !is RecyclerView.LayoutParams) {
            params = RecyclerView.LayoutParams(params)
        }
        v.layoutParams = params

        v.isFocusable = true
        v.isFocusableInTouchMode = true

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val posterImageView: ImageView = viewHolder.view.findViewById(R.id.posterImageView)
        val adContainer: FrameLayout = viewHolder.view.findViewById(R.id.adContainer)

        if (item is MovieTile) {
            viewHolder.view.setTag(MOVIE_TILE_TAG_KEY, item)

            // Handle ad content
            if (item.adImageUrl != null) {
                posterImageView.visibility = View.VISIBLE
                adContainer.visibility = View.GONE

                // Load ad image into adContainer
                /*val adImageView = ImageView(viewHolder.view.context)
                adContainer.addView(adImageView, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ))*/

                GlideApp.with(viewHolder.view.context)
                    .load(makeSecure(item.adImageUrl ?: ""))
                    .override(
                        dpToPx(viewHolder.view.context, item.tileWidth.toInt() ?: 0),
                        dpToPx(viewHolder.view.context, item.tileHeight.toInt() ?: 0)
                    )
                    .into(posterImageView)
            } else {
                // Fallback to regular content if no ad image
                posterImageView.visibility = View.GONE
                adContainer.visibility = View.GONE
                setRegularContent(item, viewHolder.view.context, viewHolder.view, posterImageView)
            }

            // Set layout params
            viewHolder.view.layoutParams = viewHolder.view.layoutParams.apply {
                width = dpToPx(viewHolder.view.context, item.tileWidth?.toInt() ?: 0)
                height = dpToPx(viewHolder.view.context, item.tileHeight?.toInt() ?: 0)
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val posterImageView: ImageView = viewHolder.view.findViewById(R.id.posterImageView)
        val adContainer: FrameLayout = viewHolder.view.findViewById(R.id.adContainer)
        try {
            GlideApp.with(viewHolder.view.context).clear(posterImageView)
            adContainer.removeAllViews()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun setRegularContent(
        movie: MovieTile,
        context: Context,
        view: View,
        imageView: ImageView
    ) {
        movie.rowLayout?.let { rowLayout ->
            when (rowLayout) {
                "portrait" -> setPortraitLayout(movie, context, view, imageView)
                "square" -> setSquareLayout(movie, context, view, imageView)
                "landscape" -> setLandscapeLayout(movie, context, view, imageView)
                else -> setDefaultLayout(movie, context, view, imageView)
            }
        } ?: setDefaultLayout(movie, context, view, imageView)
    }

    private fun setPortraitLayout(movie: MovieTile, context: Context, view: View, imageView: ImageView) {
        if (movie.title == "Refresh") {
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_refresh_black_24dp))
        } else {
            val width = dpToPx(context, context.resources.getInteger(R.integer.tilePotraitWidth))
            val height = dpToPx(context, context.resources.getInteger(R.integer.tilePotraitHeight))
            GlideApp.with(context)
                .load(makeSecure(movie.portrait))
                .override(width, height)
                .into(imageView)
        }
        view.layoutParams = view.layoutParams.apply {
            width = dpToPx(context, context.resources.getInteger(R.integer.tilePotraitWidth))
            height = dpToPx(context, context.resources.getInteger(R.integer.tilePotraitHeight))
        }
    }

    private fun setSquareLayout(movie: MovieTile, context: Context, view: View, imageView: ImageView) {
        if (movie.title == "Refresh") {
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_refresh_black_24dp))
        } else {
            val width = dpToPx(context, context.resources.getInteger(R.integer.tileSquareWidth))
            val height = dpToPx(context, context.resources.getInteger(R.integer.tileSquareHeight))
            GlideApp.with(context)
                .load(makeSecure(movie.portrait))
                .override(width, height)
                .into(imageView)
        }
        view.layoutParams = view.layoutParams.apply {
            width = dpToPx(context, context.resources.getInteger(R.integer.tileSquareWidth))
            height = dpToPx(context, context.resources.getInteger(R.integer.tileSquareHeight))
        }
    }

    private fun setLandscapeLayout(movie: MovieTile, context: Context, view: View, imageView: ImageView) {
        if (movie.title == "Refresh") {
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_refresh_black_24dp))
        } else {
            val width = dpToPx(context, context.resources.getInteger(R.integer.tileLandScapeWidth))
            val height = dpToPx(context, context.resources.getInteger(R.integer.tileLandScapeHeight))
            GlideApp.with(context)
                .load(makeSecure(movie.poster))
                .override(width, height)
                .into(imageView)
        }
        view.layoutParams = view.layoutParams.apply {
            width = dpToPx(context, context.resources.getInteger(R.integer.tileLandScapeWidth))
            height = dpToPx(context, context.resources.getInteger(R.integer.tileLandScapeHeight))
        }
    }

    private fun setDefaultLayout(movie: MovieTile, context: Context, view: View, imageView: ImageView) {
        if (movie.title == "Refresh") {
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setImageDrawable(context.resources.getDrawable(R.drawable.ic_refresh_black_24dp))
        } else {
            GlideApp.with(context)
                .load(makeSecure(movie.poster))
                .into(imageView)
        }
        view.layoutParams = view.layoutParams.apply {
            width = dpToPx(context, context.resources.getInteger(R.integer.defaulttileWidth))
            height = dpToPx(context, context.resources.getInteger(R.integer.deafulttileHeight))
        }
    }
}