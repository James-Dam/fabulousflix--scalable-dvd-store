package XMLParser;

public class StarInMovie {

    private String starName;

    private String movieId;

    public StarInMovie() {}

    public StarInMovie(String starName, String movieId) {
        this.starName = starName;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String s) {
        this.starName = s;
    }

}
