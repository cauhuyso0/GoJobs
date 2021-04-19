package vn.com.gojobs.Model;

public class MessageFormat {
    private String userName;
    private String message;
    private String userId;

    public MessageFormat(String userId , String userName , String message) {
        this.userName = userName;
        this.message = message;
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MessageFormat{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
