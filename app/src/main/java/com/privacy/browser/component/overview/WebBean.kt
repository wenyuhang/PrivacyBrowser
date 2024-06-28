package com.privacy.browser.component.overview

import android.graphics.drawable.Drawable

data class WebBean(val id: Int, val webView: CommonWebView, var tabTitle: String,
                   var tabIcon: Drawable?, var picture: Drawable?)
