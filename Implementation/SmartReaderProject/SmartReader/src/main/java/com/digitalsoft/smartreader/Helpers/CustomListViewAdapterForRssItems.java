package com.digitalsoft.smartreader.Helpers;


import java.util.List;

import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.R;
import com.digitalsoft.smartreader.Entities.RssItem;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListViewAdapterForRssItems extends BaseAdapter
{  	  
    private LayoutInflater inflater;
    private List<RssItem> rssItems;
    private List<RssChannel> rssChannels;
    private Activity context;
    
    public CustomListViewAdapterForRssItems(Activity context, List<RssItem> rssItems, List<RssChannel> rssChannels)
    {  
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context); 
        this.rssItems =  rssItems;
        this.rssChannels = rssChannels;
    }  
    public int getCount() 
    {  
        return rssItems.size();
    }   
    public Object getItem(int position) 
    {   
        return 0;  
    }  
    public long getItemId(int position) 
    {   
        return 0;  
    } 
    static class ViewHolder 
    {
    	TextView rssItem_tv;
        TextView rssChannel_tv;
    }
    public View getView(int position, View convertView, ViewGroup parent) 
    {     
    	ViewHolder holder;
    	if (convertView == null)
    	{
    		convertView = inflater.inflate(R.layout.custom_list_view_for_rssitems, null);
    		holder = new ViewHolder();
            holder.rssItem_tv = (TextView) convertView.findViewById(R.id.rssItem_textView);
            holder.rssChannel_tv = (TextView) convertView.findViewById(R.id.rssChannel_textView);
            convertView.setTag(holder);
    	}
    	else
    	{
    		 holder = (ViewHolder) convertView.getTag();
    	}
        RssItem rssItem = rssItems.get(position);
    	holder.rssItem_tv.setText(rssItem.getTitle());
        RssChannel rssChannel = DataBaseHelper.getRssChannel(context, rssChannels, rssItem.getChannelId());
        String rssChannelTitle = "null";
        if (rssChannel != null)
        {
            rssChannelTitle = rssChannel.getTitle();
        }
    	holder.rssChannel_tv.setText(rssChannelTitle);
        return convertView;
    }  
} 