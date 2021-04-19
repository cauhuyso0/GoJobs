package vn.com.gojobs.Model;

public class Employer {
    private String accessTokenDb;
    private String _id;
    private String empName;
    private String empEmail;
    private String empPhone;
    private String empAddress;
    private String empTaxCode;
    private String empLogo;
    private String empDescription;
    private String empStatus;
    private float empRating;
    private boolean empTerm;

    public Employer(String accessTokenDb, String _id, String empName, String empEmail, String empPhone, String empAddress, String empTaxCode, String empLogo, String empDescription, String empStatus, float empRating, boolean empTerm) {
        this.accessTokenDb = accessTokenDb;
        this._id = _id;
        this.empName = empName;
        this.empEmail = empEmail;
        this.empPhone = empPhone;
        this.empAddress = empAddress;
        this.empTaxCode = empTaxCode;
        this.empLogo = empLogo;
        this.empDescription = empDescription;
        this.empStatus = empStatus;
        this.empRating = empRating;
        this.empTerm = empTerm;
    }

    public float getEmpRating() {
        return empRating;
    }

    public void setEmpRating(float empRating) {
        this.empRating = empRating;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "accessTokenDb='" + accessTokenDb + '\'' +
                ", _id='" + _id + '\'' +
                ", empName='" + empName + '\'' +
                ", empEmail='" + empEmail + '\'' +
                ", empPhone='" + empPhone + '\'' +
                ", empAddress='" + empAddress + '\'' +
                ", empTaxCode='" + empTaxCode + '\'' +
                ", empLogo='" + empLogo + '\'' +
                ", empDescription='" + empDescription + '\'' +
                ", empStatus='" + empStatus + '\'' +
                ", empRating='" + empRating + '\'' +
                ", empTerm=" + empTerm +
                '}';
    }

    public String getAccessTokenDb() {
        return accessTokenDb;
    }

    public void setAccessTokenDb(String accessTokenDb) {
        this.accessTokenDb = accessTokenDb;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpTaxCode() {
        return empTaxCode;
    }

    public void setEmpTaxCode(String empTaxCode) {
        this.empTaxCode = empTaxCode;
    }

    public String getEmpLogo() {
        return empLogo;
    }

    public void setEmpLogo(String empLogo) {
        this.empLogo = empLogo;
    }

    public String getEmpDescription() {
        return empDescription;
    }

    public void setEmpDescription(String empDescription) {
        this.empDescription = empDescription;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public boolean isEmpTerm() {
        return empTerm;
    }

    public void setEmpTerm(boolean empTerm) {
        this.empTerm = empTerm;
    }
}
