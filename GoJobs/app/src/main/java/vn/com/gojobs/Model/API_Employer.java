package vn.com.gojobs.Model;

import java.util.Date;

public class API_Employer {
    String _id;
    String empName;
    String empEmail;
    String empPassword;
    String empNationalId;
    String empPhone;
    String empTaxCode;
    String empLogo;
    String empDescription;
    String empStatus;
    Date createdAt;
    String confirmedBy;
    Date confirmedAt;
    Date updatedPasswordAt;
    Date updatedInfoAt;
    String salt;

    public API_Employer(){

    }

    public API_Employer(String empName, String empEmail, String empPassword, String empNationalId, String empPhone, String empTaxCode, String empLogo, String empDescription, String empStatus, String salt) {
        this.empName = empName;
        this.empEmail = empEmail;
        this.empPassword = empPassword;
        this.empNationalId = empNationalId;
        this.empPhone = empPhone;
        this.empTaxCode = empTaxCode;
        this.empLogo = empLogo;
        this.empDescription = empDescription;
        this.empStatus = empStatus;
        this.salt = salt;
    }

    public API_Employer(String empName, String empEmail, String empPassword, String empNationalId, String empPhone, String empTaxCode, String empLogo, String empDescription, String empStatus, Date createdAt, String confirmedBy, Date confirmedAt, Date updatedPasswordAt, Date updatedInfoAt, String salt) {
        this(empName,empEmail,empPassword,empNationalId,empPhone,empTaxCode,empLogo,empDescription,empStatus,salt);
        this.createdAt = createdAt;
        this.confirmedBy = confirmedBy;
        this.confirmedAt = confirmedAt;
        this.updatedPasswordAt = updatedPasswordAt;
        this.updatedInfoAt = updatedInfoAt;

    }
}
