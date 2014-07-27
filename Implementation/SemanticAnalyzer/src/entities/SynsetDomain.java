package entities;

import java.util.Comparator;

public class SynsetDomain implements Comparator<SynsetDomain>
{
	private String domainName;
	private float weight;
	public SynsetDomain()
	{
		
	}
	public SynsetDomain (String domainName, float weight)
	{
		this.domainName = domainName;
		this.weight = weight;
	}
	public String getDomainName()
    {
        return this.domainName;
    }
    public void setWeight(float weight)
    {
        this.weight = weight;
    }
    public float getWeight()
    {
        return this.weight;
    }
    public void addToWeight(float weight)
    {
        this.weight += weight;
    }
    @Override
    public String toString()
    {
        return this.domainName + "=" + this.weight;
    }
	@Override
	public int compare(SynsetDomain arg0, SynsetDomain arg1) 
	{
		Float weight0 = arg0.weight;
		Float weight1 = arg1.weight;
		return weight1.compareTo(weight0);
	}
}
