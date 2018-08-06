package personal.renamer.parts;
import personal.renamer.MieSerie;
import personal.renamer.Riga;
import personal.renamer.exceptions.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.*;

public class Stagione {
	
	private Serie serie;
	private File fileStagione;
	private int numeroStagione;
	private String nomeFinale;
	private int lingua;
	
	private HashMap<Integer, String> mapTitoli;
	private boolean gotMapTitoli;

	public Stagione(Serie serie, File fileStagione) {
		this.serie = serie;
		this.fileStagione = fileStagione;
		this.numeroStagione = nStagione();
		this.nomeFinale = toString();
		this.lingua = ricavaLingua();
		
		gotMapTitoli = true;		
		mapTitoli = ricavaMapTitoli();
	}

	public HashMap<Integer, String> getMapTitoli() {
		return mapTitoli;
	}

	public boolean isGotMapTitoli() {
		return gotMapTitoli;
	}

	public int getNumeroStagione(){
		return this.numeroStagione;
	}
	
	public File getFile() {
		return fileStagione;
	}
	
	public String getPath() {
		return this.fileStagione.getAbsolutePath();
	}
	
	private int nStagione()
	{
		String stagione = fileStagione.getName();
		
		Matcher numero = Pattern.compile("\\d+").matcher(stagione);
		if(!numero.find())
		{
			return 1;
		}else
		{
			stagione = stagione.replaceAll("\\D", "");
			return Integer.parseInt(stagione.trim());
		}
		
	}
	
	public static boolean verificaFile(File file) {
		return file.isDirectory() ? true : false;
	}
	
	@Override
	public String toString() {
		String titoloDaTornare = serie.getNomeSerie();
		String daAggiungere = "";
		if(lingua == MieSerie.EN)
			daAggiungere = " " + MieSerie.subIta;
		
		if(this.numeroStagione != 1)
			titoloDaTornare += " " + this.numeroStagione;
		
		titoloDaTornare += daAggiungere;
		
		return titoloDaTornare;
	}
	
	public String rinominaStagione() {
		File fileStagioneNuovo = new File( fileStagione.getParent() + "\\" + toString() );
		fileStagione.renameTo( fileStagioneNuovo );
		fileStagione = fileStagioneNuovo;
		System.out.println(nomeFinale);
		return nomeFinale;		
	}
	
	private int ricavaLingua() {
		
		if(fileStagione.getName().indexOf(MieSerie.subIta) == -1)
			return MieSerie.IT;
		else
			return MieSerie.EN;
		
	}
	
	private HashMap<Integer, String> ricavaMapTitoli() {
		
		HashMap<Integer, String> titoli = new HashMap<Integer, String>(32, 0.75f);
		
		String HTMLText = "";
		try {
		HTMLText = ricavaHTMLText();
		}catch(URLNotFoundException e) {
			System.out.println("Non è stato trovato l'URL di " + toString());
			gotMapTitoli = false;
			return new HashMap<Integer, String>();
		}catch(IOException e) {
			System.out.println("Non è stato possibile scaricare la pagina wikipedia di " + toString());
			gotMapTitoli = false;
			return null;
		}
		
		
		String tabella = ricavaTable(HTMLText);
		int titoliNonTrovati = 0;
		Matcher inizioRiga = Pattern.compile("<tr>\n").matcher(tabella);
		Matcher fineRiga = Pattern.compile("<\\/tr>").matcher(tabella);
		while(inizioRiga.find()){			
			//legge e riconosce numero episodio e titolo in ongi riga della tabella
			fineRiga.find();
			Riga riga;
			try {
			riga = new Riga(tabella.substring(inizioRiga.end(), fineRiga.start()), lingua);
			}catch(StringNotFoundException e) {
				titoliNonTrovati++;
				continue;
			}
			if(riga.isRigaTitolo())
				titoli.put(riga.getNumeroEpisodio(), riga.getTitolo());
		}
		
		if(titoli.size() <= 0){
			gotMapTitoli = false;
			return null;
		}
		if(titoliNonTrovati>=1) {
			System.out.format("Non sono stati trovati i titolo di %d episodi", titoliNonTrovati);
		}
		
		return titoli;
		
	}
	
	private String ricavaTable(String HTMLText) {
		String tabella = HTMLText.substring(HTMLText.indexOf("<table"), HTMLText.indexOf("</table>"));
		if(tabella.indexOf("<th>n") == -1) {
			tabella = ricavaTable(HTMLText.substring(HTMLText.indexOf("</table>")+9));
		}
		
		return tabella;
	}

