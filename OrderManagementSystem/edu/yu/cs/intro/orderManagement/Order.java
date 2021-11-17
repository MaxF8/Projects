package edu.yu.cs.intro.orderManagement;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
public class Order
{
	private Map<Item,Integer> order;
	private boolean completed;
	public Order()
	{
		this.order= new HashMap<>();
		this.completed=false;
	}
	public Item[] getItems()
	{
		//return an array version of every key in map
		Set<Item> items= order.keySet();
		Item[] itemArray= items.toArray(new Item[items.size()]);//= (Item) items.toArray();
		return itemArray;
	}
	public int getQuantity(Item b)
	{
		//iterate through the list, record amount of times the item is in list
		return order.get(b);
	}
	public void addToOrder(Item item, int quantity)
	{
		//add the item to list quantity amount of times
		order.put(item, quantity);
	}
	public double getProductsTotalPrice()
	{
		//Benjy Katz: I'm not sure iterating through products will work to just get products in an item array
		//iterate through list, get total price
		double totalPrice=0;
		Set<Item> items= order.keySet();
		//Iterator<Product> itr=items.iterator();
		int quantity;
		for(Item p: items)
		{
			if(p instanceof Product){
				quantity=this.order.get(p);
				totalPrice+=p.getPrice()*quantity;
			}
		}
		return totalPrice;
		
	}
	public double getServicesTotalPrice()
	{
		//iterate through list, get total price
		double totalPrice=0;
		Set<Item> items= order.keySet();
		//Iterator<Item> itr=items.iterator();
		int quantity;
		for(Item s: items)
		{
			if(s instanceof Service){
				quantity=this.order.get(s);
				totalPrice+=s.getPrice()*quantity;
			}
		}
		return totalPrice;
	}
	public boolean isCompleted()
	{
		return this.completed;
	}
	public void setCompleted(boolean completed)
	{
		if(completed==true)
		{
			this.completed=true;
		}else{
			this.completed=false;
		}
	}
}