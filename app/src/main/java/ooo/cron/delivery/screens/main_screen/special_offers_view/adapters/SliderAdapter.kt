package ooo.cron.delivery.screens.main_screen.special_offers_view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ooo.cron.delivery.databinding.PagerRowBinding
import ooo.cron.delivery.screens.main_screen.special_offers_view.models.SlideModel

class SliderAdapter(private val onClick: (slideModel: SlideModel) -> Unit) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var imageList = arrayListOf<SlideModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SliderViewHolder(
        itemBinding = PagerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount() = imageList.size

    fun setData(list: List<SlideModel>){
        this.imageList.run {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(private val itemBinding: PagerRowBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: SlideModel){
//            itemView.setOnClickListener {
//                onClick(model)
//            }
            Glide.with(itemView.context)
                .load(model.imageUrl)
                .transform(CenterInside(), RoundedCorners(IMAGE_ROUND_CORNER))
                .into(itemBinding.imageView)
        }
    }

    private companion object{
        private const val IMAGE_ROUND_CORNER = 24
    }
}