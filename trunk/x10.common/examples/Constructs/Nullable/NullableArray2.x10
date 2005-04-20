
/*
 * @author kemal 4/2005
 *
 * Testing arrays whose elements are possibly nullable 
 *
 */

class mycomplex {
int re;
int im;
}

public class NullableArray2 {

	public boolean run() {
		
		(nullable int)[] ia1 = new (nullable int)[2];
		if (ia1[1]!=null) return false;

		nullable (nullable int)[] ia2 = null;
		if (ia2!=null) return false;

		nullable (nullable int)[.] ia3 = null;
		if (ia3!=null) return false;

		ia3 = new (nullable int)[(0:2)->here];
		if (ia3[2]!=null) return false;

		ia3 = new int[(0:2)->here];
		if (ia3[2]!=0) return false;

		(nullable mycomplex)[.] ia4 =
		 new (nullable mycomplex)[(0:2)->here]
                 (point [i]) {return new mycomplex();};
		if (ia4[2].im!=0) return false;

		nullable (nullable mycomplex)[.] ia5 = 
		  new (nullable mycomplex)[(0:2)->here]
                  (point [i]) {return new mycomplex();};
		if (ia5[2].im!=0) return false;

		nullable (nullable mycomplex)[.] ia7 = null;
		if (ia7!=null) return false;

		nullable (nullable mycomplex)[.] ia8 = 
			new mycomplex[(0:2)->here]
                 	(point [i]) {return new mycomplex();};
		if (ia8[2].im!=0) return false;

		nullable (nullable mycomplex)[.] ia9 = 
			new (nullable mycomplex)[(0:2)->here];
		if (ia9[2]!=null) return false;

		return true;
	}

	public static void main(String args[]) {
		boolean b= (new NullableArray2()).run();
		System.out.println("++++++ "+(b?"Test succeeded.":"Test failed."));
		System.exit(b?0:1);
	}

}
