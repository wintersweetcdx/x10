/* Will work only with 2^n elements */

import clocked.*;
import x10.io.Console;

public class MergeSort{



    public def this () {
    } 	

	static val op = Math.noOp.(Int, Int);

    static val N: int = 128;
    public static def main(args:Rail[String]!){

         Console.OUT.println("Merge Sort ");
         val c: Clock = Clock.make();
         val h = new MergeSort();  // final variable 
         val myArray = Rail.make[int @ Clocked[Int](c,op,0)](N, (i:int)=> N-i);
         
      
     	 var i: int = 0;
     	
     	 for (i = 0; i < N; i++)
     		Console.OUT.print(myArray(i) + " ");
     	 h.sort(c, myArray,  0, N-1); 
     	 next;
     	 Console.OUT.println("\nSorted Rail");
     	 for ( i = 0; i < N; i++)
     		Console.OUT.print(myArray(i) + " ");
         
      
    }
    
 
    
    /** x10doc comment for myMethod */;
    public def sort (c: Clock, myArray: Rail[int @ Clocked[int] (c, op, 0)]!, start: int, end: int) 
    @ ClockedM (c) {
    	
    	val fstart: int = start;
    	val fend: int = start + (end - start)/2;
    	val sstart: int = fend + 1;
    	val ssend: int = end;

        if (start == end)
     			return;
        	
         /* Sort into myArray */
        finish {
        async clocked (c) sort (c, myArray, fstart, fend);
        /* Sort into myArray */
		sort (c, myArray, sstart, ssend);
        next; /* Like a finish */
        
      	}
        	/* merge from myArray into myArray */
        merge(c, myArray, fstart, fend, sstart, ssend);
  
    }
    
    public def merge (c: Clock, a: Rail[int @ Clocked[int] (c, op, 0)]!,  fstart: int, fend: int,
    		sstart: int, ssend: int) @ ClockedM (c) {

    	
    var x: int = fstart;
    var y: int = sstart;
    var z: int = fstart;
    val size = (ssend - fstart) + 1;
  
    
    while(x <= fend && y <= ssend)
    {
           if(a(x) < a(y))
            {
                   a(z++) = a(x++);
                                   
                    
            }
            else
            {
            
            	   a(z++) = a(y++);
            }
    }
  //copy remaining elements to the tail of b[];
    while(x <= fend)
    {
            a(z++) = a(x++);
    }
    while(y <= ssend)
    {
            a(z++) = a(y++);
    }
	
      	
    }
    
}