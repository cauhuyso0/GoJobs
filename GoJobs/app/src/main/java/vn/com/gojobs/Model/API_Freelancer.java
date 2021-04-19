package vn.com.gojobs.Model;

import java.util.Date;

public class API_Freelancer {
    String _id;
    String flcEmail;
    String flcPassword;
    String flcName;
    String flcPhone;
    Date flcBirthday;
    String flcAvatar;
    String flcSex;
    String flcEdu;
    String flcMajor;
    String flcJobTitle;
    String flcLanguages;
    boolean flcTerm;
    Date updatedPasswordAt;
    Date updatedInfoAt;

    public API_Freelancer(){

    }

    public API_Freelancer(String flcEmail, String flcPassword, String flcName, String flcPhone, Date flcBirthday, String flcAvatar, String flcSex, String flcEdu, String flcMajor, String flcJobTitle, String flcLanguages, boolean flcTerm) {
        this.flcEmail = flcEmail;
        this.flcPassword = flcPassword;
        this.flcName = flcName;
        this.flcPhone = flcPhone;
        this.flcBirthday = flcBirthday;
        this.flcAvatar = flcAvatar;
        this.flcSex = flcSex;
        this.flcEdu = flcEdu;
        this.flcMajor = flcMajor;
        this.flcJobTitle = flcJobTitle;
        this.flcLanguages = flcLanguages;
        this.flcTerm = flcTerm;
    }

    public API_Freelancer(String _id, String flcEmail, String flcPassword, String flcName, String flcPhone, Date flcBirthday, String flcAvatar, String flcSex, String flcEdu, String flcMajor, String flcJobTitle, String flcLanguages, boolean flcTerm, Date updatedPasswordAt, Date updatedInfoAt) {
        this._id = _id;
        this.flcEmail = flcEmail;
        this.flcPassword = flcPassword;
        this.flcName = flcName;
        this.flcPhone = flcPhone;
        this.flcBirthday = flcBirthday;
        this.flcAvatar = flcAvatar;
        this.flcSex = flcSex;
        this.flcEdu = flcEdu;
        this.flcMajor = flcMajor;
        this.flcJobTitle = flcJobTitle;
        this.flcLanguages = flcLanguages;
        this.flcTerm = flcTerm;
        this.updatedPasswordAt = updatedPasswordAt;
        this.updatedInfoAt = updatedInfoAt;
    }
}
