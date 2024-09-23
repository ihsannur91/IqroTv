package id.myindo.ecosystem.iqrotv.utils

import android.os.Bundle
import android.view.View
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter

class ListFragment : RowsSupportFragment() {

    private var itemSelectedListener: ((DataModel.Result.Detail) -> Unit)? = null
    private var itemClickedListener: ((DataModel.Result.Detail) -> Unit)? = null
    private var rootAdapter: ArrayObjectAdapter =
        ArrayObjectAdapter(ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickedListener()


        // fill data dummy
        bindData(createDummyData())
    }

    private fun createDummyData(): DataModel {
        val dummyDetails = listOf(
            DataModel.Result.Detail(
                title = "IQRO 1",
                overview = "Deskripsi Iqro 1",
                backdrop_path = "iqra1",
                poster_path = "iqra1"
            ),
            DataModel.Result.Detail(
                title = "IQRO 2",
                overview = "Deskripsi Iqro 2",
                backdrop_path = "iqra2",
                poster_path = "iqra2"
            ),
            DataModel.Result.Detail(
                title = "IQRO 3",
                overview = "Deskripsi Iqro 3",
                backdrop_path = "iqra3",
                poster_path = "iqra3"
            ), DataModel.Result.Detail(
                title = "IQRO 4",
                overview = "Deskripsi Iqro 4",
                backdrop_path = "iqra4",
                poster_path = "iqra4"
            ),
            DataModel.Result.Detail(
                title = "IQRO 5",
                overview = "Deskripsi Iqro 5",
                backdrop_path = "iqra5",
                poster_path = "iqra5"
            ),
            DataModel.Result.Detail(
                title = "IQRO 6",
                overview = "Deskripsi Iqro 6",
                backdrop_path = "iqra6",
                poster_path = "iqra6"
            )
        )

        val dummyResult = DataModel.Result(
            title = "IQRO",
            details = dummyDetails
        )

        return DataModel(result = listOf(dummyResult))
    }

    fun bindData(dataList: DataModel) {
        dataList.result.forEachIndexed { _, result ->
            val arrayObjectAdapter = ArrayObjectAdapter(ItemPresenter())

            result.details.forEach {
                arrayObjectAdapter.add(it)
            }

            val headerItem = HeaderItem(result.title)
            val listRow = ListRow(headerItem, arrayObjectAdapter)
            rootAdapter.add(listRow)
        }
    }

    fun setOnContentSelectedListener(listener: (DataModel.Result.Detail) -> Unit) {
        this.itemSelectedListener = listener
    }

    fun setOnContentClickedListener(listener: (DataModel.Result.Detail) -> Unit) {
        this.itemClickedListener = listener
    }

    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is DataModel.Result.Detail) {
                itemSelectedListener?.invoke(item)
            }
        }
    }

    inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is DataModel.Result.Detail) {
                itemClickedListener?.invoke(item)
            }
        }
    }

}