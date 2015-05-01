package com.jobber.core;

import com.jobber.core.util.Result;
import com.jobber.core.util.SimilarityResult;
import com.jobber.lucene.JobCandidateSimilarity;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jobber.lucene.QueryIndex;

public class Core {
    // Template used: http://startbootstrap.com/template-overviews/sb-admin/

    private static Logger logger = Logger.getLogger(Core.class);
    private static List<Result> jobResult;
    private static List<String> skills = null;
    private static String globalUrl = ""; // Hack to let only one user use it but avoid complication
    private static Map<String, SimilarityResult> chartResultMap = new HashMap<String, SimilarityResult>();


    public static String getMorrisChartByLinkedUrl(String url) {
        try {
            if (null == url)
                url = globalUrl; // TODO: Make it normal
            if (null == url || url.trim().length() == 0)
                url = "https://www.linkedin.com/in/gunjaagrawal";
            skills = fetchSkillsFromUrl(url);
            chartResultMap = getSimilarityResult(jobResult, skills);
            String morrisData = morrisChartFormattedJobs(chartResultMap);
            return morrisData;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Issue", ex);
            return "An exception occurred while fetching recommendations. Check logs";
        }
    }

    public static Map<String, SimilarityResult> getSimilarityResult(List<Result> jobResults, List<String> skills) throws IOException{
        Map<String, SimilarityResult> chartResultMap = new HashMap<String, SimilarityResult>();;

        String candidateProfile="";
        for (String skill : skills) {
            candidateProfile += " " + skill;
        }

        for(Result result : jobResults){
            String job = readFile(result.getFilePath());

            JobCandidateSimilarity jobCandidateSimilarity = new JobCandidateSimilarity(job, candidateProfile);
            SimilarityResult similarityResult = extract(result.getJobTitle(), jobCandidateSimilarity);

            logger.info("jiri" + result.getFilePath());
            chartResultMap.put(result.getFilePath(), similarityResult);
        }
        return chartResultMap;
    }

