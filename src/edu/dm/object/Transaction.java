//091044043	
//zeynep gazal KAYA

package edu.dm.object;


import java.util.*;


//transaction islemlerinin yapýldýgý class

public class Transaction
{
	List<String> transaction;
	
	public Transaction(List<String> trans) 
	{
		this.transaction = trans;
		
	}
	
	public boolean itemSetIsContains(String itemSet)
	{
		boolean status = true;
		
		String items[] = itemSet.split(",");
		
		for(int i = 0 ; i < items.length ; i++)
		{
			if( !transaction.contains(items[i]))
			{
				status = false;
				break;
			}
		}
		
		return status;
	}

	
	
	

}
