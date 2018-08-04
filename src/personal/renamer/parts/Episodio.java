package personal.renamer.parts;
import personal.renamer.*;

import java.io.*;
import java.text.DecimalFormat;

public class Episodio implements withTitolo{
	
	private Serie serie;
	private Stagione stagione;
	private File fileEpisodio;
	
	private Titolo titolo;
	private String estensione;
	
	private String titoloFinale;
	
	private boolean fileAccettabile;

	public Episodio(Serie serie, Stagione stagione, File fileEpisodio) {
		this.serie = serie;
		this.stagione = stagione;
		this.fileEpisodio = fileEpisodio;
		
		titolo = new Titolo(this);
		if(fileAccettabile = titolo.getFileAccettabile())
		{
			estensione = titolo.getEstensione();
			titoloFinale = this.toString();
		}
	}
	
	@Override
	public Stagione getStagione() {
		return stagione;
	}

	@Override
	public File getFile() {
		return this.fileEpisodio;
	}
	
	public String getNomeEpisodio() {
		return this.titolo.getNomeEpisodio();
	}
	
	public String getTitoloFinale() {
		return this.titoloFinale;
	}
	
	public String ricavaNomeFile() {
		return fileEpisodio.getName();
	}
	
	public boolean controllaEpisodio() {
		return fileAccettabile ? true : false;
	}
	
	@Override
	public String toString() //metodo più importante, ritorna il nuovo nome del file
	//NB tutto quello che cambio, devo cambiarlo anche sull'else
	{
		DecimalFormat df = new DecimalFormat("00");
		String parteIniziale = serie.getNomeSerie() + MieSerie.SERIE_STAGIONE + stagione.getNumeroStagione() + MieSerie.STAGIONE_EPISODIO + df.format(titolo.getNumeroEpisodio());
		if(titolo.getTitolo() != "")
			return parteIniziale + MieSerie.EPISODIO_TITOLO + titolo.getTitolo() + "." + estensione;
		else
			return parteIniziale + "." + estensione;
	}
	
	public String rinominaEpisodio() {
		File fileNuovo = new File( stagione.getPath() + "\\" + titoloFinale );
		fileEpisodio.renameTo( fileNuovo );
		fileEpisodio = fileNuovo;
		System.out.println(titoloFinale);
		return titoloFinale;
		
	}
}
