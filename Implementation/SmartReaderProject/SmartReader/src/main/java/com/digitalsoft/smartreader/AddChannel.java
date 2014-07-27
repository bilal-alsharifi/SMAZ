package com.digitalsoft.smartreader;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Helpers.DataBaseHelper;
import com.digitalsoft.smartreader.Helpers.WebHelper;

import java.util.List;

public class AddChannel extends Activity
{
    private Activity context;
    private Button submit_button;
    private EditText title_editText;
    private EditText link_editText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel);


        context = this;
        submit_button = (Button) findViewById(R.id.submit_button);
        title_editText = (EditText) findViewById(R.id.title_editText);
        link_editText = (EditText) findViewById(R.id.link_editText);
        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = title_editText.getText().toString();
                String link = link_editText.getText().toString();
                if (title.trim().length() > 0 && Patterns.WEB_URL.matcher(link).matches())
                {
                    Boolean addedSuccessfully = DataBaseHelper.addRssChannel(context, title, link, "");
                    if (addedSuccessfully)
                    {
                        List<Domain> allDomains = DataBaseHelper.getAllDomains(context);
                        WebHelper.loadAllRssItemsForAllRssChannels(context, allDomains);
                        Toast.makeText(context, getString(R.string.channel_added_successfullt_message), Toast.LENGTH_SHORT).show();
                        context.finish();
                    }
                    else
                    {
                        Toast.makeText(context, getString(R.string.channel_exists_message), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(context, getString(R.string.fill_info_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
