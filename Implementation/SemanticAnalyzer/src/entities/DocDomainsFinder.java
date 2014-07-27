package entities;
import helpers.ExDomainsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DocDomainsFinder
{
	private List<SynsetDomain> docDomains = null;
	public DocDomainsFinder(List<String> domainFilesNames)
	{
		this.docDomains = new ArrayList<SynsetDomain>();
		for (String domainFileName : domainFilesNames)
		{
			String domainName = domainFileName.substring(0, domainFileName.length() - 4);
			SynsetDomain sd = new SynsetDomain(domainName, 0);
			docDomains.add(sd);
		}
	}
	public void addDomainsForSynset(List<SynsetDomain> synsetDomains)
	{
		if (synsetDomains != null)
		{
			for (int i = 0; i < docDomains.size(); i++)
			{
				docDomains.get(i).addToWeight(synsetDomains.get(i).getWeight());
			}
		}
	}
	private List<SynsetDomain> addWeightsToAncestors(List<SynsetDomain> docDomains)
	{
		List<SynsetDomain> result = new ArrayList<SynsetDomain>();
		// copy list
		for (SynsetDomain sd : docDomains)
		{
			result.add(new SynsetDomain(sd.getDomainName(), sd.getWeight()));
		}
		for (SynsetDomain sd : result)
		{
			List<Integer> ancestors = ExDomainsHelper.getDomainAncestors(sd.getDomainName());
			for (Integer a : ancestors)
			{
				if (a != -1)
				{
					result.get(a).addToWeight(sd.getWeight());
				}
			}
		}
		return result;
	}
	private List<SynsetDomain> normalizeDocDomainsWeights(List<SynsetDomain> docDomains)
	{
		List<SynsetDomain> result = new ArrayList<SynsetDomain>();
		float maxWeight = 0;
		// copy list and find max weight
		for (SynsetDomain sd : docDomains)
		{
			if (sd.getWeight() > maxWeight)
			{
				maxWeight = sd.getWeight();
			}
			result.add(new SynsetDomain(sd.getDomainName(), sd.getWeight()));
		}
		// normalize the weights
		float normalizedWeight;
		for (SynsetDomain sd : result)
		{		
			if (maxWeight == 0)
			{
				normalizedWeight = sd.getWeight();
			}
			else
			{
				normalizedWeight = sd.getWeight() / maxWeight;
			}		
			sd.setWeight(normalizedWeight);
		}
		return result;
	}
	public List<SynsetDomain> getDocDomains(Boolean sortByWeight, Boolean addWeightsToAncestors)
	{
		List<SynsetDomain> result; 
		if (addWeightsToAncestors)
		{
			result = this.addWeightsToAncestors(this.docDomains);
		}
		else
		{
			result = this.docDomains;
		}
		result = this.normalizeDocDomainsWeights(result);
		if (sortByWeight)
		{
			Collections.sort(result, new SynsetDomain());
		}
		return result;
	}
}
