package xiang;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Querizer {
	
	
	private BufferedReader br;
	public Vector<Myquery> queries;
	
	public Querizer(String file) throws FileNotFoundException
	{
		br = new BufferedReader(new FileReader(file));
		queries = new Vector<Myquery>();
	}
	
	public void createQueryVector() throws IOException
	{
		StringBuilder strb = new StringBuilder();
		String line = br.readLine();
		Myquery temp=null;
		
		while(line!=null)
		{
			if(line.startsWith(".I"))
			{
				temp = new Myquery();
				strb.setLength(0);
				temp.id = Integer.parseInt(line.substring(3));
				//System.out.println("Text ID :"+temp.id+" "+temp.id.length());
				line = br.readLine();

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
			queries.addElement(temp);
		}
	}

}
