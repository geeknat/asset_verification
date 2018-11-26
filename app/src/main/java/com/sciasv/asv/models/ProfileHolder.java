package com.sciasv.asv.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sciasv.asv.activities.LogIn;

/**
 * @author Geek Nat
 * On 9/15/2016.
 */
public class ProfileHolder {
    Context context;
    SharedPreferences userDetails;
    SharedPreferences.Editor editor;
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String USER_LOGGED_IN = "userLoggedIn";
    public static final String USER_ID = "userId";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String PHOTO = "photo";
    public static final String OFFER_SLOTS = "offer_slots";
    public static final String REFERRAL_CODE = "referral_code";
    public static final String NOTIFICATIONS = "notifications";
    public static final String IS_VALID = "isValid";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String BUSINESS_NAME = "businessName";
    public static final String DEPARTMENT_NAME = "departmentName";


    public ProfileHolder(Context context) {
        this.context = context;
        this.userDetails = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        this.editor = this.userDetails.edit();
    }

    public void setUserDetails(Bundle userBundle) {
        this.editor.putString(FIRST_NAME, userBundle.getString(FIRST_NAME));
        this.editor.putString(LAST_NAME, userBundle.getString(LAST_NAME));
        this.editor.putString(MIDDLE_NAME, userBundle.getString(MIDDLE_NAME));
        this.editor.putString(EMAIL_ADDRESS, userBundle.getString(EMAIL_ADDRESS));
        this.editor.putString(PHONE_NUMBER, userBundle.getString(PHONE_NUMBER));
        this.editor.apply();
    }


    public void logOutNoConfirmation() {
        editor.clear();
        editor.apply();
        // context.startActivity(new Intent(context, LogIn.class));
    }

    public void setFirstName(String firstName) {
        editor.putString(FIRST_NAME, firstName);
        editor.apply();
    }

    public void setOfferSlots(String slots) {
        editor.putString(OFFER_SLOTS, slots);
        editor.apply();
    }

    public void setReferralCode(String slots) {
        editor.putString(REFERRAL_CODE, slots);
        editor.apply();
    }

    public void setEmailAddress(String emailAddress) {
        editor.putString(EMAIL_ADDRESS, emailAddress);
        editor.apply();
    }

    public void setPhoneNumber(String phoneNumber) {
        editor.putString(PHONE_NUMBER, phoneNumber);
        editor.apply();
    }

    public void setDepartmentName(String phoneNumber) {
        editor.putString(DEPARTMENT_NAME, phoneNumber);
        editor.apply();
    }

    public void setAccountType(String phoneNumber) {
        editor.putString(ACCOUNT_TYPE, phoneNumber);
        editor.apply();
    }

    public void setBusinessName(String phoneNumber) {
        editor.putString(BUSINESS_NAME, phoneNumber);
        editor.apply();
    }

    public String getFirstName() {
        return this.userDetails.getString(FIRST_NAME, "");
    }

    public String getEmailAddress() {
        return this.userDetails.getString(EMAIL_ADDRESS, "");
    }

    public String getPhoneNumber() {
        return this.userDetails.getString(PHONE_NUMBER, null);
    }

    public String getOfferSlots() {
        return this.userDetails.getString(OFFER_SLOTS, "0");
    }

    public String getReferralCode() {
        return this.userDetails.getString(REFERRAL_CODE, "-");
    }

    public String getAccountType() {
        return this.userDetails.getString(ACCOUNT_TYPE, "");
    }

    public String getBusinessName() {
        return this.userDetails.getString(BUSINESS_NAME, "");
    }

    public String getDepartmentName() {
        return this.userDetails.getString(DEPARTMENT_NAME, "");
    }

    public void setUserLoggedIn(Boolean userLoggedIn) {
        this.editor.putBoolean(USER_LOGGED_IN, userLoggedIn);
        this.editor.apply();
    }

    public boolean userHasLoggedIn() {
        return this.userDetails.getBoolean(USER_LOGGED_IN, false);
    }

    public String getUserId() {
        return this.userDetails.getString(USER_ID, null);
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String getAccessToken() {
        return this.userDetails.getString(ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public void logOut() {
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, LogIn.class));
    }


    public String getPhoto() {
        return this.userDetails.getString(PHOTO, "");
    }

    public void setPhoto(String photo) {
        editor.putString(PHOTO, photo);
        editor.apply();
    }

    public String getNotifications() {
        return this.userDetails.getString(NOTIFICATIONS, "0");
    }

    public void setNotifications(String notifications) {
        editor.putString(NOTIFICATIONS, notifications);
        editor.apply();
    }

    public boolean isValid() {
        return this.userDetails.getBoolean(IS_VALID, false);
    }

    public void setIsValid(boolean isValid) {
        editor.putBoolean(IS_VALID, isValid);
        editor.apply();
    }

    public String getCompanyName() {
        return this.userDetails.getString("companyName", null);
    }

    public void setCompanyName(String companyName) {
        editor.putString("companyName", companyName);
        editor.apply();
    }

    public String getCompanyId() {
        return this.userDetails.getString("companyId", null);
    }

    public void setCompanyId(String companyId) {
        editor.putString("companyId", companyId);
        editor.apply();
    }

    public boolean isVerifying() {
        return this.userDetails.getBoolean("verifying", false);
    }

    public void setVerifying(boolean verifying) {
        editor.putBoolean("verifying", verifying);
        editor.apply();
    }

    public String getType() {
        return this.userDetails.getString("type", null);
    }

    public void setType(String type) {
        editor.putString("type", type);
        editor.apply();
    }

    public boolean isBTLAgent() {
        return getType().equals("sub");
    }

    public void setLocation(String type) {
        editor.putString("location", type);
        editor.apply();
    }

    public String getLocation() {
        return this.userDetails.getString("location", null);
    }

    public void setZone(String type) {
        editor.putString("zone", type);
        editor.apply();
    }

    public String getZone() {
        return this.userDetails.getString("zone", null);
    }

    public void setPickUpPoint(String type) {
        editor.putString("pick_up", type);
        editor.apply();
    }

    public String getPickUp() {
        return this.userDetails.getString("pick_up", null);
    }

    public void setIDNumber(String type) {
        editor.putString("id_number", type);
        editor.apply();
    }

    public String getIDNumber() {
        return this.userDetails.getString("id_number", null);
    }

    public void setConstituency(String type) {
        editor.putString("constituency", type);
        editor.apply();
    }

    public String getConstituency() {
        return this.userDetails.getString("constituency", null);
    }

    public void setCounty(String type) {
        editor.putString("county", type);
        editor.apply();
    }

    public String getCounty() {
        return this.userDetails.getString("county", null);
    }

    public void setSubCounty(String type) {
        editor.putString("sub_county", type);
        editor.apply();
    }

    public String getSubCounty() {
        return this.userDetails.getString("sub_county", null);
    }

    public void setWard(String type) {
        editor.putString("ward", type);
        editor.apply();
    }

    public String getWard() {
        return this.userDetails.getString("ward", null);
    }

    public void setStreet(String type) {
        editor.putString("street", type);
        editor.apply();
    }

    public String getStreet() {
        return this.userDetails.getString("street", null);
    }

    public void setPOBox(String type) {
        editor.putString("po_box", type);
        editor.apply();
    }

    public String getPOBox() {
        return this.userDetails.getString("po_box", null);
    }

    public void setPhone(String type) {
        editor.putString("phone", type);
        editor.apply();
    }

    public String getPhone() {
        return this.userDetails.getString("phone", null);
    }
}
