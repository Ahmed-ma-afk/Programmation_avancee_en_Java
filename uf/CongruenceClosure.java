package uf;

import java.util.HashSet;

// fermeture par congruence
// 
// étant donnée une liste d'égalités entre termes,
// on veut déterminer si une autre égalité en est la conséquence
//
// exemple : f(f(f(x)))=x /\ f(f(f(f(f(x))))) => f(x)=x ?
//
// (de façon équivalente, on pourrait prendre une liste d'égalités
//  et de diségalités et se poser le problème de la cohérence logique) 

// ici les termes sont des variables ou des applications de fonctions
// non interprétées ; on représente les variables comme des fonctions
// sans arguments
class Term {
  final String symb;
  final Term[] args;
  
  Term(String symb, Term[] args) {
    super();
    this.symb = symb;
    this.args = args;
  }
  Term(String symb) {
    this(symb, new Term[] {});
  }
  
  @Override
  public int hashCode() {
    int h = this.symb.hashCode();
    for (Term t: this.args)
      h = 31 * h + t.hashCode();
    return h;
  }
  
  @Override
  public boolean equals(Object obj) {
    Term that = (Term)obj;
    if (!this.symb.equals(that.symb)) return false;
    int n1 = this.args.length;
    assert n1 == that.args.length;
    for (int i = 0; i < n1; i++)
      if (!this.args[i].equals(that.args[i]))
        return false;
    return true;
  }
  
  @Override
  public String toString() {
    int n = this.args.length;
    if (n == 0) return this.symb;
    StringBuffer sb = new StringBuffer();
    sb.append(this.symb);
    sb.append("(");
    for (Term t: this.args) {
      sb.append(t);
      if (--n > 0) sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }
  
}

// principe de la fermeture par congruence :
// maintenir une structure union-find avec tous les termes (et sous-termes)
// on fait union(e1, e2) pour chaque égalité déclarée
// puis on sature la structure union-find avec toutes les égalités
// obtenues par congruence i.e. si x1=y1,...,xn=yn alors f(x1,...,xn)=f(y1,...,yn)

public class CongruenceClosure {
  // l'ensemble de tous les termes
  private HashSet<Term> terms = new HashSet<>();
  // la structure union-find
  private HashUnionFind<Term> uf = new HashUnionFind<>();
  
  // renvoie l'élément associé à t,
  // en ajoutant t et ses sous-termes si c'est la première fois
  private void add(Term t) {
    if (terms.contains(t)) return;
    terms.add(t);
    uf.add(t);
    for (Term s: t.args) // s'assurer que tous les sous-termes sont ajoutés
      add(s);
  }
  
  // déclare une nouvelle égalité
  void declareEq(Term t1, Term t2) {
    assert !t1.symb.equals(t2.symb) || t1.args.length == t2.args.length;
    add(t1);
    add(t2);
    uf.union(t1, t2);
  }
  
  // teste si deux listes de termes sont égales 
  private boolean checkEqArgs(Term[] l1, Term[] l2) {
    int n1 = l1.length;
    assert n1 == l2.length;
    for (int i = 0; i < n1; i++)
      if (!uf.sameClass(l1[i], l2[i]))
        return false;
    return true;
  }

  // la fermeture par congruence proprement dite
  private void cc() {
    boolean change = true;
    while (change) {
      change = false;
      // pour toute paire de termes t1,t2
      for (Term t1: this.terms)
        for (Term t2: this.terms)
          if (!uf.sameClass(t1, t2) && t1.symb.equals(t2.symb) && checkEqArgs(t1.args, t2.args)) {
            uf.union(t1, t2);
            change = true;
            System.out.println("congruence : " + t1 + "=" + t2);
          }
    }
  }   
  
  boolean queryEq(Term t1, Term t2) {
    add(t1);
    add(t2);
    cc();
    return uf.sameClass(t1, t2);
  }
  
  void debug() {
    System.out.println(terms.size() + " termes");
  }
  
  // tests
  
  static Term f(Term t) { return new Term("f", new Term[] { t }); }

  public static void main(String[] args) {
    CongruenceClosure cc = new CongruenceClosure();
    Term x = new Term("x");
    cc.declareEq(f(f(f(x))), x);
    cc.declareEq(f(f(f(f(f(x))))), x);
    System.out.println(cc.queryEq(f(x), x));
    // Term y = new Term("y");
    // System.out.println(cc.queryEq(x, y));
    // cc.declareEq(f(f(x)), y);
    // System.out.println(cc.queryEq(x, y));
    cc.debug();
  }
  
}
