package com.kagan.chatapp.utils

import android.content.Context
import com.kagan.chatapp.R
import com.kagan.chatapp.utils.APIStatusCode.ERR01001
import com.kagan.chatapp.utils.APIStatusCode.ERR01002
import com.kagan.chatapp.utils.APIStatusCode.ERR01003
import com.kagan.chatapp.utils.APIStatusCode.ERR01004
import com.kagan.chatapp.utils.APIStatusCode.ERR01005
import com.kagan.chatapp.utils.APIStatusCode.ERR01006
import com.kagan.chatapp.utils.APIStatusCode.ERR01007
import com.kagan.chatapp.utils.APIStatusCode.ERR01008
import com.kagan.chatapp.utils.APIStatusCode.ERR01009
import com.kagan.chatapp.utils.APIStatusCode.ERR01010
import com.kagan.chatapp.utils.APIStatusCode.ERR02001
import com.kagan.chatapp.utils.APIStatusCode.ERR02002
import com.kagan.chatapp.utils.APIStatusCode.ERR02003
import com.kagan.chatapp.utils.APIStatusCode.ERR02004
import com.kagan.chatapp.utils.APIStatusCode.ERR02005
import com.kagan.chatapp.utils.APIStatusCode.ERR02006
import com.kagan.chatapp.utils.APIStatusCode.ERR02007
import com.kagan.chatapp.utils.APIStatusCode.ERR02008
import com.kagan.chatapp.utils.APIStatusCode.ERR02009
import com.kagan.chatapp.utils.APIStatusCode.ERR02010
import com.kagan.chatapp.utils.APIStatusCode.ERR02011
import com.kagan.chatapp.utils.APIStatusCode.ERR02012
import com.kagan.chatapp.utils.APIStatusCode.ERR02013
import com.kagan.chatapp.utils.APIStatusCode.ERR02014
import com.kagan.chatapp.utils.APIStatusCode.ERR02015
import com.kagan.chatapp.utils.APIStatusCode.ERR02016
import com.kagan.chatapp.utils.APIStatusCode.ERR02017
import com.kagan.chatapp.utils.APIStatusCode.ERR02018
import com.kagan.chatapp.utils.APIStatusCode.ERR02019
import com.kagan.chatapp.utils.APIStatusCode.ERR02020
import com.kagan.chatapp.utils.APIStatusCode.ERR02021
import com.kagan.chatapp.utils.APIStatusCode.ERR02022
import com.kagan.chatapp.utils.APIStatusCode.ERR02023
import com.kagan.chatapp.utils.APIStatusCode.ERR02024
import com.kagan.chatapp.utils.APIStatusCode.ERR02025
import com.kagan.chatapp.utils.APIStatusCode.ERR02026
import com.kagan.chatapp.utils.APIStatusCode.ERR03001
import com.kagan.chatapp.utils.APIStatusCode.ERR03002
import com.kagan.chatapp.utils.APIStatusCode.ERR03003
import com.kagan.chatapp.utils.APIStatusCode.ERR03004
import dagger.hilt.android.qualifiers.ApplicationContext

object ErrorCodes {

    fun getDescription(errorCode: String, context: Context): String {
        return when (errorCode) {
            // ERR01 - General Errors
            ERR01001 -> context.resources.getString(R.string.ERR01001)
            ERR01002 -> context.resources.getString(R.string.ERR01002)
            ERR01003 -> context.resources.getString(R.string.ERR01003)
            ERR01004 -> context.resources.getString(R.string.ERR01004)
            ERR01005 -> context.resources.getString(R.string.ERR01005)
            ERR01006 -> context.resources.getString(R.string.ERR01006)
            ERR01007 -> context.resources.getString(R.string.ERR01007)
            ERR01008 -> context.resources.getString(R.string.ERR01008)
            ERR01009 -> context.resources.getString(R.string.ERR01009)
            ERR01010 -> context.resources.getString(R.string.ERR01010)
            // ERR02 - Identity Errors
            ERR02001 -> context.resources.getString(R.string.ERR02001)
            ERR02002 -> context.resources.getString(R.string.ERR02002)
            ERR02003 -> context.resources.getString(R.string.ERR02003)
            ERR02004 -> context.resources.getString(R.string.ERR02004)
            ERR02005 -> context.resources.getString(R.string.ERR02005)
            ERR02006 -> context.resources.getString(R.string.ERR02006)
            ERR02007 -> context.resources.getString(R.string.ERR02007)
            ERR02008 -> context.resources.getString(R.string.ERR02008)
            ERR02009 -> context.resources.getString(R.string.ERR02009)
            ERR02010 -> context.resources.getString(R.string.ERR02010)
            ERR02011 -> context.resources.getString(R.string.ERR02011)
            ERR02012 -> context.resources.getString(R.string.ERR02012)
            ERR02013 -> context.resources.getString(R.string.ERR02013)
            ERR02014 -> context.resources.getString(R.string.ERR02014)
            ERR02015 -> context.resources.getString(R.string.ERR02015)
            ERR02016 -> context.resources.getString(R.string.ERR02016)
            ERR02017 -> context.resources.getString(R.string.ERR02017)
            ERR02018 -> context.resources.getString(R.string.ERR02018)
            ERR02019 -> context.resources.getString(R.string.ERR02019)
            ERR02020 -> context.resources.getString(R.string.ERR02020)
            ERR02021 -> context.resources.getString(R.string.ERR02021)
            ERR02022 -> context.resources.getString(R.string.ERR02022)
            ERR02023 -> context.resources.getString(R.string.ERR02023)
            ERR02024 -> context.resources.getString(R.string.ERR02024)
            ERR02025 -> context.resources.getString(R.string.ERR02025)
            ERR02026 -> context.resources.getString(R.string.ERR02026)
            // ERR03 - ModelState Errors
            ERR03001 -> context.resources.getString(R.string.ERR03001)
            ERR03002 -> context.resources.getString(R.string.ERR03002)
            ERR03003 -> context.resources.getString(R.string.ERR03003)
            ERR03004 -> context.resources.getString(R.string.ERR03004)
            else -> ""
        }
    }
}