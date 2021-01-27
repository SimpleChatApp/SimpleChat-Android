package com.kagan.chatapp.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.kagan.chatapp.models.UserVM
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.protocol.User
import org.json.JSONException
import java.lang.reflect.Type
import javax.inject.Inject

class ParseJsonToVM
@Inject
constructor() : ImplParseJsonToVM {

    override fun <T : Any> parseJsonToVM(body: String, types: Type, gson: Gson): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                types
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }

    override fun <T : Any> parseJsonToVM(
        body: String,
        clazz: Class<T>,
        gson: Gson
    ): T {
        var parse: T? = null
        try {
            parse = gson.fromJson(
                body,
                clazz
            )
        } catch (e: JsonSyntaxException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        } catch (e: JSONException) {
            Sentry.captureMessage(e.toString(), SentryLevel.ERROR)
        }
        return parse!!
    }
}