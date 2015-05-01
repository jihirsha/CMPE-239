package com.jobber.core.util;

/**
 * Created by gunjaagrawal on 4/27/15.
 */
public class Result {
    private String filePath;
    private String jobTitle;
    private String company;
    private String locationCity;
    private String score;
    private String url;


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
