package com.kagan.chatapp.utils

import com.google.gson.Gson
import java.lang.reflect.Type

interface ImplParseJsonToVM {
    fun <T : Any> parseJsonToVM(body: String, types: Type, gson: Gson): T
    fun <T : Any> parseJsonToVM(body: String, clazz: Class<T>, gson: Gson): T
}