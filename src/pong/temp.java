package pong;

public class temp {
	public static void main(String[] args) {
		int p = 2;
	      int v = "QDL".hashCode() % 3000;
	      int i = "XDI".hashCode() % 3000;
	      for (int a = 0; a <= i; a++)
	         p = (p ^ a) % v;
	      System.out.println(p);
	}
}
