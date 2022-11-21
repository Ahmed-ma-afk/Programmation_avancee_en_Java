package backtracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Sudoku {

  private int[] grid; // 0..80 (9 * row + col)

  Sudoku() {
    this.grid = new int[81];
  }

  Sudoku(String s) {
    this.grid = new int[81];
    for (int i = 0; i < 81; i++)
      this.grid[i] = s.charAt(i) - '0';
    printSpace();
  }

  void printSpace() {
    int z = 0;
    BigInteger s = BigInteger.ONE;
    for (int i = 0; i < 81; i++)
      if (this.grid[i] == 0) {
        z++;
        int c = 0;
        for (int v = 1; v <= 9; v++) {
          grid[i] = v;
          if (check(i)) c++;
        }
        grid[i] = 0;
        s = s.multiply(BigInteger.valueOf(c));
        System.err.print(c + "*");
      }
    System.err.println();
    System.err.println(z + " cases vides");
    BigInteger b = BigInteger.valueOf(9).pow(z);
    System.err.println("9^" + z + " = " + b + " = " + b.doubleValue());
    System.err.println("espace = " + s + " = " + s.doubleValue());
  }

  int   row(int c) { return c / 9; }
  int   col(int c) { return c % 9; }
  int group(int c) { return 3 * (row(c) / 3) + col(c) / 3; }

  boolean sameZone(int c1, int c2) {
    return   row(c1) == row(c2)
        ||   col(c1) == col(c2)
        || group(c1) == group(c2);
  }

  // vérifie que la valeur dans p est compatible avec les autres cases
  boolean check(int p) {
    for (int c = 0; c < 81; c++)
      if (c != p && sameZone(p, c) && this.grid[p] == this.grid[c])
        return false;
    return true;
  }

  // stats
  int[] levels = new int[82];
  int level = 0;
  
  // essaye de résoudre la grille courante et renvoie un booléen indiquant le succès
  boolean solve() {
    levels[level++]++;
    for (int c = 0; c < 81; c++)
      if (this.grid[c] == 0) {
        for (int v = 1; v < 10; v++) {
          this.grid[c] = v;
          if (check(c) && solve())
            return true;
        }
        this.grid[c] = 0;
        level--;
        return false;
      }
    return true;
  }
  
  int count = 0;
  
  // essaye de résoudre la grille courante et renvoie un booléen indiquant le succès
  void count() {
    levels[level++]++;
    for (int c = 0; c < 81; c++)
      if (this.grid[c] == 0) {
        for (int v = 1; v < 10; v++) {
          this.grid[c] = v;
          if (check(c))
            count();
        }
        this.grid[c] = 0;
        level--;
        return;
      }
    level--;
    count++;
  }
  
  void print() {
    for (int i = 0; i < 9; i++) {
      if (i % 3 == 0) System.out.println("+---+---+---+");
      for (int j = 0; j < 9; j++) {
        if (j % 3 == 0) System.out.print("|");
        System.out.print(this.grid[9*i+j]);
      }
      System.out.println("|");
    }
    System.out.println("+---+---+---+");
  }

  void stat() {
    System.out.println(Arrays.toString(levels));
//    for (int i = 0; i < levels.length; i++)
//      System.out.println(i + " " + levels[i]);
    int tot = 0;
    for (int v: levels) tot += v;
    System.out.println(tot + " calls");
  }
  
  public static void main(String[] args) throws FileNotFoundException {
    double start = System.currentTimeMillis();
    Sudoku sk;
    
    sk = new Sudoku("200000060000075030048090100000300000300010009000008000001020570080730000090000004");
    // sk = new Sudoku("000316059006000807000000200050030090790602018010080040008000000309000600560847000");
    sk.print();
    sk.solve();
    sk.print();
    sk.stat();
    System.out.println(((System.currentTimeMillis() - start) / 1000) + " s");
    System.exit(0);
    
    start = System.currentTimeMillis();
    sk = new Sudoku("200000060000075030048090100000300000300010009000008000001020570080730000090000004");
    // sk = new Sudoku("200000060000075030048090100000300000300010009000008000001020570080730000090000004"); // 1 solution
    // sk = new Sudoku("200000060000075030048090100000300000300010009000008000001020570080730000090000004");
    // sk = new Sudoku("200000060000070030048090100000300000300010009000008000001020570080730000090000004"); // 7 solutions
    // sk = new Sudoku("200000060000070030048090100000000000300010009000008000001020570080730000090000004"); // 7 solutions
    // sk = new Sudoku("200000060000070030048090100000300000300010000000008000001020570080730000090000004"); // 433 solutions
    sk.print();
    sk.count();
    System.out.println(sk.count + " solutions");
    sk.stat();
    System.out.println(((System.currentTimeMillis() - start) / 1000) + " s");
    // System.exit(0);
    
    Scanner sc = new Scanner(new File("sudoku.txt"));
    while (sc.hasNext()) {
      String s = sc.nextLine();
      System.out.println("s = " + s);
      Sudoku sks = new Sudoku(s);
      sks.print();
      System.out.println();
      if (sks.solve())
        sks.print();
      else
        System.out.println("pas de solution");
      System.out.println();
    }
    sc.close();
    System.out.println((System.currentTimeMillis() - start) / 1000);
  }
}

/* expérience : maintenir la liste des cases à remplir (par ex. dans une pile)
 * n'améliore en rien les performances */
