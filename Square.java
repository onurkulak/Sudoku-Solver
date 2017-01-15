/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author onur
 */
public class Square {
    private int[] probs;
    private int current;
    
    public Square(){
        current=0;
        probs = new int[]{1,2,3,4,5,6,7,8,9};
    }
    
    public boolean isEmpty(){
        return current==0;
    }
    
    public boolean onePossibility(){
        if(probs.length==1)
            return true;
        return false;
    }
    
    public int getPossible(){
        return probs[0];
    }
    
    public boolean isPossible(int x){
        for(int i=0; i<probs.length;i++)
            if(x==probs[i])
                return true;
        return false;
    }
    
    public void giveValue(int x){
        probs= new int[0];
        current=x;
    }
    
    public int getValue(){
        return current;
    }
    
    public void removePossibility(int x){
        if(isEmpty()&&isPossible(x)){
            int[] temp= new int[probs.length-1];
            for(int i=0; probs[i]!=x; i++)
                temp[i]=probs[i];
            for(int i=probs.length-1; probs[i]!=x; i--)
                temp[i-1]=probs[i];
            probs=temp;
        }        
    }
}
