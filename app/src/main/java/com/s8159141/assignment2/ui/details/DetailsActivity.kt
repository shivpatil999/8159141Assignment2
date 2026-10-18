package com.s8159141.assignment2.ui.details

import android.os.Build
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.s8159141.assignment2.R
import com.s8159141.assignment2.data.model.Entity
import com.s8159141.assignment2.databinding.ActivityDetailsBinding
import com.s8159141.assignment2.databinding.ItemDetailRowBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Screen 3 - Details.
 *
 * Shows every field of the selected [Entity], including the full description. The entity
 * is received as a Parcelable extra from the Dashboard screen. Each fact is shown with a
 * reusable labelled row (icon + label + value).
 */
@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val entity = getEntityExtra()
        if (entity == null) {
            finish()
            return
        }
        bind(entity)
    }

    private fun bind(entity: Entity) {
        binding.collapsingToolbar.title = entity.name

        row(binding.rowArchitect, R.drawable.ic_person_outline, R.string.label_architect, entity.architect)
        row(binding.rowLocation, R.drawable.ic_location, R.string.label_location, entity.location)
        row(binding.rowYear, R.drawable.ic_calendar, R.string.label_year, entity.yearCompleted.toString())
        row(binding.rowStyle, R.drawable.ic_style, R.string.label_style, entity.style)
        row(binding.rowHeight, R.drawable.ic_height, R.string.label_height, getString(R.string.height_metres, entity.height))

        binding.tvDescription.text = entity.description
    }

    /** Fills one labelled row (icon, label, value). */
    private fun row(
        rowBinding: ItemDetailRowBinding,
        @DrawableRes icon: Int,
        @StringRes label: Int,
        value: String
    ) {
        rowBinding.imgIcon.setImageResource(icon)
        rowBinding.tvLabel.setText(label)
        rowBinding.tvValue.text = value
    }

    /** Reads the Parcelable extra using the API that matches the device's SDK level. */
    private fun getEntityExtra(): Entity? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ENTITY, Entity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ENTITY)
        }

    companion object {
        const val EXTRA_ENTITY = "extra_entity"
    }
}
