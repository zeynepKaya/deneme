

package edu.dm.apriori;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import edu.dm.object.Item;
import edu.dm.object.ItemSet;
import edu.dm.object.Transaction;

public class Elimination 
{
	
	List<Transaction> TransactionList;
	
	float N; // toplam eleman sayýsý
	float supCount; // support degeri

	List<Item> ItemList;     // minSup degerlerine göre  öðeler sýralanýr
	HashMap<String, Item> ItemHash = new HashMap<String, Item>(); 

	
	List<ItemSet> newSet[]; // yeni ýtemSet
  

	

	public Elimination(List<Item> itemList, float supCount, List<Transaction> transaction)
	{
		
		this.supCount = supCount;
		this.ItemList = itemList;
		this.TransactionList = transaction;
		Collections.sort(ItemList);

		// listeye item  ýn sýrasýný verir
		for( int i = 0 ; i < ItemList.size() ; i++)
		{
			ItemList.get(i).setRank(i);
		}

		
		N = transaction.size();
		newSet =  (List<ItemSet>[] )new ArrayList[(int)N];

		// hash map  ý doldurma
		for(Item item : itemList)
		{
			ItemHash.put(item.getItemValue(), item);
		}

		List<Item> L = minSupValue();
		
		FisrtElimination(L);
		
		
		
		int count = 0;

		for ( int i = 1; !newSet[i-1].isEmpty(); i++ )
		{
			
			if( i == 1)
			{
				secondElimination(L);
			}
			else
			{

				otherElimination(newSet[i-1],i);
			}

			cutItemSetList(newSet[i]);			
			
			count++;
		}


		// sonucu yazma
		try
		{
			File file = new File("res.txt");
			if(file.exists())
			{
				file.delete();
			}

			file.createNewFile();

			FileWriter fw = new FileWriter(file);



			for( int i = count ; i >=0 ; i--)
			{

				
					fw.write(" length "+ (i+1) +" frequent itemsets: " + newSet[i].size());
					fw.write("\n");
					System.out.println("Nlength "+ (i+1) +" frequent itemsets: " + newSet[i].size());

					for ( int j = 0 ; j < newSet[i].size() ; j++)
					{
						fw.write( "Support-Count=" +newSet[i].get(j).getCount() + " : {" + newSet[i].get(j).getItemSet() + "}");
						fw.write("\n");
						System.out.println( "Support-Count=" +newSet[i].get(j).getCount() + " :{" + newSet[i].get(j).getItemSet() + "}");
					}
					fw.write("\n");
				}

				fw.close();
			
		}
		catch(Exception e)
		{

		}


	}





	// count/n >= minSup degerini döndürür.
	private List<Item> minSupValue()
	{
		List<Item> L = new ArrayList<Item>();


		Item i = null;

		for(Item item : ItemList)
		{
			if(i == null)
			{
				if( item.getCount()/N >= item.getMinSup())
				{
					i = item;
					L.add(item);
				}
			}
			else
			{
				if( item.getCount()/N >= i.getMinSup() )
				{
					L.add(item);
				}
			}
		}


		return L;
	}

	private void cutItemSetList(List<ItemSet> newSetlist)
	{
		Iterator<ItemSet> x= newSetlist.iterator();

		while(x.hasNext())
		{
			ItemSet temp = x.next();

			if(temp.getItemSet().equals("15"))
			{
				int sd = 0;
			}

			if(temp.getCount()/N < ItemHash.get(temp.getFirstItem()).getMinSup())
			{
				x.remove();
			}
		}
	}


	private void FisrtElimination(List<Item> L)
	{
		List<ItemSet> f1 = new ArrayList<ItemSet>();

		for(Item item : L)
		{

			ItemSet itemSet = new ItemSet(item.getItemValue(), item.getCount());
			f1.add(itemSet);
		}

		cutItemSetList(f1);
		newSet[0] = f1;
	}

	private void secondElimination(List<Item> oneLevel)
	{
		List<ItemSet> two = new ArrayList<ItemSet>();

		for(int i = 0 ; i < oneLevel.size() ; i++)
		{
			Item item = oneLevel.get(i);
			if(item.getCount()/N >= item.getMinSup())
			{
				for(int j = i+1 ; j < oneLevel.size() ; j++)
				{
					Item item2 = oneLevel.get(j);

					if( ( item2.getCount()/N >= item.getMinSup() )  
							&& this.complete( (Math.abs( item2.getCount()/N - item.getCount()/N ) ), 2)  <= supCount)
					
					{
						two.add(createNewItemSet(item.getItemValue() + "," + item2.getItemValue()));
					}
				}
			}
		}

		newSet[1] = two;

	}

	private void otherElimination(List<ItemSet> level,int levelNumber )
	{
		List<ItemSet> newLevel = new ArrayList<ItemSet>();

		for ( int i = 0 ; i < level.size() ; i++ )
		{
			ItemSet one = level.get(i);

			for ( int j = i+1 ; j < level.size() ; j++ )
			{
				ItemSet two = level.get(j);

				String first =  one.getItemSet().substring( 0, one.getItemSet().lastIndexOf(",") );

				String second = two.getItemSet().substring( 0,two.getItemSet().lastIndexOf(",") );

				if(first.compareTo(second) == 0)
				{
					String firstBreak[] = one.getItemSet().split(",");
					String secondBreak[] = two.getItemSet().split(",");

					if( this.complete( Math.abs(( ItemHash.get( firstBreak[firstBreak.length-1] ).getCount()/N ) - 
							ItemHash.get( secondBreak[secondBreak.length-1] ).getCount()/N ), 2 ) <= supCount )
					{

						if ( ItemHash.get(firstBreak[firstBreak.length-1]).getRank() <  ItemHash.get(secondBreak[secondBreak.length-1]).getRank() )
						{
							String newItemSet = one.getItemSet() + "," + secondBreak[secondBreak.length-1];

							if (IsItemSetValid(newItemSet, level))
							{
								newLevel.add(createNewItemSet(newItemSet));
							}
						}
						else
						{
						
							System.out.println("this should not happen");
						}
					}
				}				

			}
		}

		newSet[levelNumber] = newLevel;
	}

	
	
	private ItemSet createNewItemSet(String newItemSet)
	{
		ItemSet itemSet = null;
		int count = 0 ;

		for(Transaction transction : TransactionList)
		{
			if (transction.itemSetIsContains(newItemSet))
				count++;
		}

		itemSet = new ItemSet(newItemSet, count);

		return itemSet;
	}




	// yeni ýtem set i kontrol eder

	private boolean IsItemSetValid(String newItemSet,List<ItemSet> level)
	{
		boolean status = true;

		String item[] = newItemSet.split(",");

		for(int i = 0 ; i < item.length ; i++ )
		{
			StringBuffer s = new StringBuffer();

			for(int j = 0 ; j < item.length ; j++)
			{
				if(j != i)
				{
					if(s.length() == 0 )
					{
						s.append(item[j]);
					}
					else
						s.append("," + item[j]);
				}
			}


			if (  (i != 0) ||  ( ItemHash.get(item[0]).getMinSup() == ItemHash.get(item[1]).getMinSup()) )
			{
				ItemSet temp = new ItemSet(s.toString(), 0f);

				if( !level.contains(temp) )
				{
					status = false;
					break;
				}
			}
		}

		return status;
	}

	public float complete(float value, int precision) {
		float p = (float)Math.pow(10,precision);
		float temp = Math.round(value * p);
		return (float)temp/p;
	}


}
