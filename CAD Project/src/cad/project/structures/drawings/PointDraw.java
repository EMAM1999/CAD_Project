/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.drawings;

import javafx.scene.*;
import javafx.scene.shape.*;

/**

 @author EMAM
 */
public class PointDraw extends Group {

          private final double RADIUS;
          private final String ID;


          public PointDraw(String id) {
                    this(id , 8);
          }


          public PointDraw(String id , double r) {
                    this.ID = id;
                    this.RADIUS = r;
                    draw();
          }


          public void draw() {
                    Circle dot = new Circle(this.RADIUS);
                    this.getChildren().add(dot);
          }


          public String getID() {
                    return ID;
          }

}
