package id.myindo.ecosystem.iqrotv.iqro1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import id.myindo.ecosystem.iqrotv.R
import java.util.ArrayList


class IqroPage3Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>
    private lateinit var ayatTexts: List<TextView>
    private lateinit var iqraOnePageThree: HashMap<Int,List<TextView>>

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

        iqraOnePageThree = HashMap<Int,List<TextView>>()
        iqraOnePageThree[0] = listOf(view.findViewById(R.id.ayat1))
        iqraOnePageThree[1] = listOf(view.findViewById(R.id.ayat2),view.findViewById(R.id.ayat3))
        iqraOnePageThree[2] = listOf(view.findViewById(R.id.ayat4),view.findViewById(R.id.ayat5))
        iqraOnePageThree[3] = listOf(view.findViewById(R.id.ayat6),view.findViewById(R.id.ayat7))
        iqraOnePageThree[4] = listOf(view.findViewById(R.id.ayat8),view.findViewById(R.id.ayat9))
        iqraOnePageThree[5] = listOf(view.findViewById(R.id.ayat10),view.findViewById(R.id.ayat11))
        iqraOnePageThree[6] = listOf(view.findViewById(R.id.ayat12),view.findViewById(R.id.ayat13))
        iqraOnePageThree[7] = listOf(view.findViewById(R.id.ayat14),view.findViewById(R.id.ayat15))


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

    fun highlightRow(row: Int, highlight: Boolean) {
        rows[row].setBackgroundColor(
            if (highlight) resources.getColor(R.color.highlight_color)
            else resources.getColor(android.R.color.transparent)
        )
    }

    fun getAyatText(index: Int): List<String> {
        Log.d("getAyatText", index.toString())
        if(iqraOnePageThree.contains(index)) {
            var list = ArrayList<String>()
            for (textView: TextView in iqraOnePageThree[index]!!) {
                list.add(textView.text.toString());
            }
            return list;
        }
//        val ayat1 = ayatTexts.getOrNull(index)?.text?.toString() ?: ""
//        val ayat2 = ayatTexts.getOrNull(index + 1)?.text?.toString() ?: ""
        return listOf("");
    }


}