package vn.com.gojobs.Model;

public class Receipt {
    private String receiverId;
    private String senderId;
    private String updatedValue;
    private String createdAt;

    public Receipt(String receiverId, String senderId, String updatedValue, String createdAt) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.updatedValue = updatedValue;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiverId='" + receiverId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", updatedValue='" + updatedValue + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(String updatedValue) {
        this.updatedValue = updatedValue;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
