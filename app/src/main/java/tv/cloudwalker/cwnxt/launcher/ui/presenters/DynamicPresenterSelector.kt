package tv.cloudwalker.cwnxt.launcher.ui.presenters

import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import tv.cloudwalker.cwnxt.launcher.models.MovieTile
import tv.cloudwalker.cwnxt.launcher.ui.viewmodel.MovieViewModel

class DynamicPresenterSelector(private val mainViewModel: MovieViewModel) : PresenterSelector() {
    private val cardPresenter = CardPresenter(mainViewModel)
    private val adCardPresenter = AdCardPresenter(mainViewModel)

    override fun getPresenter(item: Any?): Presenter {
        return when (item) {
            is MovieTile -> {
                if (item.ads_server != null) adCardPresenter else cardPresenter
            }
            else -> cardPresenter
        }
    }
}