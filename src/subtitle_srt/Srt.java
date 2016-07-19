/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitle_srt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author castiel
 */
public class Srt {
    /**
     *  errorEncodeUTF8_UTF16 la key Exception.toString() error co the la loi encode UTF8 va UTF16;
     *  try{ 
     *  .... new Srt(file)
     *  }
     *  catch(Exception e){
     *      if (e.toString().compareTo(Srt.errorEncodeUTF8_UTF16 ) ){
     *          ..... new Srt(file,UTF16) 
     *  }
     * 
     */
    public static final String errorEncodeUTF8_UTF16 = "Srt.Srt(file)|encode_problem_utf8_or_utf16?";
    /**
     * raw data cua subtitle dc noi lien
     */
    private StringBuilder Raw = new StringBuilder();
    

    
    private ArrayList<Point> lPoint = new ArrayList<>();
    private int position=0;
    
    public Srt(){
    }
    public Srt(String file, String encode ) throws java.lang.Exception {
        if ( encode == null ){
            Exception e = new Exception("Srt.Srt(file,encode) -> Encode == NUll");
            throw e;
        }
        Scanner f = new Scanner(new InputStreamReader(new FileInputStream(file), encode ));

        while(f.hasNextLine()){
            Raw.append( f.nextLine() + '\n' );
        }
        f.close();
        this.genData();
    }
    /**
     * 
     * @param file path VD : "/home/castiel/subtitle.srt
     * @throws java.lang.Exception khi bi loi input hoac encode sai !
     */
    public Srt(String file) throws java.lang.Exception {
        Scanner f = new Scanner(new InputStreamReader(new FileInputStream(file) ));
        

        while(f.hasNextLine()){
            Raw.append( f.nextLine() + '\n' );
        }
        f.close();
        if ( Raw.length() > 0 && (int)Raw.charAt(0) == (int)Raw.charAt(1) && (int)Raw.charAt(0) == 65533 ){
            throw new Exception(Srt.errorEncodeUTF8_UTF16);// loi encode throw
        }
        this.genData();
    }
    /**
     * Doc Du Lieu Vua Nhap
     * @throws Exception 
     */
    private void genData() throws Exception{
        char first = Raw.charAt(0);
        while ( !( first >= '0' && first <= '9' ) ){// Srt format char dau tien luon la number ID
            Raw.delete(0,1);// Delete first char
            first = Raw.charAt(0);
        }
   
        if ( Raw.lastIndexOf("\n") >= 1 && Raw.charAt( Raw.lastIndexOf("\n") -1 ) != '\n' )// dam bao ky tu cuoi cung gom 2 char \n\n
            Raw.insert( Raw.length(), "\n" );
        
        StringBuilder tmp = new StringBuilder( Raw );
        while ( tmp.length() > 0 ){
            try{
                Point p = new Point( tmp.substring(0, tmp.indexOf("\n\n") + 2 ));
                lPoint.add(p);            
                tmp.delete(0, tmp.indexOf("\n\n") + 2);
            }
            catch(Exception e){
                throw new Exception( "Catch exception after Point : \n" + lPoint.get(lPoint.size()-2) + "\nError Message : \n" +  e.getMessage() );
            }

        }
    }
    public String getRaw(){
        return Raw.toString();
    }
    public ArrayList<Point> getlPoint(){
        return lPoint;
    }
    public void addPoint(Point o) throws java.io.IOException {
        if ( o != null )
            lPoint.add(o);
        else{
            java.io.IOException e = new java.io.IOException("Srt.addPoint : o = null ");
            throw e;
        }
    }
    public Point nextPoint(){
        if ( position < lPoint.size() ){
            position++;
            return lPoint.get(position-1);
        }
        else{
            return null;
        }
    }
    public void reSetPosition(){
        position = 0;
    }
    public void setPoint(int position,Point p) throws java.io.IOException {
        
        if ( position < 0 ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position < 0 ");
            throw e;
        }
        else if ( position >= lPoint.size() ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position > lPoint.size ");
            throw e;            
        }
        else{
            lPoint.set(position,p);
        }
    }
    public Point getPoint(int position) throws java.io.IOException {
        
        if ( position < 0 ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position < 0 ");
            throw e;
        }
        else if ( position >= lPoint.size() ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position > lPoint.size ");
            throw e;            
        }
        else{
            return lPoint.get(position);
        }
    }
    public void removePoint(int position) throws java.io.IOException {
        
        if ( position < 0 ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position < 0 ");
            throw e;
        }
        else if ( position >= lPoint.size() ){
            java.io.IOException e = new java.io.IOException("Srt.getPoint : position > lPoint.size ");
            throw e;            
        }
        else{
            lPoint.remove(position);
        }
    }
    public int getPointSize(){
        return lPoint.size();
    }
    public void outFile(String f) throws java.io.IOException {
        PrintWriter w = new PrintWriter( f, "UTF-8");
        String tmp = "";
        for ( Point e : lPoint ){
            tmp = tmp + e.toString() + "\n\n";
        }
        w.write(tmp);
        w.close();
    }
    /**
     * 
     * @param tag --> Vi tri hien thi tren video, vlc tang an1, an2, ... , an9
     */
    public void setTag(String tag){
        for ( Point e : lPoint ){
            e.addTag(tag);
        }
    }

    /**
     * Ham de test ko dung lam gi het
     * 
     */
    private String testlPointOut(){
        String r = new String();
        for ( Point e : lPoint ){
            r = r + e.toString() + "\n\n";
        }
        return r;
    }
    public static void main(String[] args){
        try{
            String file = "/home/castiel/Dev/Java/Project/MergeSubtitle/Resource/Arrow.S03E23.HDTV.x264-LOL.srt";
            Srt s = new Srt( file );
            s.outFile("/home/castiel/Dev/Java/Project/MergeSubtitle/Resource/out.srt");
            //System.out.println( s.testlPointOut() );
        }
        catch( java.lang.Exception e ){
            System.out.println(e.toString());
        }
    }
}
