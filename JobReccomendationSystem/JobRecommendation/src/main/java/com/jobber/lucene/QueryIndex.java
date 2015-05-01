package com.jobber.lucene;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.ext.ExtendableQueryParser;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.log4j.Logger;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import com.jobber.core.util.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class QueryIndex{
    static String  indexRootFolder = "/Users/gunjaagrawal/Work2/JobsData/JobIndex";
    private final Set<String> terms = new HashSet<String>();
    public static final String CONTENT = "Content";

    int count =0;
    private static Logger logger = Logger.getLogger(QueryIndex.class);

    public List<Result> queryRecommendedJobs(List<String> skills)
    {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
        List<Result> resultList = new ArrayList<Result>();

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexRootFolder)));
            IndexSearcher searcher = new IndexSearcher(reader);

            String queryString="";
            for (String skill : skills) {
                queryString += " " + skill;
            }
            logger.info("Skills to search : " + queryString);
            Query q = new ExtendableQueryParser(Version.LUCENE_41, "Content", analyzer).parse(ExtendableQueryParser.escape(queryString));
            int nNumberOfHits = 5;
            TopScoreDocCollector collector = TopScoreDocCollector.create(nNumberOfHits, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;


            // Display results
            logger.info("Found " + hits.length + " hits.");
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                Document d = searcher.doc(docId);
                Result result = new Result();
                result.setFilePath(d.get("FilePath"));
                result.setJobTitle(d.get("JobTitle"));
                result.setCompany(d.get("Company"));
                result.setLocationCity(d.get("LocationCity"));
                result.setUrl(d.get("ApplyURL"));
                result.setScore(hits[i].score + "");
                resultList.add(result);

                logger.info((i + 1) + ". docId:" + docId + " path : " + d.get("FilePath") + " score=" + hits[i].score);

            }
          reader.close();

        } catch (Exception e) {
            logger.error("", e);
        }
        return resultList;
    }
}


