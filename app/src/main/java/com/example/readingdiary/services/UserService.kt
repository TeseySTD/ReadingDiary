package com.example.readingdiary.services

import com.example.readingdiary.models.User

class UserService {



    companion object {
        private var _user: User? = null
        private var _isAuthenticated = false
        val user: User?
            get() = _user


        fun registerUser(user: User) {
            if(user.name.isEmpty() || user.password.isEmpty())
                throw IllegalArgumentException("User can`t have null or empty name or password.")
            _user = user
        }

        fun loginUser(user: User) {
            if (_user != null && _user?.name == user.name && _user?.password == user.password)
                _isAuthenticated = true;
            else
                throw IllegalArgumentException("Can`t login unregistered user.")
        }

        fun logoutUser(){
            _isAuthenticated = false
        }

        fun isAuthenticated(user:User):Boolean{
            return _user != null && _user?.name == user.name && _user?.password == user.password && _isAuthenticated
        }
    }
}