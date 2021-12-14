/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.drawings;

import cad.project.controllers.Skitch;
import java.io.Serializable;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.shape.*;

/**

 @author EMAM
 */
public abstract class ElementDraw extends Group implements Serializable {

          public static final int UV = 1; // up vertical
          public static final int RH = 2; // right herozintal
          public static final int DV = 3; // down vertical
          public static final int LH = 4; // left herozintal

          private final String ID;
          private final PointDraw startPoint;
          private final Circle centerPoint;
          private final Group body;
          private final PointDraw endPoint;

          private final double drawingSteps = 3;
          private final double stepWidth = Skitch.getInstance(0 , 0).getStep();


          public ElementDraw(String id , PointDraw startPoint , PointDraw endPoint) {
                    this.ID = id;
                    this.startPoint = startPoint;
                    this.centerPoint = new Circle();
                    this.endPoint = endPoint;
                    this.body = new Group();
                    this.setOnMouseReleased(e -> {
                              if ( ((MouseEvent) e).getButton().equals(MouseButton.SECONDARY) ) {
                                        delete();
                              } else {
//                                        release(e);
                              }
                    });
                    this.setCursor(Cursor.OPEN_HAND);
                    draw();
                    initAction();
          }


          public Group getBody() {
                    return body;
          }


          public Circle getCenterPoint() {
                    return centerPoint;
          }


          public double getDrawingSteps() {
                    return drawingSteps;
          }


          public PointDraw getEndPoint() {
                    return endPoint;
          }


          public Node getHoldedNode() {
                    return holdedNode;
          }


          public String getID() {
                    return ID;
          }


          public PointDraw getStartPoint() {
                    return startPoint;
          }


          public double getStepWidth() {
                    return stepWidth;
          }


          private void delete() {
                    Skitch.getInstance(0 , 0).deleteElement(this);
          }


          public void drawBody() {
                    this.getBody().getChildren().clear();
                    double[] cordinates = drawPointBodyLink();

                    drawCentralBody(cordinates[0] ,
                            cordinates[1] , cordinates[2] ,
                            cordinates[3] , cordinates[4]);

                    double centerX = (cordinates[1] + cordinates[3]) / 2;
                    double centerY = (cordinates[2] + cordinates[4]) / 2;
                    double r = Math.sqrt(Math.pow(cordinates[1] - cordinates[3] , 2) + Math.pow(cordinates[2] - cordinates[4] , 2));
                    double x1 = centerX - r / 2;
                    double y1 = centerY - r / 2;
                    double x3 = centerX + r / 2;
                    double y3 = centerY + r / 2;
                    drawLabel(x1 , y1 , x3 , y3);
          }


          public abstract void drawCentralBody(double direction , double x1 , double y1 , double x2 , double y2);


          private void draw() {
                    getStartPoint().setTranslateX(-getDrawingSteps() * getStepWidth());
                    getEndPoint().setTranslateX(getDrawingSteps() * getStepWidth());
                    drawBody();
                    this.getChildren().addAll(getStartPoint() , getCenterPoint() , getBody() , getEndPoint());
          }


          /**
           (x1, y1) is the coordinates of the left up corner of the available place to draw label<br>
           (x3, y3) is the coordinates of the right down corner of the available place to draw label

           @param x1
           @param y1
           @param x3
           @param y3
           */
          public abstract void drawLabel(double x1 , double y1 , double x3 , double y3);


