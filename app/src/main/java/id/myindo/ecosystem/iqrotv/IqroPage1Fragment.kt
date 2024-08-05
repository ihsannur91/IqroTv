package id.myindo.ecosystem.iqrotv

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.myindo.ecosystem.iqrotv.databinding.FragmentIqroPage1Binding


class IqroPage1Fragment : Fragment() {

    private var listener: OnRowClickListener? = null
    private lateinit var rows: List<ConstraintLayout>

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
        val view = inflater.inflate(R.layout.fragment_iqro_page1, container, false)

        // Set click listeners for each row
        rows = listOf(
            view.findViewById(R.id.row1),
            view.findViewById(R.id.row2),
            view.findViewById(R.id.row3),
            view.findViewById(R.id.row4),
            view.findViewById(R.id.row5),
            view.findViewById(R.id.row6),
            view.findViewById(R.id.row7),
            // Add more rows here
        )

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
            if (highlight) ContextCompat.getColor(requireContext(), R.color.highlight_color)
            else Color.TRANSPARENT
        )
    }

}