package id.myindo.ecosystem.iqrotv.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Ayat(
    val number:String,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val audioFileName: String
):Parcelable
