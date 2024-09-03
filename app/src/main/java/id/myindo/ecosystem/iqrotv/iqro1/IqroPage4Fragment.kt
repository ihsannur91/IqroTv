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


class IqroPage4Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>

    private lateinit var iqraOnePageFour: HashMap<Int,List<String>>

    interface OnRowClickListener {
        fun onRowClick(row: Int)
        fun getAyatText(index: Int): List<String>

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
        val view = inflater.inflate(R.layout.fragment_iqro_page4, container, false)

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

        iqraOnePageFour = HashMap<Int,List<String>>()
        iqraOnePageFour[0] = listOf("ja")
        iqraOnePageFour[1] = listOf("ja a sa","ja ja a")
        iqraOnePageFour[2] = listOf("ja ta sa","ja ta a")
        iqraOnePageFour[3] = listOf("ja a sa","sa ja ba")
        iqraOnePageFour[4] = listOf("sa a ja","ba a ja")
        iqraOnePageFour[5] = listOf("ja ja sa","ja a sa")
        iqraOnePageFour[6] = listOf("sa sa ja","ja a ja")
        iqraOnePageFour[7] = listOf("ja sa ta ba a")



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

    fun highlightRow(row: Int, prevRow: Int, highlight: Boolean) {

        for (row in rows) {
            row.setBackgroundColor(Color.TRANSPARENT)
        }
        rows[row].setBackgroundColor(
            if (highlight) ContextCompat.getColor(requireContext(), R.color.highlight_color)
            else Color.TRANSPARENT
        )
    }

    fun getAyatText(index: Int): List<String> {
        Log.d("getAyatText", index.toString())
        if(iqraOnePageFour.contains(index)) {
            var list = ArrayList<String>()
            for (textView: String in iqraOnePageFour[index]!!) {
                list.add(textView)
            }
            return list
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("")
    }

}