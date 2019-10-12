package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author SaiChitta
 * Model file to read Map file JSON data and parse to Java Objects using GSON library. 
 */
public class MapFileJSONParser {
	
	public static final String MAP_FILE_NAME = "default.map";
	String fileData;

	public String getFileData() {
		return fileData;	
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}
		
	public void readFileData(){
		File mapFile = new File(MAP_FILE_NAME);
		try {
			FileInputStream fInputStream = new FileInputStream(mapFile);
			byte[] fileByte = new byte[(int) mapFile.length()];
			fInputStream.read(fileByte);
			fInputStream.close();			
			String fileData = new String(fileByte, "UTF-8");			
			setFileData(fileData);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
