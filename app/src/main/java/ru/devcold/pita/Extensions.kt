package ru.devcold.pita

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

fun AppCompatActivity.toast(stringRes: Int) {
    Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context.getSP(): SharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

fun Context.getSPE(): SharedPreferences.Editor = getSP().edit()

val TextInputLayout.text get() = editText!!.text.toString()

//Добавить расширение для интента

