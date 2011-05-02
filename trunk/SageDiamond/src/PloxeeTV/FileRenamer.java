/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import java.io.File;

/**
 *
 * @author SBANTA
 */
public class FileRenamer {


    public static void main(String[] args){
    File curr = new File("c:\\Examples\\");
    File[] files = curr.listFiles();
    for(File folder:files){
    String CurrName = folder.toString();
    CurrName = CurrName.substring(CurrName.lastIndexOf("\\")+1);
    System.out.println("CurrName="+CurrName);
    File[] currimage=folder.listFiles();
    for(File image:currimage){
    ScaleImage.scale(image.toString(),200,300, folder.toString()+".png");
   }

    }

    }

}
