/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.graph;

/**

 @author EMAM
 */
public class Branch {

          private final String ID;
          private Node startPoint;
          private Node endPoint;

          private final double resistance;
          private double sourceCurrent;
          private double sourceVoltage;


          public Branch(String ID , Node startPoint , Node endPoint , double resistance , double current , double voltage) {
                    this.ID = ID;
                    this.startPoint = startPoint;
                    this.endPoint = endPoint;
                    this.resistance = resistance;
                    this.sourceCurrent = current;
                    this.sourceVoltage = voltage;
          }


          public double getSourceCurrent() {
                    return sourceCurrent;
          }


          public void setSourceCurrent(double sourceCurrent) {
                    this.sourceCurrent = sourceCurrent;
          }


          public Node getEndPoint() {
                    return endPoint;
          }


          public void setEndPoint(Node endPoint) {
                    this.endPoint = endPoint;
          }


          public String getID() {
                    return ID;
          }


          public double getResistance() {
                    return resistance;
          }


          public Node getStartPoint() {
                    return startPoint;
          }


          public void setStartPoint(Node startPoint) {
                    this.startPoint = startPoint;
          }


          public double getSourceVoltage() {
                    return sourceVoltage;
          }


          public void setSourceVoltage(double sourceVoltage) {
                    this.sourceVoltage = sourceVoltage;
          }


          public void setNode(Node node1 , Node node2) {
                    if ( this.getStartPoint().equals(node1) ) {
                              System.out.println(getStartPoint().getID());
                              System.out.println(node2.getID());
                              setStartPoint(node2);
                              System.out.println(getStartPoint().getID());
                    } else if ( this.getEndPoint().equals(node1) ) {
                              setEndPoint(node2);
                    }
          }


          @Override
          public String toString() {
                    return "\"" + getID() + "\" : " + getStartPoint() + " >> " + getEndPoint();
          }

}
