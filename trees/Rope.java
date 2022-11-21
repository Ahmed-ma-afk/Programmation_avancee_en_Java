package trees;

// cordes

public abstract class Rope {

  protected final int length;
  
  protected Rope(int length) {
    this.length = length;
  }
  
  public int length() {
    return this.length;
  }
  
  public static Rope ofString(String s) {
    return new Str(s);
  }
  
  @SuppressWarnings("unused")
  private static final int SMALL_LENGTH = 256;
  
  public Rope append(Rope r) {
    if (this.length == 0) return r;
    if (r.length == 0) return this;
    Rope r1 = new App(this, r);
    // if (r1.length < SMALL_LENGTH) return new Str(r1.toString()); // OPTIM
    return r1;
  }
 
  protected void checkIndex(int i) {
    if (i < 0 || i >= this.length) throw new IllegalArgumentException();
  }
  
  protected void checkRange(int begin, int end) {
    if (begin < 0 || end < begin || end > this.length) throw new IllegalArgumentException();
  }
  
  public abstract char get(int i);
  public abstract Rope sub(int begin, int end);

  static final Rope empty = new Str("");
  
  public String toString() {
    StringBuffer b = new StringBuffer();
    toStringHelper(b);
    return b.toString();
  }
  protected abstract void toStringHelper(StringBuffer b);
  
  static final int maxFib = 44;
  static final int[] fib = new int[maxFib + 1];
  static { fib[1] = 1; for(int i = 2; i <= maxFib; i++) fib[i] = fib[i-2] + fib[i-1]; }
  
  public Rope balance() {
    Rope[] queue = new Rope[maxFib];
    for (int i = 0; i < maxFib; i++) queue[i] = empty;
    this.insertLeaves(queue);
    Rope r = empty;
    for (int i = 2; i < maxFib; i++) r = queue[i].append(r);
    return r;
  }
  
  protected void insertQueue(Rope[] queue, Rope r, int i) {
    assert (i < maxFib);
    Rope c = queue[i].append(r);
    if (c.length < fib[i+1]) queue[i] = c;
    else { queue[i] = empty; insertQueue(queue, c, i+1); }
  }

  protected abstract void insertLeaves(Rope[] queue);
  
  public int cost() {
    return cost(0);
  }
  
  protected abstract int cost(int depth);
  
}

class Str extends Rope {
  
  private final String str;
  
  Str(String str) {
    super(str.length());
    this.str = str;
  }
 
  @Override
  public char get(int i) {
    checkIndex(i);
    return this.str.charAt(i);
  }

  @Override
  public Rope sub(int begin, int end) {
    if (begin == 0 && end == this.length) // OPTIM
    	return this;
    checkRange(begin, end);
    return new Str(this.str.substring(begin, end));
  }

  @Override
  protected void insertLeaves(Rope[] queue) {
    insertQueue(queue, this, 2);
  }
  
  @Override
  protected int cost(int depth) {
    return depth * length;
  }

  @Override
  protected void toStringHelper(StringBuffer b) {
    b.append(this.str);
  }
  
  @Override
  public boolean equals(Object that) {
  	Rope r = (Rope)that;
  	return r.toString().equals(this.str);
  }

}

class App extends Rope {

  private final Rope left, right;
  
  App(Rope left, Rope right) {
    super(left.length + right.length);
    this.left = left;
    this.right = right;
  }

  @Override
  public char get(int i) {
    checkIndex(i);
    return i < this.left.length ? this.left.get(i) : this.right.get(i - this.left.length);
  }

  @Override
  public Rope sub(int begin, int end) {
    if (begin == 0 && end == this.length) // OPTIM
    	return this;
    checkRange(begin, end);
    int endr = end - this.left.length;
    if (endr <= 0) // all in left
      return this.left.sub(begin, end);
    int beginr = begin - this.left.length;
    if (beginr >= 0) // all in right
      return this.right.sub(beginr, endr);
    return new App(this.left.sub(begin, this.left.length),
        this.right.sub(0, endr));
  }
  
  @Override
  protected void insertLeaves(Rope[] queue) {
    this.left.insertLeaves(queue);
    this.right.insertLeaves(queue);
  }

  @Override
  protected int cost(int depth) {
    return this.left.cost(depth + 1) + this.right.cost(depth + 1);
  }

  @Override
  protected void toStringHelper(StringBuffer b) {
    this.left.toStringHelper(b);
    this.right.toStringHelper(b);
  }
  
  @Override
  public boolean equals(Object that) {
  	Rope r = (Rope)that;
  	return this.length == r.length && this.left.equals(r.sub(0, this.left.length))
  			&& this.right.equals(r.sub(this.left.length, this.length));
  }
  
}

class TestRope {
  public static void main(String[] args) {
    Rope r = new App(new Str("Ec"),
                     new App(new App(new Str("oleP"), new Str("o")),
                             new App(new App(new Str("lytech"), new Str("ni")), new Str("que"))));
    System.out.println(r);
    assert r.sub(0, 3).toString().equals("Eco");
    assert r.sub(4, 8).toString().equals("ePol");
    assert r.sub(7, 12).toString().equals("lytec");
    assert r.sub(6, 16).toString().equals("olytechniq");
    assert r.sub(0, r.length).toString().equals("EcolePolytechnique");
    System.out.println(r.cost());
    assert r.cost() == 58;
    r = r.balance();
    System.out.println(r);
    System.out.println(r.cost());
    assert r.cost() < 58;
    
    r = Rope.empty;
    for (char c = 'a'; c <= 'z'; c++)
      r = r.append(new Str("" + c));
    System.out.println(r);
    assert r.length() == 26;
    r = r.balance();
    System.out.println(r);
    assert r.length() == 26;
    
    System.out.println("TestRope OK");
  }
}