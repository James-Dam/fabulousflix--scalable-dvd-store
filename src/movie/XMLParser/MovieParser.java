package XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class MovieParser extends DefaultHandler {

    List<Movie> myMovies;

    private String tempVal;
    private String currDirector = "";


    //to maintain context
    private Movie tempMovie;

    public MovieParser() {
        myMovies = new ArrayList<Movie>();
    }

    public void runExample() {
        parseDocument();
        //printData();
    }

    private void parseDocument() {

        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            //
            sp.parse("./src/XMLParser/stanford-movies/mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData() {

        System.out.println("No of Movies '" + myMovies.size() + "'.");

        Iterator<Movie> it = myMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("film")) {
            //create a new instance of film
            tempMovie = new Movie();
            tempMovie.setDirector(currDirector);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("film")) {
            //add it to the list
            myMovies.add(tempMovie);

        } else if (qName.equalsIgnoreCase("fid")) {
            if (!tempVal.toLowerCase().startsWith("tt")) {
                tempMovie.setId(tempVal);
            } else {
                tempMovie.setId(null);
            }
        } else if (qName.equalsIgnoreCase("t")) {
            tempMovie.setMovieName(tempVal);
        } else if (qName.equalsIgnoreCase("year")) {
            try {
                tempMovie.setYear(Integer.parseInt(tempVal));
            } catch (Exception e) {

            }
        } else if (qName.equalsIgnoreCase("cat")) {
            String[] genres = tempVal.split("\\s+");

            for (String genre : genres) {
                tempMovie.addGenre(genre.toLowerCase());
            }
        } else if (qName.equalsIgnoreCase("dirname")) {
            currDirector = tempVal;
        }

    }

    public static void main(String[] args) {
        MovieParser spe = new MovieParser();
        spe.runExample();
    }
}