          private double[] drawPointBodyLink() {
                    double cordinates[] = new Object() {
                              ElementDraw thisDraw;


                              private double[] drawLine(ElementDraw thisDraw) {
                                        this.thisDraw = thisDraw;
                                        double zigzagW = getDrawingSteps() * getStepWidth();

                                        double[] cordinates = getCordinates();

                                        double x0 = cordinates[0];
                                        double y0 = cordinates[1];
                                        double x1 = cordinates[2];
                                        double y1 = cordinates[3];
                                        double x2 = cordinates[4];
                                        double y2 = cordinates[5];
                                        boolean isHorizontal = Math.abs(x1 - x2) >= Math.abs(y1 - y2);
                                        if ( isHorizontal ) {
                                                  boolean isLeft = x1 > x2;
                                                  if ( isLeft ) {
                                                            return leftHorizontalLine(x1 , y1 , x2 , y2 , x0 , y0 , zigzagW);
                                                  } else {
                                                            return rightHorizontalLine(x1 , y1 , x2 , y2 , x0 , y0 , zigzagW);
                                                  }
                                        } else {
                                                  boolean isUp = y2 < y1;
                                                  if ( isUp ) {
                                                            return upVerticalLine(x1 , y1 , x2 , y2 , x0 , y0 , zigzagW);
                                                  } else {
                                                            return downVerticalLine(x1 , y1 , x2 , y2 , x0 , y0 , zigzagW);
                                                  }
                                        }

                              }


                              private double[] getCordinates() {
                                        PointDraw p1, p2;
                                        if ( getHoldedNode() == getStartPoint() ) {
                                                  p2 = getStartPoint();
                                                  p1 = getEndPoint();
                                        } else {
                                                  p1 = getStartPoint();
                                                  p2 = getEndPoint();
                                        }
                                        double[] cordinates = {
                                                  getCenterPoint().getTranslateX() , getCenterPoint().getTranslateY() ,
                                                  p1.getTranslateX() , p1.getTranslateY() ,
                                                  p2.getTranslateX() , p2.getTranslateY()
                                        };
                                        return cordinates;
                              }


                              private double[] leftHorizontalLine(double x1 , double y1 , double x2 , double y2 , double x0 , double y0 , double zigzagW) {
                                        double n = ((x1 - x2) - zigzagW) / 2;
//                                        double step = getStepWidth();
                                        Polyline line1 = new Polyline(x2 , y2 , x2 , y0 , x2 + n , y0);
                                        //                                                  x2 + n + step , y0 + step ,
                                        //                                                  x2 + n + step , y0 - step ,
                                        //                                                  x1 - n - step , y0 + step ,
                                        //                                                  x1 - n - step , y0 - step ,
                                        Polyline line2 = new Polyline(x1 - n , y0 , x1 , y0 , x1 , y1);
                                        line1.setStrokeWidth(3);
                                        line2.setStrokeWidth(3);
                                        thisDraw.getBody().getChildren().clear();
                                        thisDraw.getBody().getChildren().addAll(line1 , line2);
                                        return new double[] { LH , x2 + n , y0 , x1 - n , y0 };
                              }


                              private double[] upVerticalLine(double x1 , double y1 , double x2 , double y2 , double x0 , double y0 , double zigzagW) {
                                        double n = (y1 - y2 - zigzagW) / 2;
//                                        double step = getStepWidth();
                                        Polyline line1 = new Polyline(x2 , y2 , x0 , y2 , x0 , y2 + n);
                                        //                                                x0 + step , y2 + n + step ,
                                        //                                                x0 - step , y2 + n + step ,
                                        //                                                x0 + step , y1 - n - step ,
                                        //                                                x0 - step , y1 - n - step ,
                                        Polyline line2 = new Polyline(x0 , y1 - n , x0 , y1 , x1 , y1);
                                        line1.setStrokeWidth(3);
                                        line2.setStrokeWidth(3);
                                        thisDraw.getBody().getChildren().clear();
                                        thisDraw.getBody().getChildren().addAll(line1 , line2);
                                        return new double[] { UV , x0 , y2 + n , x0 , y1 - n };
                              }


                              private double[] downVerticalLine(double x1 , double y1 , double x2 , double y2 , double x0 , double y0 , double zigzagW) {
                                        double n = (y2 - y1 - zigzagW) / 2;
//                                        double step = getStepWidth();
                                        Polyline line1 = new Polyline(x2 , y2 , x0 , y2 , x0 , y2 - n);
                                        //                                                x0 + step , y2 + n + step ,
                                        //                                                x0 - step , y2 + n + step ,
                                        //                                                x0 + step , y1 - n - step ,
                                        //                                                x0 - step , y1 - n - step ,
                                        Polyline line2 = new Polyline(x0 , y1 + n , x0 , y1 , x1 , y1);
                                        line1.setStrokeWidth(3);
                                        line2.setStrokeWidth(3);
                                        thisDraw.getBody().getChildren().clear();
                                        thisDraw.getBody().getChildren().addAll(line1 , line2);
                                        return new double[] { DV , x0 , y2 - n , x0 , y1 + n };
                              }


                              private double[] rightHorizontalLine(double x1 , double y1 , double x2 , double y2 , double x0 , double y0 , double zigzagW) {
                                        double n = ((x2 - x1) - zigzagW) / 2;
//                                        double step = getStepWidth();
                                        Polyline line1 = new Polyline(x2 , y2 , x2 , y0 , x2 - n , y0);
                                        //                                                x2 - n - step , y0 - step ,
                                        //                                                x2 - n - step , y0 + step ,
                                        //                                                x1 + n + step , y0 - step ,
                                        //                                                x1 + n + step , y0 + step ,
                                        Polyline line2 = new Polyline(x1 + n , y0 , x1 , y0 , x1 , y1);
                                        line1.setStrokeWidth(3);
                                        line2.setStrokeWidth(3);
                                        thisDraw.getBody().getChildren().clear();
                                        thisDraw.getBody().getChildren().addAll(line1 , line2);
                                        return new double[] { RH , x2 - n , y0 , x1 + n , y0 };
                              }

                    }.drawLine(this);
                    return cordinates;
          }


