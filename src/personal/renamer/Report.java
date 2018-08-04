package personal.renamer;
import personal.renamer.parts.*;

import java.io.*;
import java.util.regex.*;
import java.util.*;

public class Report {
	
	private File report = null;
	private static final int LUN_NOME = 65;
	private BufferedWriter output;
	
	public Report() {
		
		try {
			createFile();

			output = new BufferedWriter( new FileWriter(report) );
			report.setWritable(true);
			
			output.write("REPORT");
		
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	private void createFile() throws IOException {
		
		ArrayList<Integer> numeri = new ArrayList<Integer>();
		boolean trovato = false;
		for(File f : MieSerie.serie_dir.listFiles())
		{
			if(f.isFile() && f.getName().matches(MieSerie.regexReport))
			{
				if(trovato==false)
					trovato = true;
				String nomeReport = f.getName();
				Matcher numero = Pattern.compile("[\\d]+").matcher(nomeReport);
				if(numero.find())
					numeri.add( Integer.parseInt( nomeReport.substring( numero.start(), numero.end() ) ) );
				else
					numeri.add(0);	
			}
		}
		if(numeri.size() == 0)
		{
			report = MieSerie.fileReport;
			report.createNewFile();
			return;
		}
		Collections.sort(numeri, (Integer a, Integer b) -> {
			if(a>b)
				return -1;
			else if(a==b)
				return 0;
			else
				return 1;
		});
		String nomeReport = MieSerie.fileReport.getName();
		report = new File(nomeReport.substring(0, nomeReport.length()-4) + "_" + numeri.get(0) + ".txt");
		report.createNewFile();
	}
	
	public void closeStream() throws IOException
	{
		output.close();
	}
	
	public void printURL() {
		
	}
	
	public void printSerieStagione(Serie serie, Stagione stagione)
	{
		try {
			output.newLine();
			output.newLine();
			output.write(serie.getNomeSerie().toUpperCase() + " - Stagione " + stagione.getNumeroStagione());
		} catch( IOException e ) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void printFileNonLetto( File file ) throws IOException
	{
		output.newLine();
		if(file.isFile())
		{
			output.write("\tIl file " + file.getName() + " in " + file.getParent() + " non è stato considerato.");
		}
		else if(file.isDirectory())
		{
			output.write("\tLa cartella " + file.getName() + " in " + file.getParent() + " non è stata considerata.");
		}
	}
	
	public void printTitoloCambiato(Episodio episodio) {
		
		try {
			output.newLine();
			StringBuffer nome = new StringBuffer(episodio.getNomeEpisodio());
			if(episodio.getNomeEpisodio().length() <= LUN_NOME && episodio.getTitoloFinale().length() <= LUN_NOME)
			{
				nome.setLength(LUN_NOME);
				nome.insert(LUN_NOME, episodio.getTitoloFinale());
			}
			else
			{
				nome.setLength(LUN_NOME*3/2);
				nome.insert(LUN_NOME*3/2, episodio.getTitoloFinale());
			}
			output.write(nome.toString());
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void printSottotitoliCambiati(Sottotitoli subt) {
		try {
			output.newLine();
			StringBuffer nome = new StringBuffer(subt.getVecchioNome());
			if(subt.getVecchioNome().length() <= LUN_NOME && subt.getNomeFinale().length() <= LUN_NOME)
			{
				nome.setLength(LUN_NOME);
				nome.insert(LUN_NOME, subt.getNomeFinale());
			}
			else
			{
				nome.setLength(LUN_NOME*3/2);
				nome.insert(LUN_NOME*3/2, subt.getNomeFinale());
			}
			if(subt instanceof SottotitoliFile)
				output.write("\t");
			output.write(nome.toString());
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
