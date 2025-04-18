package com.example.readingdiary.services

import com.example.readingdiary.models.User

class UserService {



    companion object {
        private var _user: User? = null
        private var _isAuthenticated = false
        val user: User?
            get() = _user


        fun registerUser(user: User) {
            _user = user
        }

        fun loginUser(user: User) {
            if (_user != null && _user?.name == user.name && _user?.password == user.password)
                _isAuthenticated = true;
        }

        fun isAuthenticated(user:User):Boolean{
            return _user != null && _user?.name == user.name && _user?.password == user.password && _isAuthenticated
        }
    }
}