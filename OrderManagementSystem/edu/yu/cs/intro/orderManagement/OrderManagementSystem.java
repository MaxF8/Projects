//Fixed the validate service so that it doesnt place order as well december 17th 11:42
//Added three shift component updated 11:05pm December 16th 2020
//Fixed default stock level to default product stock level assertion demoline 47 fixed
package edu.yu.cs.intro.orderManagement;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
//import java.util.Map;
/**
* Takes orders, manages the warehouse as well as service providers
*/
public class OrderManagementSystem {
Warehouse myWarehouse;
Set<Service> allOfferedServices;
Set<Service> allDiscontinuedServices;
HashMap<ServiceProvider, Set<Service>> serviceProvidersHashMap;
HashMap<ServiceProvider, Set<Service>> busyServiceProviders;
HashMap<ServiceProvider, Set<Service>> firstShiftProviders;
HashMap<ServiceProvider, Set<Service>> secondShiftProviders;
HashMap<ServiceProvider, Set<Service>> thirdShiftProviders;
int defaultStockLevel;
boolean orderPlaced;
 /**
 * Creates a new Warehouse instance and calls the other constructor
 *
 * @param products
 * @param defaultProductStockLevel
 * @param serviceProviders
 */
 public OrderManagementSystem(Set<Product> products, int defaultProductStockLevel, Set<ServiceProvider> serviceProviders) {
 	//this.myWarehouse = new Warehouse();
 	//this.defaultStockLevel = defaultProductStockLevel;
 	this(products, defaultProductStockLevel, serviceProviders, new Warehouse());
 }

 /**
 * 1) populate the warehouse with the products.
 * 2) retrieve set of services provided by the ServiceProviders, to save it as the set of
services the business can provide
 * 3) create map of services to the List of service providers
 *
 * @param products - set of products to populate the warehouse with
 * @param defaultProductStockLevel - the default number of products to stock for any product
 * @param serviceProviders - set of service providers and the services they provide, to make up the services arm of the business
 * @param warehouse - the warehouse that we will store our products in
 */
 public OrderManagementSystem(Set<Product> products, int defaultProductStockLevel, Set<ServiceProvider> serviceProviders, Warehouse warehouse) {
 	this.allOfferedServices = new HashSet<>();
 	this.allDiscontinuedServices = new HashSet<>();
 	this.serviceProvidersHashMap = new HashMap<>();
 	this.busyServiceProviders = new HashMap<>();
 	this.firstShiftProviders = new HashMap<>();
 	this.secondShiftProviders = new HashMap<>();
 	this.thirdShiftProviders = new HashMap<>();
 	this.defaultStockLevel = defaultProductStockLevel;
 	this.orderPlaced = false;
 	
 	//for(Product nextProduct: products){
 	//	this.addNewProducts(nextProduct, this.defaultProductStockLevel);
 	//}
 	for(ServiceProvider nextProvider: serviceProviders){
 		this.allOfferedServices.addAll(nextProvider.getServices());
 		this.serviceProvidersHashMap.put(nextProvider,nextProvider.getServices());
	}
	this.myWarehouse = warehouse;
	this.addNewProducts(products);
 }

/**
* Accept an order:
* 1) See if we have ServiceProviders for all Services in the order. If not, reject the order.
* 2) See if the we can fulfill the order for Items. If so, place the product orders with the
warehouse and handle the service orders inside this class
* 2a) We CAN fulfill a product order if either the warehouse currently has enough quantity in
stock OR if the product is NOT on the "do not restock" list.
* In the case that the current quantity of a product is < the quantity in the order AND the
product is NOT on the "do not restock" list, the order management system should
* first instruct the warehouse to restock the item, and then tell the warehouse to fulfill this
order.
* 3) Mark the order as completed
* 4) Update busy status of serviced providers...
* @throws IllegalArgumentException if any part of the order for PRODUCTS can not be fulfilled
* @throws IllegalStateException if any part of the order for SERVICES can not be fulfilled
*/
 public void placeOrder(Order order) {
 	Item[] itemArray = new Item[order.getItems().length];
 	HashSet<Service> orderServices = new HashSet<>();
 	HashSet<Product> orderProducts = new HashSet<>();
 	itemArray = order.getItems();
 	for(int i = 0; i<itemArray.length; i++){
 		if(itemArray[i] instanceof Service){
 			orderServices.add((Service)itemArray[i]);
 		}
 		else{
 			orderProducts.add((Product)itemArray[i]);
 		}
 	}
 	this.orderPlaced = true;
 	if(validateServices(orderServices, order)==0||orderServices.isEmpty()){
 		this.orderPlaced = false;
 	

 		for(Product nextOrderProduct: orderProducts){
 			if(this.myWarehouse.isInCatalog(nextOrderProduct.getItemNumber())==false){
 				throw new IllegalArgumentException();
 			}
 			try{
 				this.myWarehouse.fulfill(nextOrderProduct.getItemNumber(), order.getQuantity(nextOrderProduct));
 			}
 			catch(IllegalArgumentException e){
 				if(this.myWarehouse.isRestockable(nextOrderProduct.getItemNumber())){
	 				this.myWarehouse.restock(nextOrderProduct.getItemNumber(), order.getQuantity(nextOrderProduct));
	 				this.myWarehouse.fulfill(nextOrderProduct.getItemNumber(), order.getQuantity(nextOrderProduct));
 				}
 				else{
 					throw new IllegalArgumentException();
 				}
 			}
 		}
 		for(ServiceProvider nextBusyServiceProvider: this.busyServiceProviders.keySet()){
 			nextBusyServiceProvider.assignToCustomer();
 		}
 	}
 	else{
 		this.orderPlaced = false;
 		throw new IllegalStateException();
 	}
 	order.setCompleted(true);
 }