          private void initAction() {
//                    point 1
                    this.getStartPoint().setOnMousePressed(e -> holdPoint(e));
                    this.getStartPoint().setOnMouseDragged(e -> dragPoint(e));
                    this.getStartPoint().setOnMouseReleased(e -> release(e));
//                    point 2
                    this.getEndPoint().setOnMousePressed(e -> holdPoint(e));
                    this.getEndPoint().setOnMouseDragged(e -> dragPoint(e));
                    this.getEndPoint().setOnMouseReleased(e -> release(e));
//                    body
                    this.getBody().setOnMousePressed(e -> holdBody(e));
                    this.getBody().setOnMouseDragged(e -> dragBody(e));
                    this.getBody().setOnMouseReleased(e -> release(e));
          }
          private double mouseX;
          private double mouseY;

          private Node holdedNode;


          private void holdPoint(MouseEvent e) {
                    holdedNode = ((Node) e.getSource());
                    mouseX = holdedNode.getTranslateX() - e.getSceneX();
                    mouseY = holdedNode.getTranslateY() - e.getSceneY();
                    this.setCursor(Cursor.CLOSED_HAND);
          }


          private void dragPoint(MouseEvent e) {
                    double minimumWidth = (drawingSteps + 1) * stepWidth;
                    /*
                     double scaleX = CADProject.skitch.getGraph().getScaleX();
                     double scaleY = CADProject.skitch.getGraph().getScaleY();
                     */

                    double difX = e.getSceneX() + mouseX;
                    double difY = e.getSceneY() + mouseY;
                    difX = Math.round(difX / stepWidth) * stepWidth;
                    difY = Math.round(difY / stepWidth) * stepWidth;

                    double tx = holdedNode.getTranslateX();
                    double ty = holdedNode.getTranslateY();
                    holdedNode.setTranslateX(difX);
                    holdedNode.setTranslateY(difY);
                    if ( Math.abs(getStartPoint().getTranslateX() - getEndPoint().getTranslateX()) <= minimumWidth
                            && Math.abs(getStartPoint().getTranslateY() - getEndPoint().getTranslateY()) <= minimumWidth ) {
                              holdedNode.setTranslateX(tx);
                              holdedNode.setTranslateY(ty);
                    } else if ( !(tx == difX && ty == difY) ) {
                              drawBody();
                    }
          }


          private void holdBody(MouseEvent e) {
                    mouseX = this.getTranslateX() - e.getSceneX();
                    mouseY = this.getTranslateY() - e.getSceneY();
                    this.setCursor(Cursor.CLOSED_HAND);
          }


          private void dragBody(MouseEvent e) {
                    double difX = e.getSceneX() + mouseX;
                    double difY = e.getSceneY() + mouseY;
                    difX = Math.round(difX / stepWidth) * stepWidth;
                    difY = Math.round(difY / stepWidth) * stepWidth;
                    this.setTranslateX(difX);
                    this.setTranslateY(difY);
          }


          private void release(MouseEvent e) {
                    this.setCursor(Cursor.OPEN_HAND);
          }

//          private double mouseX;
//          private double mouseY;
//
//          private void hold(MouseEvent e) {
//                    mouseX = ((Node) e.getSource()).getTranslateX() - e.getSceneX();
//                    mouseY = ((Node) e.getSource()).getTranslateY() - e.getSceneY();
//                    this.setCursor(Cursor.CLOSED_HAND);
//          }
//
//
//          public void drag(MouseEvent e) {
//                    int step = CADProject.skitch.getStep();
//
//                    double difX = e.getSceneX() + mouseX;
//                    double difY = e.getSceneY() + mouseY;
//                    difX = Math.round(difX / step) * step;
//                    difY = Math.round(difY / step) * step;
//                    this.setTranslateX(difX);
//                    this.setTranslateY(difY);
//          }
//
//
//          public void release(MouseEvent e) {
//                    this.setCursor(Cursor.OPEN_HAND);
//          }
}
