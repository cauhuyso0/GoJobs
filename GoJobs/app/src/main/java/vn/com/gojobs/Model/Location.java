package vn.com.gojobs.Model;

public class Location {
    private String title;
    private int ID;

    @Override
    public String toString() {
        return "Location{" +
                "title='" + title + '\'' +
                ", ID=" + ID +
                '}';
    }

    public Location(String title, int ID) {
        this.title = title;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
