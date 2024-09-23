package id.myindo.ecosystem.iqrotv.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.leanback.widget.Presenter
import id.myindo.ecosystem.iqrotv.R

class ItemPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_view, parent, false)

        val params = view.layoutParams
        params.width = getWidthInPercent(parent!!.context, 12)
        params.height = getHeightInPercent(parent.context, 32)

        return ViewHolder(view)
    }

    private fun getWidthInPercent(context: Context, percent: Int): Int {
        val width = context.resources.displayMetrics.widthPixels
        return (width * percent) / 100
    }

    private fun getHeightInPercent(context: Context, percent: Int): Int {
        val height = context.resources.displayMetrics.heightPixels
        return (height * percent) / 100
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val content = item as? DataModel.Result.Detail
        val imageView = viewHolder?.view?.findViewById<ImageView>(R.id.poster_image)

        val resourceId = viewHolder?.view?.context?.resources?.getIdentifier(content?.poster_path, "drawable", viewHolder.view.context.packageName)
        imageView?.setImageResource(resourceId ?: 0)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {}
}