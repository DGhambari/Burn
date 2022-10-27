package burn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import burn.models.BurnModel
import org.wit.burn.databinding.CardViewBinding

interface BurnListener {
    fun onBurnClick(burn: BurnModel)
}

class BurnAdapter constructor(private var burns: List<BurnModel>,
                                   private val listener: BurnListener) :
    RecyclerView.Adapter<BurnAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val burn = burns[holder.adapterPosition]
        holder.bind(burn, listener)
    }

    override fun getItemCount(): Int = burns.size

    class MainHolder(private val binding : CardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(burn: BurnModel, listener: BurnListener) {
            binding.burnTitle.text = burn.title
            binding.description.text = burn.description
            Picasso.get().load(burn.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onBurnClick(burn) }
        }
        }
}
