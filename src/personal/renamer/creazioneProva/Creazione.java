package personal.renamer.creazioneProva;

import java.io.File;

public class Creazione{

	public void createFile( File file, File fileVecchio ) {
		if(!file.exists())
		{
			if(fileVecchio.isFile())
			{
				try{
					file.createNewFile();
				}catch(Exception e) {}
			} else
			{
				try{
					file.mkdir();
				}catch(Exception e) {}
			}
		}
	}
}