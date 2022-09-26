package controllers

import models.User

class UserStore {

    private val users = ArrayList<User>()
    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0
    private fun getId() = lastId++


    fun findAll(): List<User> {
        return users
    }

    fun create(user: User) {
        user.id = getId()
        users.add(user)
    }

    fun findOne(id: Int): User? {
        return users.find { p -> p.id == id }
    }

    fun delete(user: User?): Boolean {
        return users.remove(user)
    }

    fun update(user: User): Boolean {
        val foundUser = findOne(user.id)
        if (foundUser != null) {
            foundUser.name = user.name
            foundUser.email = user.email
            foundUser.gender = user.gender
            foundUser.height = user.height
            foundUser.weight = user.weight
            return true
        }
        return false
    }
}
