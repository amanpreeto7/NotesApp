package com.o7solutions.notesapp

/**
 * @Author: 017
 * @Date: 27/06/23
 * @Time: 4:28 pm
 */
data class TestingClass(
    var firstName : String ?= null,
    var lastName: String? = null
){
    override fun toString(): String {
        return "firstName $firstName lastName $lastName"
    }

    fun getFullName() : String{
        return "$firstName $lastName"
    }
}
