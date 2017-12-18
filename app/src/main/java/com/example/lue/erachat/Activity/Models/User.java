package com.example.lue.erachat.Activity.Models;

/**
 * Created by lue on 17-06-2017.
 */

public class User {
    private boolean isSelected;
    private String userName;
    private String userId;
    private String UserLastSeen;
    private String contactNumber;
    private String contactName;
    private String contactEmail;
    private String contactId;
    private String usercontactid;
    private String contactStatus;
    private String contactRegId;
    private String contactImage;
    private String contactVisibility;

    public User(String contactNumber,String contactName,String contactStatus, String contactEmail,String contactimage, String userid) {
        this.contactNumber=contactNumber;
        this.contactName=contactName;
        this.contactStatus=contactStatus;
        this.contactEmail=contactEmail;
        this.contactImage=contactimage;
        this.usercontactid=userid;
    }

    public User() {

    }

    public User(String contactName, String contactNumber) {
        this.contactName=contactName;
        this.contactNumber=contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    @Override
    public boolean equals(Object obj) {
        User other = (User) obj;
        if(this.contactNumber==other.contactNumber){
            return true;
        }else{
            return false;
        }
    }
    public boolean isSimilarButNotEqual(User other) {
        if(!this.contactNumber.equals(other.contactNumber))
            return false;
        return !(this.contactNumber + this.contactName).equals(other.contactNumber + other.contactName);
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }




    public String getContactRegId() {
        return contactRegId;
    }

    public void setContactRegId(String contactRegId) {
        this.contactRegId = contactRegId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUsercontactid() {
        return usercontactid;
    }

    public void setUsercontactid(String usercontactid) {
        this.usercontactid = usercontactid;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactVisibility() {
        return contactVisibility;
    }

    public void setContactVisibility(String contactVisibility) {
        this.contactVisibility = contactVisibility;
    }
}
