package com.mufiid.pilwali2020.views

import com.mufiid.pilwali2020.models.Config

interface IConfigView {
    fun isLoadingConfig()
    fun hideLoadingConfig()
    fun getSuccessConfig(message: String?, data: Config)
    fun getFailedConfig(message: String?)
}