	private String ricavaHTMLText() throws URLNotFoundException, IOException
	{
		/*
		 * Vede se c'è la mappa degli urls
		 * se c'è, vede se c'è l'url della stagione
		 * se c'è. apre una connessione e crea un input
		 * se non c'è la mappa o la stagione, chima findURL, che potrebbe lanciare un'eccezione gestita da ricavaMapTitoli
		 */
		String link = "";
		boolean linkTrovato = false;
		BufferedReader br = null;
		if(serie.isFoundURLs()) {
			if(serie.getURL(numeroStagione) != null)
			{
				link = serie.getURL(numeroStagione);
				linkTrovato = true;
			}
		}
		
		if(linkTrovato == false) {
			link = findURL();
		}
	
		try {
			br = (BufferedReader)tryConnection(link);
		}catch(Exception e) {
			throw new URLNotFoundException();
		}
		String HTMLText = "";
		String line = "";
		while( (line=br.readLine()) != null)
			HTMLText += line + "\n";
		
		if(HTMLText.indexOf("<table") == -1)
			throw new URLNotFoundException();
		
		return HTMLText;
		
		
		
	}
	
	private String findURL() throws URLNotFoundException{
		
		//passa qua solo se il file non esiste o non viene trovato l'url desideratp
		HashMap<Integer, String> numeriOrdinali = new HashMap<Integer, String>(15, 1);
		numeriOrdinali.put(1, "prima");
		numeriOrdinali.put(2, "seconda");
		numeriOrdinali.put(3, "terza");
		numeriOrdinali.put(4, "quarta");
		numeriOrdinali.put(5, "quinta");
		numeriOrdinali.put(6, "sesta");
		numeriOrdinali.put(7, "settima");
		numeriOrdinali.put(8, "ottava");
		numeriOrdinali.put(9, "nona");
		numeriOrdinali.put(10, "decima");
		numeriOrdinali.put(11, "undicesima");
		numeriOrdinali.put(12, "dodicesima");
		numeriOrdinali.put(13, "tredicesima");
		numeriOrdinali.put(14, "quattordicesima");
		numeriOrdinali.put(15, "quindicesima");
		
		/*gli url sono del tipo
		 * https://it.wikipedia.org/wiki/Episodi_di_Agents_of_S.H.I.E.L.D._(quinta_stagione)
		 * https://it.wikipedia.org/wiki/Episodi_di_The_Flash_(prima_stagione)
		 */
		
		String link = "https://it.wikipedia.org/wiki/Episodi_di_";
		StringTokenizer st = new StringTokenizer(serie.getNomeSerie());
		while(st.hasMoreTokens()){
			String parola = st.nextToken();
			parola = parola.substring(0, 1).toUpperCase() + parola.substring(1, parola.length());
			if(parola.equals("Of"))
				parola = parola.toLowerCase();
			link += parola + "_";
		}
		link += String.format("(%s_stagione)", numeriOrdinali.get(numeroStagione)); //link possibile
		
		String HTMLText = "";
		try { //da rivedere
			tryConnection(link);
		}catch(Exception e) {
			if(numeroStagione == 1) {
				//devo levare dal link _(prima_stagione) (17 caratteri)
				link = link.substring(0, link.length()-17);
				try {
					tryConnection(link);
				}catch(Exception e1) {
					System.out.println(link);
					throw new URLNotFoundException();
				}
				printURL(link);
				return link;
			}
			System.out.println(link);
			throw new URLNotFoundException();
		}
		//se passa il try-catch dovrebbe aver trovato l'url
		printURL(link);
		return link;		
	}
	
	private Reader tryConnection(String link) throws IOException, UnknownServiceException {
		URL url = new URL(link);
		URLConnection uc = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		
		//Se arriva qua, il link esiste
		return br;
	}
	
	private void printURL(String link) {
		try {
			File fileURLsTemp = new File(serie.getFileSerie().getAbsolutePath() + "\\" + MieSerie.URLSFILE);
			if(!fileURLsTemp.exists())
				fileURLsTemp.createNewFile();
			RandomAccessFile fileURLs = new RandomAccessFile( fileURLsTemp, "rw");
			fileURLs.seek(fileURLs.length());
			fileURLs.writeBytes(String.format("Stagione %d: %s --- ", numeroStagione, link));
			fileURLs.close();
		}catch(Exception e) {
			System.out.println("Impossibile aggiungere l'url di " + toString());
		}

	}
}