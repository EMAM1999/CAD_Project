/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.controllers;

import cad.project.files.Data;
import cad.project.structures.drawings.CurrentSourceDraw;
import cad.project.structures.drawings.ElementDraw;
import cad.project.structures.drawings.PointDraw;
import cad.project.structures.drawings.ResistorDraw;
import cad.project.structures.drawings.VoltageSourceDraw;
import cad.project.structures.graph.Branch;
import cad.project.structures.graph.Circuit;
import cad.project.structures.graph.Main;
import cad.project.structures.graph.Node;
import com.jfoenix.controls.JFXButton;
import java.io.File;
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
import javafx.stage.*;
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
          private JFXButton JbBtn;
          @FXML
          private JFXButton VbBtn;

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
          private void reset(MouseEvent event) throws IOException {
                    int c = JOptionPane.showConfirmDialog(null , "Do you want to save the drawing !?");
                    System.out.println(c);
                    switch ( c ) {
                              default: break;
                              case JOptionPane.YES_OPTION:
                                        save();
                              case JOptionPane.NO_OPTION:
                                        skitch.deleteAllElements();
                                        BBtn.setDisable(true);
                                        CBtn.setDisable(true);
                                        JbBtn.setDisable(true);
                                        VbBtn.setDisable(true);
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


                              private void setCurrentsVoltageValuesOnTheWires() {
                                        List<ElementDraw> elements = skitch.getDrawingsElements();
                                        for ( ElementDraw ele : elements ) {
                                                  if ( ele instanceof ResistorDraw ) {

                                                  }

                                        }
                              }


                              private void solve() {
                                        wires = new ArrayList<>();
                                        points = new HashMap<>();
                                        elements = new ArrayList<>(skitch.getDrawingsElements());
                                        filterResistances();
                                        filterCurrentSources();
                                        filterVoltageSources();
                                        circuit = Circuit.getInstance(wires);

//                                        Print the matrices
                                        System.out.println("Jb");
                                        double[][] m = circuit.getGraph().getJb().getArrayCopy();
                                        Main.print(circuit.getGraph() , m);
                                        System.out.println("Vb");
                                        m = circuit.getGraph().getVb().getArrayCopy();
                                        Main.print(circuit.getGraph().getBranchs() , circuit.getGraph().getLinks() , m);

//                                        Set The values on the drawn
                                        setCurrentsVoltageValuesOnTheWires();
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
                    ABtn.setDisable(false);
                    BBtn.setDisable(false);
                    CBtn.setDisable(false);
                    JbBtn.setDisable(false);
                    VbBtn.setDisable(false);
          }


          @FXML
          private void getA(ActionEvent event) {
                    CADProject.initMatrixStage("A" , circuit.getGraph().getA());
          }


          @FXML
          private void getB(ActionEvent event) {
                    CADProject.initMatrixStage("Tie-Set" , circuit.getGraph().getB());
          }


          @FXML
          private void getC(ActionEvent event) {
                    CADProject.initMatrixStage("Cut-Set" , circuit.getGraph().getC());
          }


          @FXML
          private void getJb(ActionEvent event) {
                    CADProject.initMatrixStage("Current in branches" , circuit.getGraph().getJb());
          }


          @FXML
          private void getVb(ActionEvent event) {
                    CADProject.initMatrixStage("Voltage in branches" , circuit.getGraph().getVb());
          }


          @FXML
          private void externalFile(ActionEvent event) throws ClassNotFoundException , IOException {
                    FileChooser chooser = new FileChooser();
                    File f = chooser.showOpenDialog(CADProject.stage);
                    List<ElementDraw> elements = Data.read(f);
                    elements.forEach((ele) -> {
                              skitch.addElement(ele);
                    });
          }


          @FXML
          void save(ActionEvent event) throws IOException {
                    save();
          }


          private void save() throws IOException {
                    FileChooser chooser = new FileChooser();
                    File f = chooser.showSaveDialog(CADProject.stage);
                    Data.write(f , skitch.getDrawingsElements());
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
