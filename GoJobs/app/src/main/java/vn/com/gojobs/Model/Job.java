package vn.com.gojobs.Model;

public class Job  {
    private String _id;
    private Employer empId;
    private String jobTitle;
    private String jobDescription;
    private String jobPaymentType;
    private int jobSalary;
    private boolean experiencRequired;
    private String jobField;
    private String jobStart;
    private String jobEnd;
    private int jobDuaration;
    private String jobPublishDate;
    private String jobStatus;
    private int jobTotalSalaryPerHeadCount;
    private int jobHeadCountTarget;
    private int jobPaidContractCount;
    private String jobAddress;

    public Job(Employer empId, String jobTitle, String jobDescription, String jobPaymentType, int jobSalary, boolean experiencRequired, String jobField, String jobStart, String jobEnd, int jobDuaration, String jobPublishDate, String jobStatus, int jobTotalSalaryPerHeadCount, int jobHeadCountTarget, String jobAddress) {
        this.empId = empId;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobPaymentType = jobPaymentType;
        this.jobSalary = jobSalary;
        this.experiencRequired = experiencRequired;
        this.jobField = jobField;
        this.jobStart = jobStart;
        this.jobEnd = jobEnd;
        this.jobDuaration = jobDuaration;
        this.jobPublishDate = jobPublishDate;
        this.jobStatus = jobStatus;
        this.jobTotalSalaryPerHeadCount = jobTotalSalaryPerHeadCount;
        this.jobHeadCountTarget = jobHeadCountTarget;
        this.jobAddress = jobAddress;
    }

    public Job() {
    }

    public Job(String jobTitle, String jobPaymentType, int jobSalary, String jobStart, String jobEnd, String jobStatus) {
        this.jobTitle = jobTitle;
        this.jobPaymentType = jobPaymentType;
        this.jobSalary = jobSalary;
        this.jobStart = jobStart;
        this.jobEnd = jobEnd;
        this.jobStatus = jobStatus;
        this.jobHeadCountTarget = jobHeadCountTarget;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Employer getEmpId() {
        return empId;
    }

    public void setEmpId(Employer empId) {
        this.empId = empId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobPaymentType() {
        return jobPaymentType;
    }

    public void setJobPaymentType(String jobPaymentType) {
        this.jobPaymentType = jobPaymentType;
    }

    public int getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(int jobSalary) {
        this.jobSalary = jobSalary;
    }

    public boolean isExperiencRequired() {
        return experiencRequired;
    }

    public void setExperiencRequired(boolean experiencRequired) {
        this.experiencRequired = experiencRequired;
    }

    public String getJobField() {
        return jobField;
    }

    public void setJobField(String jobField) {
        this.jobField = jobField;
    }

    public String getJobStart() {
        return jobStart;
    }

    public void setJobStart(String jobStart) {
        this.jobStart = jobStart;
    }

    public String getJobEnd() {
        return jobEnd;
    }

    public void setJobEnd(String jobEnd) {
        this.jobEnd = jobEnd;
    }

    public int getJobDuaration() {
        return jobDuaration;
    }

    public void setJobDuaration(int jobDuaration) {
        this.jobDuaration = jobDuaration;
    }

    public String getJobPublishDate() {
        return jobPublishDate;
    }

    public void setJobPublishDate(String jobPublishDate) {
        this.jobPublishDate = jobPublishDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getJobTotalSalaryPerHeadCount() {
        return jobTotalSalaryPerHeadCount;
    }

    public void setJobTotalSalaryPerHeadCount(int jobTotalSalaryPerHeadCount) {
        this.jobTotalSalaryPerHeadCount = jobTotalSalaryPerHeadCount;
    }

    public int getJobHeadCountTarget() {
        return jobHeadCountTarget;
    }

    public void setJobHeadCountTarget(int jobHeadCountTarget) {
        this.jobHeadCountTarget = jobHeadCountTarget;
    }

    public int getJobPaidContractCount() {
        return jobPaidContractCount;
    }

    public void setJobPaidContractCount(int jobPaidContractCount) {
        this.jobPaidContractCount = jobPaidContractCount;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    @Override
    public String toString() {
        return "Job{" +
                "empId='" + empId + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", jobPaymentType='" + jobPaymentType + '\'' +
                ", jobSalary=" + jobSalary +
                ", experiencRequired=" + experiencRequired +
                ", jobField='" + jobField + '\'' +
                ", jobStart='" + jobStart + '\'' +
                ", jobEnd='" + jobEnd + '\'' +
                ", jobDuaration=" + jobDuaration +
                ", jobPublishDate='" + jobPublishDate + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", jobTotalSalaryPerHeadCount=" + jobTotalSalaryPerHeadCount +
                ", jobHeadCountTarget=" + jobHeadCountTarget +
                ", jobPaidContractCount=" + jobPaidContractCount +
                ", jobAddress='" + jobAddress + '\'' +
                '}';
    }
}