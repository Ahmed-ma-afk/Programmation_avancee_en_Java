package trees;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

// arbres de préfixes

public class Trie implements intf.Set<String> {

  private boolean word;
  private final Map<Character, Trie> branches;

  public Trie() {
    this.word = false;
    this.branches = new HashMap<Character, Trie>();
  }

  public boolean contains(String s) {
    Trie t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
      t = t.branches.get(s.charAt(i));
      if (t == null) return false;
    }
    return t.word;
  }

  public Trie branch(char c) {
    return this.branches.get(c);
  }
  
  public void add(String s) {
    Trie t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
      char c = s.charAt(i);
      Map<Character, Trie> b = t.branches;
      t = b.get(c);
      if (t == null) {
        t = new Trie();
        b.put(c, t);
      }
    }
    t.word = true;
  }

  public boolean isEmpty() {
    return !this.word && this.branches.isEmpty();
  }
  
  // cette méthode remove conduit à des sous-arbres entièrement vides
  public void removeSimple(String s) {
    Trie t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
      t = t.branches.get(s.charAt(i));
      if (t == null) return;
    }
    t.word = false;
  }

  // cette méthode remove assure que l'arbre ne contient jamais de sous-arbre vide
  private void remove(String s, int i) {
    if (i == s.length()) { this.word = false; return; }
    char c = s.charAt(i);
    Trie b = this.branches.get(c);
    if (b == null) return;
    b.remove(s, i+1);
    if (b.isEmpty()) this.branches.remove(c);
  }
  
  public void remove(String s) {
    remove(s, 0);
  }
  
  public int size() {
    int s = 0;
    if (this.word) s++;
    for (Trie b: this.branches.values())
      s += b.size();
    return s;
  }
  
  public Trie get(String s) {
    Trie t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
      t = t.branches.get(s.charAt(i));
      if (t == null) return null;
    }
    return t;
  }
  
  // tous les mots dont s est un préfixe
  public Collection<String> below(String s) {
    Collection<String> c = new LinkedList<String>();
    Trie t = this.get(s);
    if (t == null) return c;
    t.collect(c, s);
    return c;
  }
  
  // tous les mots, en leur ajoutant le préfixe prefix
  private void collect(Collection<String> c, String prefix) {
    if (this.word) c.add(prefix);
    for (Entry<Character, Trie> b: this.branches.entrySet())
      b.getValue().collect(c, prefix + b.getKey());
  }
  
  // tous les mots
  public Collection<String> elements() {
    return this.below("");
  }

  @Override
  public void clear() {
    this.word = false;
    this.branches.clear();
  }
  
}

class TestTrie {

  public static void main(String[] args) {
    Trie t = new Trie();
    assert (!t.contains(""));
    t.add("");
    assert (t.contains(""));
    assert (!t.contains("a"));
    t.add("a");
    assert (t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("b"));
    t.add("b");
    assert (t.contains("a"));
    assert (!t.contains("ab"));
    assert (t.contains("b"));
    t.add("abc");
    assert (t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("ab"));
    assert (!t.contains("acb"));
    assert (!t.contains("ba"));
    assert (t.contains("b"));
    assert (t.contains("abc"));
    
    t.remove("a");
    assert (!t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("ab"));
    assert (!t.contains("acb"));
    assert (!t.contains("ba"));
    assert (t.contains("b"));
    assert (t.contains("abc"));
    
    t.remove("b");
    assert (t.contains("abc"));
    t.remove("abc");
    assert (!t.isEmpty());
    t.remove("");
    assert (t.isEmpty());
   
    System.out.println("TestTrie OK");
  }

}

// version optimisée où les feuilles ont un champ branches qui vaut null
class Trie2 implements intf.Set<String> {

  private boolean word;
  private Map<Character, Trie2> branches;

  public Trie2() {
    this.word = false;
    this.branches = null;
  }

