package com.jobber.test;

import com.jobber.lucene.JobCandidateSimilarity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gunjaagrawal on 4/27/15.
 */

public class JobRecommendationTest {
    JobCandidateSimilarity softwareSimilarity;
    JobCandidateSimilarity accountSimilarity;

    @Before
    public void setUp() throws Exception {
        String candidateProfile = readFile("/Users/gunjaagrawal/Work2/JobsData/JobTestData/CandidateData/Resume_Text.txt");
        String softwareJob = readFile("/Users/gunjaagrawal/Work2/JobsData/JobTestData/JobData/SoftwareJob.xml");
        String accountJob = readFile("/Users/gunjaagrawal/Work2/JobsData/JobTestData/JobData/AccountingJob.xml");

        softwareSimilarity = new JobCandidateSimilarity(candidateProfile, softwareJob);
        accountSimilarity = new JobCandidateSimilarity(candidateProfile, accountJob);
    }

    @Test
    public void cosineTest() {
        System.out.println("cosineTest Started");
        Double sim1 = accountSimilarity.getCosineSimilarity();
        Double sim2 = softwareSimilarity.getCosineSimilarity();
        Assert.assertTrue(sim1 < sim2);
        System.out.println("cosineTest Ended");
    }

    @Test
    public void jaccardTest() {
        System.out.println("jaccardTest Started");
        Double sim1 = accountSimilarity.getJaccardSimilarity();
        Double sim2 = softwareSimilarity.getJaccardSimilarity();
        Assert.assertTrue(sim1 < sim2);
        System.out.println("jaccardTest Ended");
    }

    @Test
    public void pearsonTest() {
        System.out.println("pearsonTest Started");
        Double sim1 = accountSimilarity.getPearsonCofficient();
        Double sim2 = softwareSimilarity.getPearsonCofficient();
        Assert.assertTrue(sim1 < sim2);
        System.out.println("pearsonTest Ended");
    }

    @Test
    public void euclideanTest() {
        System.out.println("cosineTest Started");
        Double distance1 = accountSimilarity.getEuclideanDistance();
        Double distance2 = softwareSimilarity.getEuclideanDistance();
        Assert.assertTrue(distance1 >  distance2);
        System.out.println("cosineTest Ended");
    }

    private static String readFile(String filename){
        String content = null;
        File file = new File(filename);
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
}
