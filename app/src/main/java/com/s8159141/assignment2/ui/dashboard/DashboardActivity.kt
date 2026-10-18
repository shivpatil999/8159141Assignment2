package com.s8159141.assignment2.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.s8159141.assignment2.databinding.ActivityDashboardBinding
import com.s8159141.assignment2.ui.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Screen 2 - Dashboard.
 *
 * Uses the keypass passed from Login to load the entity list, shows it in a RecyclerView,
 * and opens the Details screen when a row is tapped.
 */
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    private val adapter = EntityAdapter { entity ->
        startActivity(
            Intent(this, DetailsActivity::class.java)
                .putExtra(DetailsActivity.EXTRA_ENTITY, entity)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Load data only on first creation; the ViewModel keeps it across rotation.
        if (savedInstanceState == null) {
            val keypass = intent.getStringExtra(EXTRA_KEYPASS).orEmpty()
            viewModel.loadEntities(keypass)
        }

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility =
                        if (state is DashboardUiState.Loading) View.VISIBLE else View.GONE
                    binding.tvError.visibility =
                        if (state is DashboardUiState.Error) View.VISIBLE else View.GONE

                    when (state) {
                        is DashboardUiState.Success -> {
                            adapter.submitList(state.entities)
                            binding.tvCount.text =
                                resources.getQuantityString(
                                    com.s8159141.assignment2.R.plurals.items_found,
                                    state.entities.size, state.entities.size
                                )
                            binding.tvCount.visibility = View.VISIBLE
                        }
                        is DashboardUiState.Error -> {
                            binding.tvError.text = state.message
                            binding.tvCount.visibility = View.GONE
                        }
                        is DashboardUiState.Loading -> binding.tvCount.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }
}
