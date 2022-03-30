package com.example.kotlin_weatherapiapp

data class ResponseData(
    val message: String,
    val user_id: Int,
    val name: String,
    val email: String,
    val mobile: Long,
    //profile_detail 오브젝트
    val profile_details: ProfileDetails,
    //data_list 오브젝트
    val data_list: List<DataListDetail>

)
data class ProfileDetails(
    val is_profile_completed: Boolean,
    val rating: Double

)
data class DataListDetail(
    val id: Int,
    val value: String
)