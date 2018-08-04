package personal.renamer.parts;
import personal.renamer.MieSerie;
import personal.renamer.exceptions.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class SottotitoliDir extends Sottotitoli implements withTitolo{
	
	private Serie serie;
	private Stagione stagione;
	private File dirSubt;
	
	private Titolo titolo;

	private ArrayList<String> lingue = new ArrayList<String>();
	
	public SottotitoliDir(Serie serie, Stagione stagione, File dirSubt) {
		super();
		this.serie = serie;
		this.stagione = stagione;
		this.dirSubt = dirSubt;
		
		super.vecchioNome = dirSubt.getName();
		
		titolo = new Titolo(this);
		nomeFinale = this.toString();
	}
	
	@Override
	public Stagione getStagione() {
		return stagione;
	}

	@Override
	public File getFile() {
		return this.dirSubt;
	}
	
	public String getParentPathDir() {
		return dirSubt.getParent();
	}
	
	public String getAbsolutePathDir() {
		return dirSubt.getAbsolutePath();
	}
	
	public void addLingua(String lingua) throws LanguageException
	{
		if(lingue.contains(lingua))
			throw new LanguageException();
		else
			lingue.add(lingua);			
	}
	
	private String ricavaNomeFile() {
		return this.dirSubt.getName();
	}
	
	public String toString() //metodo più importante, ritorna il nuovo nome del file
	//NB tutto quello che cambio, devo cambiarlo anche sull'else
	{
		DecimalFormat df = new DecimalFormat("00");
		String parteIniziale = serie.getNomeSerie() + MieSerie.SERIE_STAGIONE + stagione.getNumeroStagione() + MieSerie.STAGIONE_EPISODIO + df.format(titolo.getNumeroEpisodio());
		if(titolo.getTitolo() != "")
			return parteIniziale + MieSerie.EPISODIO_TITOLO + titolo.getTitolo();
		else
			return parteIniziale;
	}
	
	public String rinominaDir()
	{
		File dirSubtNuova = new File( getParentPathDir() + "\\" + nomeFinale );
		dirSubt.renameTo( dirSubtNuova );
		dirSubt = dirSubtNuova;
		System.out.println("Sottotitolo: " + nomeFinale);
		return nomeFinale;
	}
	

}
