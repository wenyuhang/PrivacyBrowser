package com.privacy.browser.api

import com.privacy.browser.config.Constants
import com.privacy.browser.pojo.Result
import com.privacy.browser.pojo.SearchHistory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
interface ApiService {
    /**
     * 查询国内油价
     */
    @GET("gnyj/query")
    suspend fun getOilPriceInfo(@Query("key") key: String = Constants.OIL_PRICE_KEY): Result<List<SearchHistory>>

}