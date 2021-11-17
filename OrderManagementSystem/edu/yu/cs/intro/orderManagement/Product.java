package edu.yu.cs.intro.orderManagement;
public class Product implements Item
{
	public String name;
	public double price;
	public int productID;

	public Product(String name, double price, int productID)
	{
		this.name=name;
		this.price=price;
		this.productID=productID;
	}

	public int getItemNumber()
	{
		return this.productID;
	}

	public String getDescription()
	{
		return this.name;
	}

	public double getPrice()
	{
		return this.price;
	}

	public boolean equals(Object o)
	{
		if(this==o)
		{
			return true;
		}
		if(o==null)
		{
			return false;
		}
		if(o instanceof Product){
			Product otherProduct= (Product) o;
			if(this.productID==otherProduct.productID){
				return true;
			}else{
				return false;
		}
	}
			return false;
		
	}

	public int hashCode()
	{
		return this.productID;
	}
}