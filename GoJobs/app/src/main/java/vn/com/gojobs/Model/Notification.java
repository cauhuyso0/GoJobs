package vn.com.gojobs.Model;

public class Notification {
    private String _id;
    private Job jobId;
    private String empId;
    private String content;
    private String createdBy;



    @Override
    public String toString() {
        return "Notification{" +
                "_id='" + _id + '\'' +
                ", jobId=" + jobId +
                ", empId='" + empId + '\'' +
                ", content='" + content + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}