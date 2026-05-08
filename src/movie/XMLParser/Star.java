package XMLParser;

public class Star {

    private String starName;

    private int dob;

    private String id;

    public Star() {}

    public Star(String starName, String id, int dob) {
        this.starName = starName;
        this.dob = dob;
        this.id  = id;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String s) {
        this.starName = s;
    }

}
