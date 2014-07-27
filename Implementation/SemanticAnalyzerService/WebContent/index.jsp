<%@page import="java.net.ConnectException"%>
<%@ page language="java" contentType="text/html; charset=windows-1256" pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Semantic Analyzer Service</title>
</head>
<body>
	<%@page import="entities.*" %>
	<%@page import="java.rmi.registry.*" %>
	<%@page import="java.util.*" %>
	<% 
		int port = 3234;
		String serverIP = "127.0.0.1";
		Registry registry = LocateRegistry.getRegistry(serverIP, port);
		SemanticAnalyzer sa = (SemanticAnalyzer)registry.lookup("sa");
		
		try
		{
			String input = request.getParameter("input");
			Boolean inputIsText = Boolean.parseBoolean(request.getParameter("inputIsText"));
			Boolean sortDocDomainsByWeight = Boolean.parseBoolean(request.getParameter("sortDocDomainsByWeight"));
			Boolean addWeightsToAncestors = Boolean.parseBoolean(request.getParameter("addWeightsToAncestors"));
			int shortWordsLimit = Integer.parseInt(request.getParameter("shortWordsLimit"));
			List<String> acceptedTags = new ArrayList<String>(Arrays.asList(request.getParameter("acceptedTags").split(",")));
			Boolean searchInDbPedia = Boolean.parseBoolean(request.getParameter("searchInDbPedia"));
			List<String> dbPediaPredicates = new ArrayList<String>(Arrays.asList(request.getParameter("dbPediaPredicates").split(",")));
			int dbPediaResultsLimit = Integer.parseInt(request.getParameter("dbPediaResultsLimit"));
			int disambiguationMethod = Integer.parseInt(request.getParameter("disambiguationMethod"));	
			int leskWindowSize = Integer.parseInt(request.getParameter("leskWindowSize"));	
			int leskComparisonWay = Integer.parseInt(request.getParameter("leskComparisonWay"));		 	
			Boolean withCoreference = Boolean.parseBoolean(request.getParameter("withCoreference"));				
			String result = sa.processRequest(input, inputIsText, sortDocDomainsByWeight, addWeightsToAncestors, shortWordsLimit, acceptedTags, searchInDbPedia, dbPediaPredicates, dbPediaResultsLimit, disambiguationMethod, leskWindowSize, leskComparisonWay, withCoreference);	
			out.print("<pre>" + result + "</pre>"); 
		}
		catch (NumberFormatException e)
		{
			out.print("some of the parameters are missing"); 
		}
		
	%>
</body>
</html>