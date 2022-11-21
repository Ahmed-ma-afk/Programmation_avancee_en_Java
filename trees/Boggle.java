package trees;

// le jeu du Boggle

// trouve tous les mots d'au moins 3 lettres que l'on peut faire
// avec un tirage donné 

// suppose un dictionnaire (en majuscules et sans accents)
// dans le fichier french-up.txt

// exemple : java -cp bin trees.Boggle RHREYPCSWNSNTEGO
// solutionne le tirage
//   RHRE
//   YPCS
//   WNSN
//   TEGO
// et affiche 34 mots (en 195 ms)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Boggle {

  static String letters;
  static int count = 0;
  static HashSet<String> words = new HashSet<>();
  
  public static void main(String[] args) throws FileNotFoundException {
    double start = System.currentTimeMillis();
    Trie t = buildDict("french-up.txt");
    letters = args[0];
    assert letters.length() == 16;
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++)
        find(t, i, j, 0, "");
    // on imprime les mots dans l'ordre alphabétique
    ArrayList<String> list = new ArrayList<>(words);
    Collections.sort(list);
    for (String w: list)
      System.out.println(w);
    System.out.println(words.size() + " words");
    System.err.println((System.currentTimeMillis() - start) + " ms");
  }
  
  // cherche à partir de t et de la position i, j
  // mask = les lettres déjà utilisées
  // prefix = le mot formé par les lettres déjà utilisées
  //  (si bien que prefix.length() + pop(mask) == 16)
  static void find(Trie t, int i, int j, int mask, String prefix) {
    int pos = 4 * i + j, bit = 1 << pos;
    if ((mask & bit) != 0) return;
    mask |= bit;
    char c = letters.charAt(pos);
    t = t.branch(c);
    if (t == null) return;
    prefix += c;
    if (t.contains("") && prefix.length() >= 3)
      words.add(prefix);
    for (int di = -1; di <= 1; di++)
      for (int dj = -1; dj <= 1; dj++)
        if ((di != 0 || dj != 0) && validPos(i + di, j + dj))
          find(t, i + di, j + dj, mask, prefix);
  }
  
  static boolean validPos(int i, int j) {
    return 0 <= i && i < 4 && 0 <= j && j < 4;
  }
  
  static Trie buildDict(String filename) throws FileNotFoundException {
    Scanner sc = new Scanner(new File(filename));
    Trie t = new Trie();
    while (sc.hasNext()) {
      String s = sc.next();
      if (s.length() >= 3 && validWord(s))
        t.add(s);
    }
    sc.close();
    return t;
  }
  
  static boolean validWord(String s) {
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < 'A' || c > 'Z') {
        return false;
      }
    }
    return true;
  }
  
}
