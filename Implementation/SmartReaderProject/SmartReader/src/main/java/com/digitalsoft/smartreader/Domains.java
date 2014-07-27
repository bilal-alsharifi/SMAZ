package com.digitalsoft.smartreader;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Helpers.CustomListViewAdapterForDomains;
import com.digitalsoft.smartreader.Helpers.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Domains extends Activity
{
    private Activity context;
    private ListView lv;
    private CheckBox checkAll_checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.domains);

        // enable back in navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        lv = (ListView) findViewById(R.id.domains_listView);
        checkAll_checkBox = (CheckBox) findViewById(R.id.checkAll_checkBox);

        List<Domain> domains = DataBaseHelper.getAllDomains(context);
        CustomListViewAdapterForDomains adapter = new CustomListViewAdapterForDomains(context, domains);
        lv.setAdapter(adapter);


        checkAll_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    DataBaseHelper.editDomains(context, 1);
                }
                else
                {
                    DataBaseHelper.editDomains(context, 0);
                }
                List<Domain> domains = DataBaseHelper.getAllDomains(context);
                CustomListViewAdapterForDomains adapter = new CustomListViewAdapterForDomains(context, domains);
                lv.setAdapter(adapter);
            }
        });
    }

    // enable back in navigation bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
