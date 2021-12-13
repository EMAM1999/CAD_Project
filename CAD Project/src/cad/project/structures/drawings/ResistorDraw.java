/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.drawings;

import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
// Draw Current Direction Araw 

/**

 @author EMAM
 */
public final class ResistorDraw extends ElementDraw {

          private final double resistance;
          private double voltage;
          private double current;


          public ResistorDraw(String id , double resistanceValue , String startPointID , String endPointID) {
                    this(id , resistanceValue , new PointDraw(startPointID) , new PointDraw(endPointID));
          }


          public ResistorDraw(String id , double resistanceValue , PointDraw startPoint , PointDraw endPoint) {
                    super(id , startPoint , endPoint);
                    this.resistance = resistanceValue;
                    this.voltage = 0;
                    drawBody();
          }


          @Override
          public void drawLabel(double x1 , double y1 , double x3 , double y3) {
//                    Resistance Label
                    Text RValue = new Text(x1 , y1 , this.getResistance() + " Ω");
                    RValue.setFont(Font.font("Arial" , FontWeight.BOLD , 20));
                    RValue.setFill(Color.DARKRED);
                    double offset = RValue.getBaselineOffset();
                    RValue.setTranslateX(-offset);

//                    Current Label
                    Text AValue = new Text(x1 , y3 , this.getCurrent() + " A");
                    AValue.setFont(Font.font("Arial" , FontWeight.BOLD , 20));
                    AValue.setFill(Color.DARKGREEN);
                    offset = AValue.getBaselineOffset();
                    AValue.setTranslateX(-offset);

//                    Voltage Label
                    Text VValue = new Text(x3 , y3 , this.getVoltage() + " V");
                    VValue.setFont(Font.font("Arial" , FontWeight.BOLD , 20));
                    VValue.setFill(Color.DARKGREEN);
                    offset = VValue.getBaselineOffset();
                    AValue.setTranslateX(-offset);

                    this.getBody().getChildren().addAll(RValue , AValue , VValue);

          }


          public double getCurrent() {
                    return current;
          }


          public void setCurrent(double current) {
                    this.current = current;
          }


          @Override
          public void drawCentralBody(double direction , double x1 , double y1 , double x2 , double y2) {
                    double step = getStepWidth() * getDrawingSteps() / 4;

                    Polyline line = new Polyline(x1 , y1);
                    switch ( (int) direction ) {
                              case UV:
                                        line.getPoints().addAll(
                                                x1 + step , y1 + step ,
                                                x1 - step , y1 + step ,
                                                x2 + step , y2 - step ,
                                                x2 - step , y2 - step
                                        );
                                        break;
                              case RH:
                                        line.getPoints().addAll(
                                                x1 - step , y1 - step ,
                                                x1 - step , y1 + step ,
                                                x2 + step , y2 - step ,
                                                x2 + step , y2 + step
                                        );
                                        break;
                              case DV:
                                        line.getPoints().addAll(
                                                x1 - step , y1 - step ,
                                                x1 + step , y1 - step ,
                                                x2 - step , y2 + step ,
                                                x2 + step , y2 + step
                                        );
                                        break;
                              case LH:
                                        line.getPoints().addAll(
                                                x1 + step , y1 + step ,
                                                x1 + step , y1 - step ,
                                                x2 - step , y2 + step ,
                                                x2 - step , y2 - step
                                        );
                                        break;
                    }
                    line.getPoints().addAll(x2 , y2);
                    line.setStrokeWidth(3);
                    this.getBody().getChildren().add(line);
          }


          public double getVoltage() {
                    return this.voltage;
          }


          public void setVoltage(double voltage) {
                    this.voltage = voltage;
          }


          public double getResistance() {
                    return resistance;
          }

}