  public boolean contains(String s) {
    Trie2 t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
    	if (t.branches == null) return false;
      t = t.branches.get(s.charAt(i));
      if (t == null) return false;
    }
    return t.word;
  }

  public void add(String s) {
    Trie2 t = this;
    for (int i = 0; i < s.length(); i++) { // invariant t != null
      char c = s.charAt(i);
      Map<Character, Trie2> b = t.branches;
      if (b == null) b = t.branches = new HashMap<Character, Trie2>();
      t = b.get(c);
      if (t == null) {
        t = new Trie2();
        b.put(c, t);
      }
    }
    t.word = true;
  }

  public boolean isEmpty() {
    return !this.word && this.branches == null;
  }
  
  // cette méthode remove assure que l'arbre ne contient jamais de sous-arbre vide
  private void removeRec(String s, int i) {
    if (i == s.length()) { this.word = false; return; }
    char c = s.charAt(i);
    if (this.branches == null) return;
    Trie2 b = this.branches.get(c);
    if (b == null) return;
    b.removeRec(s, i+1);
    if (b.isEmpty()) this.branches.remove(c);
    if (this.branches.isEmpty()) this.branches = null;
  }
  
  public void remove(String s) {
    removeRec(s, 0);
  }
  
  public int size() {
    int s = 0;
    if (this.word) s++;
    for (Trie2 b: this.branches.values())
      s += b.size();
    return s;
  }

  @Override
  public void clear() {
    this.word = false;
    this.branches.clear();
  }
  
}

class TestTrie2 {

  public static void main(String[] args) {
    Trie2 t = new Trie2();
    assert (!t.contains(""));
    t.add("");
    assert (t.contains(""));
    assert (!t.contains("a"));
    t.add("a");
    assert (t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("b"));
    t.add("b");
    assert (t.contains("a"));
    assert (!t.contains("ab"));
    assert (t.contains("b"));
    t.add("abc");
    assert (t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("ab"));
    assert (!t.contains("acb"));
    assert (!t.contains("ba"));
    assert (t.contains("b"));
    assert (t.contains("abc"));
    

    
    t.remove("a");
    assert (!t.contains("a"));
    assert (!t.contains("aa"));
    assert (!t.contains("ab"));
    assert (!t.contains("acb"));
    assert (!t.contains("ba"));
    assert (t.contains("b"));
    assert (t.contains("abc"));
    
    t.remove("b");
    assert (t.contains("abc"));
    t.remove("abc");
    assert (!t.isEmpty());
    t.remove("");
    assert (t.isEmpty());
   
    System.out.println("TestTrie2 OK");
   
  }

}

//version avec des méthodes statiques
class Trie1 {

	private boolean word;
	private final HashMap<Character, Trie1> branches;

	Trie1() {
		this.word = false;
		this.branches = new HashMap<Character, Trie1>();
	}

	static boolean contains(Trie1 t, String s) {
		assert (t != null);
		for (int i = 0; i < s.length(); i++) { // invariant t != null
			t = t.branches.get(s.charAt(i));
			if (t == null) return false;
		}
		return t.word;
	}

	static void add(Trie1 t, String s) {
		assert (t != null);
		for (int i = 0; i < s.length(); i++) { // invariant t != null
			char c = s.charAt(i);
			HashMap<Character, Trie1> b = t.branches;
			t = b.get(c);
			if (t == null) {
				t = new Trie1();
				b.put(c, t);
			}
		}
		t.word = true;
	}

	static boolean isEmpty(Trie1 t) {
		return !t.word && t.branches.isEmpty();
	}

	static private void removeRec(Trie1 t, String s, int i) {
		assert (t != null);
		if (i == s.length()) { t.word = false; return; }
		char c = s.charAt(i);
		Trie1 b = t.branches.get(c);
		if (b == null) return;
		removeRec(b, s, i+1);
		if (isEmpty(b)) t.branches.remove(c);
	}

	static void remove(Trie1 t, String s) {
		assert (t != null);
		removeRec(t, s, 0);
	}

}

