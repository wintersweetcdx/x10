/**
 *Tests declaration of arrays, storing in local variables, accessing and
 *updating for 2D arrays.
 *
 */
public class Array3 {

	public boolean run() {
		
		region e= [1:10];
		region r = [e,e];
		dist d=r->here;
		chk(d.equals([1:10,1:10]->here));
		int[.] ia = new int[d];
		ia[1,1] = 42;
		return 42 == ia[1,1];
	
	}

    static void chk(boolean b) {if (!b) throw new Error();}
	
	
    public static void main(String[] args) {
        final boxedBoolean b=new boxedBoolean();
        try {
                finish async b.val=(new Array3()).run();
        } catch (Throwable e) {
                e.printStackTrace();
                b.val=false;
        }
        System.out.println("++++++ "+(b.val?"Test succeeded.":"Test failed."));
        x10.lang.Runtime.setExitCode(b.val?0:1);
    }
    static class boxedBoolean {
        boolean val=false;
    }

}