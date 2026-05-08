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

public class StarInMovieParser extends DefaultHandler {

    List<StarInMovie> myStarInMovies;

    private String tempVal;

    //to maintain context
    private StarInMovie tempStarInMovie;

    public StarInMovieParser() {
        myStarInMovies = new ArrayList<StarInMovie>();
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
            sp.parse("./src/XMLParser/stanford-movies/casts124.xml", this);

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

        System.out.println("No of StarInMovies '" + myStarInMovies.size() + "'.");

        Iterator<StarInMovie> it = myStarInMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("m")) {
            //create a new instance of film
            tempStarInMovie = new StarInMovie();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("m")) {
            //add it to the list
            myStarInMovies.add(tempStarInMovie);

        } else if (qName.equalsIgnoreCase("f")) {
            tempStarInMovie.setMovieId(tempVal);
        } else if (qName.equalsIgnoreCase("a")) {
            tempStarInMovie.setStarName(tempVal);
        }
    }

    public static void main(String[] args) {
        MovieParser spe = new MovieParser();
        spe.runExample();
    }
}