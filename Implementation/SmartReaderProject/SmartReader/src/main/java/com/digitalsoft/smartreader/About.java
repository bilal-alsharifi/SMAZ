package com.digitalsoft.smartreader;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends Activity 
{
	private TextView developerName_tv;
	private TextView version_tv;
	private TextView supportWebsite_tv;
	private TextView supportEmail_tv;
	private Activity context;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        // enable back in navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        context = this;
        developerName_tv = (TextView) findViewById(R.id.developerName_textView);
        version_tv = (TextView) findViewById(R.id.version_textView);
        supportWebsite_tv = (TextView) findViewById(R.id.supportWebsite_textView);
        supportEmail_tv = (TextView) findViewById(R.id.supportEmail_textView);
              
        
        developerName_tv.setText(getResources().getString(R.string.by) + " " + getResources().getString(R.string.developer_name));
        String versionName = null;      
        try 
        {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} 
        catch (NameNotFoundException e) 
        {
			e.printStackTrace();
		}
        version_tv.append(" " + versionName);
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