package com.jobber.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ExtractAttributes {

    String jobRootFolder = "/Users/gunjaagrawal/Work2/JobsData/CareerbuilderJobData";
    static String  indexRootFolder = "/Users/gunjaagrawal/Work2/JobsData/JobIndex";
    public static final String CONTENT = "Content";
    public static final String FILE_PATH = "FilePath";
    public static final FieldType TYPE_STORED = new FieldType();
    public static String[] strAttributeArray= {"ExperienceRequired","DegreeRequired","LocationState","LocationCity", "JobTitle", "ApplyURL", "Company"};
    public static FSDirectory directory;
    public static Analyzer analyzer;
    public static IndexWriterConfig iwc;
    public static IndexWriter writer;
    static {
        try {
            directory = FSDirectory.open(new File(indexRootFolder));
            analyzer = new SimpleAnalyzer(Version.LUCENE_41);
            iwc = new IndexWriterConfig(Version.LUCENE_41, analyzer);
            writer = new IndexWriter(directory, iwc);
        }
        catch(IOException e){}
    }
    static {
        TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
    }

    FSDirectory getDirectories() {

        File rootFolder = new File(jobRootFolder);
        File[] listOfDirectories = rootFolder.listFiles();

        for (int i = 0; i < listOfDirectories.length; i++) {
            if (listOfDirectories[i].isDirectory()) {
                String strDirectoryPath = listOfDirectories[i].getAbsolutePath();

                File folder = new File(strDirectoryPath);
                File[] listOfFiles = folder.listFiles();

                for (int j = 0; j < listOfFiles.length; j++) {
                    if (listOfFiles[j].isFile() && listOfFiles[j].getName().compareTo("JobSearchList.xml") != 0) {

                        String fileName = listOfFiles[j].getName();
                        System.out.println("indexing : " + fileName);
                        File file = listOfFiles[j].getAbsoluteFile();
                        ArrayList<String> strListOfAttributes = retrieveAttributes(file);

                        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                        String content = readFile(file);
                        Field field = new Field(CONTENT, content, TYPE_STORED);
                        doc.add(field);
                        Field filePath = new Field(FILE_PATH,file.getAbsolutePath(),TYPE_STORED);
                        doc.add(filePath);
                        for (int k = 0; k < strListOfAttributes.size(); k++) {
                            Field tempField = new Field(strAttributeArray[k], strListOfAttributes.get(k), TYPE_STORED);
                            doc.add(tempField);
                        }
                        try{
                            writer.addDocument(doc);
                        }
                        catch(IOException e){

                        }
                    }
                }
            }
        }
        try{
            writer.close();
        }catch(IOException e){

        }
        return directory;
    }

    public static String readFile(File file){
        String content = null;
        //File file = new File(filename);
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    ArrayList<String> retrieveAttributes(File file) {

        ArrayList<String> listAttributes = new ArrayList<String>();
        try {
            //File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(file);

            xmlDocument.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            String xPathExpression = "/ResponseJob/Job";

            NodeList nList = (NodeList) xPath.compile(xPathExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String strExperience = eElement.getElementsByTagName("ExperienceRequired").item(0).getTextContent();
                    String strEducation = eElement.getElementsByTagName("DegreeRequired").item(0).getTextContent();
                    String strLocationState = eElement.getElementsByTagName("LocationState").item(0).getTextContent();
                    String strLocationCity = eElement.getElementsByTagName("LocationCity").item(0).getTextContent();
                    String strJobTitle = eElement.getElementsByTagName("JobTitle").item(0).getTextContent();
                    String strApplyURL = eElement.getElementsByTagName("ApplyURL").item(0).getTextContent();
                    String strCompany = eElement.getElementsByTagName("Company").item(0).getTextContent();

                    listAttributes.add(0, strEducation);
                    listAttributes.add(1, strExperience);
                    listAttributes.add(2, strLocationState);
                    listAttributes.add(3, strLocationCity);
                    listAttributes.add(4, strJobTitle);
                    listAttributes.add(5, strApplyURL);
                    listAttributes.add(6, strCompany);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAttributes;
    }

    public static void main(String[] args){
        ExtractAttributes extractAttributes = new ExtractAttributes();
        extractAttributes.getDirectories();
    }
}
