package com.dhy.adapterx.demo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.HORIZONTAL
import com.dhy.adapterx.AdapterX
import com.dhy.adapterx.IViewHolder
import kotlinx.android.synthetic.main.activity_grid.*

class GridActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        updateSpan(4)
        update(50)
        rv.addItemDecoration(MyItemDecoration(true))
        rowLabel.addItemDecoration(MyItemDecoration())
    }

    private fun update(max: Int) {
        rv.adapter = AdapterX(this, GridHolder::class, (1..max).toList(), this)
    }

    private fun updateSpan(span: Int) {
        rowLabel.layoutManager = StaggeredGridLayoutManager(span, HORIZONTAL)
        rowLabel.adapter = AdapterX(this, GridHolder::class, (1..span).toList(), this)

        rv.layoutManager = StaggeredGridLayoutManager(span, HORIZONTAL)
    }

    private inner class GridHolder(v: View) : IViewHolder<Int>(v, R.layout.grid_item) {
        private val tv = itemView as TextView

        init {
            itemView.setOnClickListener {
                val span = tv.text.toString().toInt()
                updateSpan(span)
            }
            itemView.setOnLongClickListener {
                val max = tv.text.toString().toInt()
                update(max)
                true
            }
        }

        override fun update(data: Int, position: Int) {
            tv.text = data.toString()
        }
    }

    /**
     *  https://www.jianshu.com/p/9b6135f21c2b GridLayoutManager 添加分割线
     *  https://www.jianshu.com/p/566f866392e4 分割线onDraw的用法
     * */
    class MyItemDecoration(private val noLeftDivider: Boolean = false) : RecyclerView.ItemDecoration() {
        private val paint = Paint()
        private val space = 20
        private val half = space / 2
        private val rect = Rect()

        init {
            paint.color = Color.BLACK
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val manager = parent.layoutManager as StaggeredGridLayoutManager
            val span = manager.spanCount
            val position = parent.getChildAdapterPosition(view)

            outRect.left = if (position < span) (if (noLeftDivider) 0 else space) else half
            outRect.top = if (position % span == 0) space else half
            outRect.right = if (position / span == parent.lastColumn(span)) space else half
            outRect.bottom = if (position % span == span - 1) space else half
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            for (i in 0 until parent.childCount) {
                val view = parent.getChildAt(i)
                parent.getDecoratedBoundsWithMargins(view, rect)
                c.drawRect(rect, paint)
            }
        }

        private fun RecyclerView.lastColumn(span: Int): Int {
            val total = adapter!!.itemCount
            return (total / span) - 1
        }
    }
}