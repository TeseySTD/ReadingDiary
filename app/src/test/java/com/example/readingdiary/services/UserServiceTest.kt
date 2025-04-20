package com.example.readingdiary.services

import com.example.readingdiary.models.User
import org.junit.Assert.*
import org.junit.Test

class UserServiceTest{
    @Test fun whenRegisterUserWithEmptyName_ThenThrowsIllegalArgumentException(){
        val exception = assertThrows(IllegalArgumentException::class.java){
            UserService.registerUser(User("", ""))
        }
        assertEquals("User can`t have null or empty name or password.", exception.message)
    }

    @Test fun whenLoginUserThatNotRegistered_ThenThrowsIllegalArgumentException(){
        val user = User("toxa", "123")
        UserService.registerUser(user)
        val exception = assertThrows(IllegalArgumentException::class.java){
            UserService.loginUser(User("petya","123"))
        }
        assertEquals(exception.message,"Can`t login unregistered user.")
    }
}