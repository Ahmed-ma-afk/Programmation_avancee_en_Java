package array;

import java.util.NoSuchElementException;

// une pile réalisée à l'aide d'un tableau redimensionnable

public class Stack implements intf.Stack {

  private ResizableArray elts;

  public Stack() {
    this.elts = new ResizableArray(0);
  }

  public boolean isEmpty() {
    return this.elts.size() == 0;
  }

  public int size() {
    return this.elts.size();
  }
  
  public void clear() {
    this.elts.setSize(0);
  }

  public void push(int x) {
    int n = this.elts.size();
    this.elts.setSize(n + 1);
    this.elts.set(n, x);
  }

  public int pop() {
    int n = this.elts.size();
    if (n == 0)
      throw new NoSuchElementException();
    int e = this.elts.get(n - 1);
    this.elts.setSize(n - 1);
    return e;
  }

  public int top() {
    int n = this.elts.size();
    if (n == 0)
      throw new NoSuchElementException();
    return this.elts.get(n - 1);
  }

  public void swap() {
    int n = this.elts.size();
    if (n <= 1) throw new IllegalArgumentException();
    int tmp = this.elts.get(n - 1);
    this.elts.set(n - 1, this.elts.get(n - 2));
    this.elts.set(n - 2, tmp);
  }
  
  // uniquement pour débugger
  @Override
  public String toString() {
    return this.elts.toString();
  }
  
}


class TestStack {
  
  static void swap(Stack s) {
    if (s.size() <= 1) throw new IllegalArgumentException();
    int x = s.pop();
    int y = s.pop();
    s.push(x);
    s.push(y);
  }
  
  public static void main(String[] args) {
    Stack s = new Stack();
    assert (s.isEmpty());
    s.push(1); 
    assert (!s.isEmpty());
    s.push(2);
    assert (s.top() == 2);
    s.push(3);
    assert (s.top() == 3);
    assert (s.pop() == 3);
    assert (s.top() == 2);
    assert (s.pop() == 2);
    assert (s.top() == 1);
    assert (s.pop() == 1);
    assert (s.isEmpty());
    
    s.push(1);
    try { s.swap(); assert false; } catch (IllegalArgumentException e) { };
    s.push(2);
    s.swap();
    assert (s.top() == 1);
    s.pop();
    assert (s.top() == 2);
    
    s.clear();
    assert s.size() == 0;
    
    s.push(1);
    try { swap(s); assert false; } catch (IllegalArgumentException e) { };
    s.push(2);
    swap(s);
    assert (s.top() == 1);
    s.pop();
    assert (s.top() == 2);
    
    System.out.println("TestStack OK");
  }
}
