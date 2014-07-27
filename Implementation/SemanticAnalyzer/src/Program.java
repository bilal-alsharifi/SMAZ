import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import entities.SemanticAnalyzer;
import entities.SemanticAnalyzerImpl;


public class Program 
{
	public static void main(String[] args) 
	{
		int port = 3234;
		Boolean showErrors = true;
		Boolean coreferenceEnabled = false;
		
		if (args.length >= 2)
		{
			port = Integer.parseInt(args[0]);
			showErrors = Boolean.parseBoolean(args[1]);
			coreferenceEnabled = Boolean.parseBoolean(args[2]);
		}
					
		try 
		{
			Registry registry = LocateRegistry.createRegistry(port);
			SemanticAnalyzer sa = new SemanticAnalyzerImpl();		
			Boolean successfulLoad = sa.load(showErrors, coreferenceEnabled);
			if (successfulLoad)
			{
				System.err.println("Server is connected and ready to process requests.");	
			}
			else
			{
				System.err.println("can not load the the Semantic Analyzer");
			}
			registry.rebind("sa", sa);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	

	}
}
