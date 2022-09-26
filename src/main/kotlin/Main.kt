import controllers.UserStore
import models.User
import mu.KotlinLogging
import utils.Conversion
import utils.Conversion.convertKGtoPounds
import utils.Conversion.convertMetresToInches
import utils.isValidInRange
import utils.readValidEmail
import utils.validGender

val userStore = UserStore()
private val logger = KotlinLogging.logger {}
/**

 */
fun main() {
    logger.info { "Welcome to Health Tracker" }

    //Some Temporary Test Data
    userStore.create((User(1, "Homer Simpson", "homer@simpson.com", 178.0, 2.0f, 'M')))
    userStore.create((User(2, "Marge Simpson", "marge@simpson.com", 140.0, 1.6f, 'F')))
    userStore.create((User(3, "Lisa Simpson", "lisa@simpson.com", 100.0, 1.2f, 'F')))
    userStore.create((User(4, "Bart Simpson", "bart@simpson.com", 80.0, 1.0f, 'M')))
    userStore.create((User(5, "Maggie Simpson", "maggie@simpson.com", 50.0, 0.7f, 'F')))

    //TODO fix this interoperability issue
    //  convertMetresToInches(7.0, 2.0)

    runApp()
}

/**
 * Perpetually calls the main menu and executes the relevant code block based on user selection
 */
fun runApp(){
    var input: Int
    do {
        input = menu()
        when(input) {
            1 -> addUser()
            2 -> listUsers()
            3 -> searchById()
            4 -> deleteUser()
            5 -> updateUser()
            6 -> searchByGender()
            7 -> usersReport()
            8 -> usersImperial()
            0 -> println("Bye...")
            else -> print("Invalid Option")
        }
    } while (input != 0)
}

/**
 * Displays the menu and returns the [user selection]
 */
fun menu(): Int{
    print("""
        |Main Menu:
        |  1. Add User
        |  2. List User
        |  3. Search by Id
        |  4. Delete by Id
        |  5. Update by Id
        |  6. Search by Gender
        |  7. Users Report
        |  8. Users Imperial
        |  0. Exit
        |Please enter your option: """.trimMargin())
    return readLine()?.toIntOrNull() ?: -1
}

/**
 * Requests details from console and adds the user
 */
fun addUser(){
    userStore.create(getUserDetails())
}

/**
 * Displays the user to the console
 */
fun listUsers(){
    println("The user details are:")
    userStore.findAll()
        .sortedBy { it.name }
        .forEach{println(it)}
}

fun deleteUser(){
    if (userStore.delete(getUserById()))
        println ("User deleted")
    else
        logger.info{"Delete - no user found"}
}

fun updateUser() {
    listUsers()
    val foundUser = getUserById()

    if(foundUser != null) {
        val user = getUserDetails()  //TODO replaced with this call
        user.id = foundUser.id
        if (userStore.update(user))
            println("User updated")
        else
            println("User not updated")
    }
    else
        logger.info{"Update - no user found"}
}
fun getUserById() : User?{
    print("Enter the id of the user: ")
    return  userStore.findOne(readLine()?.toIntOrNull() ?: -1)
}

fun searchById() {
    val user = getUserById()
    if (user == null)
        logger.info{"Search - no user found"}
    else
        println(user)
}

fun searchByGender() {
    print ("Search by Gender (M/F/O): ")
    val gender = validGender(readLine()!!.getOrNull(0) ?: ' ')
    userStore.findAll()
        .filter { it.gender.equals(gender, ignoreCase = true) }
        .sortedBy { it.name }
        .forEach{println(it)}
}


fun usersReport() {

    val users = userStore.findAll()

    println("""
        |------------------------
        |     USERS REPORT
        |------------------------
        |
        |  Number of Users:  ${users.size}
        |  Gender Breakdown: ${users.groupingBy{it.gender}.eachCount()}
        |  Average Weight:   ${users.sumOf { it.weight }.div(users.size)} kg
        |  Min Weight:       ${users.minByOrNull { it.weight }?.weight} kg
        |  Max Weight:       ${users.maxByOrNull { it.weight }?.weight} kg
        |  Average Height:   ${Conversion.round(users.sumOf { it.height.toDouble() }.div(users.size), 2.0)} metres
        |  Min Height:       ${users.minByOrNull { it.height }?.height} metres
        |  Max Height:       ${users.maxByOrNull { it.height }?.height} metres
        |
        |------------------------
        |""".trimMargin())
}

fun usersImperial() {
    println("The user details (imperial) are:")
    userStore.findAll()
        .sortedBy { it.name }
        .forEach{println("  ${it.name} (${it.email}) ${convertKGtoPounds(it.weight, 1.0)} pounds, ${convertMetresToInches(it.height.toDouble(), 1.0)} inches.")}
}
private fun getUserDetails() : User{
    val user = User()

    println("Please enter the following for the user:")
    print("    Name: ")
    user.name = readLine()!!

    //Validation approach: user is asked repeatedly to enter the correct email format
    user.email = readValidEmail()

    //Validation approach: user is asked once to enter a value in a range.  Default is provided if outside of range.
    print ("    Height (meters): ")
    val height = readLine()?.toFloatOrNull() ?: 0f
    if (isValidInRange(0.5,3.0, height.toDouble()))
        user.height = height
    else
        user.height = 0f

    //Validation approach: user is asked once to enter a value in a range.  Default is provided if outside of range.
    print ("    Weight (kg): ")
    val weight = readLine()?.toDoubleOrNull() ?: .0
    if (isValidInRange(25.0,500.0, weight))
        user.weight = weight
    else
        user.weight = 0.0

    //Validation approach: user is asked once to enter the gender, if they don't
    // enter a valid value, a ' '  is defaulted.
    print ("    Gender (M/F/O): ")
    user.gender = validGender(readLine()!!.getOrNull(0) ?: ' ')

    return user
}