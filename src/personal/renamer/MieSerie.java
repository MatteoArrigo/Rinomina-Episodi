package personal.renamer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class MieSerie {
	
//	public static final File serie_dir = new File("E:\\Serie Tv\\Serie Tv Supereroi");
	public static final File serie_dir = new File("C:\\Users\\Matteo\\eclipse-workspace\\Rinomina Episodi\\src\\personal\\renamer\\Prova");
	public static final File fileReport = new File(serie_dir.getAbsolutePath() + "\\Report.txt"); //report principale
	public static final String regexReport = "Report[_[\\d]+]*";
	public static final String URLSFILE = "URLs.txt";
	
	public static final int IT = 0;
	public static final int EN = 1;
	public static final String subIta = "SubIta";
	
	public static final HashSet<String> terminators = new HashSet<String>(Arrays.asList(	"hardsub",
																							"ITA",
																							"WEBMux",
																							"subita",
																							"Subbed",
																							"FastSubITA",
																							"iTALiAN",
																							"WEB-DLMux" ));
	public static final HashSet<String> estensioni = new HashSet<String>(Arrays.asList(	"avi",
																						"mp4",
																						"mkv",
																						"srt", //da
																						""));//vedere
	public static final ArrayList<Pattern> format = new ArrayList<Pattern>(Arrays.asList(	Pattern.compile("\\d+[xXeE]\\d+"),
																							Pattern.compile("\\d+\\.[eE]\\d+") ));
	public static final HashSet<Pattern> coseStrane = new HashSet<Pattern>(Arrays.asList(	Pattern.compile("[\\.]{4}"),
																							Pattern.compile("\\p{Upper}\\.\\p{Upper}\\."),
																							Pattern.compile("[xXeE][\\d]+[\" \"]*[-]*[\" \"]*[\\.]+") ));

	//Separatori nel nome finale dei file
	public static final String SERIE_STAGIONE = " - ";
	public static final String STAGIONE_EPISODIO = "x";
	public static final String EPISODIO_TITOLO = " - ";
	public static final String TITOLO_LINGUA = " - ";
	
	public static void checkMainDir() {
		
		if(!serie_dir.exists())
		{
			System.out.println("Il file specificato non è stato trovato.");
			System.exit(0);
		}
	}
	
	public static void checkFileReport() {
		
		if(!fileReport.exists())
		{
			try {
				fileReport.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
}
