package vn.com.gojobs.Model;

import java.util.List;

public class RoomMessage {
    String empId;
    String flcId;
    String _id;
    List<MessageFormat> content;

    public RoomMessage(String empId, String flcId, String _id, List<MessageFormat> content) {
        this.empId = empId;
        this.flcId = flcId;
        this._id = _id;
        this.content = content;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFlcId() {
        return flcId;
    }

    public void setFlcId(String flcId) {
        this.flcId = flcId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<MessageFormat> getContent() {
        return content;
    }

    public void setContent(List<MessageFormat> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RoomMessage{" +
                "empId='" + empId + '\'' +
                ", flcId='" + flcId + '\'' +
                ", _id='" + _id + '\'' +
                ", content=" + content +
                '}';
    }
}
