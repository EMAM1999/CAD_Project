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
public class CurrentSourceDraw extends ElementDraw {

          private final double current;


          public CurrentSourceDraw(String id , double current , String startPointID , String endPointID) {
                    this(id , current , new PointDraw(startPointID) , new PointDraw(endPointID));
          }


          public CurrentSourceDraw(String id , double current , PointDraw startPoint , PointDraw endPoint) {
                    super(id , startPoint , endPoint);
                    this.current = current;
                    drawBody();
          }


          @Override
          public void drawCentralBody(double direction , double x1 , double y1 , double x2 , double y2) {
                    double radius = Math.sqrt(Math.pow(x1 - x2 , 2) + Math.pow(y1 - y2 , 2));
                    Circle circle = new Circle((x1 + x2) / 2 , (y1 + y2) / 2 , radius / 2 , Color.TRANSPARENT);
                    circle.setStrokeWidth(3);
                    circle.setStroke(Color.BLACK);
                    this.getBody().getChildren().add(circle);

                    Line l = new Line(x1 , y1 , x2 , y2);
                    l.setStrokeWidth(3);
                    this.getBody().getChildren().addAll(l);
          }


          @Override
          public void drawLabel(double x1 , double y1 , double x3 , double y3) {
                    Text value = new Text(x1 , y1 , this.getCurrent() + " A");
                    value.setFont(Font.font("Arial" , FontWeight.BOLD , 20));
                    value.setFill(Color.DARKRED);
                    double offset = value.getBaselineOffset();
                    value.setTranslateX(- offset);
//                    value.setTranslateY(-radius / 2);
                    this.getBody().getChildren().add(value);
          }


          public double getCurrent() {
                    return current;
          }

//
//          public void setCurrent(double current) {
//                    this.current = current;
//          }
}
