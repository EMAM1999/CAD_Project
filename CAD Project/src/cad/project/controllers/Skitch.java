/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.controllers;

import cad.project.structures.drawings.ElementDraw;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;

/**

 @author EMAM
 */
public class Skitch {

          private final double step;
          private final double range;

          private final List<ElementDraw> drawingsElements;
          private StackPane graph;
          private static Skitch instance;


          private Skitch(double w , double gap) {
                    this.range = w;
                    this.step = gap;
                    drawingsElements = new ArrayList<>();
                    initGraph();
          }


          public void deleteAllElements() {
                    ObservableList list = getSkitchGroup().getChildren();
                    for ( Object ele : drawingsElements ) {
                              list.remove(ele);
                    }
                    drawingsElements.clear();
          }


          public boolean deleteElement(ElementDraw ele) {
                    ObservableList list = getSkitchGroup().getChildren();
                    for ( Object listEle : list ) {
                              if ( ele == listEle ) {
                                        list.remove(ele);
                                        break;
                              }
                    }
                    return drawingsElements.remove(ele);
          }


          public boolean addElement(ElementDraw ele) {
                    double w = CADProject.stage.getWidth();
                    double h = CADProject.stage.getHeight();
                    double x = (500) - range - getGraph().getTranslateX();
                    double y = (500) - range - getGraph().getTranslateY();
                    ele.setTranslateX(Math.round(x / step) * step);
                    ele.setTranslateY(Math.round(y / step) * step);
//                    this.graph.getChildren().add(ele);
//                    return drawings.add(ele);
                    return getSkitchGroup().getChildren().add(ele)
                            && drawingsElements.add(ele);
          }


          public static Skitch getInstance(double w , double gap) {
                    if ( instance == null ) {
                              instance = new Skitch(w , gap);
                    }
                    return instance;
          }


          public List<ElementDraw> getDrawingsElements() {
                    return drawingsElements;
          }


          public double getRange() {
                    return range;
          }


          public double getStep() {
                    return step;
          }


          public void initGraph() {
                    Group g = new Group();
                    for ( int i = 0 ; i <= range ; i += step ) {
//                              vertical
                              g.getChildren().add(new Line(i , range , i , -range));
//                              horizontal
                              g.getChildren().add(new Line(range , i , -range , i));
//                              vertical
                              g.getChildren().add(new Line(-i , range , -i , -range));
//                              horizontal
                              g.getChildren().add(new Line(range , -i , -range , -i));
                    }
                    StackPane p = new StackPane(g);
                    p.setPrefSize(range * 2 , range * 2);

                    p.setOnMousePressed((e) -> {
                              if ( e.getClickCount() == 2 ) {
                                        holded = true;
                                        hold(e);
                              }
                    });
                    p.setOnMouseDragged((e) -> {
                              if ( holded ) {
                                        double w = ((Scene) ((Node) e.getSource()).getScene()).getWindow().getWidth();
                                        double h = ((Scene) ((Node) e.getSource()).getScene()).getWindow().getHeight();
                                        if ( p.getTranslateX() <= 0 && p.getTranslateX() >= -(range * 2 - w)
                                                && p.getTranslateY() <= 0 && p.getTranslateY() >= -(range * 2 - h) ) {
                                                  drag(e);
                                        }
                              }
                    });
                    p.setOnMouseReleased((e) -> {
                              holded = false;
                              double w = ((Scene) ((Node) e.getSource()).getScene()).getWindow().getWidth();
                              double h = ((Scene) ((Node) e.getSource()).getScene()).getWindow().getHeight();
                              if ( p.getTranslateX() > 0 ) {
                                        p.setTranslateX(0 - step);
                              } else if ( p.getTranslateX() < -(range * 2 - w) ) {
                                        p.setTranslateX(-(range * 2 - w) + step);
                              }
                              if ( p.getTranslateY() > 0 ) {
                                        p.setTranslateY(0 - step);
                              } else if ( p.getTranslateY() < -(range * 2 - h) ) {
                                        p.setTranslateY(-(range * 2 - h) + step);
                              }
                    });
                    this.graph = p;
          }


          public Parent getGraph() {
                    return this.graph;
          }

          private double mouseX;
          private double mouseY;
          private boolean holded;


          private Group getSkitchGroup() {
                    return ((Group) ((StackPane) this.getGraph()).getChildren().get(0));
          }


          private void hold(MouseEvent e) {
                    mouseX = ((Node) e.getSource()).getTranslateX() - e.getSceneX();
                    mouseY = ((Node) e.getSource()).getTranslateY() - e.getSceneY();
          }


          public void drag(MouseEvent e) {
                    double difX = e.getSceneX() + mouseX;
                    double difY = e.getSceneY() + mouseY;
                    ((Node) e.getSource()).setTranslateX(Math.round(difX / step) * step);
                    ((Node) e.getSource()).setTranslateY(Math.round(difY / step) * step);
          }

}
