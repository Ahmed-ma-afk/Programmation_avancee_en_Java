package compression;

// codage de Huffman

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import heap.Heap;

abstract class HuffmanTree implements Comparable<HuffmanTree> {
  int freq;
  @Override
  public int compareTo(HuffmanTree that) {
    return this.freq - that.freq;
  }
  abstract void traverse(String prefix, Map<Character, String> m);
  abstract char find(String s, int i);
  abstract void encode(StringBuffer sb);
}

class Leaf extends HuffmanTree {
  final char c;
  Leaf(char c) {
    this.c = c;
    this.freq = 0;
  }
  @Override
  void traverse(String prefix, Map<Character, String> m) {
    m.put(this.c, prefix);
  }
  @Override
  char find(String s, int i) {
    return this.c;
  }
  @Override
  void encode(StringBuffer sb) {
    sb.append('0');
    sb.append(this.c);
  }
}

class Node extends HuffmanTree {
  HuffmanTree left, right;
  Node(HuffmanTree left, HuffmanTree right) {
    this.left = left;
    this.right = right;
    this.freq = left.freq + right.freq;
  }
  @Override
  void traverse(String prefix, Map<Character, String> m) {
    this.left.traverse(prefix + '0', m);
    this.right.traverse(prefix + '1', m);
  }
  @Override
  char find(String s, int i) {
    if (i == s.length()) throw new Error("corrupted code; bad alphabet?");
    return (s.charAt(i) == '0' ? this.left : this.right).find(s, i+1);
  }
  @Override
  void encode(StringBuffer sb) {
    sb.append('1');
    this.left.encode(sb);
    this.right.encode(sb);
  }
}

public class Huffman {
  
  private HuffmanTree tree;
  private Map<Character, String> codes;
  
  Huffman(Collection<Leaf> alphabet) {
    if (alphabet.size() <= 1) throw new IllegalArgumentException();
    this.tree = buildTree(alphabet);
    this.codes = new HashMap<Character, String>();
    this.tree.traverse("", this.codes);
  }
  
  HuffmanTree buildTree(Collection<Leaf> alphabet) {
    Heap<HuffmanTree> pq = new Heap<HuffmanTree>(); // ou SkewHeap
    for (Leaf l: alphabet)
      pq.add(l);
    while (pq.size() > 1) {
      HuffmanTree left = pq.removeMin();
      HuffmanTree right = pq.removeMin();
      pq.add(new Node(left, right));
    }
    return pq.getMin();
  }
  
  String encode(String msg) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < msg.length(); i++)
      sb.append(this.codes.get(msg.charAt(i)));
    return sb.toString();
  }
  
  String decode(String msg) {
    StringBuffer sb = new StringBuffer();
    int i = 0;
    while (i < msg.length()) {
      char c = this.tree.find(msg, i);
      sb.append(c);
      i += this.codes.get(c).length();
    }
    return sb.toString();
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for (Entry<Character, String> e: this.codes.entrySet())
      sb.append("[" + e.getKey() + " " + e.getValue() + "]");
    return sb.toString();
  }
   
  public String encodeTree() {
    StringBuffer sb = new StringBuffer();
    this.tree.encode(sb);
    return sb.toString();
  }
  
  private HuffmanTree decodeTree(StringReader r) throws IOException {
    int c = r.read();
    if (c == -1) throw new IllegalArgumentException("bad tree");
    if (c == '0') return new Leaf((char)r.read());
    HuffmanTree left = decodeTree(r);
    HuffmanTree right = decodeTree(r);
    return new Node(left, right);
  }
  
  public void decodeTree(String s) throws IOException {
    this.tree = decodeTree(new StringReader(s));
    this.codes = new HashMap<Character, String>();
    this.tree.traverse("", this.codes);
  }
  
}

class TestHuffman {
  
  static Collection<Leaf> buildAlphabet(String text) {
    HashMap<Character, Leaf> index = new HashMap<Character, Leaf>();
    for (int i = 0; i < text.length(); i++) {
      Character c = text.charAt(i);
      Leaf l = index.get(c);
      if (l == null) { l = new Leaf(c); index.put(c, l); }
      l.freq++;
    }
    return index.values();
  }
  
  public static void main(String[] args) {
    String text = "les bases de la programmation et de l'algorithmique";
    Collection<Leaf> alphabet = buildAlphabet(text);
    for (Leaf l: alphabet)
      System.out.print("[" + l.c + " " + l.freq + "]");
    System.out.println();
    Huffman h = new Huffman(alphabet);
    System.out.println(h);
    System.out.println(h.encode(text));
    System.out.println(h.encode(text).length() + " / " + 7 * text.length());
    assert (h.decode(h.encode("bases")).equals("bases"));
    assert (h.decode(h.encode("goal")).equals("goal"));

    Huffman h1 = new Huffman(buildAlphabet("abb")); // de sorte que a sera 0 et b 1
    System.out.println(h1);
    System.out.println(h1.encode("aaabbb"));
    assert(h1.decode("1010").equals("baba"));
    
    String s2 = "Mississippi";
    Huffman h2 = new Huffman(buildAlphabet(s2));
    System.out.println(h2);
    System.out.println(h2.encode(s2));
    assert(h2.decode(h2.encode(s2)).equals(s2));
    
    System.out.println("TestHuffman OK");
  }
}