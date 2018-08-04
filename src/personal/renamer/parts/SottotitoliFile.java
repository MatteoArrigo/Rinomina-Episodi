package personal.renamer.parts;
import java.io.File;
import java.util.StringTokenizer;

import personal.renamer.MieSerie;
import personal.renamer.exceptions.LanguageException;

public class SottotitoliFile extends Sottotitoli{
	
	private File fileSubt;
	private SottotitoliDir dir;
	private String lingua;

	public SottotitoliFile(SottotitoliDir dir, File fileSubt) {
		this.fileSubt = fileSubt;
		this.dir = dir;
		this.lingua = ricavaLingua();
		super.nomeFinale = ricavaTitolo();
		
		super.vecchioNome = fileSubt.getName();
	}
	
	private String ricavaTitolo() {
		if(lingua != "")
			return dir.getNomeFinale() + MieSerie.TITOLO_LINGUA + lingua + ".srt";
		else
			return dir.getNomeFinale() + ".srt";
	}
	private String ricavaLingua()
	{
		String nome = fileSubt.getName();
		
		nome = nome.replaceAll("\\.", " ").trim();
		StringTokenizer st = new StringTokenizer( nome );
		String parolaPrecedente = "";
		String parola = "";
		String linguaTemp = "";
		while(st.hasMoreTokens())
		{
			parola = st.nextToken();
			if(parola.equals("srt") || parola.equals("txt"))
			{
				linguaTemp = parolaPrecedente;
				break;
			}
			else
				parolaPrecedente = parola;
		}
		
		if(linguaTemp.length() > 3)
			linguaTemp = "";
		
		try {
			dir.addLingua(linguaTemp);
		}catch(LanguageException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return linguaTemp;
	}
	
	
	public String rinominaFile()
	{
		File fileNuovo = new File( dir.getAbsolutePathDir() + "\\" + nomeFinale );
		fileSubt.renameTo( fileNuovo );
		fileSubt = fileNuovo;
		return nomeFinale;
	}
	
}
