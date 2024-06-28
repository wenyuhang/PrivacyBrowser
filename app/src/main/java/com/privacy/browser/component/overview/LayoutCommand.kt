package com.privacy.browser.component.overview

import com.privacy.browser.component.overview.views.Overview


@FunctionalInterface
interface RecentListInterface {
    fun execute(layout: Overview)
}