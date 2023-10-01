package com.example.bookshelfapp.utils

import java.util.regex.Pattern

object PatternUtils {

    fun isValidPassword(password: String?): Boolean {
        if(password == null) return false
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
