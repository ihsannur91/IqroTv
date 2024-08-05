package id.myindo.ecosystem.iqrotv

import androidx.constraintlayout.widget.ConstraintLayout

interface IqroPageFragment {

    fun highlightRow(row: Int, highlight: Boolean)
    val rows: List<ConstraintLayout>

}