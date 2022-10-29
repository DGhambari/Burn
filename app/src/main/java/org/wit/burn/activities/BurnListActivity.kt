package org.wit.burn.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.burn.adapters.BurnAdapter
import org.wit.burn.adapters.BurnListener
import org.wit.burn.main.MainApp
import org.wit.burn.models.BurnModel
import org.wit.burn.R
import org.wit.burn.databinding.ActivityBurnListBinding

class BurnListActivity : AppCompatActivity(), BurnListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityBurnListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBurnListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = BurnAdapter(app.burns.findAll(),this)
        loadBurns()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, BurnActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBurnClick(burn: BurnModel) {
        val launcherIntent = Intent(this, BurnActivity::class.java)
        launcherIntent.putExtra("burn_edit", burn)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { binding.recyclerView.adapter?.notifyDataSetChanged() }
            { loadBurns() }
    }

    private fun loadBurns() {
        showBurns(app.burns.findAll())
    }

    fun showBurns (burns: List<BurnModel>) {
        binding.recyclerView.adapter = BurnAdapter(burns, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}