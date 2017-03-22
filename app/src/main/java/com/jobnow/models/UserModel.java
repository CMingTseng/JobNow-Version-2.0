package com.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SANG on 8/21/2016.
 */
public class UserModel implements Serializable {

    public int id;
    @SerializedName("Username")
    public String username;

    @SerializedName("Email")
    public String email;

    @SerializedName("Fullname")
    public String fullname;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("IsCompany")
    public int isCompany;

    @SerializedName("CreateDate")
    public String createDate;

    @SerializedName("IsEmailConfirmed")
    public int isEmailConfirmed;

    @SerializedName("PasswordSalt")
    public String passwordSalt;

    @SerializedName("fb_id")
    public String fb_id;

    @SerializedName("google_id")
    public String google_id;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    @SerializedName("ApiToken")
    public String apiToken;

    @SerializedName("Avatar")
    public String avatar;

    @SerializedName("BirthDay")
    public String birthDay;

    @SerializedName("PhoneNumber")
    public String phoneNumber;

    @SerializedName("CountryID")
    public String countryID;

    @SerializedName("Gender")
    public Integer gender;

    @SerializedName("CurriculumVitae")
    public String curriculumVitae;

    @SerializedName("Description")
    public String description;

    @SerializedName("CountryName")
    public String countryName;

    @SerializedName("PostalCode")
    public String postalCode;

    @SerializedName("CreditNumber")
    public float creditNumber;

    @SerializedName("TokenFirebase")
    public String tokenFirebase;

    public UserModel() {
    }

    public UserModel(int id, String username, String email, String fullname, String fullName, int isCompany, String createDate, int isEmailConfirmed, String passwordSalt, String fb_id, String google_id, String created_at, String updated_at, String apiToken, String avatar, String birthDay, String phoneNumber, String countryID, Integer gender, String curriculumVitae, String description, String countryName, String postalCode, float creditNumber, String tokenFirebase) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.fullName = fullName;
        this.isCompany = isCompany;
        this.createDate = createDate;
        this.isEmailConfirmed = isEmailConfirmed;
        this.passwordSalt = passwordSalt;
        this.fb_id = fb_id;
        this.google_id = google_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.apiToken = apiToken;
        this.avatar = avatar;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.countryID = countryID;
        this.gender = gender;
        this.curriculumVitae = curriculumVitae;
        this.description = description;
        this.countryName = countryName;
        this.postalCode = postalCode;
        this.creditNumber = creditNumber;
        this.tokenFirebase = tokenFirebase;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(float creditNumber) {
        this.creditNumber = creditNumber;
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(int isCompany) {
        this.isCompany = isCompany;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getIsEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setIsEmailConfirmed(int isEmailConfirmed) {
        this.isEmailConfirmed = isEmailConfirmed;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(String curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
