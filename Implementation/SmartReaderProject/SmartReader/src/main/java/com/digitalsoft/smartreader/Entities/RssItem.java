package com.digitalsoft.smartreader.Entities;

/**
 * Created by Bilalo89 on 7/6/13.
 */
public class RssItem
{
    private int id;
    private String title;
    private String link;
    private String description;
    private int read;
    private int channelId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getRead()
    {
        return read;
    }

    public void setRead(int read)
    {
        this.read = read;
    }

    public int getChannelId()
    {
        return channelId;
    }

    public void setChannelId(int channelId)
    {
        this.channelId = channelId;
    }

    @Override
    public String toString()
    {
        return this.getTitle();
    }
}
