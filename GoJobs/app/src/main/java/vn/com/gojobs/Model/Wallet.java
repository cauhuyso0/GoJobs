package vn.com.gojobs.Model;

public class Wallet {
    private String empId;
    private String flcId;
    private int balance;

    public Wallet(String empId, String flcId, int balance) {
        this.empId = empId;
        this.flcId = flcId;
        this.balance = balance;
    }

    public Wallet() {
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "empId='" + empId + '\'' +
                ", flcId='" + flcId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
