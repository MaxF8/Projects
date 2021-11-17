package edu.yu.cs.intro.orderManagement;
import java.util.HashSet;
import java.util.Set;
/**
* 1) has a Set of services that he can provide
* 2) can only work on one order at a time - once assigned to a customer, can not take another
assignment until 3 other orders have been placed with the order management system
* 3) is uniquely identified by its ID
*/

public class ServiceProvider {
 /**
 *
 * @param name
 * @param id unique id of the ServiceProvider
 * @param services set of services this provider can provide
 */

/*
 public static void main (String[] args)
{
    
   /* Set<Service> heyo = new HashSet<>();
    Set<Service> hi = new HashSet<>();

    Service s1 = new Service(1,2,3,"sup");

    ServiceProvider a = new ServiceProvider("hey", 80, heyo);
    ServiceProvider b = new ServiceProvider("yo", 4, hi);
    System.out.println(a.getName() + a.getId());
   // System.out.println(a.addService(s1));

    a.assignToCustomer();
}
    */

  


 String name;
 int id;
 Set<Service> services;
//this.name = name;
//Set<ServiceProvider> occupiedProv = new Set<ServiceProvider>(); 
boolean busy;
 public ServiceProvider(String name, int id, Set<Service> services)
 {
    this.name = name;
    this.id = id;
    this.services = services;
    this.busy = false;
 }
 
 public String getName()
 {
    return this.name;
 }

 public int getId()
 {
    return this.id;
 }
 /**
 * Assign this provider to a customer. Record the fact that he is busy.
 * @throws IllegalStateException if the provider is currently assigned to a job
 */
 protected void assignToCustomer()
 {
     
    if (this.busy == true)
    {
       throw new IllegalStateException(); 
    }
    
    this.busy = true;
   
   
 }
 /**
 * Free this provider up - is not longer assigned to a customer
 * @throws IllegalStateException if the provider is NOT currently assigned to a job
 */
 protected void endCustomerEngagement()
 {
   if (this.busy == false)
   throw new IllegalStateException(); 
   
       this.busy = false;
 }
 /**
 * @param s add the given service to the set of services this provider can provide
 * @return true if it was added, false if not
 */
 protected boolean addService(Service s)
 {


    boolean bool = services.add(s);
    return bool;
  
    /*

    services.add(s);
    if (services.contains(s))
    {
        return true;
    }
    return false;
   /* try 
    { 
        sevices.add(s);
    }  
    catch (Exception e)  
    { 
       return false;
    } 
    return true;
    */
    
 }
 /**
 * @param s remove the given service to the set of services this provider can provide
 * @return true if it was removed, false if not
 */
 protected boolean removeService(Service s)
 {
    boolean bool = services.remove(s);
    return bool;
    
 }
 /**
 *
 * @return a COPY of the set of services. MUST NOT return the Set instance itself, since that
would allow a caller to then add/remove services
 */

 public Set<Service> getServices()
 {
    //Alternative: Set<Service> copySet = new HashSet<>(services);
     Set<Service> copySet = new HashSet<>();
    copySet.addAll(services);
   // return this.copySet??
   return copySet;
 }
 @Override
 public boolean equals(Object o) 
 {
    if (this == o)
    {
        return true;
    }
    if (o == null)
    {
        return false;
    }
    //See if it is an instance of the same class
    if (getClass() != o.getClass()) 
    {
        return false;
    }
    ServiceProvider idObject = (ServiceProvider)o;
    if (this.id == idObject.id)
    {
        return true;
    }
    return false;

 }
 @Override
 public int hashCode() 
 {
     return this.id;
 }
}
