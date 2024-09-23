package id.myindo.ecosystem.iqrotv.iqro4

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.myindo.ecosystem.iqrotv.R
import java.util.ArrayList

class Iqro4Page1Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var iqraOnePageOne: HashMap<Int,List<String>>

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
        val view = inflater.inflate(R.layout.fragment_iqro4_page1, container, false)

        rows = listOf(
            view.findViewById(R.id.row2),
            view.findViewById(R.id.row3),
            view.findViewById(R.id.row4),
            view.findViewById(R.id.row5),
            view.findViewById(R.id.row6),
            view.findViewById(R.id.row7),
        )

        iqraOnePageOne = HashMap()
        iqraOnePageOne[0] = listOf("a a","a ba")
        iqraOnePageOne[1] = listOf("ba a ba","a ba a")
        iqraOnePageOne[2] = listOf("ba a a","a a ba")
        iqraOnePageOne[3] = listOf("ba ba a","a ba ba")
        iqraOnePageOne[4] = listOf("ba a ba","a ba a ")
        iqraOnePageOne[5] = listOf(" a a a","ba ba ba")
        iqraOnePageOne[6] = listOf("a ba","a ba","a ba")


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

    fun highlightRow(row: Int, prevRow: Int, highlight: Boolean) {
        Log.d("highlightRow",row.toString())
        Log.d("rows.size",rows.size.toString())

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
        if(iqraOnePageOne.contains(index)) {
            val list = ArrayList<String>()
            for (textView: String in iqraOnePageOne[index]!!) {
                list.add(textView)
            }
            return list
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("")
    }

}