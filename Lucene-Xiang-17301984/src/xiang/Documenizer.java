package xiang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Documenizer {
	
	private BufferedReader br;
	public Vector<Documents> docs;
	
	public Documenizer(String file) throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader(file));
		docs = new Vector<Documents>();
	}
	
	public void createDocumentVector() throws IOException
	{
		StringBuilder strb = new StringBuilder();
		String line = br.readLine();
		Documents temp=null;

		
		while(line!=null)
		{
			if(line.startsWith(".I"))
			{
				temp = new Documents();
				strb.setLength(0);
				temp.id = line.substring(3);
				line = br.readLine();

			}
			if(line.startsWith(".T"))
			{
				line = br.readLine();
				while(!line.startsWith(".A")){
					strb.append(line);
					line = br.readLine();
				}
				temp.title = strb.toString();
				strb.setLength(0);
			}
			if(line.startsWith(".A"))
			{
				line = br.readLine();
				while(!line.startsWith(".B")){
					strb.append(line);
					line = br.readLine();
				}
				temp.author = strb.toString();
				strb.setLength(0);
			}
			if(line.startsWith(".B"))
			{
				line = br.readLine();
				while(!line.startsWith(".W")){
					strb.append(line);
					line = br.readLine();
				}
				temp.biography = strb.toString();
				strb.setLength(0);
			}
			if(line.startsWith(".W"))
			{
				line = br.readLine();
				while(!line.startsWith(".I")){
					strb.append(line);
					line = br.readLine();
					if(line==null)
					{
						temp.words = strb.toString();
						break;
					}
				}
				temp.words = strb.toString();
				strb.setLength(0);
			}
			docs.addElement(temp);
		}
	}

}
