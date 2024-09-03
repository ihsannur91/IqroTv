package id.myindo.ecosystem.iqrotv.iqro6

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

class Iqro6Page3Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var ayatTexts: List<TextView>
    private lateinit var iqraOnePageOne: HashMap<Int,List<TextView>>


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
        val view = inflater.inflate(R.layout.fragment_iqro6_page3, container, false)

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

        iqraOnePageOne = HashMap<Int,List<TextView>>()
        iqraOnePageOne[0] = listOf(view.findViewById(R.id.ayat1))
        iqraOnePageOne[1] = listOf(view.findViewById(R.id.ayat2))
        iqraOnePageOne[2] = listOf(view.findViewById(R.id.ayat3))
        iqraOnePageOne[3] = listOf(view.findViewById(R.id.ayat4))
        iqraOnePageOne[4] = listOf(view.findViewById(R.id.ayat5))
        iqraOnePageOne[5] = listOf(view.findViewById(R.id.ayat6))
        iqraOnePageOne[6] = listOf(view.findViewById(R.id.ayat7))
        iqraOnePageOne[7] = listOf(view.findViewById(R.id.ayat8))


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
            var list = ArrayList<String>()
            for (textView: TextView in iqraOnePageOne[index]!!) {
                list.add(textView.text.toString());
            }
            return list;
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("");
    }
}