package com.digitalsoft.smartreader.Helpers;

/**
 * Created by Bilalo89 on 7/11/13.
 */
public class Config
{
    //public static String serviceURL = "http://192.168.1.101:8080/SemanticAnalyzerService/index.jsp";
    //public static String serviceURL = "http://10.0.2.2:8080/SemanticAnalyzerService/index.jsp";
    public static String serviceURL = "http://10.0.0.2:8080/SemanticAnalyzerService/index.jsp";
    public static Float acceptedWeightThreshold = 0.5f;
    public static boolean sortDocDomainsByWeight = true;
    public static boolean addWeightsToAncestors = false;
    public static int shortWordsLimit = 3;
    public static String acceptedTags = "NN,VB,JJ";
    public static boolean searchInDbPedia = false;
    public static String dbPediaPredicates = "http://purl.org/dc/terms/subject";
    public static int dbPediaResultsLimit = 0;
    public static int  disambiguationMethod = 2;
    public static int leskWindowSize = 3;
    public static int leskComparisonWay = 1;
    public static boolean withCoreference = false;
}
