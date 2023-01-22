package app.zimablue.artbookfragmentversion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import app.zimablue.artbookfragmentversion.databinding.ActivityMainBinding
import app.zimablue.artbookfragmentversion.view.ArtListFragment
import app.zimablue.artbookfragmentversion.view.ArtListFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.MainActivityFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this,navController)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_art,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.MainActivityFragmentContainerView)
        return navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.addArtItem){
            val action = ArtListFragmentDirections.actionArtListFragmentToDetailArtFragment("new")
            Navigation.findNavController(this,R.id.MainActivityFragmentContainerView).navigate(action)
        }

        return super.onOptionsItemSelected(item)
    }


}