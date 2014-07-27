package com.digitalsoft.smartreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digitalsoft.smartreader.Helpers.DataBaseHelper;
import com.digitalsoft.smartreader.Helpers.WebHelper;

public class EditChannel extends Activity
{
    private Activity context;
    private Button submit_button;
    private EditText title_editText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_channel);


        context = this;
        submit_button = (Button) findViewById(R.id.submit_button);
        title_editText = (EditText) findViewById(R.id.title_editText);
        String rssChannelTitle = getIntent().getExtras().getString("rssChannelTitle");
        final int rssChannelId = getIntent().getExtras().getInt("rssChannelId");
        title_editText.setText(rssChannelTitle);

        submit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newTitle = title_editText.getText().toString();

                if (newTitle.trim().length() > 0)
                {
                    DataBaseHelper.editRssChannel(context, rssChannelId, newTitle);
                    Toast.makeText(context, getString(R.string.channel_edited_successfullt_message), Toast.LENGTH_SHORT).show();
                    context.finish();
                }
                else
                {
                    Toast.makeText(context, getString(R.string.fill_info_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
