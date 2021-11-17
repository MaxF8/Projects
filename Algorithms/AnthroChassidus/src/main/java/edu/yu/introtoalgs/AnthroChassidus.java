package edu.yu.introtoalgs;

/** Defines and implements the AnthroChassidus API per the requirements
 * documentation.
 *
 * @author Avraham Leff
 */

public class AnthroChassidus {

  /** Constructor.  When the constructor completes, ALL necessary processing
   * for subsequent API calls have been made such that any subsequent call will
   * incur an O(1) cost.
   *
   * @param n the size of the underlying population that we're investigating:
   * need not correspond in any way to the number of people actually
   * interviewed (i.e., the number of elements in the "a" and "b" parameters).
   * Must be greater than 2.
   * @param a interviewed people, element value corresponds to a unique "person
   * id" in the range 0..n-1
   * @param b interviewed people, element value corresponds to a unique "person
   * id" in the range 0..n-1.  Pairs of a_i and b_i entries represent the fact
   * that the corresponding people follow the same Chassidus (without
   * specifying what that Chassidus is).
   */
  private int[] id; // parent link (site indexed)
  private int[] size; // size of component for roots (site indexed)
  private int count; // number of components

  public AnthroChassidus(final int n, final int[] a, final int[] b) {
    if (a.length != b.length)
    {
      throw new IllegalArgumentException();
    }
    count = n;

    id = new int[n];
    size = new int[n];

    for (int i = 0; i < n; i++)
    {
      id[i] = i;
      size[i] = 1; 
    }

    for (int i = 0; i < a.length; i++)
    {
      union(a[i], b[i]); //connect all indices
    }
  }

  private int find(int p)
    { //find the root
      while (p != id[p]) 
      {
        id[p] = id[id[p]]; //(path compression) one pass: making every other node in path point to its grandparent

        p = id[p];
      }
      return p;
    }
    private void union(int p, int q)
    {
      int i = find(p);
      int j = find(q);
      if (i == j) return;
      if (size[i] < size[j]) 
      { 
        id[i] = j; 
        size[j] += size[i];
      }
      else 
      {
        id[j] = i;
        size[i] += size[j]; 
      }
      count--;
    }
  
  //TYPES OF CHASSIDIM
  /** Return the tightest value less than or equal to "n" specifying how many
   * types of Chassidus exist in the population: this answer is inferred from
   * the interviewers data supplied to the constructor
   *
   * @return tightest possible lower bound on the number of Chassidus in the
   * underlying population.
   */
  public int getLowerBoundOnChassidusTypes() {
    return this.count;
  }

  //PEOPLE WHO SHARE THE SAME TYPE
  //return 1 if Person is only chassid type
  /** Return the number of interviewed people who follow the same Chassidus as
   * this person.
   *
   * @param id uniquely identifies the interviewed person
   * @return the number of interviewed people who follow the same Chassidus as
   * this person.
   */
  
  public int nShareSameChassidus(final int id) {
    return size[this.id[id]];
  }

} // class
