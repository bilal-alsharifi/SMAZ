package com.digitalsoft.smartreader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.Helpers.DataBaseHelper;

import java.util.List;

public class Channels extends Activity
{

    private ListView lv;
    private Activity context;
    private ArrayAdapter<RssChannel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);

        // enable back in navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        lv = (ListView) findViewById(R.id.channels_listView);

        loadRssChannels();

        //to enable the context menu
        registerForContextMenu(lv);

        //to show context menu not only when long press but also when short press
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                view.showContextMenu();
            }
        });
    }

    public void loadRssChannels()
    {
        List<RssChannel> rssChannels = DataBaseHelper.getAllRssChannels(context);
        adapter = new ArrayAdapter<RssChannel>(context, android.R.layout.simple_list_item_1 , rssChannels);
        lv.setAdapter(adapter);
    }

    // to reload the channels when coming from another activity
    @Override
    protected void onResume()
    {
        loadRssChannels();
        super.onResume();
    }

    // options menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channelsoptionsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_add_channel)
        {
            Intent intent = new Intent(context, AddChannel.class);
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // end options menu methods

    //context menu methods
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.channelscontextmenu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info;
        try
        {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        }
        catch (ClassCastException e)
        {
            return false;
        }
        int selectedItemPosition = (int) adapter.getItemId(info.position);
        RssChannel rssChannel = (RssChannel)lv.getItemAtPosition(selectedItemPosition);
        if (item.getItemId() == R.id.remove_menuItem)
        {
            DataBaseHelper.removeRssChannel(context, rssChannel.getTitle());
            adapter.remove(rssChannel);
            adapter.notifyDataSetChanged();
        }
        else if (item.getItemId() == R.id.edit_menuItem)
        {
            Intent intent = new Intent(context, EditChannel.class);
            intent.putExtra("rssChannelTitle", rssChannel.getTitle());
            intent.putExtra("rssChannelId", rssChannel.getId());
            context.startActivity(intent);
        }
        return true;
    }
    // end context menu methods

}
