/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.controllers;

import cad.project.structures.drawings.CurrentSourceDraw;
import cad.project.structures.drawings.ElementDraw;
import cad.project.structures.drawings.PointDraw;
import cad.project.structures.drawings.ResistorDraw;
import cad.project.structures.drawings.VoltageSourceDraw;
import cad.project.structures.graph.Branch;
import cad.project.structures.graph.Circuit;
import cad.project.structures.graph.Node;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javax.swing.JOptionPane;
//solve() -> solve()

/**

 @author EMAM
 */
public class FXMLDocumentController implements Initializable {

          @FXML
          private JFXButton graphBtn;

          @FXML
          private JFXButton ABtn;

          @FXML
          private JFXButton BBtn;

          @FXML
          private JFXButton CBtn;

          @FXML
          private JFXButton solveBtn;

          @FXML
          private Button deleteBtn;

          @FXML
          private Pane nodesPane;

          @FXML
          private StackPane skitchPane;

          private Skitch skitch;
          private Circuit circuit;


          @FXML
          private void zoomIn(MouseEvent event) {
                    Parent g = skitch.getGraph();
                    if ( g.getScaleX() < 5 ) {
                              g.setScaleX(g.getScaleX() + 0.2);
                              g.setScaleY(g.getScaleY() + 0.2);
                    }
          }


          @FXML
          private void zoomOut(MouseEvent event) {
                    Parent g = skitch.getGraph();
                    if ( g.getScaleX() > 0.5 ) {
                              g.setScaleX(g.getScaleX() - 0.2);
                              g.setScaleY(g.getScaleY() - 0.2);
                    }
          }


          @FXML
          private void resetScale(MouseEvent event) {
                    Parent g = skitch.getGraph();
                    g.setScaleX(1);
                    g.setScaleY(1);
          }


          @FXML
          private void reset(MouseEvent event) {
                    int c = JOptionPane.showConfirmDialog(null , "Are you sure you want to delete the drawing !?");
                    System.out.println(c);
                    if ( c == 0 ) {
                              skitch.deleteAllElements();
                    }
          }


