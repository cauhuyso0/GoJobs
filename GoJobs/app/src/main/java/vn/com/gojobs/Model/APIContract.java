package vn.com.gojobs.Model;

public class APIContract {
    private String flcId;
    private String empId;
    private String jobId;
    private String _id;

    public APIContract(String flcId, String empId, String jobId, String _id) {
        this.flcId = flcId;
        this.empId = empId;
        this.jobId = jobId;
        this._id = _id;
    }

    public String getFlcId() {
        return flcId;
    }

    @Override
    public String toString() {
        return "APIContract{" +
                "flcId='" + flcId + '\'' +
                ", empId='" + empId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", _id='" + _id + '\'' +
                '}';
    }

    public void setFlcId(String flcId) {
        this.flcId = flcId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
