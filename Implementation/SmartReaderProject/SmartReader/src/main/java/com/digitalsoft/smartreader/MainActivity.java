package com.digitalsoft.smartreader;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.digitalsoft.smartreader.Entities.Domain;
import com.digitalsoft.smartreader.Entities.RssChannel;
import com.digitalsoft.smartreader.Entities.RssItem;
import com.digitalsoft.smartreader.Helpers.Config;
import com.digitalsoft.smartreader.Helpers.CustomListViewAdapterForRssItems;
import com.digitalsoft.smartreader.Helpers.DataBaseHelper;
import com.digitalsoft.smartreader.Helpers.GeneralHelper;
import com.digitalsoft.smartreader.Helpers.WebHelper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<Domain> allDomains;
    private List<Domain> checkedDomains;
    private List<RssChannel> rssChannels;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // enable back in navigation bar
        getActionBar().setDisplayHomeAsUpEnabled(true);


        //my code
        context = this;
        GeneralHelper.getOverflowMenu(context); //solve a problem that the overflow menu button is not shown in some deivces
        DataBaseHelper.openDatabase(context);
        GeneralHelper.doFirstLaunchInitialization(context);
        allDomains = DataBaseHelper.getAllDomains(context);
        WebHelper.loadAllRssItemsForAllRssChannels(context, allDomains);
        //------------

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main options menu content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectItem(i);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )
        {
            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    // to reload rss channels when coming from another activity
    @Override
    protected void onResume()
    {
        rssChannels = DataBaseHelper.getAllRssChannels(context);
        allDomains = DataBaseHelper.getAllDomains(context);
        checkedDomains = DataBaseHelper.getAllCheckedDomains(context, allDomains);

        setDrawerList();

        if (checkedDomains.size() > 0)
        {
            selectItem(0);
        }
        super.onResume();
    }

    private void setDrawerList()
    {
        String showCount = GeneralHelper.loadStringFromSharedPreferences(context, "showCount");
        if (showCount == null || !showCount.equalsIgnoreCase("true"))
        {
            ArrayAdapter<Domain> adapter = new ArrayAdapter<Domain>(context, R.layout.drawer_list_item, checkedDomains);
            mDrawerList.setAdapter(adapter);
        }
        else
        {
            List<String> checkedDomainsWithCount = new ArrayList<String>();
            for (Domain d : checkedDomains)
            {
                int count = DataBaseHelper.getCountOfAllRssItemsForDomain(context, d.getName(), allDomains);
                String dWithCount = d.getName() + " (" + count + ")";
                checkedDomainsWithCount.add(dWithCount);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.drawer_list_item, checkedDomainsWithCount);
            mDrawerList.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainoptionsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_channels).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId())
        {
            case R.id.action_channels:
            {
                Intent intent = new Intent(context, Channels.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_domains:
            {
                Intent intent = new Intent(context, Domains.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings:
            {
                Intent intent = new Intent(context, Settings.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_about:
            {
                Intent intent = new Intent(context, About.class);
                startActivity(intent);
                return true;
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }



    private void selectItem(int position)
    {
        setDrawerList();
        // update the mainoptionsmenu content by replacing fragments
        Fragment fragment = new DomainFragment();
        Bundle args = new Bundle();
        args.putInt(DomainFragment.ARG_DOMAIN_POSITION, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(checkedDomains.get(position).getName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class DomainFragment extends Fragment
    {
        public static final String ARG_DOMAIN_POSITION = "domain_position";
        public DomainFragment()
        {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_domain, container, false);
            int position = getArguments().getInt(ARG_DOMAIN_POSITION);
            String domainName = checkedDomains.get(position).getName();
            getActivity().setTitle(domainName);


            // my code
            final List<RssItem> rssItems = DataBaseHelper.getAllRssItemsForDomain(context, domainName, allDomains);
            Toast.makeText(context, getString(R.string.thereAre) + " " +rssItems.size() + " " + getString(R.string.newsToRead), Toast.LENGTH_SHORT).show();

            ListView lv = (ListView)rootView.findViewById(R.id.listView);
            CustomListViewAdapterForRssItems adapter = new CustomListViewAdapterForRssItems(context, rssItems, rssChannels);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    GeneralHelper.browseURL(context, rssItems.get(i).getLink());
                }
            });
            //------------

            return rootView;
        }
    }
}