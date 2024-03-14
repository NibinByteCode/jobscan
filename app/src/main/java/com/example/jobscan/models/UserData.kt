package com.example.jobscan.models

import java.time.LocalDate

data class UserData(
    var userId: String = "",
    var userType: String = "",
    var firstName: String = "",
    var lastName: String = "",
//    var dateOfBirth: LocalDate = LocalDate.now(), // Using current date as default
    var dateOfBirth: String = "",
    var profileImage: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var companyName: String = "",
    var designation: String = "",
    var educationQualification: String = "",
    var endOfGraduationDate: LocalDate = LocalDate.now(), // Using current date as default
    var connections: List<String> = listOf(),
    var postIds: List<String> = listOf(),
    var connectionCount: Int=0
){}