package com.jobber.core.util;

/**
 * Created by gunjaagrawal on 4/27/15.
 */
public class SimilarityResult {
    private String jobTitle;
    private String cosine;
    private String jaccard;
    private String pearson;
    private String euclidean;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCosine() {
        return cosine;
    }

    public void setCosine(String cosine) {
        this.cosine = cosine;
    }

    public String getJaccard() {
        return jaccard;
    }

    public void setJaccard(String jaccard) {
        this.jaccard = jaccard;
    }

    public String getPearson() {
        return pearson;
    }

    public void setPearson(String pearson) {
        this.pearson = pearson;
    }

    public String getEuclidean() {
        return euclidean;
    }

    public void setEuclidean(String euclidean) {
        this.euclidean = euclidean;
    }


}
