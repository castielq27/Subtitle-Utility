/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mergesubtitle;

import Srt.*;
import java.io.File;


/**
 *
 * @author castiel
 */
public class MergeSubtitle {

    /**
     * @param args the command line arguments
     */
    private String file1;
    private String tag1;

    private String file2;
    private String tag2;
    
    private String fileOut;
    
    private Srt sub1;
    private Srt sub2;
    private Srt subOut;
    
    public void setInputFile(String srt1, String srt2){
        this.file1 = srt1;
        this.file2 = srt2;
    }
    public void setTag1(String tag){
        this.tag1 = tag;
    }
    public void setTag2(String tag){
        this.tag2 = tag;
    }
    public void setOutputFile(String out){
        this.fileOut = out;
    }
    public void getInputFile(String file1, String file2) throws Exception {
        this.setInputFile(file1, file2);
        this.getInputFile();
    }
    public void getInputFile() throws Exception {
        if ( file1 == null || file2 == null ){
            throw new Exception("MergeSubtitle.getInputFile filename null " + "file1:" + file1 + "file2:" + file2 );
        }
        //sub1
        try{
            sub1 = new Srt(file1);
        }
        catch(Exception e){
            if ( e.getMessage().compareTo(Srt.errorEncodeUTF8_UTF16) == 0 ){
                System.out.println("Try with other encode : UTF16 " + file1 );
                sub1 = new Srt(file1,"UTF16");
            }
            else
                throw e;
        }
        //sub2
        try{
            sub2 = new Srt(file2);
        }
        catch(Exception e){
            if ( e.getMessage().compareTo(Srt.errorEncodeUTF8_UTF16) == 0 ){
                System.out.println("Try with other encode : UTF16 " + file1 );
                sub2 = new Srt(file2,"UTF16");
            }
            else
                throw e;
        }
    }
    public void Merge() throws Exception {
        if ( tag1 != null )
            sub1.setTag(tag1);
        if ( tag2 != null )
            sub2.setTag(tag2);

        subOut = new Srt();        
        Point p1 = sub1.nextPoint();
        Point p2 = sub2.nextPoint();
        int ID = 1;
        while ( p1 != null || p2 != null ){
            if ( p1 != null && p2 != null ){
                if ( p1.compareStart(p2) <= 0 ){
                    p1.setID(ID);
                    subOut.addPoint(p1);
                    ID++;
                    p1 = sub1.nextPoint();
                }
                else{
                    p2.setID(ID);
                    subOut.addPoint(p2);
                    ID++;
                    p2 = sub2.nextPoint();
                }
            }
            else if ( p1 == null && p2 != null ){
                p2.setID(ID);
                subOut.addPoint(p2);
                ID++;
                p2 = sub2.nextPoint();
            }
            else if ( p1 != null && p2 == null ){
                p1.setID(ID);
                subOut.addPoint(p1);
                ID++;
                p1 = sub1.nextPoint();
            }
        }
    }
    public void SaveMergeSub(String outFile) throws Exception {
        this.setOutputFile(outFile);
        this.SaveMergeSub();
    }
    public void SaveMergeSub() throws Exception {
        if ( fileOut == null ){
            throw new Exception("MergeSubtitle.SaveMergeSub fileOut == NULL ");
        }
        else{
            int count = 0;
            String tmp = fileOut.substring(0, fileOut.lastIndexOf(".") );// loai bo duoi .srt 
            File f = null;
            

            this.fileOut = tmp + "00.srt";// First time count == 0 -- path du nguyen
            f = new File( this.fileOut );                

            while ( f.exists() ){
                count++;// count >=1
                if ( count < 10 )
                    this.fileOut = tmp + "0" + count + ".srt";// new Path + count + .srt
                else
                    this.fileOut = tmp + count + ".srt";// new Path + count + .srt
                f = new File( this.fileOut );
                System.out.println("File exists --> new Path" + this.fileOut );
            }
            
            subOut.outFile(fileOut);
        }
            
    }
    
    public static void main(String[] args) throws Exception {

        try{
            MergeSubtitle merge = new MergeSubtitle();
            merge.getInputFile("/run/media/castiel/Other/a/Tron.Uprising.S01E01-19.720p.WEB-DL.x264.AAC/e/Tron.Uprising.S01E08.The.Reward.480p.WEB-DL.x264-mSD.srt",
                    "/run/media/castiel/Other/a/Tron.Uprising.S01E01-19.720p.WEB-DL.x264.AAC/v/TrUpr.108.srt");
            merge.setTag1("{\\an2}");
            merge.setTag2("{\\an8}");
            merge.Merge();
            merge.SaveMergeSub("/run/media/castiel/Other/a/Tron.Uprising.S01E01-19.720p.WEB-DL.x264.AAC/out8.srt");

        }
        catch( java.lang.Exception e ){
            System.out.println( e.toString() + "Main" );
            throw e;
        }
        
        
    }
    
}
