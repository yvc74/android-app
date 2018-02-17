package mx.ucargo.android.orderlist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import mx.ucargo.android.R


abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#606060")
    private val paint = makePaint()


    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2

        c!!.drawText("NO ME", (itemView.right - 270).toFloat(), (itemView.top + (itemHeight-100) / 2).toFloat(), paint)
        c!!.drawText("INTERESA", (itemView.right - 300).toFloat(), (itemView.top + (itemHeight-20) / 2).toFloat(), paint)
        c!!.drawText("ELIMINAR", (itemView.right - 300).toFloat(), (itemView.top + (itemHeight+150) / 2).toFloat(), paint)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun makePaint() : Paint{
        val paint = Paint()
        paint.setTextSize(40F)
        paint.setColor(Color.WHITE)
        paint.setFakeBoldText(true);
        return paint
    }
}
