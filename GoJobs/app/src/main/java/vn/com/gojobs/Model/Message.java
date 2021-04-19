package vn.com.gojobs.Model;

import java.util.List;

public class Message {
    public int icon;
    public String mess;
    private String _id;
    private Employer empId;
    private Freelancer flcId;
    private List<MessageFormat> content;

    public Message(String _id, Employer empId, Freelancer flcId, List content) {
        this._id = _id;
        this.empId = empId;
        this.flcId = flcId;
        this.content = content;
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

    public Freelancer getFlcId() {
        return flcId;
    }

    public void setFlcId(Freelancer flcId) {
        this.flcId = flcId;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public Message(int icon, String mess) {
        this.icon = icon;
        this.mess = mess;
    }

    @Override
    public String toString() {
        return "Message{" +
                "icon=" + icon +
                ", mess='" + mess + '\'' +
                ", _id='" + _id + '\'' +
                ", empId=" + empId +
                ", flcId=" + flcId +
                ", content=" + content +
                '}';
    }
}
