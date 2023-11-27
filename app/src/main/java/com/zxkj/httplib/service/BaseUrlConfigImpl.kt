package com.zxkj.httplib.service

import com.zxkj.libhttp.config.IBaseUrlConfig

class BaseUrlConfigImpl : IBaseUrlConfig {

    override fun getBaseUrl(hostType: Int): String {
        for (enumValue in HostType.values()) {
            if (enumValue.hostType == hostType) {
                return enumValue.baseUrl
            }
        }
        throw IllegalArgumentException("unKnow hostType $hostType")
    }
}

enum class HostType(var hostType: Int, var baseUrl: String) {

    WWW(1, "https://www.kuaidi100.com");
}