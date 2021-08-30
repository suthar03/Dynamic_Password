/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Suthar
 */
public class rough {
    public static void main(String[] args){
        try {
            Scanner scn = new Scanner(new File("D:\\Shopy\\Shopy/charset.txt"));
            while(scn.hasNext()){
                System.out.println(scn.next()+":");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(rough.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