    private static SimilarityResult extract(String title, JobCandidateSimilarity jobCandidateSimilarity) {
        double val1 = Math.abs(jobCandidateSimilarity.getCosineSimilarity());
        double val2 = Math.abs(jobCandidateSimilarity.getJaccardSimilarity());
        double val3 = Math.abs(jobCandidateSimilarity.getPearsonCofficient());
        double val4 = Math.abs(jobCandidateSimilarity.getEuclideanDistance());

        SimilarityResult similarityResult = new SimilarityResult();
        similarityResult.setJobTitle(title);
        similarityResult.setCosine(val1 + "");
        similarityResult.setJaccard(val2 + "");
        similarityResult.setPearson(val3 + "");
        similarityResult.setEuclidean(val4 + "");
        return similarityResult;
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

    public static String getMorrisJobLegend() {  // TODO: Make it normal
        // Always call this after calling getMorrisChartByLinkedUrl (TODO: Improve this)
        return jobLegend;
    }

    public static String getBarChartByLinkedUrl(String url, String type) {
        try {
            if (null == url)
                url = globalUrl; // TODO: Make it normal
            if (null == url || url.trim().length() == 0)
                url = "https://www.linkedin.com/in/gunjaagrawal";
            skills = fetchSkillsFromUrl(url);
            String barData = barChartFormattedJobs(skills, type);
            return barData;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Issue", ex);
            return "An exception occurred while fetching recommendations. Check logs";
        }
    }


    public static String getJobSuggestionsByLinkedUrl(String url) {
        try {
            if (null == url)
                url = globalUrl; // TODO: Make it normal
            if (null == url || url.trim().length() == 0)
                url = "https://www.linkedin.com/in/gunjaagrawal";
            skills = fetchSkillsFromUrl(url);
            jobResult = new QueryIndex().queryRecommendedJobs(skills);
            String jobSuggestionsHtml = htmlFormattedJobs(jobResult);
            // String jobSuggestionsHtml = getJobRecommendationsFromLucene(skills);
            return jobSuggestionsHtml;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Issue", ex);
            return "An exception occurred while fetching recommendations. Check logs";
        }
    }

    private static List<String> fetchSkillsFromUrl(String urlString) throws Exception {
        String data = readUrl(urlString);
        //System.out.println(data);
        skills = findSkills(data);
        return skills;
    }

    private static List<String> findSkills(String html) throws Exception {
        List<String> skillList = new ArrayList<String>();
        Pattern regex = Pattern.compile("endorse-item-name-text\">(.*?)</a>", Pattern.DOTALL);
        Matcher matcher = regex.matcher(html);
        while (matcher.find()) {
            String skill = matcher.group(1);
            skillList.add(skill);
            //System.out.println(skill);
        }
        return skillList;
    }

    // Helper
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private static String htmlFormattedJobs(List<Result> results) {
        String html = "";
        for (Result result : results) {
            String jobRow = "<tr>\n";
            jobRow += "\t<td>" + result.getJobTitle() + "</td>\n";
            jobRow += "\t<td>" + result.getCompany() + "</td>\n";
            jobRow += "\t<td>" + result.getLocationCity() + "</td>\n";
            jobRow += "\t<td>" + result.getScore() + "</td>\n";
            jobRow += "\t<td><a href=\"" + result.getUrl() + "\" target=\"_blank\">Apply here</a></td>\n";  // TODO: Change this
            jobRow += "</tr>\n";
            html += jobRow + "\n";
        }
        return html;
    }

    private static String jobLegend = "";  // TODO: Make it normal

    private static String morrisChartFormattedJobs(Map<String, SimilarityResult> chartResultMap) {
        jobLegend = "";
        String morrisData = "$(function() {";

        String areaChart = "Morris.Area({ \n element: 'morris-area-chart', \n";

        String xKey = "xkey: 'job' ";
        String yKey = "ykeys: ['cosine', 'jaccard', 'pearson', 'euclidean'] ";
        String labels = "labels: ['Cosine', 'Jaccard', 'Pearson', 'Euclidean'] ";
        String more = "pointSize: 2, hideHover: 'auto', resize: true ";

        areaChart +="data: [\n";

        // Add data points here
        // data point format : " { job: 'job name', cosine: 0, jaccard: 0, pearson: 0, euclidean: 0 } "
        boolean first = true;
        String dataPointTemplate = "{ job: 'val0', cosine: val1, jaccard: val2, pearson: val3, euclidean: val4 }";
        //Map<String, List<String>> jobData = getSimilarityData(skills); // get similarity data
        int idx = 1000;
        for (String job : chartResultMap.keySet()) {
            if (first) {
                first = false;
            } else {
                areaChart += ", \n";
            }
            SimilarityResult value = chartResultMap.get(job);
            String dataPoint = dataPointTemplate.replaceAll("val0", idx + "");
            dataPoint = dataPoint.replaceAll("val1", "" + value.getCosine());
            dataPoint = dataPoint.replaceAll("val2", "" + value.getJaccard());
            dataPoint = dataPoint.replaceAll("val3", "" + value.getPearson());
            dataPoint = dataPoint.replaceAll("val4", "" + value.getEuclidean());
            areaChart += dataPoint;
            jobLegend += "<tr><td>" + idx + "</td><td>" + value.getJobTitle() + "</td></tr>\n";
            idx ++;
        }

        // Finish adding data points

        areaChart += "],\n";
        areaChart += xKey + ",\n";
        areaChart += yKey + ",\n";
        areaChart += labels + ",\n";
        areaChart += more + "\n";
        areaChart += "}); \n";
        morrisData += areaChart + "\n";

        morrisData += "});";
        return morrisData;
    }

    private static String barChartFormattedJobs(List<String> skills, String type) {
        String barData = "$(function() {";

        String barChart = "Morris.Bar({ \n element: 'morris-bar-chart-" + type + "', \n";

        String xKey = "xkey: 'job' ";
        String yKey = "ykeys: ['" + type + "'] ";
        String labels = "labels: ['" + type + "'] ";
        String more = "barRatio: 0.4, xLabelAngle: 35, hideHover: 'auto', resize: true ";

        barChart +="data: [\n";

        // Add data points here
        // data point format : " { job: 'job name', cosine: 0, jaccard: 0, pearson: 0, euclidean: 0 } "
        boolean first = true;
        String dataPointTemplate = "{ job: 'val0', " + type + ": val1 }";
        Map<String, List<String>> jobData = getSimilarityData(skills); // get similarity data

        for (String job : jobData.keySet()) {
            if (first) {
                first = false;
            } else {
                barChart += ", \n";
            }
            List<String> values = jobData.get(job);
            String dataPoint = dataPointTemplate.replaceAll("val0", values.get(4));
            int idx = 0;
            if ("jaccard".equals(type))
                idx = 1;
            else if ("pearson".equals(type))
                idx = 2;
            else if ("euclidean".equals(type))
                idx = 3;
            String value = values.get(idx);
            dataPoint = dataPoint.replaceAll("val1", "" + value);
            barChart += dataPoint;
        }

        // Finish adding data points

        barChart += "],\n";
        barChart += xKey + ",\n";
        barChart += yKey + ",\n";
        barChart += labels + ",\n";
        barChart += more + "\n";
        barChart += "}); \n";
        barData += barChart + "\n";

        barData += "});";
        return barData;
    }

    private static Map<String, List<String>> getSimilarityData(List<String> skills) {
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        int idx = 1000;
        for (Map.Entry<String, SimilarityResult> sim : chartResultMap.entrySet()) {
            List<String> list = new ArrayList<String>();
            list.add(sim.getValue().getCosine());
            list.add(sim.getValue().getJaccard());
            list.add(sim.getValue().getPearson());
            list.add(sim.getValue().getEuclidean());
            list.add(sim.getValue().getJobTitle());
            map.put(idx + "", list);
            idx ++;
        }
        return map;
    }


    public static void main(String args[]) throws Exception {
        String url = "https://www.linkedin.com/in/gunjaagrawal";
        String skills = getJobSuggestionsByLinkedUrl(url);
        System.out.println(skills);
    }
}
