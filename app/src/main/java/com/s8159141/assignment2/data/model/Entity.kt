package com.s8159141.assignment2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * A single architecture item returned by the dashboard endpoint.
 *
 * The API returns domain-specific keys (name, architect, location, ...). The class is
 * [Parcelable] so a selected entity can be passed to the Details screen through an Intent.
 */
@Parcelize
data class Entity(
    val name: String,
    val architect: String,
    val location: String,
    val yearCompleted: Int,
    val style: String,
    val height: Int,
    val description: String
) : Parcelable {

    /** Short title shown in the RecyclerView row. */
    val title: String get() = name

    /** One-line summary shown under the title (no description, per the spec). */
    val subtitle: String get() = "$style · $location"
}