          @FXML
          private void solve(MouseEvent event) {
                    new Object() {
                              private List<Branch> wires;
                              private Map<String , Node> points;
                              private List<ElementDraw> elements;


                              private Branch getBranchByNodes(Node node1 , Node node2) {
                                        for ( Branch branch : wires ) {
                                                  if ( branch.getStartPoint().equals(node1) && branch.getEndPoint().equals(node2)
                                                          || branch.getEndPoint().equals(node1) && branch.getStartPoint().equals(node2) ) {
                                                            return branch;
                                                  }
                                        }
                                        return null;
                              }


                              private List<Integer> getBranchsByNode(Node node) {
                                        List<Integer> branchs = new ArrayList<>();
                                        for ( int i = 0 ; i < wires.size() ; i++ ) {
                                                  if ( wires.get(i).getStartPoint().equals(node) || wires.get(i).getEndPoint().equals(node) ) {
                                                            branchs.add(i);
                                                  }
                                        }
                                        return branchs;
                              }


                              private String getPointPlace(ElementDraw ele , PointDraw point) {
                                        return (point.getTranslateX() + ele.getTranslateX()) + "" + (point.getTranslateY() + ele.getTranslateY());
                              }


                              private Node addPointGetNode(ElementDraw ele , PointDraw point) {
                                        Node node = null;
//                                                The  point place
                                        String place = getPointPlace(ele , point);
//                                                Is the point found already (repeated)
                                        if ( points.containsKey(place) ) {
                                                  node = points.get(place);
                                        } else {
                                                  node = new Node(point.getID());
                                                  points.put(place , node);
                                        }
                                        return node;
                              }


                              private void solve() {
                                        wires = new ArrayList<>();
                                        points = new HashMap<>();
                                        elements = new ArrayList<>(skitch.getDrawingsElements());
                                        filterResistances();
                                        filterCurrentSources();
                                        filterVoltageSources();
                                        circuit = Circuit.getInstance(wires);

                                        System.out.println("Jb");
                                        double[][] m = circuit.getGraph().getJb().getArrayCopy();
                                        print(circuit.getGraph() , m);
                                        System.out.println("Vb");
                                        m = circuit.getGraph().getVb().getArrayCopy();
                                        print(circuit.getGraph().getBranchs() , circuit.getGraph().getLinks() , m);

//                                        Set The values on the drawn
                              }


                              public void print(List<Branch> branchs , List<Branch> links , double[][] num) {
                                        for ( int i = 0 ; i < branchs.size() ; i++ ) {
                                                  String b = branchs.get(i).getID();
                                                  String sp = branchs.get(i).getStartPoint().getID();
                                                  String ep = branchs.get(i).getEndPoint().getID();
                                                  for ( int j = 0 ; j < num[i].length ; j++ ) {
                                                            System.out.printf("%s (%s >> %s)\t%.3f " , b , sp , ep , num[i][j]);
                                                  }
                                                  System.out.println("");
                                        }
                                        for ( int i = 0 ; i < links.size() ; i++ ) {
                                                  String b = links.get(i).getID();
                                                  String sp = links.get(i).getStartPoint().getID();
                                                  String ep = links.get(i).getEndPoint().getID();
                                                  for ( int j = 0 ; j < num[i + branchs.size()].length ; j++ ) {
                                                            System.out.printf("%s (%s >> %s)\t%.3f " , b , sp , ep , num[i + branchs.size()][j]);
                                                  }
                                                  System.out.println("");
                                        }
                              }


                              private void print(Circuit.Graph graph , double[][] m) {
                                        print(graph.getBranchs() , graph.getLinks() , m);
                              }


                              private void filterCurrentSources() {
                                        List<ElementDraw> temp = new ArrayList<>(elements);
                                        temp.forEach(ele -> {
                                                  if ( ele instanceof CurrentSourceDraw ) {
//                                                The first point Place
                                                            String firstPointPlace = getPointPlace(ele , ele.getStartPoint());
//                                                The second point Place
                                                            String secondPointPlace = getPointPlace(ele , ele.getEndPoint());
//                                                The first node
                                                            Node node1 = points.get(firstPointPlace);
//                                                The second node
                                                            Node node2 = points.get(secondPointPlace);
//                                                The branch attached with current source
                                                            Branch branch = getBranchByNodes(node1 , node2);
//                                                The direction of the current
                                                            if ( branch != null ) {
                                                                      if ( branch.getStartPoint().equals(ele.getStartPoint()) ) {
                                                                                branch.setSourceCurrent(-1 * ((CurrentSourceDraw) ele).getCurrent());
                                                                      } else {
                                                                                branch.setSourceCurrent(((CurrentSourceDraw) ele).getCurrent());
                                                                      }
                                                            }

                                                            elements.remove(ele);
                                                  }
                                        });
                              }


                              private void filterResistances() {
                                        List<ElementDraw> temp = new ArrayList<>(elements);
                                        temp.forEach(ele -> {
                                                  if ( ele instanceof ResistorDraw ) {
//                                                The first point
                                                            Node n1 = addPointGetNode(ele , ele.getStartPoint());
//                                                The second point
                                                            Node n2 = addPointGetNode(ele , ele.getEndPoint());
//                                                 Add new resistor wire
                                                            wires.add(new Branch(ele.getID() , n1 , n2 , ((ResistorDraw) ele).getResistance() , 0 , 0));
                                                            elements.remove(ele);
                                                  }
                                        });
                              }


                              private void filterVoltageSources() {
                                        List<ElementDraw> temp = new ArrayList<>(elements);
                                        temp.forEach(ele -> {
                                                  if ( ele instanceof VoltageSourceDraw ) {
//                                                The first point Place
                                                            String firstPointPlace = getPointPlace(ele , ele.getStartPoint());
//                                                The second point Place
                                                            String secondPointPlace = getPointPlace(ele , ele.getEndPoint());
//                                                The first node
                                                            Node startNode = points.get(firstPointPlace);
//                                                The second node
                                                            Node endNode = points.get(secondPointPlace);
//                                                The branch attached with current source
                                                            List<Integer> branchsIndeces = getBranchsByNode(startNode);
                                                            if ( branchsIndeces.size() > 1 ) {
                                                                      branchsIndeces = getBranchsByNode(endNode);
                                                                      for ( int index : branchsIndeces ) {
                                                                                wires.get(index).setNode(endNode , startNode);
                                                                                points.remove(secondPointPlace);
                                                                      }
                                                            } else {
                                                                      for ( int index : branchsIndeces ) {
                                                                                wires.get(index).setNode(startNode , endNode);
                                                                                points.remove(firstPointPlace);
                                                                      }
                                                            }
//                                                The direction of the voltage
                                                            if ( wires.get(branchsIndeces.get(0)).getStartPoint().equals(startNode)
                                                                    || wires.get(branchsIndeces.get(0)).getEndPoint().equals(endNode) ) {//         +
                                                                      wires.get(branchsIndeces.get(0)).setSourceVoltage(((VoltageSourceDraw) ele).getVolt());
                                                            } else if ( wires.get(branchsIndeces.get(0)).getStartPoint().equals(endNode) ) {//      -
                                                                      wires.get(branchsIndeces.get(0)).setSourceVoltage(-1 * ((VoltageSourceDraw) ele).getVolt());
                                                            }
                                                            elements.remove(ele);
                                                  }
                                        });
                              }
                    }.solve();
//                    System.out.println(CADProject.circuit.getGraph().getTree());
//                    System.out.println(CADProject.circuit.getGraph().getCoTree());
//                    print(CADProject.circuit.getGraph().getA());
//                    print(CADProject.circuit.getGraph().getAt());
//                    print(CADProject.circuit.getGraph().getAl());
//                    print(CADProject.circuit.getGraph().getB());
//                    print(CADProject.circuit.getGraph().getBt());
//                    print(CADProject.circuit.getGraph().getBl());
//                    print(CADProject.circuit.getGraph().getC());
//                    print(CADProject.circuit.getGraph().getCl());
//                    print(CADProject.circuit.getGraph().getCt());
          }


          @FXML
          void getA(ActionEvent event) {
                    CADProject.initMatrixStage("A" , circuit.getGraph().getA());
          }


          @FXML
          void getB(ActionEvent event) {
                    CADProject.initMatrixStage("Tie-Set" , circuit.getGraph().getB());
          }


          @FXML
          void getC(ActionEvent event) {
                    CADProject.initMatrixStage("Cut-Set" , circuit.getGraph().getC());
          }


          @Override

          public void initialize(URL url , ResourceBundle rb) {
                    try {
                              Parent nodes = FXMLLoader.load(getClass().getResource("..\\designs\\NodesFXML.fxml"));
                              nodesPane.getChildren().add(nodes);
                              skitch = Skitch.getInstance(CADProject.width , CADProject.gab);
                              skitch.getGraph().setTranslateX(-CADProject.width / 2);
                              skitch.getGraph().setTranslateY(-CADProject.width / 2);
                              skitchPane.getChildren().add(skitch.getGraph());
                    } catch ( IOException ex ) {
                              Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE , null , ex);
                    }
          }

}
