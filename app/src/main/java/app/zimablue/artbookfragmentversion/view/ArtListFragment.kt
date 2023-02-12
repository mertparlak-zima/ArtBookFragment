package app.zimablue.artbookfragmentversion.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import app.zimablue.artbookfragmentversion.adapter.ArtListAdapter
import app.zimablue.artbookfragmentversion.databinding.FragmentArtListBinding
import app.zimablue.artbookfragmentversion.model.ArtEntity
import app.zimablue.artbookfragmentversion.roomdb.ArtDao
import app.zimablue.artbookfragmentversion.roomdb.ArtDatabase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtListFragment : Fragment() {



    private var binding: FragmentArtListBinding? = null

    private lateinit var artAdapter : ArtListAdapter
    private lateinit var artDao : ArtDao
    private lateinit var artDatabase : ArtDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artDatabase = Room.databaseBuilder(requireContext(), ArtDatabase::class.java, "Arts").build()
        artDao = artDatabase.artDao()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentArtListBinding.inflate(layoutInflater,container,false)
        return (binding!!.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArtFromDB()
    }

    private fun getArtFromDB(){



        lifecycleScope.launch {
            artDao.getArtWithNameAndId().collect(){

                handleResponse(it)



            }
        }
    }

    private fun handleResponse(artList: List<ArtEntity>){


        binding?.artListFragmentRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        artList.let {
            artAdapter = ArtListAdapter(it)

        }
//        artAdapter = ArtListAdapter(artList)
        binding?.artListFragmentRecyclerView?.adapter = artAdapter
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }



}