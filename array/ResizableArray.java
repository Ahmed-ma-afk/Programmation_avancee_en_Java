package array;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

// tableau redimensionnable

public class ResizableArray {

  private int length; // la taille du tableau = le nombre d'éléments
                      // significatifs dans data
  private int[] data; // invariant: 0 <= length <= data.length

  public ResizableArray(int len) {
    this.length = len;
    this.data = new int[len];
  }

  public int size() {
    return this.length;
  }

  public int get(int i) {
    if (i < 0 || i >= this.length)
      throw new ArrayIndexOutOfBoundsException(i);
    return this.data[i];
  }

  public void set(int i, int v) {
    if (i < 0 || i >= this.length)
      throw new ArrayIndexOutOfBoundsException(i);
    this.data[i] = v;
  }

  public void setSize(int len) {
    int n = this.data.length;
    if (len > n) {
      int[] a = new int[Math.max(len, 2 * n)];
      for (int i = 0; i < this.length; i++)
        a[i] = this.data[i];
      this.data = a;
      // note : on peut remplacer les quatre lignes ci-dessus par
      // this.data = Arrays.copyOfRange(this.data, 0, Math.max(len, 2 * n));
    } else if (4 * len < n)
    	this.data = Arrays.copyOfRange(this.data, 0, n / 2);
    this.length = len;
  }

  public void append(int v) {
    int n = this.length;
    this.setSize(n + 1);
    this.data[n] = v;
  }

  public void append(int a[]) {
    int n = this.length;
    this.setSize(n + a.length);
    for (int v : a)
      this.data[n++] = v;
  }

  public int[] toArray() {
    return Arrays.copyOfRange(this.data, 0, this.length);
  }

  public String toString() {
    StringBuffer b = new StringBuffer("("+this.data.length+")[");
    if (0 < this.length)
      b.append(this.data[0]);
    for (int i = 1; i < this.length; i++)
      b.append(", ").append(this.data[i]);
    return b.append("]").toString();
  }

}

class ReadLines {

  public static void main(String[] args) throws IOException {
    ResizableArray r = new ResizableArray(0);
    BufferedReader f = new BufferedReader(new FileReader("numbers.txt"));
    while (true) {
    	String s = f.readLine();
    	if (s == null)
    		break;
    	System.out.println(s);
    	r.append(Integer.parseInt(s));
    }
    System.out.println(r.size() + " lines");
    f.close();
  }
}

class TestResizeableArray {

  static void check(String test, String result, String expected) {
    System.out.print("  test " + test + "...");
    if (result.equals(expected)) {
      System.out.println("OK");
    } else {
      System.out.println("ECHEC");
      System.out.println("    attendu = \"" + expected + "\"");
      System.out.println("    obtenu  = \"" + result + "\"");
      throw new Error("ECHEC");
    }
  }

  static void check(String test, int result, String expected) {
    check(test, String.valueOf(result), expected);
  }

  static void check(String test, ResizableArray result, String expected) {
    check(test, result.toString(), expected);
  }

  public static void main(String[] args) {
    System.out.println("Tests question 1.1");
    ResizableArray a = new ResizableArray(3);
    check("1.1.a", a.size(), "3");
    check("1.1.b", a, "(3)[0, 0, 0]");
    check("1.1.c", a.get(1), "0");
    a.set(1, 42);
    check("1.1.d", a.get(1), "42");

    System.out.println();
    System.out.println("Tests question 1.2");
    a.setSize(7);
    check("1.2.a", a.size(), "7");
    check("1.2.b", a.get(1), "42");
    check("1.2.c", a, "(7)[0, 42, 0, 0, 0, 0, 0]");
    a.set(3, 17);
    check("1.2.d", a, "(7)[0, 42, 0, 17, 0, 0, 0]");
    a.setSize(4);
    check("1.2.e", a.size(), "4");
    check("1.2.f", a, "(7)[0, 42, 0, 17]");
    a.setSize(8);
    check("1.2.g", a.size(), "8");
    check("1.2.h", a, "(14)[0, 42, 0, 17, 0, 0, 0, 0]");
    a.set(7, 28);
    check("1.2.i", a, "(14)[0, 42, 0, 17, 0, 0, 0, 28]");

    System.out.println();
    System.out.println("Tests question 1.3");
    a.append(new int[] { 1, 2, 3 });
    check("1.3.a", a.size(), "11");
    check("1.3.b", a, "(14)[0, 42, 0, 17, 0, 0, 0, 28, 1, 2, 3]");
    a.append(new int[] {});
    check("1.3.c", a.size(), "11");
    a.append(new int[] { 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 });
    check("1.3.d", a.size(), "21");
    check("1.3.e", a,
        "(28)[0, 42, 0, 17, 0, 0, 0, 28, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]");
    
    ResizableArray r = new ResizableArray(0);
    for (int i = 0; i < 10; i++) {
    	r.append(i);
    	System.out.println(r);
    }
  }

}
