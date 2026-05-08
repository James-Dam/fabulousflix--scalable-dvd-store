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

public class StarParser extends DefaultHandler {

    List<Star> myStars;

    private String tempVal;

    private int id = 9423081;
    private Star prevStar;

    //to maintain context
    private Star tempStar;

    public StarParser() {
        myStars = new ArrayList<Star>();
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
            sp.parse("./src/XMLParser/stanford-movies/actors63.xml", this);

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

        System.out.println("No of Stars '" + myStars.size() + "'.");

        Iterator<Star> it = myStars.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

    //Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset
        tempVal = "";
        if (qName.equalsIgnoreCase("actor")) {
            //create a new instance of film
            tempStar = new Star();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("actor")) {
            //add it to the list
            if (tempStar != prevStar) {
                myStars.add(tempStar);
            }
            prevStar = tempStar;

        } else if (qName.equalsIgnoreCase("stagename")) {
            tempStar.setStarName(tempVal);
            tempStar.setId(String.format("nm%07d", id));
            id++;
        } else if (qName.equalsIgnoreCase("dob")) {
            try {
                tempStar.setDob(Integer.parseInt(tempVal));
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) {
        MovieParser spe = new MovieParser();
        spe.runExample();
    }
}