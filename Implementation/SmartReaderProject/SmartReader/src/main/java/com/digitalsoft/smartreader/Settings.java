package com.digitalsoft.smartreader;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.digitalsoft.smartreader.Helpers.GeneralHelper;

public class Settings extends Activity
{
    private SeekBar sb;
    private TextView tv;
    private Switch sw;
    private Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // enable back in navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);


        context = this;
        sb = (SeekBar) findViewById(R.id.strictness_seekBar);
        tv = (TextView) findViewById(R.id.strictness_textView);
        sw = (Switch) findViewById(R.id.showNewsCount_switch);

        // get saved progress value
        int progress = Math.round(GeneralHelper.loadFloatFromSharedPreferences(context, "strictness") * 100);
        sb.setProgress(progress);
        tv.setText(context.getResources().getString(R.string.strictness) + " (" + progress + "%)");

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                tv.setText(context.getResources().getString(R.string.strictness) + " (" + progress + "%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                int progress = seekBar.getProgress();
                GeneralHelper.saveFloatInSharedPreferences(context, "strictness", progress/100.0f);
            }
        });

        // get saved show count state
        String showCount = GeneralHelper.loadStringFromSharedPreferences(context, "showCount");
        if (showCount == null || showCount == "false")
        {
            sw.setChecked(false);
        }
        else
        {
            sw.setChecked(true);
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    GeneralHelper.saveStringInSharedPreferences(context, "showCount", "true");
                }
                else
                {
                    GeneralHelper.saveStringInSharedPreferences(context, "showCount", "false");
                }
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
