package vn.com.gojobs.Model;

public class Contract {

    private String _id;
    private Job jobId;
    private Freelancer flcId;
    private int jobTotalSalaryPerHeadCount;
    private String contractStatus;

    public Contract(String _id, Job jobId, Freelancer flcId, int jobTotalSalaryPerHeadCount, String contractStatus) {
        this._id = _id;
        this.jobId = jobId;
        this.flcId = flcId;
        this.jobTotalSalaryPerHeadCount = jobTotalSalaryPerHeadCount;
        this.contractStatus = contractStatus;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    public Freelancer getFlcId() {
        return flcId;
    }

    public void setFlcId(Freelancer flcId) {
        this.flcId = flcId;
    }

    public int getJobTotalSalaryPerHeadCount() {
        return jobTotalSalaryPerHeadCount;
    }

    public void setJobTotalSalaryPerHeadCount(int jobTotalSalaryPerHeadCount) {
        this.jobTotalSalaryPerHeadCount = jobTotalSalaryPerHeadCount;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "_id='" + _id + '\'' +
                ", jobId=" + jobId +
                ", flcId=" + flcId +
                ", jobTotalSalaryPerHeadCount=" + jobTotalSalaryPerHeadCount +
                ", contractStatus='" + contractStatus + '\'' +
                '}';
    }
}
