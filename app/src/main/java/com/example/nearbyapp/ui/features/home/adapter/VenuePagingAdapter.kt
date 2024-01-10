
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nearbyapp.data.features.home.local.entity.Venue
import com.example.nearbyapp.databinding.ItemVenueLayoutBinding
import com.example.nearbyapp.utils.viewBinding

class VenuePagingAdapter(
    private val onVenueItemClick: (String) -> Unit,
) : PagingDataAdapter<Venue, VenuePagingAdapter.VenueViewHolder>(
    COMPARATOR,
) {

    class VenueViewHolder(
        private val binding: ItemVenueLayoutBinding,
        private val onVenueItemClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(venue: Venue) {
            binding.apply {
                venueNameTv.isVisible = venue.name.isNotBlank()
                venueAddressTv.isVisible = venue.address.isNotBlank()
                venueCityTv.isVisible = venue.city.isNotBlank()
                venueNameTv.text = venue.name
                venueAddressTv.text = venue.address
                venueCityTv.text = venue.city
            }
            itemView.setOnClickListener {
                if (venue.url.isNotBlank()) {
                    onVenueItemClick(venue.url)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        return VenueViewHolder(
            binding = parent.viewBinding(ItemVenueLayoutBinding::inflate),
            onVenueItemClick = onVenueItemClick,
        )
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venueItem = getItem(position)
        venueItem?.let {
            holder.bind(venueItem)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Venue>() {
            override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
                return oldItem == newItem
            }
        }
    }
}
