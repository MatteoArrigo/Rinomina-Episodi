package personal.renamer;

import personal.renamer.exceptions.StringNotFoundException;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.*;

public class Riga {
	
	private String riga;
	
	private int lingua;
	
	private int numeroEpisodio;
	private String titolo;
	
	private boolean rigaTitolo;
	private boolean titoloTrovato = true;
	
	private Hashtable<Integer, Integer> colonnaTitolo;

	public Riga(String riga, int lingua) throws StringNotFoundException {
		super();
		this.riga = riga;
		this.lingua = lingua;
		
		rigaTitolo = controllaRiga();		
		if(rigaTitolo)
		{
			colonnaTitolo = new Hashtable<Integer, Integer>();
			colonnaTitolo.put(MieSerie.EN, 2);
			colonnaTitolo.put(MieSerie.IT, 3);
			numeroEpisodio = ricavaNumeroEpisodio();
			titolo = ricavaTitolo();
		}
	}
	
	public boolean isRigaTitolo() {
		return this.rigaTitolo;
	}
	
	public boolean isTitoloTrovato() {
		return this.titoloTrovato;
	}
	
	public void setTitoloTrovato(boolean b) {
		this.titoloTrovato = b;
	}
	
	public int getNumeroEpisodio() {
		return numeroEpisodio;
	}

	public String getTitolo() {
		return titolo;
	}
	
	private boolean controllaRiga() {
		return (riga.indexOf("<th>") == -1) ? true : false;
	}
	
	/* La riga è del tipo:
	 * <tr>
	 * <td>1</td>
	 * <td><i>The Man Who Saved Central City</i></td>
	 * <td><i><a href="#L&#39;uomo_che_ha_salvato_Central_City">L'uomo che ha salvato Central City</a></i></td>
	 * <td>6 ottobre 2015</td>
	 * <td>11 gennaio 2016
	 * </td></tr>
	 * 
	 * <tr>
	 * <td>2</td>
	 * <td><i>Flash of Two Worlds</i></td>
	 * <td><i><a href="#Flash_dei_due_mondi"><i>Flash dei due mondi</i></a></i></td>
	 * <td>13 ottobre 2015</td>
	 * <td>11 gennaio 2016
	 * </td></tr>
	 * 
	 * <tr>
	 * <td>3</td>
	 * <td><i>Family of Rogues</i></td>
	 * <td><i><a href="#Famiglia_di_nemici">Famiglia di nemici</a></i></td>
	 * <td>20 ottobre 2015</td>
	 * <td>22 gennaio 2016
	 * </td></tr>
	 */

	public int ricavaNumeroEpisodio() throws StringNotFoundException
	{
		if(riga.indexOf("<td>")!=-1 && riga.indexOf("</td>")!=-1)
		{
			String cellaNumero = riga.substring(riga.indexOf("<td>"), riga.indexOf("</td>")+5);
			cellaNumero = cellaNumero.replaceAll("\\n", "");
			Matcher matcherNumero = Pattern.compile(">\\d+<").matcher(cellaNumero);
			if(matcherNumero.find()) {
				String numero = cellaNumero.substring(matcherNumero.start()+1, matcherNumero.end()-1);
				try{
					return Integer.parseInt(numero);
				}catch(Exception e) {
					titoloTrovato = false;
					throw new StringNotFoundException();
				}
			}
		}
		titoloTrovato = false;
		throw new StringNotFoundException();
	}
	
	public String ricavaTitolo() throws StringNotFoundException
	{
		int nColonna = 0;
		Matcher inizioCella = Pattern.compile("<td").matcher(riga);
		Matcher fineCella = Pattern.compile("<\\/td>").matcher(riga);
		Pattern patternTitolo = Pattern.compile(">[^ ][^<>]+<");
		while(inizioCella.find() && fineCella.find()) {
			nColonna++;
			if(colonnaTitolo.get(lingua) == nColonna) {
				String cella = riga.substring(inizioCella.start(), fineCella.end());
				cella = cella.replaceAll("\\n", "");
				if(cella.equals("<td></td>"))
					return "";
				Matcher matcherTitolo = patternTitolo.matcher(cella);
				if(matcherTitolo.find()) {
					String titoloTemp = cella.substring(matcherTitolo.start()+1, matcherTitolo.end()-1);
					StringTokenizer st = new StringTokenizer(titoloTemp);
					String titolo = "";
					while(st.hasMoreTokens()) {
						String parola = st.nextToken();
						titolo += parola.substring(0, 1).toUpperCase() + parola.substring(1, parola.length()).toLowerCase() + " ";
					}
					return titolo.substring(0, titolo.length()-1);
				}else {
					titoloTrovato = false;
					throw new StringNotFoundException();
				}
			}
		}
		//Se è uscito dal while, qualcosa è andato storto
		titoloTrovato = false;
		throw new StringNotFoundException();
		
	}
	
}
