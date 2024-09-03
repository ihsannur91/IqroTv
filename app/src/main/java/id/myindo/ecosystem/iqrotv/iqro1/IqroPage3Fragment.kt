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


class IqroPage3Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var ayatTexts: List<TextView>
    private lateinit var iqraOnePageThree: HashMap<Int,List<String>>

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
        val view = inflater.inflate(R.layout.fragment_iqro_page3, container, false)

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

        iqraOnePageThree = HashMap<Int,List<String>>()
        iqraOnePageThree[0] = listOf("ba ta ta")
        iqraOnePageThree[1] = listOf("sa ba ta","sa a ba")
        iqraOnePageThree[2] = listOf("ba a sa"," ba ta ta")
        iqraOnePageThree[3] = listOf("sa ba sa","a ta ba")
        iqraOnePageThree[4] = listOf("a sa sa","ta ba ta")
        iqraOnePageThree[5] = listOf("ba sa sa","ta ta a")
        iqraOnePageThree[6] = listOf("ba ta sa","sa ba sa")
        iqraOnePageThree[7] = listOf("a ba ta sa","a ba ta sa")


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
        if(iqraOnePageThree.contains(index)) {
            var list = ArrayList<String>()
            for (textView: String in iqraOnePageThree[index]!!) {
                list.add(textView)
            }
            return list
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("")
    }


}