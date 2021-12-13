/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.controllers;

import cad.project.structures.drawings.ResistorDraw;
import cad.project.structures.drawings.CurrentSourceDraw;
import cad.project.structures.drawings.VoltageSourceDraw;
import static cad.project.controllers.CADProject.*;
import java.awt.HeadlessException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.input.*;
import javax.swing.JOptionPane;

/**
 FXML Controller class

 @author EMAM
 */
public class NodesFXMLController implements Initializable {

          @FXML
          void drawPoint(MouseEvent event) {
//                    CADProject.skitch.addElement(new PointDraw(nodeIds + ""));
//                    nodeIds++;
          }


          @FXML
          void drawResistor(MouseEvent event) {
                    try {
                              int r = Integer.parseInt(JOptionPane.showInputDialog("Enter Resistance Value"));
                              skitch.addElement(new ResistorDraw(branchIds + "" , r , CADProject.nodeIds++ + "" , CADProject.nodeIds++ + ""));
                              branchIds++;
                    } catch ( HeadlessException | NumberFormatException e ) {
                    }
          }


          @FXML
          void drawCurrentResource(MouseEvent event) {
                    try {
                              int c = Integer.parseInt(JOptionPane.showInputDialog("Enter Current Value"));
                              skitch.addElement(new CurrentSourceDraw(branchIds + "" , c , CADProject.nodeIds++ + "" , CADProject.nodeIds++ + ""));
                              branchIds++;
                    } catch ( HeadlessException | NumberFormatException e ) {
                    }
          }


          @FXML
          void drawVoltageResource(MouseEvent event) {
                    try {
                              int v = Integer.parseInt(JOptionPane.showInputDialog("Enter Voltage Value"));
                              skitch.addElement(new VoltageSourceDraw(branchIds + "" , v , CADProject.nodeIds++ + "" , CADProject.nodeIds++ + ""));
                              branchIds++;
                    } catch ( HeadlessException | NumberFormatException e ) {
                    }
          }

          private Skitch skitch;


          /**
           Initializes the controller class.

           @param url
           @param rb
           */
          @Override
          public void initialize(URL url , ResourceBundle rb) {
                    skitch = Skitch.getInstance(0 , 0);
          }

}
