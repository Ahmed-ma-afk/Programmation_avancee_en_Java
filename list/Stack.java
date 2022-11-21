package list;

import java.util.NoSuchElementException;

// pile implémentée avec une liste simplement chaînée

public class Stack implements intf.Stack {

  private Singly head;
  private int size;

  public Stack() {
    this.head = null;
    this.size = 0;
  }

  public boolean isEmpty() {
    return this.head == null;
  }

  public int size() {
    return this.size;
  }

  public void push(int x) {
    this.head = new Singly(x, this.head);
    this.size++;
  }

  public int top() {
    if (this.head == null)
      throw new NoSuchElementException();
    return this.head.element;
  }

  public int pop() {
    if (this.head == null)
      throw new NoSuchElementException();
    int e = this.head.element;
    this.head = this.head.next;
    this.size--;
    return e;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer("[");
    for (Singly s = this.head; s != null; s = s.next) {
      sb.append(s.element);
      if (s.next != null) sb.append(", ");
    }
    return sb.append("]").toString();
  }

  public static void main(String[] args) {
    Stack s = new Stack();
    assert (s.isEmpty());
    s.push(1); 
    assert (!s.isEmpty());
    s.push(2);
    assert (s.top() == 2);
    s.push(3);
    System.out.println(s);
    assert (s.top() == 3);
    assert (s.pop() == 3);
    assert (s.top() == 2);
    assert (s.pop() == 2);
    assert (s.top() == 1);
    assert (s.pop() == 1);
    assert (s.isEmpty());
    System.out.println("TestStack OK");
  }
  
}
