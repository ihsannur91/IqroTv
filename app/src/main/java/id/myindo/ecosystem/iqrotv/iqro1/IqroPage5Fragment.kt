package id.myindo.ecosystem.iqrotv.iqro1

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.myindo.ecosystem.iqrotv.R
import java.util.ArrayList


class IqroPage5Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var iqraOnePageFifth: HashMap<Int,List<String>>


    interface OnRowClickListener {
        fun onRowClick(row: Int)
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
        val view = inflater.inflate(R.layout.fragment_iqro_page5, container, false)

        rows = listOf(
            view.findViewById(R.id.row1),
            view.findViewById(R.id.row2),
            view.findViewById(R.id.row3),
            view.findViewById(R.id.row4),
            view.findViewById(R.id.row5),
            view.findViewById(R.id.row6),
            view.findViewById(R.id.row7),
            view.findViewById(R.id.row8)
        )

        iqraOnePageFifth = HashMap<Int,List<String>>()
        iqraOnePageFifth[0] = listOf("ha ja")
        iqraOnePageFifth[1] = listOf("sa ha ja","ha a ja")
        iqraOnePageFifth[2] = listOf("sa ha ba","sa ja ha")
        iqraOnePageFifth[3] = listOf("ba ha a","sa ha ja")
        iqraOnePageFifth[4] = listOf("ha a ha","sa a ja")
        iqraOnePageFifth[5] = listOf("ha a ta","ha ba sa")
        iqraOnePageFifth[6] = listOf("ha ha a","ja ja a")
        iqraOnePageFifth[7] = listOf("ha ja sa ta ba a")

        rows.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                listener?.onRowClick(index)
            }
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun getAyatText(index: Int): List<String> {
        Log.d("getAyatText", index.toString())
        if(iqraOnePageFifth.contains(index)) {
            var list = ArrayList<String>()
            for (textView: String in iqraOnePageFifth[index]!!) {
                list.add(textView)
            }
            return list
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("")
    }

    fun highlightRow(row: Int, prevRow: Int, highlight: Boolean) {

        for (row in rows) {
            row.setBackgroundColor(Color.TRANSPARENT)
        }
        rows[row].setBackgroundColor(
            if (highlight) ContextCompat.getColor(requireContext(), R.color.highlight_color)
            else Color.TRANSPARENT
        )
    }
}