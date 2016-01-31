/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitletranslate;

import Srt.*;
import googletranslate.GoogleTranslate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author castiel
 */
public class SubtitleTranslate {

    private String file = null;
    private String source_lang = null;
    private String targe_lang = null;
    private String fileOut = null;
    
    private Srt sub;
    private threadWork thread = new threadWork();
    
    private void setFile(String file){
        this.file = file;
    }
    private void setFileOut(String f ){
        this.fileOut = f;
    }
    private void setSourceLang(String l){
        this.source_lang = l;
    }
    private void setTargeLang(String l){
        this.targe_lang = l;
    }
    
    private void getInput() throws Exception{
        try {
            sub = new Srt(this.file);
        }
        catch(Exception e){
            if ( e.getMessage().compareTo(Srt.errorEncodeUTF8_UTF16) == 0 ){
                System.out.println("Try with other encode : UTF16 " + this.file );
                sub = new Srt(this.file,"UTF16");
            }
            else
                throw e;
        }
    }
    private void Translate() throws Exception{
        if ( this.source_lang == null || this.targe_lang == null || this.sub == null || this.fileOut == null ){
            throw new Exception("SubtitleTranslate.Translate(): Input null\n" + this.toString() );
        }
        
        /*
        * Translate tuan tu
        */
        /*sub.reSetPosition();
        
        int max = sub.getPointSize()-1;
        int position = 0;
        Point p = sub.nextPoint();
        while ( p != null ){
            p.setCaption( translate.Translate( this.source_lang, this.targe_lang, p.getCaption() ) );
            p = sub.nextPoint();
            position++;
            this.percent = (int)(((float)position/max)*100.00);
            //System.out.println( this.percent );
        }*/
        
        /*
         * Thread Mutil translate 
         */
        thread.setParameters( sub.getlPoint(), this.source_lang, this.targe_lang );
        thread.doIt();
    }
    private void save() throws Exception{
        if ( this.fileOut == null )
            throw new Exception("SubtitleTranslate.save(): fileOut null ");
        sub.outFile(this.fileOut);
    }
    public void Translate(String source_lang, String targe_lang, String fileinput, String fileOut) throws Exception{
        this.setSourceLang(source_lang);
        this.setTargeLang(targe_lang);
        this.setFile(fileinput);
        this.setFileOut(fileOut);
        this.getInput();
        this.Translate();
        this.save();
    }
    public int getPercent(){
        return thread.getPercent();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        SubtitleTranslate t = new SubtitleTranslate();
        t.Translate("en", "vi", "subTest.srt", "translate.srt");
    }
    
}


class threadWork{
    private String source_lang = null;
    private String targe_lang = null;
    private ArrayList lpoint = null;
    
    private int runningCount = 0;
    private int MaxThread = 50;
    
    private int done = 1;
    private int size = 0;
    private int percent = 0;
    
    private class translateThread extends Thread implements Runnable {
        private Point p = null;
        private GoogleTranslate translate = new GoogleTranslate();
        public translateThread(Point o){
            super();
            this.p = o;
        }
        @Override
        public void run(){
            try {
                threadWork.this.runningCount++;
                p.setCaption( this.translate.Translate( threadWork.this.source_lang , threadWork.this.targe_lang, p.getCaption() ) );
            } catch (Exception ex) {
                Logger.getLogger(threadWork.class.getName()).log(Level.SEVERE, null, ex);
            }
            threadWork.this.runningCount--;
            threadWork.this.done++;
        }
    }
    
    
    public threadWork(){
        
    }
    
    public void setParameters(ArrayList<Point> o, String source_lang, String targe_lang ){
        this.lpoint = o;
        this.source_lang = source_lang;
        this.targe_lang = targe_lang;
        this.MaxThread = lpoint.size()/10;
        this.size = lpoint.size();
        this.done = 1;
    }
    public void doIt() throws Exception {
        if ( this.lpoint == null || this.source_lang == null || this.targe_lang == null )
            throw new Exception("threadWork.doIt : parameters null");
        
        int i = 0;
        while ( i < this.size ){
            if ( this.runningCount < this.MaxThread ){
                Thread t = new translateThread( (Point) lpoint.get(i) );
                t.start();
                i++;
            }
            else
                Thread.sleep(1000);
        }
    }
    public int getPercent(){
        if ( this.size == 0 )
            return 0;
        this.percent = (int)(((float)this.done)/(this.size)*100);
        return this.percent;
    }
}