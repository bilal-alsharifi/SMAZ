package com.digitalsoft.smartreader.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.Entities.RssItem;
import com.digitalsoft.smartreader.Entities.SemanticAnalyzerDomain;
import com.digitalsoft.smartreader.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bilalo89 on 7/6/13.
 */
public class DataBaseHelper
{
    private static SQLiteDatabase db;
    private static String dbName = "database";
    private static String domainTableName = "Domain";
    private static String rssItemTableName = "RssItem";
    private static String rssChannelTableName = "RssChannel";
    private static String domainRssItemTableName = "DomainRssItem";
    private static int mode = Context.MODE_PRIVATE;
    public static void openDatabase(Context context)
    {
        db = context.openOrCreateDatabase(dbName, mode, null);
    }
    public static void beginTransaction()
    {
        db.beginTransaction();
    }
    public static void setTransactionSuccessful()
    {
        db.setTransactionSuccessful();;
    }
    public static void endTransaction()
    {
        db.endTransaction();
    }
    public static void addDomain(Context context, String domainName, int checked)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + domainTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT UNIQUE NOT NULL, checked INTEGER NOT NULL);");
        ContentValues values = new ContentValues();
        values.put("name", domainName);
        values.put("checked", checked);
        try
        {
            db.insertOrThrow(domainTableName, null, values);
        }
        catch (SQLiteConstraintException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
        }
    }
    public static void addAllDomains(Context context, int checked)
    {
        addDomain(context, "acoustics", checked);
        addDomain(context, "administration", checked);
        addDomain(context, "agriculture", checked);
        addDomain(context, "anatomy", checked);
        addDomain(context, "animal_husbandry", checked);
        addDomain(context, "animals", checked);
        addDomain(context, "anthropology", checked);
        addDomain(context, "applied_science", checked);
        addDomain(context, "archaeology", checked);
        addDomain(context, "archery", checked);
        addDomain(context, "architecture", checked);
        addDomain(context, "art", checked);
        addDomain(context, "artisanship", checked);
        addDomain(context, "astrology", checked);
        addDomain(context, "astronautics", checked);
        addDomain(context, "astronomy", checked);
        addDomain(context, "athletics", checked);
        addDomain(context, "atomic_physic", checked);
        addDomain(context, "aviation", checked);
        addDomain(context, "badminton", checked);
        addDomain(context, "banking", checked);
        addDomain(context, "baseball", checked);
        addDomain(context, "basketball", checked);
        addDomain(context, "betting", checked);
        addDomain(context, "biochemistry", checked);
        addDomain(context, "biology", checked);
        addDomain(context, "body_care", checked);
        addDomain(context, "book_keeping", checked);
        addDomain(context, "bowling", checked);
        addDomain(context, "boxing", checked);
        addDomain(context, "buildings", checked);
        addDomain(context, "card", checked);
        addDomain(context, "chemistry", checked);
        addDomain(context, "chess", checked);
        addDomain(context, "cinema", checked);
        addDomain(context, "color", checked);
        addDomain(context, "commerce", checked);
        addDomain(context, "computer_science", checked);
        addDomain(context, "cricket", checked);
        addDomain(context, "cycling", checked);
        addDomain(context, "dance", checked);
        addDomain(context, "dentistry", checked);
        addDomain(context, "diplomacy", checked);
        addDomain(context, "diving", checked);
        addDomain(context, "drawing", checked);
        addDomain(context, "earth", checked);
        addDomain(context, "economy", checked);
        addDomain(context, "electricity", checked);
        addDomain(context, "electronics", checked);
        addDomain(context, "electrotechnology", checked);
        addDomain(context, "engineering", checked);
        addDomain(context, "enterprise", checked);
        addDomain(context, "entomology", checked);
        addDomain(context, "environment", checked);
        addDomain(context, "ethnology", checked);
        addDomain(context, "exchange", checked);
        addDomain(context, "factotum", checked);
        addDomain(context, "fashion", checked);
        addDomain(context, "fencing", checked);
        addDomain(context, "finance", checked);
        addDomain(context, "fishing", checked);
        addDomain(context, "folklore", checked);
        addDomain(context, "food", checked);
        addDomain(context, "football", checked);
        addDomain(context, "free_time", checked);
        addDomain(context, "furniture", checked);
        addDomain(context, "gas", checked);
        addDomain(context, "gastronomy", checked);
        addDomain(context, "genetics", checked);
        addDomain(context, "geography", checked);
        addDomain(context, "geology", checked);
        addDomain(context, "geometry", checked);
        addDomain(context, "golf", checked);
        addDomain(context, "grammar", checked);
        addDomain(context, "graphic_arts", checked);
        addDomain(context, "health", checked);
        addDomain(context, "heraldry", checked);
        addDomain(context, "history", checked);
        addDomain(context, "hockey", checked);
        addDomain(context, "home", checked);
        addDomain(context, "humanities", checked);
        addDomain(context, "hunting", checked);
        addDomain(context, "hydraulics", checked);
        addDomain(context, "industry", checked);
        addDomain(context, "insurance", checked);
        addDomain(context, "jewellery", checked);
        addDomain(context, "law", checked);
        addDomain(context, "linguistics", checked);
        addDomain(context, "literature", checked);
        addDomain(context, "mathematics", checked);
        addDomain(context, "mechanics", checked);
        addDomain(context, "medicine", checked);
        addDomain(context, "meteorology", checked);
        addDomain(context, "metrology", checked);
        addDomain(context, "military", checked);
        addDomain(context, "money", checked);
        addDomain(context, "mountaineering", checked);
        addDomain(context, "music", checked);
        addDomain(context, "mythology", checked);
        addDomain(context, "nautical", checked);
        addDomain(context, "number", checked);
        addDomain(context, "numismatics", checked);
        addDomain(context, "occultism", checked);
        addDomain(context, "oceanography", checked);
        addDomain(context, "optics", checked);
        addDomain(context, "painting", checked);
        addDomain(context, "paleontology", checked);
        addDomain(context, "paranormal", checked);
        addDomain(context, "pedagogy", checked);
        addDomain(context, "person", checked);
        addDomain(context, "pharmacy", checked);
        addDomain(context, "philately", checked);
        addDomain(context, "philology", checked);
        addDomain(context, "philosophy", checked);
        addDomain(context, "photography", checked);
        addDomain(context, "physics", checked);
        addDomain(context, "physiology", checked);
        addDomain(context, "plants", checked);
        addDomain(context, "plastic_arts", checked);
        addDomain(context, "play", checked);
        addDomain(context, "politics", checked);
        addDomain(context, "post", checked);
        addDomain(context, "psychiatry", checked);
        addDomain(context, "psychoanalysis", checked);
        addDomain(context, "psychological_features", checked);
        addDomain(context, "psychology", checked);
        addDomain(context, "publishing", checked);
        addDomain(context, "pure_science", checked);
        addDomain(context, "quality", checked);
        addDomain(context, "racing", checked);
        addDomain(context, "radio", checked);
        addDomain(context, "radiology", checked);
        addDomain(context, "railway", checked);
        addDomain(context, "religion", checked);
        addDomain(context, "roman_catholic", checked);
        addDomain(context, "rowing", checked);
        addDomain(context, "rugby", checked);
        addDomain(context, "school", checked);
        addDomain(context, "sculpture", checked);
        addDomain(context, "sexuality", checked);
        addDomain(context, "skating", checked);
        addDomain(context, "skiing", checked);
        addDomain(context, "soccer", checked);
        addDomain(context, "social", checked);
        addDomain(context, "social_science", checked);
        addDomain(context, "sociology", checked);
        addDomain(context, "sport", checked);
        addDomain(context, "statistics", checked);
        addDomain(context, "sub", checked);
        addDomain(context, "surgery", checked);
        addDomain(context, "swimming", checked);
        addDomain(context, "table_tennis", checked);
        addDomain(context, "tax", checked);
        addDomain(context, "telecommunication", checked);
        addDomain(context, "telegraphy", checked);
        addDomain(context, "telephony", checked);
        addDomain(context, "tennis", checked);
        addDomain(context, "theatre", checked);
        addDomain(context, "theology", checked);
        addDomain(context, "time_period", checked);
        addDomain(context, "topography", checked);
        addDomain(context, "tourism", checked);
        addDomain(context, "town_planning", checked);
        addDomain(context, "transport", checked);
        addDomain(context, "tv", checked);
        addDomain(context, "university", checked);
        addDomain(context, "vehicles", checked);
        addDomain(context, "veterinary", checked);
        addDomain(context, "volleyball", checked);
        addDomain(context, "wrestling", checked);
    }

    public static List<Domain> getAllDomains(Context context)
    {
        List<Domain> result = new ArrayList<Domain>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + domainTableName + ";", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                Domain d = new Domain();
                d.setId(cursor.getInt(cursor.getColumnIndex("id")));
                d.setName(cursor.getString(cursor.getColumnIndex("name")));
                d.setChecked(cursor.getInt(cursor.getColumnIndex("checked")));
                result.add(d);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
    public static List<Domain> getAllCheckedDomains(Context context, List<Domain> allDomains)
    {
        List<Domain> result = new ArrayList<Domain>();
        for (Domain d : allDomains)
        {
            if (d.getChecked() == 1)
            {
                result.add(d);
            }
        }
        return result;
    }
    public static Domain getDomain(Context context, String domainName, List<Domain> allDomains)
    {
        Domain result = null;
        for (Domain d : allDomains)
        {
            if (d.getName().equalsIgnoreCase(domainName))
            {
                result = d;
                break;
            }
        }
        return  result;
    }
    public static void editDomain(Context context, String domainName, int domainChecked)
    {
        ContentValues values = new ContentValues();
        values.put("checked", domainChecked);
        db.update(domainTableName, values, "name = '" + domainName + "'", null);
    }
    public static void editDomains(Context context, int domainChecked)
    {
        ContentValues values = new ContentValues();
        values.put("checked", domainChecked);
        db.update(domainTableName, values, null, null);
    }
    public static Boolean addRssChannel(Context context, String title, String link, String description)
    {
        Boolean addedSuccessfully = true;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + rssChannelTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT UNIQUE NOT NULL, link TEXT UNIQUE NOT NULL, description TEXT);");
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("link", link);
        values.put("description", description);
        try
        {
            db.insertOrThrow(rssChannelTableName, null, values);
        }
        catch (SQLiteConstraintException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            addedSuccessfully = false;
        }
        return addedSuccessfully;
    }
    public static void editRssChannel(Context context, int id, String newTitle)
    {
        ContentValues values = new ContentValues();
        values.put("title", newTitle);
        db.update(rssChannelTableName, values, "id = " + id, null);
    }
    public static void removeRssChannel(Context context, String channelTitle)
    {
        // get channel id
        Cursor cursor = db.rawQuery("SELECT * FROM " + rssChannelTableName + " WHERE title = ?;", new String[] { channelTitle });
        cursor.moveToFirst();
        int channelId = cursor.getInt(cursor.getColumnIndex("id"));
        cursor.close();
        // remove DomainRssItems related to the rss items
        List<Integer> rssItemsIds = new ArrayList<Integer>();
        cursor = null;
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + rssItemTableName + " WHERE channelId =" + channelId + " ;", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
        }
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                rssItemsIds.add(id);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        if (rssItemsIds.size() > 0)
        {
            String inClause = rssItemsIds.toString().replace("[", "(").replace("]",")");
            db.delete(domainRssItemTableName, "rssItemId IN " + inClause, null);
        }
        // remove all rss items related to the channel
        db.delete(rssItemTableName, "channelId = " + channelId, null);
        // remove the channel
        db.delete(rssChannelTableName, "title = " + "'" + channelTitle + "'", null);
    }
    public static List<RssChannel> getAllRssChannels(Context context)
    {
        List<RssChannel> result = new ArrayList<RssChannel>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + rssChannelTableName + ";", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                RssChannel c = new RssChannel();
                c.setId(cursor.getInt(cursor.getColumnIndex("id")));
                c.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                c.setLink(cursor.getString(cursor.getColumnIndex("link")));
                c.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                result.add(c);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
    public static RssChannel getRssChannel(Context context, List<RssChannel> rssChannels, int channelId)
    {
        RssChannel result = null;
        for (RssChannel c : rssChannels)
        {
            if (c.getId() == channelId)
            {
                result = c;
                break;
            }
        }
        return  result;
    }
    public static long addRssItem(Context context, String title, String link, String description, int channelId)
    {
        long rssItmId = -1;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + rssItemTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, link TEXT UNIQUE NOT NULL, description TEXT, read INTEGER NOT NULL, channelId INTEGER NOT NULL);");
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("link", link);
        values.put("description", description);
        values.put("read", 0);
        values.put("channelId", channelId);
        try
        {
            rssItmId = db.insertOrThrow(rssItemTableName, null, values);
        }
        catch (SQLiteConstraintException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag) , e.toString());
        }
        return  rssItmId;
    }
    public static int addDomainRssItems(Context context, long rssItemId, List<SemanticAnalyzerDomain> semanticAnalyzerDomains, List<Domain> allDomains)
    {
        int addedDomainRssItemsCount = 0;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + domainRssItemTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, domainId INTEGER NOT NULL, rssItemId INTEGER NOT NULL, weight REAL NOT NULL, UNIQUE(domainId, rssItemId));");
        for (SemanticAnalyzerDomain sad : semanticAnalyzerDomains)
        {
            int domainId = getDomain(context, sad.getName(), allDomains).getId();
            ContentValues values = new ContentValues();
            values.put("domainId", domainId);
            values.put("rssItemId", rssItemId);
            values.put("weight", sad.getWeight());
            try
            {
                db.insertOrThrow(domainRssItemTableName, null, values);
                addedDomainRssItemsCount++;
            }
            catch (SQLiteConstraintException e)
            {
                Log.d(context.getResources().getString(R.string.log_tag) , e.toString());
            }
        }
        return addedDomainRssItemsCount;
    }
    public static RssItem getRssItem(Context context, String rssItemLink)
    {
        RssItem result = null;
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + rssItemTableName + " WHERE link = ? ;", new String[] { rssItemLink });
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            result = new RssItem();
            result.setId(cursor.getInt(cursor.getColumnIndex("id")));
            result.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            result.setLink(cursor.getString(cursor.getColumnIndex("link")));
            result.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            result.setRead(cursor.getInt(cursor.getColumnIndex("read")));
            result.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
        }
        cursor.close();
        return result;
    }
    public static List<RssItem> getAllRssItems(Context context)
    {
        ArrayList<RssItem> result = new ArrayList<RssItem>();
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + rssItemTableName + " ORDER BY id DESC ;", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                RssItem i = new RssItem();
                i.setId(cursor.getInt(cursor.getColumnIndex("id")));
                i.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                i.setLink(cursor.getString(cursor.getColumnIndex("link")));
                i.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                i.setRead(cursor.getInt(cursor.getColumnIndex("read")));
                i.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
                result.add(i);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
    public static int getCountOfAllRssItemsForDomain(Context context, String domainName, List<Domain> allDomains)
    {
        int result = 0;
        Cursor cursor = null;
        // get domain id
        int domainId = getDomain(context, domainName, allDomains).getId();
        cursor = null;
        try
        {
            Config.acceptedWeightThreshold = GeneralHelper.loadFloatFromSharedPreferences(context, "strictness");
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + domainRssItemTableName + " WHERE domainId =" + domainId + " AND weight >= " + Config.acceptedWeightThreshold + " ;", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            result = cursor.getInt(0);
        }
        cursor.close();
        return  result;
    }
    public static List<RssItem> getAllRssItemsForDomain(Context context, String domainName, List<Domain> allDomains)
    {
        List<RssItem> result = new ArrayList<RssItem>();
        Cursor cursor = null;
        // get domain id
        int domainId = getDomain(context, domainName, allDomains).getId();
        // get rss items ids
        List<Integer> rssItemsIds = new ArrayList<Integer>();
        cursor = null;
        try
        {
            Config.acceptedWeightThreshold = GeneralHelper.loadFloatFromSharedPreferences(context, "strictness");
            cursor = db.rawQuery("SELECT * FROM " + domainRssItemTableName + " WHERE domainId =" + domainId + " AND weight >= " + Config.acceptedWeightThreshold + " ;", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return  result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                int rssItemId = cursor.getInt(cursor.getColumnIndex("rssItemId"));
                rssItemsIds.add(rssItemId);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        // get rss items
        if (rssItemsIds.size() == 0)
        {
            return result;
        }
        cursor = null;
        String inClause = rssItemsIds.toString().replace("[", "(").replace("]",")");
        try
        {
            cursor = db.rawQuery("SELECT * FROM " + rssItemTableName + " WHERE id IN " + inClause + " ORDER BY id DESC ;", null);
        }
        catch (SQLiteException e)
        {
            Log.d(context.getResources().getString(R.string.log_tag), e.toString());
            return result;
        }
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do
            {
                RssItem i = new RssItem();
                i.setId(cursor.getInt(cursor.getColumnIndex("id")));
                i.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                i.setLink(cursor.getString(cursor.getColumnIndex("link")));
                i.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                i.setRead(cursor.getInt(cursor.getColumnIndex("read")));
                i.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
                result.add(i);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return result;
    }
}
