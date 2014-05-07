//091044043	
//zeynep gazal KAYA

package edu.dm.object;

public class Item implements Comparable<Item>
{
	String item; //elemanlar
	float minSup; // miimum esik degeri
	float count; //sayısı	

	int rank; //sırası
	
	public Item(String item, float count,float minSup)
	{
		this.item = item;
		this.count = count;
		this.minSup = minSup;
	}

	@Override
	public int compareTo(Item o) 
	{
		if(minSup < o.minSup)
			return -1;
		else if (minSup > o.minSup)
			return 1;
		else
			return 0;
	}

	public String getItemValue() {
		return item;
	}

	public float getCount() {
		return count;
	}

	public float getMinSup() {
		return minSup;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Item [item=" + item + ", count=" + count + ", minSup=" + minSup
				+ ", rank=" + rank + "]";
	}

	



}
