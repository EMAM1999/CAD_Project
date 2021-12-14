/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.graph;

import java.util.List;

/**

 @author EMAM
 */
public class Main {

          public static void main(String[] args) {
                    Node n1 = new Node("1");
                    Node n2 = new Node("2");
                    Node n3 = new Node("3");

                    Branch a = new Branch("a" , n1 , n2 , 10 , 0 , 0);
                    Branch b = new Branch("b" , n2 , n3 , 5 , 0 , 0);
                    Branch c = new Branch("c" , n1 , n3 , 5 , 0 , 0);
                    Branch d = new Branch("d" , n3 , n1 , 5 , 0 , 10);

//                    new Matrix(new double[][] {
//                              {7,-5} ,
//                              { -5,9 } 
//                    }).solve(new Matrix(new double[][] {
//                              { 0 } ,
//                              { 10 } 
//                    })).print(5 , 3);
                    Circuit circuit = Circuit.getInstance(a , b , c , d);
                    System.out.println("Tree");
                    System.out.println(circuit.getGraph().getTree());
                    System.out.println("CoTree");
                    System.out.println(circuit.getGraph().getCoTree());
//                    System.out.println("At");
//                    g.getGraph().getAt().print(5 , 2);
//                    System.out.println("Al");
//                    g.getGraph().getAl().print(5 , 2);
//                    System.out.println("A");
//                    circuit.getGraph().getA().print(5 , 2);
//                    System.out.println("Bt");
//                    g.getGraph().getBt().print(5 , 2);
//                    System.out.println("Bl");
//                    g.getGraph().getBl().print(5 , 2);
//                    System.out.println("B");
//                    circuit.getGraph().getB().print(5 , 2);
//                    System.out.println("Ct");
//                    g.getGraph().getCt().print(5 , 2);
//                    System.out.println("Cl");
//                    g.getGraph().getCl().print(5 , 2);
//                    System.out.println("C");
//                    circuit.getGraph().getC().print(5 , 2);
//                    System.out.println("Ib");
//                    circuit.getGraph().getIb().print(5 , 2);
//                    System.out.println("Eb");
//                    circuit.getGraph().getEb().print(5 , 2);
//                    System.out.println("Zb");
//                    circuit.getGraph().getZb().print(5 , 2);
//                    System.out.println("Il");
//                    circuit.getGraph().getIl().print(5 , 2);
                    System.out.println("Jb");
                    double[][] m = circuit.getGraph().getJb().getArrayCopy();
                    print(circuit.getGraph() , m);

                    System.out.println("Vb");
                    m = circuit.getGraph().getVb().getArrayCopy();
                    print(circuit.getGraph().getBranchs() , circuit.getGraph().getLinks() , m);

          }


          public static void print(List<Branch> branchs , List<Branch> links , double[][] num) {
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


          public static void print(Circuit.Graph graph , double[][] m) {
                    print(graph.getBranchs() , graph.getLinks() , m);
          }

}
