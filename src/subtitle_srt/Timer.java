/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitle_srt;

/**
 *
 * @author castiel
 */
public class Timer {
    
    /**
     * number la so luong chu so trong 1 timer
     * 00:00:02,276 --> 00:00:04,224
     * --> Hai dau phay
     */
    public final static int SoDauPhay = 2;
    /**
     * number la so luong chu so trong 1 timer
     * 00:00:02,276 --> 00:00:04,224
     * --> 4 dau ":"
     * --> 2 dau "-"
     * --> 1 dau ">"
     */
    public final static int SoDauHaiCham = 4;
    /**
     * number la so luong chu so trong 1 timer
     * 00:00:02,276 --> 00:00:04,224
     * --> 4 dau ":"
     * --> 2 dau "-"
     * --> 1 dau ">"
     */
    public final static int SoDauTru = 2;
    /**
     * number la so luong chu so trong 1 timer
     * 00:00:02,276 --> 00:00:04,224
     * --> 4 dau ":"
     * --> 2 dau "-"
     * --> 1 dau ">"
     */
    public static int SoDauLonHon = 1;
    
    private String sHour;
    private String sMin;
    private String sSec;
    private String eHour;
    private String eMin;
    private String eSec;
    
    public Timer(){
        this.sHour = this.sMin = "00";
        this.sSec = "00,000";
        this.eHour = this.eMin = "00";
        this.eSec = "00,000";
    }
    /**
     * 
     * @param t String timer theo định dạng trong subtitle "00:00:02,276 --> 00:00:04,224"
     * @throws java.io.IOException 
     */
    public Timer(String t) throws java.io.IOException {
        if ( Timer.kiemtra(t) )
        {
            // 00:00:02,276 --> 00:00:04,224
            StringBuilder tmp = new StringBuilder(t);
            
            sHour = tmp.substring(0,tmp.indexOf(":"));
            tmp.delete(0, tmp.indexOf(":")+1);
            sMin = tmp.substring(0,tmp.indexOf(":"));
            tmp.delete(0, tmp.indexOf(":")+1);   
            sSec = tmp.substring(0,tmp.indexOf(" -"));
            tmp.delete(0, tmp.indexOf("> ")+2);
            
            eHour = tmp.substring(0,tmp.indexOf(":"));
            tmp.delete(0, tmp.indexOf(":")+1);
            eMin = tmp.substring(0,tmp.indexOf(":"));
            tmp.delete(0, tmp.indexOf(":")+1);   
            eSec = tmp.toString();
            //tmp.delete(0, tmp.indexOf(":")+1);
        }
        else{
            java.io.IOException e = new java.io.IOException("Timer : wrong parameter format");
            throw e;
        }
      
            
    }
    
    public int compareStart(Timer o){
        if ( Integer.parseInt( this.sHour) > Integer.parseInt( o.sHour) )
            return 1;
        else if ( Integer.parseInt( this.sHour) < Integer.parseInt( o.sHour) )
                return -1;

        if ( Integer.parseInt( this.sMin) > Integer.parseInt( o.sMin) )
            return 1;
        else if ( Integer.parseInt( this.sMin) < Integer.parseInt( o.sMin) )
                return -1;
        
        
        if ( Float.parseFloat( this.sSec.replace(",", ".")) > Float.parseFloat( o.sSec.replace(",", ".")) )
            return 1;
        else if ( Float.parseFloat( this.sSec.replace(",", ".")) < Float.parseFloat( o.sSec.replace(",", ".")) )
                return -1;     
        return 0;
            
    }
    public int compareEnd(Timer o){
        if ( Integer.parseInt( this.eHour) > Integer.parseInt( o.eHour) )
            return 1;
        else if ( Integer.parseInt( this.eHour) < Integer.parseInt( o.eHour) )
                return -1;

        if ( Integer.parseInt( this.eMin) > Integer.parseInt( o.eMin) )
            return 1;
        else if ( Integer.parseInt( this.eMin) < Integer.parseInt( o.eMin) )
                return -1;
        
        if ( Float.parseFloat( this.eSec.replace(",", ".")) > Float.parseFloat( o.eSec.replace(",", ".")) )
            return 1;
        else if ( Float.parseFloat( this.eSec.replace(",", ".")) < Float.parseFloat( o.eSec.replace(",", ".")) )
                return -1;     
        return 0;
            
    }
    public String toString(){
        return sHour + ":" + sMin + ":" + sSec + " --> " + eHour + ":" + eMin + ":" + eSec;
    }
    /**
     * @param t String 
     * Kiem tra co phai la chuoi time ko
     */
    public static boolean kiemtra(String t){
        boolean muiten = false;
        int count = 0;
        for ( int i = 0; i<t.length(); i++)
        {
            if ( ( t.charAt(i) >= 'A' && t.charAt(i) <= 'Z' ) || ( t.charAt(i) >= 'a' && t.charAt(i) <= 'z' ) )
                return false;
            else if ( t.charAt(i) == ' ' )
            {
                if ( i+1<t.length() && t.charAt(i+1) == '-' )
                {
                    if ( i+2 < t.length() && t.charAt(i+2) == '-' )
                        if ( i+3 < t.length() && t.charAt(i+3) == '>' )
                            if ( i+4 < t.length() && t.charAt(i+4) == ' ' )
                            {
                                muiten = true;
                                i+=4;
                            }                    
                }
                else
                    return false;
            }
            else if ( t.charAt(i) == ':' )
                count++;
        }
        return muiten && count == Timer.SoDauHaiCham;
    }
    public static void main(String[] args) {
        try{
            Timer test = new Timer("00:00:02,276 --> 00:00:04,224");
            Timer test2 = new Timer("00:00:03,276 --> 00:00:04,224");

            System.out.println( test.compareStart(test2) );            
        }
        catch(java.io.IOException e ){
            System.out.println( e.toString() );
        }

        
    }
}
