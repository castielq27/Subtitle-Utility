/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google_translate;

import httprequest.httprequest;
import java.util.Random;
import java.util.logging.Logger;
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
    public final static String[] lang_code = { "af", "sq", "ar", "az", "eu", "bn", "be", "bg", "ca", "zh-CN", "zh-TW", "hr", "cs", "da", "nl", "en", "eo", "et", "tl", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "iw", "hi", "hu", "is", "id", "ga", "it", "ja", "kn", "ko", "la", "lv", "lt", "mk", "ms", "mt", "no", "fa", "pl", "pt", "ro", "ru", "sr", "sk", "sl", "es", "sw", "sv", "ta", "te", "th", "tr", "uk", "ur", "vi", "cy", "yi" };
    public final static String[] lang_label = { "Afrikaans", "Albanian", "Arabic", "Azerbaijani", "Basque", "Bengali", "Belarusian", "Bulgarian", "Catalan", "Chinese Simplified", "Chinese Traditional", "Croatian", "Czech", "Danish", "Dutch", "English", "Esperanto", "Estonian", "Filipino", "Finnish", "French", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian Creole", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese", "Kannada", "Korean", "Latin", "Latvian", "Lithuanian", "Macedonian", "Malay", "Maltese", "Norwegian", "Persian", "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish", "Swahili", "Swedish", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Welsh", "Yiddish" };
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
    
    private String[] escapeCode;// = {"\n", "\t", "\b" };
    private String[] replaceEscapeCode;// = {"[escapcode_n]", "[escapcode_t]", "[escapcode_b]" };
    
    private httprequest con = new httprequest();

    public GoogleTranslate() {
        Random r = new Random();
        escapeCode = new String[]{"\n", "\t", "\b", "\"" };
        this.replaceEscapeCode = new String[this.escapeCode.length];
        for ( int i = 0; i<this.replaceEscapeCode.length; i++ ){
            this.replaceEscapeCode[i] = r.nextLong()+"";
            Logger.getLogger(this.getClass().getName()).finest(this.escapeCode[i] + ":" +this.replaceEscapeCode[i]);
        }
    }
    
    
    
    
    
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
                i--;
            }
        }
        return t.toString();        
    }
    
    private void setContent(String content){
        
        content = this.removeTag(content);
        //
        this.haveEscapeCodeEnter(content);
        if ( this.haveEscapeCode )
            content = this.escapeCodeConvertTo(content);
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
            while( tmp.charAt(0) != '\"' ){//remove unused char
                tmp.deleteCharAt(0);
            }            
            tmp.deleteCharAt(0);// remove char \"
            this.contentTranslated = this.contentTranslated + tmp.substring(0, tmp.indexOf("\"") );
            pos = tmp.indexOf(",[\"");
            if ( pos > -1 )
                tmp.delete(0, tmp.indexOf(",[\"") + 2);
            else
                break;
        }
        if ( this.haveEscapeCode )
            return this.escapeCodeConvertBack( this.contentTranslated );
        else
            return this.contentTranslated;
    }
    /**
     * Search escapeCode dat biet trong string \n --> neu ton tai se ko sendGet dc ta phai convert thanh tag khac
     * @param s --> content
     * @return true false co hoac ko co \n
     */
    private boolean haveEscapeCodeEnter(String s){
        for ( int i = 0; i<this.escapeCode.length; i++ ){
            if ( s.indexOf(this.escapeCode[i]) >= 0 ){
                this.haveEscapeCode = true;
                return true;                
            }
        }
        this.haveEscapeCode = false;
        return false;
    }
    /**
     * Convert escapeCodeEnter VD : neu ton tai "escapeCode[i]" change to "replaceEscapeCode[i]"
     * @param s
     * @return string after convert
     */
    private String escapeCodeConvertTo(String s){
        for ( int i = 0; i<this.escapeCode.length; i++ ){
            s = s.replaceAll(this.escapeCode[i], " "+this.replaceEscapeCode[i] + " ");
        }
        return s;
    }
    /**
     * 
     * Convert nguoc lai neu co "replaceEscapeCode[i]" --> chuyen thanh "escapeCode[i]"
     * * @param s content
     * @return STring after convert
     */
    private String escapeCodeConvertBack(String s){
        for ( int i = 0; i<this.escapeCode.length; i++ ){
            s = s.replaceAll(this.replaceEscapeCode[i], this.escapeCode[i]);
        }
        return s;

    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        GoogleTranslate g = new GoogleTranslate();
        g.Translate("en", "vi", "Hello\nI \tam John");
//                
//        System.out.println( g.Translate("en", "vi", "Hello\nI \tam John") );
//        System.out.println( g.getResponseData() );
        
        
    }
    
}