 /**
 * Validate that all the services being ordered can be provided. Make sure to check how many instances of a given service are being requested in
the order, and see if we have enough providers for them.
 * @param services the set of services which are being ordered inside the order
 * @param order the order whose services we are validating
 * @return itemNumber of a requested service that we either do not have provider for at all, or for which we do not have an available provider.
Return 0 if all services are valid.
 */
 protected int validateServices(Collection<Service> services, Order order) {
 	HashMap<Service, Integer> serviceOrder = new HashMap<>();
 
 	boolean providable = false;
 	//gets the third shift out of there
 	if(this.orderPlaced == true){
	 	for(ServiceProvider nextThirdShiftProvider: this.thirdShiftProviders.keySet()){
	 			nextThirdShiftProvider.endCustomerEngagement();
	 	}
 	}
 	this.busyServiceProviders.clear();
 	for(Service nextService: services){
 		serviceOrder.put(nextService, order.getQuantity(nextService));
 	}
 	for(Service nextServiceOrder: serviceOrder.keySet()){
 		if(this.allOfferedServices.contains(nextServiceOrder)){
	 		for(int i = 0; i< serviceOrder.get(nextServiceOrder); i++){
	 			providable = false;
	 				for(ServiceProvider nextServiceProvider: this.serviceProvidersHashMap.keySet()){

		 				if(this.serviceProvidersHashMap.get(nextServiceProvider).contains(nextServiceOrder)){
		 					providable = true;
		 					this.busyServiceProviders.put(nextServiceProvider, this.serviceProvidersHashMap.get(nextServiceProvider));
		 					this.serviceProvidersHashMap.remove(nextServiceProvider);
		 					break;
		 				}
		 			}
		 			if(providable == false){
		 				this.serviceProvidersHashMap.putAll(this.busyServiceProviders);
		 				return nextServiceOrder.getItemNumber();
		 			}
	 			}	
 			}
 			else{
 				this.serviceProvidersHashMap.putAll(this.busyServiceProviders);
 				return nextServiceOrder.getItemNumber();
 			}
 		}
 	if(this.orderPlaced == false){
 		this.serviceProvidersHashMap.putAll(this.busyServiceProviders);
 	}
 	if(this.orderPlaced == true){
	 	this.serviceProvidersHashMap.putAll(this.thirdShiftProviders);
	 	this.thirdShiftProviders.clear();
	 	this.thirdShiftProviders.putAll(this.secondShiftProviders);
	 	this.secondShiftProviders.clear();
	 	this.secondShiftProviders.putAll(this.firstShiftProviders);
	 	this.firstShiftProviders.clear();
	 	this.firstShiftProviders.putAll(this.busyServiceProviders);
	 	this.orderPlaced = false;
 	}
	return 0;
 }

