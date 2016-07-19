/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitle_srt;

import java.util.logging.Logger;


/**
 *
 * @author castiel
 */
public class Point {
    
    private int ID;
    private Timer time;
    private String caption;
    
    /**
     * 
     * @param o một mẩu hội thoại trong subtitle theo format :
     * [00:00:02,276 --> 00:00:04,224
     * I was told\nonce that a man
     * 
     * ]
     * 
     */
    public Point(String o) {
        StringBuilder tmp = new StringBuilder( o );
        
        try{
            ID = Integer.parseInt( tmp.substring(0, tmp.indexOf("\n")) );
            tmp.delete(0, tmp.indexOf("\n") + 1);            
        }
        catch(Exception e){
            Logger.getLogger(this.getClass().getName()).info("Point : ID input wrong! Set ID = default ");
            ID = 0; // error --> set default : ID = 0
        }

        try{
            time = new Timer( tmp.substring(0, tmp.indexOf("\n")));
            tmp.delete(0, tmp.indexOf("\n") + 1);            
        }
        catch(Exception e ){
            Logger.getLogger(this.getClass().getName()).info("Point : time input wrong! Set Time = default ");
            time = new Timer();// error --> set default : Timer Zero 00:00:00,000
        }

        try{
            caption = new String( tmp.substring(0,tmp.indexOf("\n\n")));
        }
        catch(Exception e){
            Logger.getLogger(this.getClass().getName()).info("Point : caption input wrong! Set Caption = \"MERGESUBTITLE.POINT : CAPTION INPUT WRONG !\" ");
            caption = "MERGESUBTITLE.POINT : CAPTION INPUT WRONG !"; // error --> set default caption 
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
        Point p = new Point("2\n00:00:02,276 --> 00:00:04,224\nI was told\nonce that a man\n\n");
        System.out.println( p.toString() );
        
    }
    
}
