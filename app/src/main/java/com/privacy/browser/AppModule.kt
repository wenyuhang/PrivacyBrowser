package com.privacy.browser

import com.privacy.browser.webconfig.WebViewUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 15:12
 * version : 1.0.0
 * desc    :
 **/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWebViewUtils(): WebViewUtils {
        return WebViewUtils()
    }
}