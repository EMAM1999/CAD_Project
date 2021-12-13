/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.drawings;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

/**

 @author EMAM
 */
public class VoltageSourceDraw extends ElementDraw {

          private final double volt;


          public VoltageSourceDraw(String id , double volt , PointDraw startPoint , PointDraw endPoint) {
                    super(id , startPoint , endPoint);
                    this.volt = volt;
                    drawBody();
          }


          public VoltageSourceDraw(String id , double volt , String startPointID , String endPointID) {
                    this(id , volt , new PointDraw(startPointID) , new PointDraw(endPointID));
          }


          @Override
          public void drawCentralBody(double direction , double x1 , double y1 , double x2 , double y2) {
                    double radius = Math.sqrt(Math.pow(x1 - x2 , 2) + Math.pow(y1 - y2 , 2));
                    Circle circle = new Circle((x1 + x2) / 2 , (y1 + y2) / 2 , radius / 2 , Color.TRANSPARENT);
                    circle.setStrokeWidth(3);
                    circle.setStroke(Color.BLACK);
                    this.getBody().getChildren().add(circle);
          }


          @Override
          public void drawLabel(double x1 , double y1 , double x2 , double y2) {
                    Text value = new Text(x1 , y1 , this.getVolt() + " V");
                    value.setFont(Font.font("Arial" , FontWeight.BOLD , 20));
                    value.setFill(Color.DARKRED);
                    double offset = value.getBaselineOffset();
                    value.setTranslateX(-offset);
//                    value.setTranslateX(Math.copySign(x1-x2, -1) / 2 - offset);
//                    value.setTranslateY(Math.copySign(y1-y2, -1) / 2);
                    this.getBody().getChildren().add(value);
          }


          public double getVolt() {
                    return volt;
          }

}
