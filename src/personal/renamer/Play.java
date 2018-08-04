package personal.renamer;

import java.io.File;
import java.io.IOException;

import personal.renamer.parts.*;

public class Play {
	
	/*RICORDARSI DI
	 * - Gestire le eccezioni all'inizio di ricavaTitolo in Titolo, in particolare le cose strane
	 * - Gestire le eccezioni all'interno del costruttore di Titolo
	 * - Integrare tutte le eccezioni, dove possibile, con report
	 * - aggiungere controllo di file report a verificaFile in Serie
	 * -controllare codice commentato nel main
	 * - pensare meglioa come integrare Titolo con Episodio e SottotitoliDir
	 * - ultime due estensioni di MieSerie da rivedere
	 * - da rivedere findURL() in stagione
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MieSerie.checkMainDir();
		Report report = new Report();
		
		for(File cartellaSerie : MieSerie.serie_dir.listFiles())
		{
			if(!Serie.verificaFile(cartellaSerie))
			{
				try {
					report.printFileNonLetto( cartellaSerie );
				} catch(IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				continue;
			}
			Serie serie = new Serie(cartellaSerie);
			for(File cartellaStagione : cartellaSerie.listFiles())
			{
				if(!Stagione.verificaFile(cartellaStagione))
				{
					try {
						report.printFileNonLetto( cartellaStagione );
					} catch(IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
					continue;
				}
				Stagione stagione = new Stagione(serie, cartellaStagione);
				stagione.rinominaStagione();
				File cartellaStagioneNuova = stagione.getFile();
				report.printSerieStagione(serie, stagione);
				for(File fileEpisodio : cartellaStagioneNuova.listFiles())
				{
					if(fileEpisodio.getName().equals("Thumbs.db"))
						continue;
					
					if(fileEpisodio.isDirectory())
					{
						if(fileEpisodio.getName().equals("Sottotitoli") || fileEpisodio.getName().equals("Subtitles"))
						{
							for(File dirSubtFile : fileEpisodio.listFiles())
							{
								SottotitoliDir dirSubt = new SottotitoliDir(serie, stagione, dirSubtFile);
								dirSubt.rinominaDir();
								File dirSubtNuova = dirSubt.getFile();
								report.printSottotitoliCambiati(dirSubt);
								for(File fileSubtFile : dirSubtNuova.listFiles())
								{
									SottotitoliFile fileSubt = new SottotitoliFile(dirSubt, fileSubtFile);
									fileSubt.rinominaFile();
									report.printSottotitoliCambiati(fileSubt);
								}
							}
						}
						else
						{
							try {
								report.printFileNonLetto( fileEpisodio );
							} catch(IOException e) {
								e.printStackTrace();
								System.exit(0);
							}
						}
						continue;
					}
					
					Episodio episodio = new Episodio(serie, stagione, fileEpisodio );
					if(!episodio.controllaEpisodio())
					{
						try {
							report.printFileNonLetto( fileEpisodio );
						} catch(IOException e) {
							e.printStackTrace();
							System.exit(0);
						}
						continue;
					}
						
					episodio.rinominaEpisodio();
					report.printTitoloCambiato(episodio);
				}
			}
		}
		try {
			report.closeStream();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("\n\n\n\nProgramma terminato con successo");
	}
}
