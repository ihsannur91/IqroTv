package id.myindo.ecosystem.iqrotv

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


class Iqro6Page1Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var ayatTexts: List<TextView>

    interface OnRowClickListener {
        fun onRowClick(row: Int)
        fun getAyatText(index: Int): Pair<String, String>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRowClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnRowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_iqro6_page1, container, false)

        rows = listOf(
            view.findViewById(R.id.row1),
            view.findViewById(R.id.row2),
            view.findViewById(R.id.row3),
            view.findViewById(R.id.row4),
            view.findViewById(R.id.row5),
            view.findViewById(R.id.row6),
            view.findViewById(R.id.row7)
        )

        ayatTexts = listOf(
            view.findViewById(R.id.ayat1),
            view.findViewById(R.id.ayat2),
            view.findViewById(R.id.ayat3),
            view.findViewById(R.id.ayat4),
            view.findViewById(R.id.ayat5),
            view.findViewById(R.id.ayat6),
            view.findViewById(R.id.ayat7),
            view.findViewById(R.id.ayat8),
            view.findViewById(R.id.ayat9),
            view.findViewById(R.id.ayat10),
            view.findViewById(R.id.ayat11),
            view.findViewById(R.id.ayat12)
        )

        rows.forEachIndexed { index, row ->
            row.setOnClickListener {
                listener?.onRowClick(index)
            }
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun highlightRow(row: Int, highlight: Boolean) {
        rows[row].setBackgroundColor(
            if (highlight) ContextCompat.getColor(requireContext(), R.color.highlight_color)
            else Color.TRANSPARENT
        )
    }

    fun getAyatText(index: Int): Pair<String, String> {
        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return Pair(ayat1, ayat2)
    }
}