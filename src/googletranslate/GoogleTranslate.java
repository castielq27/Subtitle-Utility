/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googletranslate;

import httprequest.httprequest;
import java.util.regex.Matcher;

/**
 *
 * @author castiel
 */
public class GoogleTranslate {

    /**
     * Ngon ngu nguon en = english
     */
    private String source_lang = null ;
    /**
     * Ngon ngu dich vi = vietnam
     */
    private String targe_lang = null ;
    /**
     * keyWord @ la noi chen source_lang, targe_lang va content(noi dung)
     */
    private String template_link = "http://translate.googleapis.com/translate_a/single?client=gtx&sl=@SOURCE_LANG&tl=@TARGE_LANG&dt=t&q=@CONTENT&ie=UTF-8&oe=UTF-8%22";
    private String link = null;
    
    private String content = null;
    private String contentTranslated = "";
    
    private String responseData = null;
    /**
     * ton tai escapeCode trong String ko 
     * escapeCode la \n \t \b ....
     */
    private boolean haveEscapeCode = false;
    
    private String[] escapeCode = {"\n", "\t", "\b" };
    private String[] replaceEscapeCode = {"[escapcode_n]", "[escapcode_t]", "[escapcode_b]" };
    
    private httprequest con = new httprequest();
    
    
    private void setSourceLang(String lang){
        this.source_lang = lang;
    }
    private void setTargeLang(String lang){
        this.targe_lang = lang;
    }
    private void setLang(String source, String targe){
        this.setSourceLang(source);
        this.setTargeLang(targe);
    }
    /**
     * remove Tag chu font color <>
     * @param content String
     * @return String loai bo tag <> ma danh giau color, font ...
     */
    private String removeTag(String content ){
        StringBuilder t = new StringBuilder( content );
        for( int i = 0; i<t.length(); i++ ){
            if ( t.charAt(i) == '<' ){
                while(t.charAt(i) != '>' && t.length() != 0 ){
                    t.deleteCharAt(i);
                }
                t.deleteCharAt(i);
            }
        }
        return t.toString();        
    }
    private void setContent(String content){
        
        content = this.removeTag(content);
        //
        this.haveEscapeCodeEnter(content);
        if ( this.haveEscapeCode )
            content = this.escapeCodeEnterConvertTo(content);
        this.content = content;
    }
    private void setInput(String source, String targe, String content){
        this.setLang(source, targe);
        this.setContent(content);
    }
    private boolean genlink(){
        if ( this.source_lang == null || this.targe_lang == null || this.content == null )
            return false;
        else{
            this.content = content.replace(' ', '+');
            this.link = this.template_link;

            this.link = this.link.replace("@SOURCE_LANG", this.source_lang);
            this.link = this.link.replace("@TARGE_LANG", this.targe_lang);
            this.link = this.link.replace("@CONTENT", this.content);
            
            this.source_lang = this.targe_lang = this.content = null;
            return true;
        }
    }
    public String Translate(String source, String targe, String content) throws Exception{
        this.setInput(source, targe, content);
        this.genlink();
        this.Translate();
        return this.genConentTranslated();
    }
    private void Translate() throws Exception{
        this.responseData = con.sendGet(this.link);
    }
    public String getResponseData(){
        return this.responseData;
    }
    public String getContentTranslated(){
        return this.contentTranslated;
    }
    private String genConentTranslated(){
        int pos = 0;
        this.contentTranslated = "";
        
        StringBuilder tmp = new StringBuilder( this.responseData );
        while( tmp.length() > 0 ){
            while( tmp.charAt(0) != '\"' ){
                tmp.deleteCharAt(0);
            }            
            tmp.deleteCharAt(0);
            this.contentTranslated = this.contentTranslated + tmp.substring(0, tmp.indexOf("\"") );
            pos = tmp.indexOf(",[\"");
            if ( pos > -1 )
                tmp.delete(0, tmp.indexOf(",[\"") + 2);
            else
                break;
        }
        if ( this.haveEscapeCode )
            return this.escapeCodeEnterConvertBack( this.contentTranslated );
        else
            return this.contentTranslated;
    }
    /**
     * Search escapeCode dat biet trong string \n --> neu ton tai se ko sendGet dc ta phai convert thanh tag khac
     * @param s --> content
     * @return true false co hoac ko co \n
     */
    private boolean haveEscapeCodeEnter(String s){
        if ( s.indexOf("\n") >= 0 ){
            this.haveEscapeCode = true;
            return true;
        }
        else{
            this.haveEscapeCode = false;
            return false;
        }
    }
    /**
     * Convert escapeCodeEnter neu ton tai "\n" change to "#escapcode_n"
     * @param s
     * @return string after convert
     */
    private String escapeCodeEnterConvertTo(String s){
        /*StringBuilder tmp = new StringBuilder( s );
        for ( int i = 0; i<tmp.length(); i++ ){
            if ( tmp.charAt(i) == '\n' ){
                tmp.delete(i, i+1);
                tmp.insert(i, "|n|");
            }
        }*/
        return s.replaceAll("\n", "[escapcode_n]");
        //return tmp.toString();
    }
    /**
     * 
     * Convert nguoc lai neu co "#escapcode_n" --> chuyen thanh "\n"
     * * @param s content
     * @return STring after convert
     */
    private String escapeCodeEnterConvertBack(String s){
        StringBuilder tmp = new StringBuilder( s );
        int pos;
        while ( tmp.indexOf("[escapcode_n]") >= 0 ){
            pos = tmp.indexOf("[escapcode_n]");
            if ( pos+13 < tmp.length() && tmp.charAt(pos+13) == ' ' )
                tmp.replace(pos, pos+14, "\n");
            else
                tmp.replace(pos, pos+13, "\n");
        }
        return tmp.toString();

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        GoogleTranslate g = new GoogleTranslate();
        //g.Translate("en", "vi", "Good night!");
        
        //System.out.println( g.escapeCodeConvertTo("Hello \n I am John") );
        //System.out.println( g.escapeCodeConvertBack("Hello | n | I am John") );
        System.out.println( g.Translate("en", "vi", "Hello\nI am John") );
        System.out.println( g.getResponseData() );
    }
    
}
