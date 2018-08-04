package personal.renamer.creazioneProva;

import java.io.*;

public class FileProva {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Creazione cr = new Creazione();
		File cartellaOrigine = new File("E:\\Serie Tv\\Serie Tv Supereroi");
		File cartellaArrivo = new File("C:\\Users\\Matteo\\eclipse-workspace\\Rinomina Episodi\\src\\personal\\renamer\\Prova");
		try {
			cartellaArrivo.mkdir();
		}catch(Exception e) {}
		
		for(File a: cartellaOrigine.listFiles())
		{
			File a1 = new File(cartellaArrivo.getAbsolutePath() + "\\" + a.getName());
			cr.createFile(a1, a);
			if(a1.isFile())
				continue;
			for(File b : a.listFiles())
			{
				File b1 = new File(a1.getAbsolutePath() + "\\" + b.getName());
				cr.createFile(b1, b);
				if(b1.isFile())
					continue;
				for(File c : b.listFiles())
				{
					File c1 = new File(b1.getAbsolutePath() + "\\" + c.getName());
					cr.createFile(c1, c);
					if(c1.isFile())
						continue;
					for(File d: c.listFiles())
					{
						File d1 = new File(c1.getAbsolutePath() + "\\" + d.getName());
						cr.createFile(d1, d);
						if(d1.isFile())
							continue;
						for(File e: d.listFiles())
						{
							File e1 = new File(d1.getAbsolutePath() + "\\" + e.getName());
							cr.createFile(e1, e);
						}
					}
				}
			}
			
		}

	}
}
