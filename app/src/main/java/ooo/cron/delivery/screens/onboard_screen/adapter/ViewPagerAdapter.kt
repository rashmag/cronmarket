package ooo.cron.delivery.screens.onboard_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ooo.cron.delivery.databinding.OnboardItemBinding
import ooo.cron.delivery.data.network.models.OnboardingModel


class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder>() {
    private var listOnboard:ArrayList<OnboardingModel> = arrayListOf()

    fun setData(listOnboardModel: ArrayList<OnboardingModel>){
        listOnboard = listOnboardModel
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(OnboardItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,false))

    override fun getItemCount(): Int = listOnboard.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listOnboard[position])
    }
    inner class MyViewHolder(val binding: OnboardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: OnboardingModel) {
            with(binding) {
                subTitleOnboard.text = model.subTitle
                titleOnboard.text = model.title
                backOnboard.background = AppCompatResources
                    .getDrawable(root.context,model.back)
                Glide.with(root)
                    .load(model.image)
                    .into(imgOnboard)
            }
        }
    }
}

