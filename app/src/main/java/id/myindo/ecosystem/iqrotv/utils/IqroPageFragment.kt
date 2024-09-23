package id.myindo.ecosystem.iqrotv.utils

import androidx.constraintlayout.widget.ConstraintLayout

interface IqroPageFragment {

    fun highlightRow(row: Int, highlight: Boolean)
    val rows: List<ConstraintLayout>

}