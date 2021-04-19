package vn.com.gojobs.Model;

public class Follow {
    private Employer empId;
    private Job jobId;
    private Freelancer flcId;

    public Follow(Employer empId, Job jobId, Freelancer flcId) {
        this.empId = empId;
        this.jobId = jobId;
        this.flcId = flcId;
    }

    public Employer getEmpId() {
        return empId;
    }

    public void setEmpId(Employer empId) {
        this.empId = empId;
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

    @Override
    public String toString() {
        return "Follow{" +
                "empId=" + empId +
                ", jobId=" + jobId +
                ", flcId=" + flcId +
                '}';
    }
}
