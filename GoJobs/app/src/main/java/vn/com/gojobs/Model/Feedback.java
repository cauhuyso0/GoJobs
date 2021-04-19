package vn.com.gojobs.Model;

public class Feedback {
    private Job jobId;
    private int starRating;
    private Employer empId;
    private Freelancer flcId;
    private String titleFeedback;
    private int rateStar;

    public Feedback(int starRating, Employer empId, Freelancer flcId) {
        this.starRating = starRating;
        this.empId = empId;
        this.flcId = flcId;
    }

    public Feedback(String titleFeedback, int rateStar) {
        this.titleFeedback = titleFeedback;
        this.rateStar = rateStar;
    }

    public Job getJobId() {
        return jobId;
    }

    public void setJobId(Job jobId) {
        this.jobId = jobId;
    }

    public String getTitleFeedback() {
        return titleFeedback;
    }

    public void setTitleFeedback(String titleFeedback) {
        this.titleFeedback = titleFeedback;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public Employer getEmpId() {
        return empId;
    }

    public void setEmpId(Employer empId) {
        this.empId = empId;
    }

    public Freelancer getFlcId() {
        return flcId;
    }

    public void setFlcId(Freelancer flcId) {
        this.flcId = flcId;
    }

    public int getRateStar() {
        return rateStar;
    }

    public void setRateStar(int rateStar) {
        this.rateStar = rateStar;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "jobId=" + jobId +
                ", starRating=" + starRating +
                ", empId=" + empId +
                ", flcId=" + flcId +
                ", titleFeedback='" + titleFeedback + '\'' +
                ", rateStar=" + rateStar +
                '}';
    }
}
