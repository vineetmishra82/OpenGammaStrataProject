import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
	
	static List<String> headers = new ArrayList<String>();
	static List<Map<String,String>> itemList = new ArrayList<Map<String,String>>();

	public static void main(String[] args) {

	System.out.println("Running strata project");
	
	try {
		FileReader fileReader = new FileReader(args[0]);
		
		if(fileReader!=null)
		System.out.println("File found..start processing.. ");
		
		BufferedReader buffReader = new BufferedReader(fileReader);
		
		String line = "";
		int count = 0;
		while((line = buffReader.readLine())!=null)
		{
			String[] values = line.split(",");
		
			if(count==0)
			{
				for (String string : values) {
					headers.add(string);
					
				}
				count++;
				
				System.out.println("headers size is "+headers.size());
			}
			
			else if(values.length>0) {
				
				Map<String,String> item = new TreeMap<String, String>();
				
				for(int i = 0;i<headers.size();i++)
				{
					item.put(headers.get(i), values[i]);
				}
				
				itemList.add(item);
			}	
			
			
			
		}
		
		if(itemList.size()>0)
		{
			for (Map<String,String> item : itemList) {
				
				for(Map.Entry<String, String> lineItem : item.entrySet())
				{
					System.out.println(lineItem.getKey()+" : "+lineItem.getValue());
				}
				
				System.out.println("\n");	
			}
			
			
		}
		
		buffReader.close();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
	System.out.println("File not found at location...terminating");
	System.exit(0);
	} catch (IOException e) {
		// TODO Auto-generated catch block
	System.out.println("File reading error - "+e.getLocalizedMessage());
	}

	}

}
