package models

/**
 * Data class for the User
 *
 *
 */
data class User (
    var id: Int = -1,
    var name:String = "<no name yet>",
    var email:String = "<no email yet>",
    var weight:Double = 0.0,
    var height:Float = 0.0f,
    var gender:Char= ' '
)