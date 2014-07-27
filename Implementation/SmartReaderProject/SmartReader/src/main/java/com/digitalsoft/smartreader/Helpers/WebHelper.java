package com.digitalsoft.smartreader.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.Entities.RssItem;
import com.digitalsoft.smartreader.Entities.SemanticAnalyzerDomain;
import com.digitalsoft.smartreader.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Bilalo89 on 7/7/13.
 */
public class WebHelper
{
    public static void loadRssItemsForRssChannel(final Context context, final String channelLink, final int channelId, final List<Domain> allDomains)
    {
        class Worker extends AsyncTask <String, Void, Void>
        {
            @Override
            protected Void doInBackground(String... urls)
            {
                List<RssItem> rssItems = null;
                // get rss items from the rss file
                try
                {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser saxParser = factory.newSAXParser();
                    RssHandler handler = new RssHandler();
                    saxParser.parse(urls[0], handler);
                    rssItems =  handler.getItems();
                }
                catch (Exception e)
                {
                    Log.d(context.getResources().getString(R.string.log_tag), e.toString());
                    rssItems = new ArrayList<RssItem>();
                }

                // add rss items to the database
                List<RssChannel> rssChannels = DataBaseHelper.getAllRssChannels(context);
                RssChannel rssChannel = DataBaseHelper.getRssChannel(context, rssChannels, channelId);
                if (rssChannel != null) // to prevent adding rss items for a deleted channel
                {
                    for (int i = rssItems.size() - 1; i >= 0; i--) // add the older rss items first
                    {
                        RssItem rssItem = rssItems.get(i);
                        RssItem rssItemInDatabase = DataBaseHelper.getRssItem(context, rssItem.getLink());
                        if (rssItemInDatabase == null) // if the rss item hasn't added previously
                        {
                            // get and save semantic analyzer domains
                            List<SemanticAnalyzerDomain> semanticAnalyzerDomains = null;
                            try
                            {
                                String response = WebHelper.getResponse(Config.serviceURL, rssItem.getLink(), false, Config.sortDocDomainsByWeight, Config.addWeightsToAncestors, Config.shortWordsLimit, Config.acceptedTags, Config.searchInDbPedia, Config.dbPediaPredicates, Config.dbPediaResultsLimit, Config.disambiguationMethod, Config.leskWindowSize, Config.leskComparisonWay, Config.withCoreference);
                                semanticAnalyzerDomains = WebHelper.getSemanticAnalyzerDomains(response);
                                Log.d(context.getResources().getString(R.string.log_tag), rssItem.getTitle() + " " + rssItem.getLink());
                            }
                            catch (IOException e)
                            {
                                semanticAnalyzerDomains = new ArrayList<SemanticAnalyzerDomain>();
                                Log.d(context.getResources().getString(R.string.log_tag), e.toString());
                            }
                            if (semanticAnalyzerDomains.size() > 0) // if there are no errors in getting the domains
                            {
                                DataBaseHelper.beginTransaction();
                                int addedDomainRssItemsCount = 0;
                                long rssItemId = DataBaseHelper.addRssItem(context, rssItem.getTitle(), rssItem.getLink(), rssItem.getDescription(), channelId);
                                if (rssItemId != -1) // if the rss item has been added successfully
                                {
                                    addedDomainRssItemsCount = DataBaseHelper.addDomainRssItems(context, rssItemId, semanticAnalyzerDomains, allDomains);
                                }
                                if (addedDomainRssItemsCount == allDomains.size()) // if all DomainRssItems has been added successfully
                                {
                                    DataBaseHelper.setTransactionSuccessful();
                                }
                                DataBaseHelper.endTransaction();
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                Log.d(context.getResources().getString(R.string.log_tag), channelLink + " " + "finished loading");
            }
        }
        new Worker().execute(channelLink);
    }

    public static void loadAllRssItemsForAllRssChannels(Context context, List<Domain> allDomains)
    {
        List<RssChannel> rssChannels = DataBaseHelper.getAllRssChannels(context);
        for (RssChannel c : rssChannels)
        {
            loadRssItemsForRssChannel(context, c.getLink(), c.getId(), allDomains);
        }
    }

    private static String getResponse(String serviceURL, String input, Boolean inputIsText, Boolean sortDocDomainsByWeight, Boolean addWeightsToAncestors, int shortWordsLimit, String acceptedTags, Boolean searchInDbPedia, String dbPediaPredicates, int dbPediaResultsLimit, int disambiguationMethod, int leskWindowSize, int leskComparisonWay, Boolean withCoreference) throws java.io.IOException
    {
        String result = null;
        String params =  "?" + "input=" + input + "&" + "inputIsText=" + inputIsText + "&" + "sortDocDomainsByWeight=" + sortDocDomainsByWeight + "&" + "addWeightsToAncestors=" + addWeightsToAncestors + "&" + "shortWordsLimit=" + shortWordsLimit + "&" + "acceptedTags=" + acceptedTags + "&" + "searchInDbPedia=" + searchInDbPedia + "&" + "dbPediaPredicates=" + dbPediaPredicates + "&" + "dbPediaResultsLimit=" + dbPediaResultsLimit + "&" + "disambiguationMethod=" + disambiguationMethod + "&" + "leskWindowSize=" + leskWindowSize + "&" + "leskComparisonWay=" + leskComparisonWay + "&" + "withCoreference=" + withCoreference;
        serviceURL += params;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceURL.trim());
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        result = EntityUtils.toString(httpEntity, "UTF-8");
        return result;
    }
    private static List<SemanticAnalyzerDomain> getSemanticAnalyzerDomains(String response)
    {
        List<SemanticAnalyzerDomain> result = new ArrayList<SemanticAnalyzerDomain>();
        String beginSeparator = "<pre>";
        String endSeparator = "</pre>";
        int i = response.indexOf(beginSeparator) ;
        int j = response.indexOf(endSeparator);
        if (i == -1 || j == -1) // there is an error in the service response
        {
            return result;
        }
        String cleanedResponse = response.substring(i + beginSeparator.length(), j);
        if (GeneralHelper.countOccurrences(cleanedResponse, "=") != 170)
        {
            return  result;
        }
        String newLine = System.getProperty("line.separator");
        String [] domainsStrings = cleanedResponse.split(newLine);
        for (String domainString : domainsStrings)
        {
            SemanticAnalyzerDomain sad = new SemanticAnalyzerDomain(domainString);
            result.add(sad);
        }
        if (zeroResponse(result))
        {
            return new ArrayList<SemanticAnalyzerDomain>();
        }
        return result;
    }
    private static boolean zeroResponse(List<SemanticAnalyzerDomain> semanticAnalyzerDomains)
    {
        boolean result = true;
        for (SemanticAnalyzerDomain sad : semanticAnalyzerDomains)
        {
            if (sad.getWeight() > 0)
            {
                result = false;
                break;
            }
        }
        return  result;
    }
}
