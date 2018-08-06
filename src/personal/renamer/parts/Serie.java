package personal.renamer.parts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.renamer.MieSerie;
import personal.renamer.exceptions.StringNotFoundException;

public class Serie {
	
	private File fileSerie;
	private String nomeSerie;
	
	private HashMap<Integer, String> URLs;
	private boolean foundURLs = false;
	
	public Serie(File fileSerie) {
		this.fileSerie = fileSerie;
		nomeSerie = nomeSerie();
		URLs = findURLs();
	}
	
	
	public boolean isFoundURLs() {
		return foundURLs;
	}
	
	public String getURL(int key) {
		return URLs.get(key);
	}

	public String getNomeSerie() {
		return this.nomeSerie;
	}

	public File getFileSerie() {
		return fileSerie;
	}
	
	private String nomeSerie()
	{
		if(fileSerie.getName().equals("Agents Of S.H.I.E.L.D"))
			return "Agents Of S.H.I.E.L.D.";
		return fileSerie.getName();
	}
	
	public static boolean verificaFile(File file) {
		
		return file.isDirectory() ? true : false;
	}
	
	public HashMap<Integer, String> findURLs() {
		
		File FileURLs = new File(fileSerie.getAbsolutePath() + "\\" + MieSerie.URLSFILE);
		HashMap<Integer, String> URLs = new HashMap<Integer, String>();
		
		if(FileURLs.exists()) {
			foundURLs = true;
			//entro e leggo la mappa
			
			try {
			BufferedReader input = new BufferedReader( new FileReader( FileURLs ) );
			String line = "";
			String links = "";
			while((line = input.readLine()) != null) {
				links += line;
			}
			Matcher numeroStagione = Pattern.compile("Stagione \\d+").matcher(links);
			Matcher url = Pattern.compile(": http.+? --- ").matcher(links);
			while(numeroStagione.find() && url.find()) {
				URLs.put(Integer.parseInt(links.substring(numeroStagione.start()+9, numeroStagione.end())), links.substring(url.start()+2, url.end()-5));
			}
			input.close();
			if(URLs.isEmpty())
				throw new StringNotFoundException();
			}catch(Exception e) {
				e.printStackTrace();
				foundURLs = false;
				return new HashMap<Integer, String>();
			}
			return URLs;
		} else
		{
			foundURLs = false;
			return new HashMap<Integer, String>();
		}

	}

}
