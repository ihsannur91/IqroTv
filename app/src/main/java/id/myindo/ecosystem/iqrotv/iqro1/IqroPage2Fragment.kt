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

class IqroPage2Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>

    private lateinit var ayatTexts: List<TextView>
    private lateinit var iqraOnePageTwo: HashMap<Int,List<String>>
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
        val view = inflater.inflate(R.layout.fragment_iqro_page2, container, false)

        rows = listOf(
            view.findViewById(R.id.row1),
            view.findViewById(R.id.row2),
            view.findViewById(R.id.row3),
            view.findViewById(R.id.row4),
            view.findViewById(R.id.row5),
            view.findViewById(R.id.row6),
            view.findViewById(R.id.row7),
        )

        iqraOnePageTwo = HashMap<Int,List<String>>()
        iqraOnePageTwo[0] = listOf("ba ta")
        iqraOnePageTwo[1] = listOf("a ta ba","ta ba a")
        iqraOnePageTwo[2] = listOf("ta a ba","a ba ta")
        iqraOnePageTwo[3] = listOf("ba ta a","a ta ba")
        iqraOnePageTwo[4] = listOf("ta a ta","ba a ta")
        iqraOnePageTwo[5] = listOf("a ta ba","ta ta a")
        iqraOnePageTwo[6] = listOf("a ba ta","a ba ta")


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
        if(iqraOnePageTwo.contains(index)) {
            var list = ArrayList<String>()
            for (textView: String in iqraOnePageTwo[index]!!) {
                list.add(textView)
            }
            return list
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("")
    }

}