package helpers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.KeepEverythingExtractor;

public class FileReaderHelper 
{
	public static String read(String fileName) throws IOException, BoilerpipeProcessingException
	{
		String text = null;
		String ext = getFileExtesnion(fileName);
		if (ext == null || ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html"))
		{
			text = readHTM(fileName) ;
		}
		else if (ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("text"))
		{
			text = readTXT(fileName);
		}
		else if (ext.equalsIgnoreCase("doc"))
		{
			text = readDOC(fileName);
		}
		else if (ext.equalsIgnoreCase("docx"))
		{
			text = readDOCX(fileName);
		}
		else if (ext.equalsIgnoreCase("pdf"))
		{
			text = readPDF(fileName);
		}
		else
		{
			text = readHTM(fileName);
		}
		return text;
	}
	private static String readPDF(String fileName) throws IOException
	{
		String text = null;
		InputStream in = null;
		if (isURL(fileName))
		{
			URL url = new URL(fileName);
			in = url.openStream();
		}
		else
		{
			File file = new File(fileName);	
			in = new FileInputStream(file);
		}
		PDFParser parser = new PDFParser(in);
		parser.parse();
		COSDocument cosDoc = parser.getDocument();
		PDFTextStripper pdfStripper = new PDFTextStripper();
		PDDocument pdDoc = new PDDocument(cosDoc);
		text = pdfStripper.getText(pdDoc);
		pdDoc.close();
		cosDoc.close();
		in.close();
		return text;
	}
	private static String readDOC(String fileName) throws IOException
	{
		String text = null;	
		InputStream in = null;
		if (isURL(fileName))
		{
			URL url = new URL(fileName);
			in = url.openStream();
		}
		else
		{
			File file = new File(fileName);	
			in = new FileInputStream(file);
		}		
		NPOIFSFileSystem fs = new NPOIFSFileSystem(in);
		WordExtractor extractor = new WordExtractor(fs.getRoot());
		text = "";
		for(String rawText : extractor.getParagraphText()) 
		{
			text += WordExtractor.stripFields(rawText);
		}
		fs.close();
		in.close();
		return text;
	}
	private static String readDOCX(String fileName) throws IOException
	{
		String text = null;
		InputStream in = null;
		if (isURL(fileName))
		{
			URL url = new URL(fileName);
			in = url.openStream();
		}
		else
		{
			File file = new File(fileName);	
			in = new FileInputStream(file);
		}
		XWPFDocument doc = new XWPFDocument(in);
		XWPFWordExtractor wordxExtractor = new XWPFWordExtractor(doc);
        text = wordxExtractor.getText();	
		in.close();
		return text;
	}
	private static String readHTM(String fileName) throws BoilerpipeProcessingException, MalformedURLException, IOException
	{
		String text = null;
		String htmlText = readTXT(fileName);
		text = getTextFromHTMLText(htmlText);
		return text;
	}
	public static String getTextFromHTMLText(String htmlText) throws BoilerpipeProcessingException, FileNotFoundException
	{
		String text = null;
		if (htmlText != null)
		{
			text = KeepEverythingExtractor.INSTANCE.getText(htmlText);
		}
		if (text != null)
		{
			text += getAdditionalTagsFromHTM(htmlText);
		}
		return text;
	}
	private static String getAdditionalTagsFromHTM(String htmlText) throws FileNotFoundException
	{
		String text = "";
		String newLine = System.getProperty("line.separator");
		Document doc = Jsoup.parse(htmlText);
		// get page title
		text += doc.select("title").text();
		// get image alternative text
		Elements elements = doc.select("img");
		for (Element e : elements)
		{
			text += newLine + e.attr("alt");
		}	
		return text;
	}
	private static String readTXT(String fileName) throws MalformedURLException, IOException
	{
		String text = null;
		InputStream in = null;
		if (isURL(fileName))
		{
			URL url = new URL(fileName);
			in = url.openStream();
		}
		else
		{
			File file = new File(fileName);
			in = new FileInputStream(file);
		}
		Scanner scanner = new Scanner(in);
		scanner.useDelimiter("\\Z");
		text = scanner.next();
		scanner.close();
		in.close();
		return text;
	}
	private static String getFileExtesnion(String fileName)
	{
		String ext = null;
		int slashIndex = fileName.lastIndexOf('/');
		if (slashIndex != -1)
		{
			fileName = fileName.substring(slashIndex + 1);
		}
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex != -1)
		{
			ext = fileName.substring(dotIndex + 1);
		}
		return ext;
	}
	public static Boolean isURL(String fileName)
	{
		Boolean result = false;
		String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		if (fileName.matches(urlRegex))
		{
			result = true;
		}
		return result;
	}

}
