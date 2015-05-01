package com.jobber.lucene;

import java.io.IOException;
import java.util.*;
import java.io.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;

public class JobCandidateSimilarity {

    private final Set<String> terms = new HashSet<String>();
    private final RealVector jobVector_cosine;
    private final RealVector candidateVector_cosine;
    private final RealVector jobVector_jaccard;
    private final RealVector candidateVector_jaccard;

    public static final String CONTENT = "Content";
    public static final FieldType TYPE_STORED = new FieldType();
    static {
        TYPE_STORED.setIndexed(true);
        TYPE_STORED.setTokenized(true);
        TYPE_STORED.setStored(true);
        TYPE_STORED.setStoreTermVectors(true);
        TYPE_STORED.setStoreTermVectorPositions(true);
        TYPE_STORED.freeze();
    }

    public JobCandidateSimilarity(String doc1, String doc2) throws IOException {
        Directory directory = createIndex(doc1, doc2);

        IndexReader reader = DirectoryReader.open(directory);
        Map<String, Integer> f1 = getTermFrequencies(reader, 0);
        Map<String, Integer> f2 = getTermFrequencies(reader, 1);
        reader.close();

        jobVector_cosine = toCosineVector(f1);
        candidateVector_cosine = toCosineVector(f2);

        jobVector_jaccard = toJaccardVector(f1);
        candidateVector_jaccard = toJaccardVector(f2);
    }

    // create in memory index for job and candidate
    private Directory createIndex(String doc1, String doc2) throws IOException {
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_41);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_41, analyzer);
        IndexWriter writer = new IndexWriter(directory, iwc);
        addDocument(writer, doc1);
        addDocument(writer, doc2);
        writer.close();
        return directory;
    }

    // add document to index writer
    private void addDocument(IndexWriter writer, String content) throws IOException {
        Document doc = new Document();
        Field field = new Field(CONTENT, content, TYPE_STORED);
        doc.add(field);
        writer.addDocument(doc);
    }

    // get term frequencies for each term in job and candidate profile
    private Map<String, Integer> getTermFrequencies(IndexReader reader, int docId) throws IOException {
        Terms vector = reader.getTermVector(docId, CONTENT);
        TermsEnum termsEnum = null;
        termsEnum = vector.iterator(termsEnum);
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        BytesRef text = null;
        while ((text = termsEnum.next()) != null) {
            String term = text.utf8ToString();
            int freq = (int) termsEnum.totalTermFreq();
            frequencies.put(term, freq);
            terms.add(term);
        }
        return frequencies;
    }

    // Get vectors for job and document to calculate jaccard similarity
    private RealVector toJaccardVector(Map<String, Integer> map) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        for (String term : terms) {
            int value = map.containsKey(term) ? map.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return  vector;
    }

    // Get normalized vectors for job and document to calculate cosine similarity
    private RealVector toCosineVector(Map<String, Integer> termVectorMap) {
        RealVector vector = new ArrayRealVector(terms.size());
        int i = 0;
        for (String term : terms) {
            int value = termVectorMap.containsKey(term) ?
                    termVectorMap.get(term) : 0;
            vector.setEntry(i++, value);
        }
        return (RealVector)vector.mapDivide(vector.getL1Norm());
    }

    //read job and candidate files from local disk
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

    // Calculate Similarities
    public double getCosineSimilarity() {
        return (jobVector_cosine.dotProduct(candidateVector_cosine)) /
                (jobVector_cosine.getNorm() * candidateVector_cosine.getNorm());
    }

    public double getEuclideanDistance() {
        double val = jobVector_jaccard.getDistance(candidateVector_jaccard);
        
        return val;
    }

    public double getJaccardSimilarity(){
        double[] v1_arr = jobVector_jaccard.toArray();
        double[] v2_arr = candidateVector_jaccard.toArray();
        /*
            r = 0 , 1
            s = 1 , 0
            t = 1 , 1
         */
        double t = 0.0;
        double rst = 0.0;
        for(int i=0; i<v1_arr.length; i++) {
            if (v1_arr[i] == 0 && v2_arr[i] == 0) {
            }

            // (r + s)
            else if ((v1_arr[i] == 0 && v2_arr[i] == 1)
                    || (v1_arr[i] == 1 && v2_arr[i] == 0)) {
                rst++;
            }

            // t ; (r + s + t)
            else if (v1_arr[i] == 1 && v2_arr[i] == 1) {
                t++;
                rst++;
            }
        }

        // jaccard cofficient = t / (r + s + t)
        return  t /  rst;
    }

    public double getPearsonCofficient() {
        double[] v1_arr = jobVector_jaccard.toArray();
        double[] v2_arr = candidateVector_jaccard.toArray();
        SimpleRegression regression = new SimpleRegression();
        if (v1_arr.length != v2_arr.length) {
            throw new DimensionMismatchException(v1_arr.length, v2_arr.length);
        } else {
            for (int i = 0; i < v1_arr.length; i++) {
                regression.addData(v1_arr[i], v2_arr[i]);
            }
            return regression.getR();
        }
    }



}