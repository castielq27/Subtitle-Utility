/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Srt;

/**
 *
 * @author castiel
 */
public class Point {
    
    private int ID;
    private Timer time;
    private String caption;
    
    public Point(String o) throws Exception {
        StringBuilder tmp = new StringBuilder( o );
        
        try{
            ID = Integer.parseInt( tmp.substring(0, tmp.indexOf("\n")) );
            tmp.delete(0, tmp.indexOf("\n") + 1);            
        }
        catch(Exception e){
            System.out.println( "Point : ID input wrong !");
            ID = 0;
        }

        try{
            time = new Timer( tmp.substring(0, tmp.indexOf("\n")));
            tmp.delete(0, tmp.indexOf("\n") + 1);            
        }
        catch(Exception e ){
            System.out.println( "Point : time input wrong !");
            time = new Timer();// Timer Zero 00:00:00,000
        }

        try{
            caption = new String( tmp.substring(0,tmp.indexOf("\n\n")));
        }
        catch(Exception e){
            System.out.println( "Point : caption input wrong !");
            caption = "MERGESUBTITLE.POINT : CAPTION INPUT WRONG !";
        }
        
    }
    public String toString(){
        return this.ID + "\n" + this.time.toString() + "\n" + this.caption;
    }
    
    public String getCaption(){
        return this.caption;
    }
    public void setCaption(String s){
        this.caption = s;
    }
    public int compareEnd(Point p){
        return this.time.compareEnd( p.time );
    }
    public int compareStart(Point p){
        return this.time.compareStart( p.time );
    }
    public void addTag(String tag){
        this.caption = tag + this.caption;
    }
    public Point setID(int id){
        this.ID = id;
        return this;
    }
    public static void main(String[] args){
        try{
            Point p = new Point("2\n00:00:02,276 --> 00:00:04,224\nI was told\nonce that a man\n\n");
            System.out.println( p.toString() );
        }
        catch( Exception e){
            System.out.println( e.toString() );
        }
        
        
    }
    
}
