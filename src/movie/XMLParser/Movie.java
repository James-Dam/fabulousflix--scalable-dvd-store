package XMLParser;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    private String movieName;

    private int year;

    private String id;

    private String director;

    private List<String> genres;

    public Movie() {
        this.genres = new ArrayList<>();
    }

    public Movie(String movieName, String id, int year, String director, List<String> genres) {
        this.movieName = movieName;
        this.year = year;
        this.id  = id;
        this.director = director;
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int age) {
        this.year = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void addGenre(String genre) {
        if (!genres.contains(genre)) { // Prevent duplicate genres
            genres.add(genre);
        }
    }
    public List<String> getGenres() {
        return genres;
    }
}