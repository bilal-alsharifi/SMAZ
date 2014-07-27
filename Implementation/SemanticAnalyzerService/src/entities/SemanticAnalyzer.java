package entities;
import java.rmi.*;
import java.util.List;

public interface SemanticAnalyzer extends Remote
{
	public Boolean load(Boolean showErrors, Boolean coreferenceEnabled) throws RemoteException;
	public String processRequest(String input, Boolean inputIsText, Boolean sortDocDomainsByWeight, Boolean addWeightsToAncestors, int shortWordsLimit, List<String> acceptedTags, Boolean searchInDbPedia, List<String> dbPediaPredicates, int dbPediaResultsLimit, int disambiguationMethod, int leskWindowSize, int leskComparisonWay, Boolean withCoreference) throws RemoteException;
}

