package ooo.cron.delivery.screens.main_screen.special_offers_view.adapters

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ooo.cron.delivery.R
import ooo.cron.delivery.screens.main_screen.special_offers_view.constants.ActionTypes
import ooo.cron.delivery.screens.main_screen.special_offers_view.constants.ScaleTypes
import ooo.cron.delivery.screens.main_screen.special_offers_view.interfaces.ItemClickListener
import ooo.cron.delivery.screens.main_screen.special_offers_view.interfaces.TouchListener
import ooo.cron.delivery.screens.main_screen.special_offers_view.models.SlideModel

/**
 * Created by Deniz Coşkun on 6/23/2020.
 * denzcoskun@hotmail.com
 * İstanbul
 */
class ViewPagerAdapter(
    context: Context?,
    imageList: List<SlideModel>,
    private var radius: Int,
    private var errorImage: Int,
    private var placeholder: Int,
    private var titleBackground: Int,
    private var scaleType: ScaleTypes?,
    private var textAlign: String
) : PagerAdapter() {

    constructor(
        context: Context,
        imageList: List<SlideModel>,
        radius: Int,
        errorImage: Int,
        placeholder: Int,
        titleBackground: Int,
        textAlign: String
    ) :
            this(
                context,
                imageList,
                radius,
                errorImage,
                placeholder,
                titleBackground,
                null,
                textAlign
            )

    private var imageList: List<SlideModel>? = imageList
    private var layoutInflater: LayoutInflater? =
        context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListener? = null
    private var touchListener: TouchListener? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return imageList!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val itemView = layoutInflater!!.inflate(R.layout.pager_row, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linear_layout)
        val textView = itemView.findViewById<TextView>(R.id.text_view)

        linearLayout.visibility = View.INVISIBLE

        // Image from url or local path check.
        if (imageList!![position].imageUrl == null) {
            Glide.with(itemView).load(imageList!![position].imagePath!!)
        } else {
            Glide.with(itemView).load(imageList!![position].imageUrl!!)
        }.transform(CenterCrop(), RoundedCorners(radius))
            .placeholder(placeholder)
            .error(errorImage)
            .into(imageView)

        container.addView(itemView)

        imageView.setOnClickListener { itemClickListener?.onItemSelected(position) }

        if (touchListener != null) {
            imageView!!.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> touchListener!!.onTouched(ActionTypes.MOVE)
                    MotionEvent.ACTION_DOWN -> touchListener!!.onTouched(ActionTypes.DOWN)
                    MotionEvent.ACTION_UP -> touchListener!!.onTouched(ActionTypes.UP)
                }
                false
            }
        }


        return itemView
    }

    override fun getPageWidth(position: Int): Float {
        return 0.8f
    }

    /**
     * Get layout gravity value from textAlign variable
     *
     * @param  textAlign  text align by user
     */
    fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "RIGHT" -> {
                Gravity.RIGHT
            }
            "CENTER" -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.LEFT
            }
        }
    }

    /**
     * Set item click listener
     *
     * @param  itemClickListener  callback by user
     */
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /**
     * Set touch listener for listen to image touch
     *
     * @param  touchListener  interface callback
     */
    fun setTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}