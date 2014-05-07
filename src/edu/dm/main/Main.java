package edu.dm.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.dm.apriori.Elimination;
import edu.dm.object.Item;
import edu.dm.object.ItemSet;
import edu.dm.object.Transaction;

public class Main 
{

	private static List<Transaction> transaction = new ArrayList<Transaction>();
	private static List<Item> itemList = new ArrayList<Item>();
	private static float supCount;

	public static void main(String[] args)
	{
		
		takeTrans();
		minSup();
		
		new Elimination(itemList, supCount, transaction);


	}

	private static void takeTrans()
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(new File("data1.txt")));
			String line;
			System.out.println("Transactions:");
			
			while((line = br.readLine()) != null)
			{
				String[] inputchars = line.split(",");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i = 0; i < inputchars.length; i++){
				arr.add(inputchars[i].trim());
				}
				Transaction trans = new Transaction(arr);
				transaction.add(trans);

			}

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	private static void minSup()
	{
		try 
		{
			BufferedReader br = new BufferedReader(
					new FileReader(new File("data2.txt")));
			String line;
			String itemName;
			float mis;
			// System.out.println("\nItem MinSup values:");
			while((line = br.readLine()) != null)
			{
				if(!line.equals(" ") && !line.equals(""))
				{
					String[] input = line.split(" ");

					if(input[0].equals("supCount"))
					{
						supCount = Float.valueOf(input[2]);
						continue;
					}
					
					itemName = input[0].substring(input[0].indexOf('(')+1, input[0].indexOf(')'));
					mis = Float.parseFloat(input[2]);
					
					ýtemSetNew(itemName,mis);

					
				}
			}
		} 
		
		catch (FileNotFoundException e) 
		{			
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	private static void ýtemSetNew(String newItem,float mis)
	{
		Item item = null;
		int count = 0 ;

		for(Transaction trans : transaction)
		{
			if (trans.itemSetIsContains(newItem))
				count++;
		}

		item = new Item(newItem, count, mis);

		itemList.add(item);
	}

}
