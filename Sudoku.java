/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author onur
 */
public class Sudoku {

    private final Square[][] table;
    private final ArrayList<Order> todo;
    private int remaining;
    
    public Sudoku(){
        todo= new ArrayList<Order>();
        table = new Square[9][9];
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                table[i][j]=new Square();
        remaining=81;
    }
    
    public void create(){
        System.out.println("line by line, write the sudoku with spaces between squares, use 0 for empty areas");
        System.out.println("Example line: \" 0 1 2 3 4 5 6 7 8 \"");
        Scanner scan = new Scanner(System.in);
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                int value = scan.nextInt();
                if(value!=0)
                    todo.add(new Order(i,j,value));
            }
        }
        
        System.out.println("Calculating...");
    }
    
    
    
    public void findMatches(){
        for(int xMain=0; xMain<9; xMain= xMain+3)
            for(int yMain=0; yMain<9; yMain= yMain+3){
                String[] map = new String[9];
                for(int i=0; i<9; i++)
                    map[i]="";
                int[] encounterCounter= new int[9];
                
                for(int value=1; value<10; value++){
                    for(int i=0; i<3; i++)
                        for(int j=0; j<3; j++)
                            if(table[xMain+i][yMain+j].isPossible(value)){
                                encounterCounter[value-1]++;
                                map[value-1] = map[value-1].concat(""+i+j);
                            }
                }
                
                int probMatches=0;
                for(int i=0; i<9; i++)
                    if(encounterCounter[i]==2)
                        probMatches++;
                if(probMatches>1){
                    
                    System.out.println("finding matches");
                    
                    int[] values = new int[probMatches];
                    String[] places = new String[probMatches];
                    
                    int cc=0;
                    for(int i = 0; i<9; i++){
                        if(encounterCounter[i]==2){
                            values[cc]=i+1;
                            places[cc]=map[i];
                            cc++;
                        }
                    }
                    
                    for(int i=0; i<probMatches-1; i++)
                        for(int j=i+1; j<probMatches; j++)
                            if(places[i].equals(places[j])){
                                for(int k=0; k<3; k++)
                                    for(int l=0; l<3; l++)
                                        if(table[xMain+k][yMain+l].isPossible(values[i]))
                                            for(int o=1; o<10; o++)
                                                if(o!=values[i]&&o!=values[j]){
                                                    table[xMain+k][yMain+l].removePossibility(o);
                                                }
                                System.out.println("found match in" + xMain+" " +yMain+" "+ places[i]+" of "+ values[i] +" and "+ values[j]);
                                i++;
                            }
                    }      
            }
        }
            
    
    public int isTheOnlyOneInABigOne(int x, int y, int possibility){
        int xMain = (x/3)*3;
        int yMain = (y/3)*3;
        
        int encounters=0;
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                if(table[xMain+i][yMain+j].isPossible(possibility))
                    encounters++;
        return encounters;
    } 
    
    public void scan(){
        for(int i=0; i<9; i++)
            for(int j=0; j<9; j++)
                if(table[i][j].isEmpty()){
                    
                    for(int k=1; k<10; k++)
                        if(table[i][j].isPossible(k)){
                            boolean horizontal=true, vertical=true;
                            for(int l=1; l<9; l++)
                                if(table[(l+i)%9][j].isPossible(k)){
                                    horizontal=false;
                                    break;
                                }
                            
                            for(int l=1; l<9; l++)
                                if(table[i][(l+j)%9].isPossible(k)){
                                    vertical=false;
                                    break;
                                }
                            //System.out.println(horizontal + "  "+ vertical +"  " + isTheOnlyOneInABigOne(i,j,k));
                            if(isTheOnlyOneInABigOne(i,j,k)==1||vertical||horizontal)
                                todo.add(new Order(i,j,k));
                        }
            }
    }
    
    public void check(int x, int y){
        if(table[x][y].onePossibility())
            todo.add(new Order(x,y,table[x][y].getPossible()));
    }
    
    public void writeValue(Order ord){
        if(table[ord.getX()][ord.getY()].isEmpty()){
            table[ord.getX()][ord.getY()].giveValue(ord.getValue());
            
            for(int i=0; i<9; i++){
                table[ord.getX()][i].removePossibility(ord.getValue());
                check(ord.getX(),i);
            }
            
            for(int i=0; i<9; i++){
                table[i][ord.getY()].removePossibility(ord.getValue());
                check(i,ord.getY());
            }
            
            int nineX=(ord.getX()/3)*3;
            int nineY=(ord.getY()/3)*3;
            
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++){
                    table[nineX+i][nineY+j].removePossibility(ord.getValue());
                }
            
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++){
                    check(nineX+i,nineY+j);
                }
            
            
            remaining--;
        }
    }
    
    public void print(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++)
                System.out.print(" "+table[i][j].getValue());
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
    
    public static void main(String[] args) {
        Sudoku test = new Sudoku();
        test.create();
        while(test.remaining>0&&!test.todo.isEmpty()){
            while(!test.todo.isEmpty()){
                test.writeValue(test.todo.get(0));
                test.todo.remove(0);
                test.print();
            }
            //test.print();
            test.findMatches();
            test.scan();
        }
        
        test.print();
    }
    
}
