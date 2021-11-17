package edu.yu.cs.intro.orderManagement;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
/**
* Stocks products, fulfills product orders, manages stock of products.
*/
public class Warehouse {
	/*
	public static void main(String[] args)
    {
		Warehouse a = new Warehouse();
		Product p1 = new Product("max", 2, 4);
		a.doNotRestock(4);
		Product p2 = new Product("jon", 3, 2);

		a.addNewProductToWarehouse(p1,5);
		a.addNewProductToWarehouse(p2,6);
       
		System.out.println(a.getAllProductsInCatalog());

		//System.out.println(a.getAllProductsInCatalog);
	}
*/
	




	private HashMap<Integer, Integer> inventory; // k = productId; v= quantity;
	private HashMap<Integer, Integer> catalog; //k= productId v = desired stock level
	private Set<Product> products;
	private Set<Integer> dnr;
 /**
 * create a warehouse, initialize all the instance variables
 */
 protected Warehouse(){
	this.inventory = new HashMap<>(); // k = productId; v= quantity;
	this.catalog = new HashMap<>(); //k= productId v = desired stock level
	this.products = new HashSet<>();
	this.dnr = new HashSet<>();
 }

 /**
 * @return all unique Products stocked in the warehouse
 */
 protected Set<Product> getAllProductsInCatalog(){
 	return this.products;
 }

 /**
 * Add a product to the warehouse, at the given stock level.
 * @param product
 * @param desiredStockLevel the number to stock initially, and also to restock to when
subsequently restocked
 * @throws IllegalArgumentException if the product is in the "do not restock" set, or if the
product is already in the warehouse
 */
 protected void addNewProductToWarehouse(Product product, int desiredStockLevel){ 
 	//if it is not in the do not stock set or allready in the warehouse{}
 	if(!this.products.contains(product) && !this.dnr.contains(product.getItemNumber())){
 		this.catalog.put(product.getItemNumber(), desiredStockLevel);
 		this.inventory.put(product.getItemNumber(), desiredStockLevel);
 		this.products.add(product);
 		
 	}
 	else{
 		throw new IllegalArgumentException();
 	}
 }

    /**
     * If the actual stock is already >= the minimum, do nothing. Otherwise, raise it to minimum OR the default stock level, whichever is greater
     * @param productNumber
     * @param minimum
     * @throws IllegalArgumentException if the product is in the "do not restock" set, or if it is not in the catalog
     */
 protected void restock(int productNumber, int minimum){
 	if(isRestockable(productNumber)){
 		if(this.inventory.get(productNumber)<minimum){
 			if(minimum > this.catalog.get(productNumber)){
 				this.inventory.put(productNumber,  minimum);
 			}
 			else{
 				this.inventory.put(productNumber, this.catalog.get(productNumber));
 			}
 		}
 	}
 	else{
 		throw new IllegalArgumentException();
 	}

 }

 /**
 * Set the new default stock level for the given product
 * @param productNumber
 * @param quantity
 * @return the old default stock level
 * @throws IllegalArgumentException if the product is in the "do not restock" set, or if it is
not in the catalog
 */
 protected int setDefaultStockLevel(int productNumber, int quantity){
 	if(isRestockable(productNumber)){
 		return this.catalog.put(productNumber, quantity);
 	}
 	else{
 		throw new IllegalArgumentException();
 	}
 }

 /**
 * @param productNumber
 * @return how many of the given product we have in stock, or zero if it is not stocked
 */
 protected int getStockLevel(int productNumber){
 	if(this.inventory.get(productNumber) == null) return 0;
 	return this.inventory.get(productNumber);
 }

 /**
 * @param itemNumber
 * @return true if the given item number is in the warehouse's catalog, false if not
 */
 protected boolean isInCatalog(int itemNumber){
 	if(this.catalog.get(itemNumber) == null) return false;
 	else{
 		return true;
 	}
 }

 /**
 *
 * @param itemNumber
 * @return false if it's not in catalog or is in the "do not restock" set. Otherwise true.
 */
 protected boolean isRestockable(int itemNumber){
 	return(this.catalog.keySet().contains(itemNumber) && !this.dnr.contains(itemNumber));
 }

 /**
 * add the given product to the "do not restock" set
 * @param productNumber
 * @return the current actual stock level
 */
 protected int doNotRestock(int productNumber){
 	this.dnr.add(productNumber);
 	return this.inventory.get(productNumber);
 }

 /**
 * can the warehouse fulfill an order for the given amount of the given product?
 * @param productNumber
 * @param quantity
 * @return false if the product is not in the catalog or there are fewer than quantity of the
products in the catalog. Otherwise true.
 */
 protected boolean canFulfill(int productNumber, int quantity){
 	if(!this.inventory.keySet().contains(productNumber)) return false;
 	if(this.inventory.get(productNumber)>= quantity) return true;
 	return false;
 }

 /**
 * Fulfill an order for the given amount of the given product, i.e. lower the stock levels of
the product by the given amount
 * @param productNumber
 * @param quantity
 * @throws IllegalArgumentException if {@link #canFulfill(int, int)} returns false
 */
 protected void fulfill(int productNumber, int quantity){
 	if(canFulfill(productNumber, quantity)){
 		this.inventory.put(productNumber, this.inventory.get(productNumber)-quantity);
 	}
 	else{
 		throw new IllegalArgumentException();
 	}
 }

}