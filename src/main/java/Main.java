import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {

	System.out.println("Running strata project");
	
	try {
		FileReader fileReader = new FileReader(args[0]);
		
		if(fileReader!=null)
		System.out.println("File found..start processing.. ");
		
		BufferedReader buffReader = new BufferedReader(fileReader);
		
		String line = "";
		while((line = buffReader.readLine())!=null)
		{
			String[] values = line.split(",");
			
			for (String string : values) {
				System.out.print(string+" ");
			}
			System.out.println("\n");
		}
		
		
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
