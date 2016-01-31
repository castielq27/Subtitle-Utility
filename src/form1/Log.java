/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form1;

import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import javax.swing.JTextArea;


/**
 *
 * @author castiel
 * 
 * 
 * Log extends OutputStream
 * new PrintStream(OutputStream)
 * System.setOut( PrintStream new_out ) --> Set lai System.out = System.new_out
 * --> System.out.print() ---> PrintStream.write(int)
 * 
 */
public class Log extends OutputStream {

    private JTextArea txt;

    public Log(JTextArea txt){
        this.txt = txt;
    }
    
    @Override
    public void write(int x){
        txt.append( ((char)x) + "" );
    }
    
    
}

