package tv.cloudwalker.cwnxt.launcher.utils

import timber.log.Timber

/**
 * Created by vaibhavingale on 24/04/18.
 */

class NoLoggingTree : Timber.Tree() {
    protected override fun log(
        priority: Int,
        tag: String?,
        message: String,
        throwable: Throwable?
    ) {
    }
}