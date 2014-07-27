package com.digitalsoft.smartreader.Entities;

import com.digitalsoft.smartreader.Helpers.DataBaseHelper;

/**
 * Created by Bilalo89 on 7/6/13.
 */
public class Domain
{
    private int id;
    private String name;
    private int checked;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getChecked()
    {
        return checked;
    }

    public void setChecked(int checked)
    {
        this.checked = checked;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }
}
