package com.privacy.browser.ui

import android.content.Intent
import android.os.Bundle
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.databinding.ActivityBrowserRouterBinding
import com.privacy.browser.ui.vm.BrowserRouterVMImpl
import com.wlwork.libframe.base.BaseActivity
import com.wlwork.libframe.utils.LiveDataBus
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 14:34
 * version : 1.0.0
 * desc    : 浏览器路由界面
 **/

@AndroidEntryPoint
class BrowserRouterActivity: BaseActivity<BrowserRouterVMImpl,ActivityBrowserRouterBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_browser_router
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.vm = viewModel
        binding.state = this@BrowserRouterActivity
        LiveDataBus.get()
            .with("key_test",String::class.java)
            .observe(this){
                viewModel.updateRecentList(binding.recentList)
                Logger.d("打印了$it")
            }
    }

    fun jumpSearchPage(){
        startActivity(Intent(this, BrowserActivity::class.java))
        val link = "https://mbd.baidu.com/newspage/data/landingreact?pageType=2&nid=news_9532351713437502719"

        val url = URL(link)

//        Logger.d("${url.protocol}://${url.host}${url.path}")
        val urlQuery = url.query
        val baseURL = urlQuery?.let {
            val value = link.replace(it, "")
            value
        }
//        Logger.d(baseURL)
        linkParsing(baseURL!!)
    }

    private fun linkParsing(baseUrl: String) {
        val link = "baiduboxapp://v1/easybrowse/hybrid?upgrade=1&type=hybrid&tpl_id=landing_app.html&newbrowser=1&style=%7B%22toolbaricons%22%3A%7B%22tids%22%3A%5B%224%22%2C%221%22%2C%222%22%2C%223%22%5D%2C%22menumode%22%3A%222%22%2C%22actionBarConfig%22%3A%7B%22extCase%22%3A%220%22%7D%7D%7D&slog=%7B%22from%22%3A%22feed%22%7D&context=%7B%22nid%22%3A%22news_9532351713437502719%22%7D&ch_url=https%3A%2F%2Fmbd.baidu.com%2Fnewspage%2Fdata%2Flandingreact%3FpageType%3D2%26nid%3Dnews_9532351713437502719%26sourceFrom%3DlandingShare&commentInfo=%7B%22topic_id%22%3A1056000065656152%2C%22opentype%22%3A2%7D&logargs=%7B%22source%22%3A%221033856g%22%2C%22channel%22%3A%221031048b%22%7D&needlog=1&ug_params=%7B%22app%22%3A%22wise%22%2C%22app_sid%22%3A%22%22%2C%22baiduid%22%3A%22D7B465E6712EF2E3338CD50D503C49BB%22%2C%22browserid%22%3A%2224%22%2C%22isTokenShare%22%3A0%2C%22nid%22%3A%22news_8341775264008904763%22%2C%22pos%22%3A%22pos_feedNews%22%2C%22ruk%22%3A%22%22%2C%22scene%22%3A%22newslandingwise%22%2C%22shareParams_page%22%3A%22picTextShare%22%2C%22shareParams_sourceFrom%22%3A%22wise_feedlist%22%2C%22sid%22%3A%22all%22%2C%22tokentime%22%3A1718698725%7D"
        // baiduboxapp://v1/easybrowse/hybrid?upgrade=1&type=hybrid&tpl_id=landing_app.html&newbrowser=1&style={"toolbaricons":{"tids":["4","1","2","3"],"menumode":"2","actionBarConfig":{"extCase":"0"}}}&slog={"from":"feed"}&context={"nid":"news_9532351713437502719"}&ch_url=https://mbd.baidu.com/newspage/data/landingreact?pageType=2&nid=news_9532351713437502719&sourceFrom=landingShare&commentInfo={"topic_id":1056000065656152,"opentype":2}&logargs={"source":"1033856g","channel":"1031048b"}&needlog=1&ug_params={"app":"wise","app_sid":"","baiduid":"D7B465E6712EF2E3338CD50D503C49BB","browserid":"24","isTokenShare":0,"nid":"news_8341775264008904763","pos":"pos_feedNews","ruk":"","scene":"newslandingwise","shareParams_page":"picTextShare","shareParams_sourceFrom":"wise_feedlist","sid":"all","tokentime":1718698725}
        // decode编码还原
        var urlStr:String = ""
        try {
            urlStr = URLDecoder.decode(link, "UTF-8")
//            Logger.d("linkParsing >>> $urlStr")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val isBaiDuScheme = isBaiDuScheme(urlStr)
        val bdSchemeFun = getBDSchemeFun(urlStr)
        val schemeParams = getSchemeParams(urlStr)
        Logger.d("baseUrl: $baseUrl \n" +
                "isBaiDuScheme: $isBaiDuScheme \n" +
                "bdSchemeFun: $bdSchemeFun \n" +
                "schemeParams: $schemeParams")
    }

    fun isBaiDuScheme(url: String): Boolean {
        var result = false
        url?.let {
            if (url.contains("baiduboxapp://")){
                result = true
            }
        }
        return result
    }

    fun getBDSchemeFun(urlStr: String): String {
        var result = ""
        if (isBaiDuScheme(urlStr)) {
            result = urlStr
            if (urlStr.contains("?")) {
                result = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            }
            result = result.replace("baiduboxapp://", "")
        }
        return result
    }

    /**
     * get scheme params
     *
     * @param uri
     * @return
     */
    fun getSchemeParams(url: String): String? {
        // jlan-scheme://getMobeneData?data={}&callback=getMobeneData
        var urlStr = url
        var param:String? = null
        if (isBaiDuScheme(urlStr)) {
            if (urlStr.contains("?")) {
                // hostName ===>  jlan-scheme://getMobeneData
                val hostName = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0]
                urlStr = urlStr.replace("$hostName?", "")
                val splitList = urlStr.split("&")

                val contextParam = splitList.firstOrNull{
                    it.contains("context={")
                }
                val urlParam = splitList.firstOrNull{
//                    it.contains("context={")
                    it.contains("ch_url=")
                }
                param = urlParam?.let {
                    val replace = it.replace("ch_url=", "")
                    val link = contextParam?.let {
                        "$replace&$it"
                    }
                    link
                }
            }
        }
        return param
    }

    fun listToMap(list: List<String>): Map<String,Any>{
        // 使用associate函数将List转换为Map
        val map = list.associate { entry ->
            val (key, value) = entry.split("=", limit = 2)
            key to value
        }
        // 打印Map
        map.forEach { (k, v) -> Logger.d("Key : $k Value : $v") }
        return map
    }

    fun<T> List<T>.forEachWithBreak(action: (T) -> Boolean): T?{
        for (item in this){
            if (!action(item)) return item
        }
        return null
    }
}