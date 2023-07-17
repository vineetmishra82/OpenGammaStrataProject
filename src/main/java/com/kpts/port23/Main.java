package com.kpts.port23;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.ac.manchester.tornado.api.ImmutableTaskGraph;
import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoExecutionPlan;
import uk.ac.manchester.tornado.api.TornadoExecutionResult;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;

public class Main {
	
	static List<String> headers = new ArrayList<String>();
	static double loopCount = -1;
	

	public static void main(String[] args) {

	List<Map<String,String>> itemList = new ArrayList<Map<String,String>>();
	System.out.println("Running strata project");
	
	try {
		FileReader fileReader = new FileReader(args[0]);
		
		if(!args[1].equals(null))
		{
			loopCount = Double.valueOf(args[1]);
			System.out.println("Per row loop count is "+loopCount);
		}
		else {
			System.out.println("No loop count provided in argument, so per line loop will be taken into consideration.");
		}
		
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
			 long start = System.currentTimeMillis();
			 
			 ProcessComputationOfData(itemList);
			 
			 long end =  System.currentTimeMillis();
			 
			 System.out.println("Total time -> "+(end-start)+" milliseconds");
		
//			TaskGraph taskGraph = new TaskGraph("s0")
//			.transferToDevice(DataTransferMode.FIRST_EXECUTION,itemList)
//			.task("t0",Main::ProcessComputationOfData,itemList)
//			.transferToHost(DataTransferMode.EVERY_EXECUTION);
//			
//			 // Create an immutable task-graph
//	        ImmutableTaskGraph immutableTaskGraph = taskGraph.snapshot();
//
//	        // Create an execution plan from an immutable task-graph
//	        TornadoExecutionPlan executionPlan = new TornadoExecutionPlan(immutableTaskGraph);
//
//	        // Execute the execution plan
//	        TornadoExecutionResult executionResult = executionPlan.execute();
			
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
	catch(NumberFormatException e)
	{
		loopCount = -1;
	}

	}

	private static void ProcessComputationOfData(List<Map<String, String>> itemList) {
		System.out.println("File uploaded....");
		int lineNo = 1;
		StringBuilder data = new StringBuilder("");
		
		 long start = System.currentTimeMillis();
		 
		for (@Parallel Map<String,String> item : itemList) {
			
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
			
			
			double loopSize = loopCount==-1 ? Integer.valueOf(item.get("Loops")) : loopCount;
						
					
			for(@Parallel double i = 0;i<loopSize;i++)
			{
				data.append("\nFor row - "+lineNo+" running count - "+(i+1));
				data.append(product.calculatePresentValue());					
				data.append("\n");	
			}
			
			lineNo++;
			data.append("\n");	
			
		}
	
		 
		 long end =  System.currentTimeMillis();
		 
		 System.out.println("\n\nTotal time(only process not file reading/writing) -> "+(end-start)+" milliseconds\n");
		
		
		//Writing to file
		System.out.println("Result data being written to a file...");
		BufferedWriter buff;
		try {
			buff = new BufferedWriter(new FileWriter("resultData.txt"));
		
		
		buff.write(data.toString());
		
		System.out.println("Result data written to file -> resultData.txt");
		
		buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
