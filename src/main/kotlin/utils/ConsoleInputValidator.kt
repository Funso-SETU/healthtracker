package utils


fun readValidEmail(): String {
    var email = ""
    do {
        print("    Email: ")
        email = readLine()!!
        when {
            isValidEmail(email) -> return email
            else -> print ("       Invalid Email format.  Try again.")
        }
    }while (true)
}