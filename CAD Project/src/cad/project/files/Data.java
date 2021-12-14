/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.files;

import cad.project.structures.drawings.ElementDraw;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**

 @author EMAM
 */
public class Data {

          public static List<ElementDraw> read(File f) throws ClassNotFoundException , FileNotFoundException , IOException {
                    List<ElementDraw> elements = new ArrayList<>();
                    ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                    while ( true ) {
                              try {
                                        elements.add((ElementDraw) stream.readObject());
                              } catch ( EOFException e ) {
                                        break;
                              }
                    }
                    return elements;
          }


          public static void write(File f , List<ElementDraw> elements) throws FileNotFoundException , IOException {
                    ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
                    for ( ElementDraw ele : elements ) {
                              stream.writeObject(ele);
                    }
          }

}
