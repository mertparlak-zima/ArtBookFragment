package app.zimablue.artbookfragmentversion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import app.zimablue.artbookfragmentversion.databinding.ItemRowBinding
import app.zimablue.artbookfragmentversion.model.ArtEntity
import app.zimablue.artbookfragmentversion.view.ArtListFragmentDirections

class ArtListAdapter(val artList : List<ArtEntity>): RecyclerView.Adapter<ArtListAdapter.ArtHolder>() {

    class ArtHolder(val binding: ItemRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }

    override fun getItemCount(): Int {
        return artList.let {
            artList.size
        }
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        artList[position].artName.let {
            holder.binding.artNameText.text = it
        }
//        holder.binding.artNameText.text = artList[position].artName
        holder.itemView.setOnClickListener {
            val action = ArtListFragmentDirections.actionArtListFragmentToDetailArtFragment(artList[position].id,"old")
            Navigation.findNavController(it).navigate(action)
        }
    }
}