package com.jobber.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.xpath.*;
import javax.xml.xpath.XPathFactory;

public class CareerBuilderJobs {
    static String absoluteFilePath = "/Users/gunjaagrawal/Work2/JobsData/CareerbuilderJobData1/";
    static String jobCategoryDirectory;
    /*
    static String[] jobCategories = {"Accounting", "Automotive", "Banking", "BioTech",
            "Business+Development", "Business+Opportunity", "Design", "Consultant", "Education",
            "Entry+Level", "Executive", "Engineering", "Finance", "Health+Care", "Human+Resources", "Research",
            "Information+Technology", "Insurance", "Inventory", "Legal", "Management", "Manufacturing", "Nurse",
            "Professional+Services", "Telecommunications", "Training"};
     */
    static String[] jobCategories = {"BioTech", "Education", "Engineering", "Finance", "Research", "Information+Technology"};
    static String DeveloperKey = "";
    static Map<String, Integer> map = new HashMap<String, Integer>();

    public static void main(String[] args){
        saveJobs();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private static void saveJobs(){
        for(int i=0; i< jobCategories.length; i++) {
            String apiCall = "http://api.careerbuilder.com/v1/jobsearch?Category=" + jobCategories[i] + "&CountryCode=US&DeveloperKey=" + DeveloperKey;
            jobCategoryDirectory = absoluteFilePath + jobCategories[i];
            System.out.println("Inside SaveJobs");
            File file = new File(jobCategoryDirectory);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("==================" + jobCategories[i] + "==================");
                    System.out.println(jobCategoryDirectory + " - directory is created!");
                } else {
                    System.out.println("Failed to create " + jobCategoryDirectory + "directory!");
                }
            }

            String jobListFileName = jobCategoryDirectory + "/JobSearchList.xml";
            saveResponseToFile(jobListFileName, apiCall);
            getJobsUsingJobDID(jobListFileName);
        }
    }

    private static void saveResponseToFile(String fileName, String careerBuilderApiUrl){
        try {

            URL url = new URL(careerBuilderApiUrl);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            //use FileWriter to write file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            while ((inputLine = br.readLine()) != null) {
                bw.write(inputLine);
            }

            bw.close();
            br.close();

            System.out.println(fileName + " saved!");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getJobsUsingJobDID(String jobListFileName){
        try {
            File fXmlFile = new File(jobListFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(fXmlFile);

            xmlDocument.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String xPathExpression = "/ResponseJobSearch/Results/JobSearchResult";

            NodeList nList = (NodeList) xPath.compile(xPathExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String strJobDID = eElement.getElementsByTagName("DID").item(0).getTextContent();

                    System.out.println("Retrieving Job from DID - " + strJobDID);
                    String careerBuilderApiUrl =  "http://api.careerbuilder.com/v1/job?DID="+ strJobDID +"&DeveloperKey=" + DeveloperKey;

                    System.out.println(jobCategoryDirectory + "/" + strJobDID+".xml || " +  careerBuilderApiUrl);
                    if (map.containsKey(strJobDID)) {
                        int x = map.get(strJobDID);
                        map.put(strJobDID, x+1);
                    } else {
                        saveResponseToFile(jobCategoryDirectory + "/" + strJobDID+".xml", careerBuilderApiUrl);
                        map.put(strJobDID, 1);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    void retrieveAttribute(String fileName)
    {
        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(fXmlFile);

            xmlDocument.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String xPathExpression = "/ResponseJob/Job";

            NodeList nList = (NodeList) xPath.compile(xPathExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String strJobDID = eElement.getElementsByTagName("ExperienceRequired").item(0).getTextContent();

                    System.out.println("Retrieving Job from DID - " + strJobDID);
                    String apiCall =  "http://api.careerbuilder.com/v1/job?DID="+ strJobDID +"&DeveloperKey=" + DeveloperKey;

                    System.out.println(jobCategoryDirectory + "/" + strJobDID + ".xml || " + apiCall);
                    saveResponseToFile(jobCategoryDirectory + "/" + strJobDID+".xml", apiCall);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
