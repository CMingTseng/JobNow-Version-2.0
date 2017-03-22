package com.jobnow.models;

import java.io.Serializable;

public class ProfileModel implements Serializable {

    public int id;
    public int CompanyID;
    public String Logo;
    public String Name;
    public String CoverImage;
    public String Overview;
    public String WhyJoinUs;
    public String CompanySizeID;
    public String ContactName;
    public String ContactNumber;
    public String Address;
    public String RegistrationNo;
    public String Website;
    public String WorkingHour;
    public String DressCode;
    public String Benefit;
    public String Spoken;
    public String Latitude;
    public String Longitude;
    public int IsActive;
    public String created_at;
    public String updated_at;
    public int IsPremium;
    public float CreditNumber;
    public ProfileModel() {
    }

    public ProfileModel(int id, int companyID, String logo, String name, String coverImage, String overview, String whyJoinUs, String companySizeID, String contactName, String contactNumber, String address, String registrationNo, String website, String workingHour, String dressCode, String benefit, String spoken, String latitude, String longitude, int isActive, String created_at, String updated_at, int isPremium) {
        this.id = id;
        CompanyID = companyID;
        Logo = logo;
        Name = name;
        CoverImage = coverImage;
        Overview = overview;
        WhyJoinUs = whyJoinUs;
        CompanySizeID = companySizeID;
        ContactName = contactName;
        ContactNumber = contactNumber;
        Address = address;
        RegistrationNo = registrationNo;
        Website = website;
        WorkingHour = workingHour;
        DressCode = dressCode;
        Benefit = benefit;
        Spoken = spoken;
        Latitude = latitude;
        Longitude = longitude;
        IsActive = isActive;
        this.created_at = created_at;
        this.updated_at = updated_at;
        IsPremium = isPremium;
    }

    public float getCreditNumber() {
        return CreditNumber;
    }

    public void setCreditNumber(float creditNumber) {
        CreditNumber = creditNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCoverImage() {
        return CoverImage;
    }

    public void setCoverImage(String coverImage) {
        CoverImage = coverImage;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getWhyJoinUs() {
        return WhyJoinUs;
    }

    public void setWhyJoinUs(String whyJoinUs) {
        WhyJoinUs = whyJoinUs;
    }

    public String getCompanySizeID() {
        return CompanySizeID;
    }

    public void setCompanySizeID(String companySizeID) {
        CompanySizeID = companySizeID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getWorkingHour() {
        return WorkingHour;
    }

    public void setWorkingHour(String workingHour) {
        WorkingHour = workingHour;
    }

    public String getDressCode() {
        return DressCode;
    }

    public void setDressCode(String dressCode) {
        DressCode = dressCode;
    }

    public String getBenefit() {
        return Benefit;
    }

    public void setBenefit(String benefit) {
        Benefit = benefit;
    }

    public String getSpoken() {
        return Spoken;
    }

    public void setSpoken(String spoken) {
        Spoken = spoken;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
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

    public int getIsPremium() {
        return IsPremium;
    }

    public void setIsPremium(int isPremium) {
        IsPremium = isPremium;
    }
}
