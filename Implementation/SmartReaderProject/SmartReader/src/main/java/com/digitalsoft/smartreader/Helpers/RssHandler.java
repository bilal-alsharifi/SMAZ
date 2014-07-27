package com.digitalsoft.smartreader.Helpers;

import com.digitalsoft.smartreader.Entities.RssItem;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Bilalo89 on 7/6/13.
 */
public class RssHandler extends DefaultHandler
{
    private List<RssItem> rssItems;
    private RssItem currentRssItem;
    private StringBuffer chars;

    public RssHandler()
    {
        rssItems = new ArrayList<RssItem>();
        currentRssItem = new RssItem();
    }

    public List<RssItem> getItems()
    {
        return rssItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        chars = new StringBuffer();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        chars.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (localName.equalsIgnoreCase("item"))
        {
            rssItems.add(currentRssItem);
            currentRssItem = new RssItem();
        }
        else if (localName.equalsIgnoreCase("title"))
        {
            currentRssItem.setTitle(chars.toString());
        }
        else if (localName.equalsIgnoreCase("link"))
        {
            currentRssItem.setLink(chars.toString());
        }
        else if (localName.equalsIgnoreCase("description"))
        {
            currentRssItem.setDescription(chars.toString());
        }
    }
}