 /**
 * validate that the requested quantity of products can be fulfilled
 * @param products being ordered in this order
 * @param order the order whose products we are validating
 * @return itemNumber of product which is either not in the catalog or which we have insufficient quantity of. Return 0 if we can fulfill.
 */
 protected int validateProducts(Collection<Product> products, Order order) {
 	for(Product nextProduct: products){
 		//if(!this.myWarehouse.canFulfill(nextProduct.getItemNumber(),order.getQuantity(nextProduct))){
 		//	return nextProduct.getItemNumber();
 		//}
 		if(!(this.myWarehouse.isRestockable(nextProduct.getItemNumber()) || this.myWarehouse.canFulfill(nextProduct.getItemNumber(),order.getQuantity(nextProduct)))){
 			return nextProduct.getItemNumber();
 		}
 	}
 	return 0;
 }

 /**
 * Adds new Products to the set of products that the warehouse can ship/fulfill
 * @param products the products to add to the warehouse
 * @return set of products that were actually added (don't include any products that were already in the warehouse before this was called!)
 */
 protected Set<Product> addNewProducts(Collection<Product> products) {
 	Set<Product> added = new HashSet<>();
 	for(Product nextProduct: products){
 		try{
 			
 			this.myWarehouse.addNewProductToWarehouse(nextProduct, this.defaultStockLevel);
 			added.add(nextProduct);
 			
 		}
 		catch(IllegalArgumentException e){}
 	}
 	return added;
 }

 /**
 * Adds an additional ServiceProvider to the system. Update all relevant data about which Services are offered and which ServiceProviders provide
which services
 * @param provider the provider to add
 */
 protected void addServiceProvider(ServiceProvider provider) {
	this.allOfferedServices.addAll(provider.getServices());
	this.allOfferedServices.removeAll(allDiscontinuedServices);
	this.serviceProvidersHashMap.put(provider, provider.getServices());
 }

 /**
 *
 * @return get the set of all the products offered/sold by this business
 */
 public Set<Product> getProductCatalog() {
 	return this.myWarehouse.getAllProductsInCatalog();
 }

 /**
 * @return get the set of all the Services offered/sold by this business
 */
 public Set<Service> getOfferedServices() {
 	return this.allOfferedServices;
 }

 /**
 * Discontinue Item, i.e. stop selling a Service or Product.
 * Also prevent the Item from being added in the future.
 * If it's a Service - remove it from the set of provided services.
 * If it's a Product - still sell whatever instances of this Product are in stock, but do not restock it.
 * @param item the item to discontinue see {@link Item}
 */
 protected void discontinueItem(Item item) {
 	//Im not sure about this
 	if(item instanceof Product){
 		this.myWarehouse.doNotRestock(item.getItemNumber());
 	}
 	else{
 		this.allOfferedServices.remove((Service)item);
 		this.allDiscontinuedServices.add((Service)item);
 	}
 }

 /**
 * Set the default product stock level for the given product
 * @param prod
 * @param level
 */
 protected void setDefaultProductStockLevel(Product prod, int level) {
 	this.myWarehouse.setDefaultStockLevel(prod.getItemNumber(), level);
 }
}

