package hash;

import java.util.Arrays;

// tables de hachage

class Bucket {
  final String element;
  Bucket next;
  
  Bucket(String element, Bucket next) {
    this.element = element;
    this.next = next;
  }
  
  static boolean contains(Bucket b, String s) {
    for (; b != null; b = b.next)
      if (b.element.equals(s)) return true;
    return false;
  }
  
  static Bucket remove(Bucket b, String s) {
    if (b == null) return null;
    if (b.element.equals(s)) return b.next;
    b.next = remove(b.next, s);
    return b;
  }

  static String toString(Bucket b) {
    StringBuffer sb = new StringBuffer("");
    for (; b != null; b = b.next)
      sb.append(b.element + ", ");
    return sb.toString();
  }
}

public class HashTable implements intf.Set<String> {

  private int size; // total number of elements
  private Bucket[] buckets;
  
  final private static int M = 17;
  
  public HashTable() {
    this.size = 0;
    this.buckets = new Bucket[M];
  }
  
  private int hash(String s) {
    int h = 0;
    for (int i = 0; i < s.length(); i++)
      h = s.charAt(i) + 19 * h;
    return (h & 0x7fffffff) % M;
  }
  
  public void add(String s) {
    int i = hash(s);
    if (Bucket.contains(this.buckets[i], s)) return;
    this.buckets[i] = new Bucket(s, this.buckets[i]);
    this.size++;
  }
  
  public boolean contains(String s) {
    int i = hash(s);
    return Bucket.contains(this.buckets[i], s);
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer("[");
    for (Bucket b : this.buckets)
      sb.append(Bucket.toString(b));
    sb.append("]");
    return sb.toString();
  }

  public boolean isEmpty() {
    return this.size == 0;
  }

  public int size() {
    return this.size;
  }

  public void remove(String s) {
    int i = hash(s);
    if (!Bucket.contains(this.buckets[i], s)) return;
    this.buckets[i] = Bucket.remove(this.buckets[i], s);
    this.size--;
  }

  @Override
  public void clear() {
    Arrays.fill(this.buckets, null);
    this.size = 0;
  }
  
}
