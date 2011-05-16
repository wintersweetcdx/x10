package x10.compiler.ws.util;

import java.util.ArrayList;
import java.util.List;

import polyglot.util.Pair;
import x10.compiler.ws.codegen.AbstractWSClassGen;
import x10.types.Type;

/**
 * @author Haichuan
 * 
 * A reference container is used to store a reference from deeper frame to a upper frame
 * 
 * For example frame
 *    _fib
 *     _fibF0 ( up = _fib)
 *       _fibF0B0 (up = _fibF0)
 *         _fibF0B0A0 (k = _fibF0B0, up = _fibF0)
 *         
 *  In _fibF0B0A0, if it wants to acess a variable in _fib,
 *  it needs a reference up.up, or k.up.up.up
 *  
 *  This Reference container will store both of the two references,
 *  And choose the best reference from all candidates
 *
 */
public class ReferenceContainer {

    ReferenceStructure curRef;
    AbstractWSClassGen startFrame;
    
    List<ReferenceStructure> foundRefs;
    
    public ReferenceContainer(AbstractWSClassGen startFrame){
        foundRefs = new ArrayList<ReferenceStructure>();
        curRef = new ReferenceStructure();
        this.startFrame = startFrame;
    }
    
    
    public List<Pair<String, Type>> getBestRefStructure(){
        //the best is the one with less frame distance and less size;
        
        if(foundRefs.size() == 0){
            return null;
        }
        
        ReferenceStructure bestRef = foundRefs.get(0);
        for(int i = 0; i < foundRefs.size(); i++){
            ReferenceStructure curRef = foundRefs.get(i);
            
            if(curRef.distance < bestRef.distance){
                bestRef = curRef;
            }
            else if(curRef.distance == bestRef.distance){
                if(curRef.getRefsSize() < bestRef.getRefsSize()){
                    bestRef = curRef;
                }
            }
        }
        return bestRef.refList;        
    }
    
    public void setFound(){
        foundRefs.add(curRef.clone());
    }
    
    public void push(String str, AbstractWSClassGen frame){
        curRef.push(str, frame.getClassDef().asType());
        curRef.setDistance(startFrame.getFrameDepth() - frame.getFrameDepth());
    }
    
    public Pair<String, Type> pop(){
        return curRef.pop(); //after pop, the depth is not correct
    }
    
    
    
    
    class ReferenceStructure{
        List<Pair<String, Type>> refList;
        int distance; //distance between the two frames;
        
        public ReferenceStructure(){
            refList = new ArrayList<Pair<String, Type>>();
        }
        
        public void push(String str, Type type){
            refList.add(new Pair<String, Type>(str, type));
        }
        
        public Pair<String, Type> pop(){
            return refList.remove(refList.size() - 1);
        }

        protected int getRefsSize(){
            return refList.size();
        }
        
        protected int getDistance() {
            return distance;
        }

        protected void setDistance(int distance) {
            this.distance = distance;
        }

        @Override
        protected ReferenceStructure clone(){
            ReferenceStructure result = new ReferenceStructure();
            result.distance = this.distance;
            for(Pair<String, Type> p: refList){
                result.refList.add(p);
            }
            return result;
        }
    }
}