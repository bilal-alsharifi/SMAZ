package com.digitalsoft.smartreader.Entities;

/**
 * Created by Bilalo89 on 7/10/13.
 */
public class SemanticAnalyzerDomain
{
    private String name;
    private String op;
    private float weight;
    public SemanticAnalyzerDomain(String domainString)
    {
        this.setOp("=");
        int indexOfOp = domainString.indexOf(this.getOp());
        this.setName(domainString.substring(0, indexOfOp));
        this.setWeight(Float.parseFloat(domainString.substring(indexOfOp + getOp().length(), domainString.length())));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOp()
    {
        return op;
    }

    public void setOp(String op)
    {
        this.op = op;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    @Override
    public String toString()
    {
        return this.getName() + this.getOp() + this.getWeight();
    }
}
