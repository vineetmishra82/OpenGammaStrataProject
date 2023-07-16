package com.kpts.port23;
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
			System.out.println("File uploaded....");
			int lineNo = 1;
			for (Map<String,String> item : itemList) {
				
				Product product = new 
						Product(item.get("SECURITY_SCHEME")+","+item.get("SECURITY_VALUE"),
								item.get("SECURITY_SCHEME")+","+item.get("ISSUER_VALUE"),
								Long.valueOf(item.get("QUANTITY").replace("L", "")),
								Double.valueOf(item.get("NOTIONAL")),
								Double.valueOf(item.get("FIXED_RATE")),
								item.get("START_DATE"),
								item.get("END_DATE"),
								item.get("SETTLEMENT"),
								Double.valueOf(item.get("CLEAN_PRICE")),
								item.get("VAL_DATE")
								);
				
				int loopSize = Integer.valueOf(item.get("Loops"));
				
				System.out.println("Loop size is "+loopSize);
				
				for(int i = 0;i<loopSize;i++)
				{
					System.out.println("For row - "+lineNo+" running count - "+(i+1));
					product.calculatePresentValue();					
					System.out.println("\n");	
				}
				
				lineNo++;
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
