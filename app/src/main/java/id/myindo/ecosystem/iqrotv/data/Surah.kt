package id.myindo.ecosystem.iqrotv.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Surah(
    val name : String,
    val ayatCount : String,
    val desc : String,
    val ayatList: List<Ayat>
):Parcelable
