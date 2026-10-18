package com.s8159141.assignment2.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.s8159141.assignment2.databinding.ActivityLoginBinding
import com.s8159141.assignment2.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Screen 1 - Login.
 *
 * Collects the username (student ID) and password (first name), asks [LoginViewModel]
 * to authenticate, and navigates to the Dashboard with the returned keypass on success.
 * Errors from the API are shown inline below the form.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etUsername.text?.toString().orEmpty(),
                binding.etPassword.text?.toString().orEmpty()
            )
        }
        // Hide the error as soon as the user edits a field.
        binding.etUsername.doAfterTextChanged { viewModel.resetState() }
        binding.etPassword.doAfterTextChanged { viewModel.resetState() }

        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility =
                        if (state is LoginUiState.Loading) View.VISIBLE else View.GONE
                    binding.btnLogin.isEnabled = state !is LoginUiState.Loading

                    when (state) {
                        is LoginUiState.Error -> {
                            binding.tvError.text = state.message
                            binding.tvError.visibility = View.VISIBLE
                        }
                        is LoginUiState.Success -> {
                            binding.tvError.visibility = View.GONE
                            navigateToDashboard(state.keypass)
                        }
                        else -> binding.tvError.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun navigateToDashboard(keypass: String) {
        startActivity(
            Intent(this, DashboardActivity::class.java)
                .putExtra(DashboardActivity.EXTRA_KEYPASS, keypass)
        )
        finish()
    }
}
