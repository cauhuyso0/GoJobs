package vn.com.gojobs.Model;

public class Freelancer {
    private String accessTokenDb;
    private String _id;
   private String flcEmail;
    private String flcPassword;
    private String flcName;
    private String flcPhone;
    private String flcBirthday;
    private String flcAvatar;
    private String flcSex;
    private String flcAddress;
    private String flcEdu;
    private String flcMajor;
    private String flcJobTitle;
    private float flcRating;
    private String flcLanguages;
    private boolean flcTerm;

    public Freelancer(){}

    public float getFlcRating() {
        return flcRating;
    }

    public void setFlcRating(float flcRating) {
        this.flcRating = flcRating;
    }

    public Freelancer(String accessTokenDb, String _id, String flcEmail, String flcPassword, String flcName, String flcPhone, String flcBirthday, String flcAvatar, String flcSex, String flcAddress, String flcEdu, String flcMajor, String flcJobTitle, float flcRating, String flcLanguages, boolean flcTerm) {
        this.accessTokenDb = accessTokenDb;
        this._id = _id;
        this.flcEmail = flcEmail;
        this.flcPassword = flcPassword;
        this.flcName = flcName;
        this.flcPhone = flcPhone;
        this.flcBirthday = flcBirthday;
        this.flcAvatar = flcAvatar;
        this.flcSex = flcSex;
        this.flcAddress = flcAddress;
        this.flcEdu = flcEdu;
        this.flcMajor = flcMajor;
        this.flcJobTitle = flcJobTitle;
        this.flcRating = flcRating;
        this.flcLanguages = flcLanguages;
        this.flcTerm = flcTerm;
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

    public String getFlcEmail() {
        return flcEmail;
    }

    public void setFlcEmail(String flcEmail) {
        this.flcEmail = flcEmail;
    }

    public String getFlcPassword() {
        return flcPassword;
    }

    public void setFlcPassword(String flcPassword) {
        this.flcPassword = flcPassword;
    }

    public String getFlcName() {
        return flcName;
    }

    public void setFlcName(String flcName) {
        this.flcName = flcName;
    }

    public String getFlcPhone() {
        return flcPhone;
    }

    public void setFlcPhone(String flcPhone) {
        this.flcPhone = flcPhone;
    }

    public String getFlcBirthday() {
        return flcBirthday;
    }

    public void setFlcBirthday(String flcBirthday) {
        this.flcBirthday = flcBirthday;
    }

    public String getFlcAvatar() {
        return flcAvatar;
    }

    public void setFlcAvatar(String flcAvatar) {
        this.flcAvatar = flcAvatar;
    }

    public String getFlcSex() {
        return flcSex;
    }

    public void setFlcSex(String flcSex) {
        this.flcSex = flcSex;
    }

    public String getFlcAddress() {
        return flcAddress;
    }

    public void setFlcAddress(String flcAddress) {
        this.flcAddress = flcAddress;
    }

    public String getFlcEdu() {
        return flcEdu;
    }

    public void setFlcEdu(String flcEdu) {
        this.flcEdu = flcEdu;
    }

    public String getFlcMajor() {
        return flcMajor;
    }

    public void setFlcMajor(String flcMajor) {
        this.flcMajor = flcMajor;
    }

    public String getFlcJobTitle() {
        return flcJobTitle;
    }

    public void setFlcJobTitle(String flcJobTitle) {
        this.flcJobTitle = flcJobTitle;
    }

    public String getFlcLanguages() {
        return flcLanguages;
    }

    public void setFlcLanguages(String flcLanguages) {
        this.flcLanguages = flcLanguages;
    }

    public boolean isFlcTerm() {
        return flcTerm;
    }

    public void setFlcTerm(boolean flcTerm) {
        this.flcTerm = flcTerm;
    }

    @Override
    public String toString() {
        return "Freelancer{" +
                "accessTokenDb='" + accessTokenDb + '\'' +
                ", _id='" + _id + '\'' +
                ", flcEmail='" + flcEmail + '\'' +
                ", flcPassword='" + flcPassword + '\'' +
                ", flcName='" + flcName + '\'' +
                ", flcPhone='" + flcPhone + '\'' +
                ", flcBirthday='" + flcBirthday + '\'' +
                ", flcAvatar='" + flcAvatar + '\'' +
                ", flcSex='" + flcSex + '\'' +
                ", flcAddress='" + flcAddress + '\'' +
                ", flcEdu='" + flcEdu + '\'' +
                ", flcMajor='" + flcMajor + '\'' +
                ", flcJobTitle='" + flcJobTitle + '\'' +
                ", flcRating='" + flcRating + '\'' +
                ", flcLanguages='" + flcLanguages + '\'' +
                ", flcTerm=" + flcTerm +
                '}';
    }
}
