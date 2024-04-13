package com.mendelin.githubrepo.domain

import org.junit.Assert.*
import org.junit.Test

class ResourceTest {
    @Test
    fun testSuccess() {
        val resource = Resource.Success(5)
        assertEquals(resource.data, 5)
        assertNull(resource.message)
    }

    @Test
    fun testError() {
        val resource = Resource.Error(null, "Error")
        assertNull(resource.data)
        assertEquals(resource.message, "Error")
    }
}
