package com.s8159141.assignment2

import org.junit.Assert.assertEquals
import org.junit.Test

/** Verifies the summary fields used by the RecyclerView row. */
class EntityTest {

    @Test
    fun `title is the entity name`() {
        assertEquals("Eiffel Tower", sampleEntity("Eiffel Tower").title)
    }

    @Test
    fun `subtitle combines style and location without description`() {
        val subtitle = sampleEntity().subtitle
        assertEquals("Structural Expressionism · Paris, France", subtitle)
    }
}
