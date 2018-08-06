package personal.renamer.parts;
import personal.renamer.exceptions.*;
import personal.renamer.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Titolo {
	
	private String nomeFile;
	private File file;
	
	private int numeroEpisodio;
	private String estensione;
	private String stagioneXEpisodio;
	
	private boolean estensionePresente = true;
	private boolean isDirSubt;
	private boolean fileAccettabile = true;
	
	private String titoloFinale;
	
	private Stagione stagione;
	
	public Titolo(withTitolo fileClass) {
		
		this.file = fileClass.getFile();
		this.nomeFile = this.file.getName();
		try {
			this.estensione = ricavaEstensione();
			this.stagioneXEpisodio = ricavaStagioneXEpisodio();
			this.numeroEpisodio = ricavaNumeroEpisodio();
		}catch(WrongFileException e) {
			e.printStackTrace();
			System.exit(0);
		}catch(PersonalException e){
			e.printStackTrace();
			setFileAccettabile(false);
			System.out.println("Ancora da gestire");
		}
		stagione = fileClass.getStagione();
		if(stagione.isGotMapTitoli()) //c'è l'url di wikipedia, uso la mappa di stagione
		{
			//utilizza la mappa di stagione
			this.titoloFinale = matchTitoloMap();
		}else //altrimenti devo usare il metodo più artificioso ricavaTitolo()
			this.titoloFinale = ricavaTitolo();
	}

	public String getNomeEpisodio() {
		return nomeFile;
	}

	public int getNumeroEpisodio() {
		return numeroEpisodio;
	}

	public String getEstensione() {
		return estensione;
	}

	public String getStagioneXEpisodio() {
		return stagioneXEpisodio;
	}
	
	public String getTitolo() {
		return this.titoloFinale;
	}
	
	public void setFileAccettabile(boolean b) {
		this.fileAccettabile = b;
	}
	
	public boolean getFileAccettabile() {
		return this.fileAccettabile;
	}

	private String eliminaPunti() throws StringNotFoundException, StrangeThingException //metodo funzionante solo con nomeEpisodio
	{
		String temp = "";
		
		//COSE STRANE (file che non vengono considerati
		//elimina la prima parte del nome del file (che potrebbe causare errori con questo dannato metodo)
		Matcher findEpisodio = null;
		boolean found = false;
		Iterator<Pattern> it = MieSerie.format.iterator();
		int daAggiungere = 0; //numero del carattere nell stringa dove inizia la stagione
		while(it.hasNext())
		{
			findEpisodio = it.next().matcher(nomeFile);
			if(findEpisodio.find())
			{
				found = true;
				daAggiungere = findEpisodio.start();
				temp = nomeFile.substring(daAggiungere);
				break;
			}
		}
		if(!found)
		{
			throw new StringNotFoundException();
		}
		
		/*Se ci sono cose strane non cambia niente
		 * Le cose strane possono essere: 4 punti (quindi anche i 4 punti dati da tre punti e quello dell'estensione
		 * 2 punti in qualsiasi parte del video tranne alla fine (dopo aver visto se è presente ..est
		 */
		Matcher coseStrane = null;
		for(Pattern p : MieSerie.coseStrane)
		{
			coseStrane = p.matcher(temp);
			if(coseStrane.find())
			{
				// se il pattern di cose strane è [xXeE][\\d]+[\" \"]*[-]*[\" \"]*[\\.]+, devo vedere che il punto non sia quello dell'estensione
				if(coseStrane.pattern().pattern().equals("[xXeE][\\d]+[\" \"]*[-]*[\" \"]*[\\.]+")) { 	
					boolean flagEstensione = false;
					String temp2 = temp.replaceAll("\\.", " ").trim();
					String ultimaParola = temp2.substring( temp2.lastIndexOf(" ")+1 );
					for(String s : MieSerie.estensioni) //verifica che l'episodio non sia 0x00.est
					{
						if( ultimaParola.equals(s) ){
							flagEstensione = true;
							break;
						}
					}
					if(flagEstensione == true)
						continue; //non considerare questa come una cosa strana
				}
				throw new StrangeThingException();
			}
		}
		
		if(!isDirSubt)
		{
			//Cosa strana particolare: se ci sono due punti e l'estensione
			for(String est : MieSerie.estensioni)
			{
				if(temp.substring(temp.length()-5).equals(".." + est)) // vede se gli ultimi 5 caratteri sono del tipo ..est
				{
					throw new StrangeThingException();
				}	
			}
		}
		
		//COSE DA RIMETTERE PIU' AVANTI
		//Non cambia i tre punti
		boolean flagTrePuntini = false;
		Pattern patternTrePuntini = Pattern.compile("\\.\\.\\.");
		Matcher matcherTrePuntini = patternTrePuntini.matcher(temp);
		if(matcherTrePuntini.find())
		{
			flagTrePuntini = true;
			temp = temp.replaceAll("\\.\\.\\.", "asdfghjkqwertyuizxcvbn");
		}
		
		//Non cambia il punto usato per le orE, stando attendo a non confonderlo con il punto che divide il numero dell'episodio da un altro numero
		Matcher episodioOra = Pattern.compile("[xe]\\d+\\.\\d").matcher(temp); //ricerca se c'è l'episodio e un numero del titolo divisi da un punto
		String episodioOra_s = "";
		boolean flagEpisodioOra = false;
		if(episodioOra.find())
		{
			flagEpisodioOra = true;
			episodioOra_s = temp.substring(episodioOra.start(), episodioOra.end()-1); //non prende il numero facente parte del titolo
			temp = temp.replaceFirst( episodioOra_s , "qwertyuiasdfghjkzxcvbnm");
		}
		boolean flagPuntoOra = false; //ricerca se c'è ora e minuti divisi da un punto
		Matcher puntoOra = Pattern.compile("\\d\\.\\d").matcher(nomeFile);
		ArrayList<String> puntiOra = new ArrayList<String>();
		while(puntoOra.find())
		{
			if(!flagPuntoOra) //lo setta solo la prima volta
				flagPuntoOra = true;
			puntiOra.add( temp.substring(puntoOra.start() , puntoOra.end()) );
		}
		if(flagPuntoOra)
			temp = temp.replaceAll("\\d\\.\\d", "zxcvbnmqweryuioasdfhjkl");
		if(flagEpisodioOra)
			temp = temp.replaceFirst("qwertyuiasdfghjkzxcvbnm", episodioOra_s);
		
		//CAMBIO DI TUTTI I PUNTI IN SPAZI
		temp = temp.replaceAll("[\\._]", " ");
		
		//RIMESSA A POSTO DI TUTTO I CAMBI
		if(flagTrePuntini)
			temp = temp.replaceAll("asdfghjkqwertyuizxcvbn", "...");
		if(flagPuntoOra)
		{
			for(int i=0 ; i<puntiOra.size() ; i++)
				temp = temp.replaceFirst("zxcvbnmqweryuioasdfhjkl", puntiOra.get(i));
		}
		
		return nomeFile.substring(0, daAggiungere) + temp;
	}

	private String eliminaEstensione() throws NoExtensionException //metodo funzionante solo con nomeEpisodio
	{
		if(!estensionePresente)
			throw new NoExtensionException();
		String titoloDaTornare = nomeFile.trim().substring( 0 , nomeFile.length() - estensione.length() -1 ).trim();
		estensionePresente = false;
		return titoloDaTornare;
	}
	
	public boolean verificaDirSubt() {
		String path = file.getParent();
		path = path.substring(path.lastIndexOf("\\") + 1);
		return ( path.equals("Sottotitoli") || path.equals("Subtitles") ) ? true : false;
	}
	
	public boolean verificaEstensionePresente(String estensione) {
		for(String est : MieSerie.estensioni)
		{
			if(estensione.equals(est))
			{
				return true;
			}
		}
		return false;
	}
	
	private String ricavaTitolo()
	{	
		String temp = nomeFile;
		
		//OPERAZIONI DI PREPARAZIONE DEL TITOLO
		try {	
			temp = eliminaPunti();
			if(!isDirSubt)
				temp = eliminaEstensione();
			temp = temp.substring( temp.indexOf(stagioneXEpisodio) + stagioneXEpisodio.length() ); //temp è tutto il nome del file dopo stagioneXEpisodio
		}catch(NullPointerException e) {
			e.printStackTrace();
			System.exit(0);
		}catch(PersonalException e) {
			System.out.println(this.nomeFile);
			e.printStackTrace();
			System.out.println("Ancora da gestire");
			setFileAccettabile(false);
			return "";
		}
		
		if(estensionePresente)
		{
			System.out.println("Errore di programmazionem non deve essere presente l'estensione quando è attivo ricavaTitolo");
			System.exit(0);
		}
		Matcher primaLettera = Pattern.compile("\\w").matcher(temp); //ricerca la prima lettera, se non c'è il nome non ha il titolo
		if(!primaLettera.find())
			return "";
		else
			temp = temp.substring(primaLettera.start());
		
		String titoloFinale = "";
		StringTokenizer st = new StringTokenizer(temp); //controlla quale parola corrisponde ad uno dei terminators
		while(st.hasMoreTokens())
		{
			String element = st.nextToken();
			boolean ultimaParola = false;
			for(String terminator : MieSerie.terminators)
			{
				if(element.equalsIgnoreCase(terminator))
				{
					ultimaParola = true;
					break;
				}
			}
			if(ultimaParola == true) //se era l'ultima parola, esce
				break;
			element = element.substring(0, 1).toUpperCase() + element.substring(1).toLowerCase(); //altrimenti rende la prima lettera grande
			titoloFinale += element + " ";
		}
		
		System.out.println("Questo titolo è stato trovato con il metodo ricava titolo, non con Wikipedia");
		return titoloFinale.trim();
		
	}
	
	private String ricavaEstensione() throws WrongFileException//da chiamare prima di eliminaEstensione
	{
		String temp = nomeFile.replaceAll("\\.", " ");
		StringTokenizer st = new StringTokenizer(temp);
		String estensione = "";
		while(st.hasMoreTokens())
		{
			estensione = st.nextToken();
		}
		
		if(!verificaEstensionePresente(estensione))
		{
			if(!verificaDirSubt()){
				throw new WrongFileException();
			}
			estensionePresente = false;
			isDirSubt = true; //se non c'è l'estensione sul nome originale vuol dire che è una cartella
			return "";
		}
		
		return estensione;
	}
	
	private int ricavaNumeroEpisodio() throws StringNotFoundException{
		
		String formatString = getStagioneXEpisodio();
		
		Matcher matcherNEpisodio = Pattern.compile("[\\d]+").matcher(formatString);
		matcherNEpisodio.find(); //trova il numero della stagione
		matcherNEpisodio.find(matcherNEpisodio.end()); //trova il numero dell'episodio
		return Integer.parseInt(formatString.substring(matcherNEpisodio.start(), matcherNEpisodio.end()));
	}
	
	private String ricavaStagioneXEpisodio() throws StringNotFoundException {
		
		Matcher findEpisodio = null;
		String formatString = null;
		Iterator<Pattern> it = MieSerie.format.iterator();
		while(it.hasNext())
		{
			findEpisodio = it.next().matcher(nomeFile);
			if(findEpisodio.find())
			{
				formatString = nomeFile.substring(findEpisodio.start(), findEpisodio.end());
				break;
			}
		}
		if(formatString == null)
			throw new StringNotFoundException();
		return formatString;
	}
	
	
	private String matchTitoloMap() {
		if(stagione.getMapTitoli().containsKey(numeroEpisodio))
				return stagione.getMapTitoli().get(numeroEpisodio);
		else
			return ricavaTitolo();
	}
	
}
