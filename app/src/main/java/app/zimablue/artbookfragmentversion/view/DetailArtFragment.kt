package app.zimablue.artbookfragmentversion.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import app.zimablue.artbookfragmentversion.R
import app.zimablue.artbookfragmentversion.databinding.FragmentDetailArtBinding
import app.zimablue.artbookfragmentversion.model.ArtEntity
import app.zimablue.artbookfragmentversion.roomdb.ArtDao
import app.zimablue.artbookfragmentversion.roomdb.ArtDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException


class DetailArtFragment : Fragment() {



    private lateinit var binding: FragmentDetailArtBinding

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var artDatabase : ArtDatabase
    private lateinit var artDao : ArtDao

    var selectedPictureUri : Uri? = null
    var selectedBitmap : Bitmap? = null

    private lateinit var artFromMain : ArtEntity



    override fun onCreate(savedInstanceState: Bundle?) {

        registerLauncher()

        artDatabase = Room.databaseBuilder(requireContext(),ArtDatabase::class.java,"Arts").build()

        artDao = artDatabase.artDao()


        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailArtBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {





        binding.saveButton.setOnClickListener {
            save(view)
        }
        binding.imageView.setOnClickListener {
            selectImage(view)
        }
        binding.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                delete(it)
            }
        }


        arguments?.let { it ->
            val info = DetailArtFragmentArgs.fromBundle(it).info
            if (info == "new") {
                //NEW
                binding.artText.setText("")
                binding.artistText.setText("")
                binding.yearText.setText("")
                binding.saveButton.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.GONE

                val selectedImageBackground = BitmapFactory.decodeResource(context?.resources,
                    R.drawable.selectimage
                )
                binding.imageView.setImageBitmap(selectedImageBackground)

            } else {
                //OLD
                binding.saveButton.visibility = View.GONE
                binding.deleteButton.visibility = View.VISIBLE

                val selectedId = DetailArtFragmentArgs.fromBundle(it).id

                lifecycleScope.launch{


                    artDao.getArtById(selectedId).collect(){artEntity ->

                        if (artEntity != null){
                            oldArtDetails(artEntity)

                        }


                    }
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun oldArtDetails(art : ArtEntity){



        artFromMain = art




        art.artName.let {
            binding.artText.setText(it)
        }
        art.artistName.let {
            binding.artistText.setText(it)
        }
        art.year.let {
            binding.yearText.setText(it)
        }
        art.image.let {
            val bitmap = it.let {
                BitmapFactory.decodeByteArray(it,0, it.size) }
            binding.imageView.setImageBitmap(bitmap)
        }


    }


    fun delete(view: View) {

        artFromMain.let {
            lifecycleScope.launch{

                artDao.delete(it)
                goMainScreenAfterDelete()

            }
        }

    }


    private fun makeSmallerBitmap(image: Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio: Double = width.toDouble() / height.toDouble()

        if(bitmapRatio > 1){
            //landscape
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        }else{
            //portrait
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }




    fun selectImage(view: View) {

        activity?.let {
            if(ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }

        }

    }

    private fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPictureUri = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedPictureUri!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedPictureUri)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(requireContext(), "Permission needed!", Toast.LENGTH_LONG).show()

            }
        }
    }
    fun save(view: View) {

        val artName = binding.artText.text.toString()
        val artistName = binding.artistText.text.toString()
        val year = binding.yearText.text.toString()

        if (selectedBitmap != null){

            val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)
            val outputStream = ByteArrayOutputStream()

            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            val art = ArtEntity(artName = artName, artistName = artistName, year = year, image = byteArray)

            try {

                lifecycleScope.launch{
                    artDao.insert(art)

                    goMainScreenAfterSaved()

                }


            }catch (e: Exception){
                e.printStackTrace()
            }

        }


    }

    private fun goMainScreenAfterSaved(){
        val action = DetailArtFragmentDirections.actionDetailArtFragmentToArtListFragment()
        Navigation.findNavController(requireView()).navigate(action)
        Toast.makeText(requireContext(),"Successfully saved!",Toast.LENGTH_SHORT).show()
    }

    private fun goMainScreenAfterDelete(){
        val action = DetailArtFragmentDirections.actionDetailArtFragmentToArtListFragment()
        Navigation.findNavController(requireView()).navigate(action)
        Toast.makeText(requireContext(),"Successfully deleted!",Toast.LENGTH_SHORT).show()
    }



}