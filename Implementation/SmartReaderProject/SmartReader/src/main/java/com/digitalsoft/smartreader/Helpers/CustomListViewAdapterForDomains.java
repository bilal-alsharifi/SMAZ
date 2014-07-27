package com.digitalsoft.smartreader.Helpers;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.Entities.RssItem;
import com.digitalsoft.smartreader.R;

import java.util.List;

public class CustomListViewAdapterForDomains extends BaseAdapter
{
    private LayoutInflater inflater;
    private List<Domain> domains;
    private List<RssChannel> rssChannels;
    private Activity context;

    public CustomListViewAdapterForDomains(Activity context, List<Domain> domains)
    {  
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context); 
        this.domains =  domains;
    }  
    public int getCount() 
    {  
        return domains.size();
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
    	CheckBox domain_cb;
    }
    public View getView(int position, View convertView, ViewGroup parent) 
    {     
    	ViewHolder holder;
    	if (convertView == null)
    	{
    		convertView = inflater.inflate(R.layout.custom_list_view_for_domains, null);
    		holder = new ViewHolder();
            holder.domain_cb = (CheckBox) convertView.findViewById(R.id.domain_checkBox);
            holder.domain_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    String domainName = compoundButton.getText().toString();
                    int domainChecked = 0;
                    if (b)
                    {
                        domainChecked = 1;
                    }
                    DataBaseHelper.editDomain(context, domainName, domainChecked);
                    for (Domain d : domains)
                    {
                        if (d.getName().equalsIgnoreCase(domainName))
                        {
                            if (b)
                            {
                                d.setChecked(1);
                            }
                            else
                            {
                                d.setChecked(0);
                            }
                            break;
                        }
                    }
                }
            });
            convertView.setTag(holder);
    	}
    	else
    	{
    		 holder = (ViewHolder) convertView.getTag();
    	}
        Domain domain = domains.get(position);
    	holder.domain_cb.setText(domain.getName());
        if (domain.getChecked() == 1)
        {
            holder.domain_cb.setChecked(true);
        }
        else
        {
            holder.domain_cb.setChecked(false);
        }
        return convertView;
    }  
} 