/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.graph;

import Jama.Matrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**

 @author EMAM
 */
public final class Circuit {

          private List<Branch> wires;
          private List< Node> nodes;
          private Graph graph;

          private static Circuit instance;

//
//          private Circuit(Branch... b) {
//                    this(new ArrayList<>(Arrays.asList(b)));
//          }
//

          private Circuit(List<Branch> b) {
                    setWires(b);
          }


          public static Circuit getInstance(Branch... b) {
                    return getInstance(new ArrayList<>(Arrays.asList(b)));
          }


          public static Circuit getInstance(List<Branch> b) {
                    if ( instance == null ) {
                              instance = new Circuit(b);
                    }
                    return instance;
          }


          public void addWire(Branch b) {
                    this.wires.add(b);
                    initNodes(this.wires);
                    initTree();
          }


          public void setWires(List<Branch> wires) {
                    this.wires = wires;
                    initNodes(wires);
                    initTree();
          }


          public Graph getGraph() {
                    return this.graph;
          }


          @Override
          public String toString() {
                    return super.toString();
          }


          private void initTree() {
                    this.graph = new Graph(wires , nodes);
          }


          public List<Node> getNodes() {
                    return nodes;
          }


          public void setNodes(List<Node> nodes) {
                    this.nodes = nodes;
          }


          public List<Branch> getWires() {
                    return wires;
          }


          private void initNodes(List<Branch> branchs) {
                    Set<Node> nodesSet = new HashSet();
                    branchs.stream().map((branch) -> {
                              nodesSet.add(branch.getStartPoint());
                              return branch;
                    }).forEachOrdered((branch) -> {
                              nodesSet.add(branch.getEndPoint());
                    });
                    this.nodes = new ArrayList<>(nodesSet);
                    this.nodes.sort(Node::compareTo);

          }

          public final class Graph {

                    private final List<Branch> branchs;
                    private final List<Branch> links;
                    private final List<Node> nodes;

                    private Matrix At;
                    private Matrix Al;
                    private Matrix A;
                    private Matrix C;
                    private Matrix Cl;
                    private Matrix Ct;
                    private Matrix B;
                    private Matrix Bt;
                    private Matrix Bl;
                    private Matrix Ib;
                    private Matrix Il;
                    private Matrix Jb;
                    private Matrix Eb;
                    private Matrix Vb;
                    private Matrix Zb;


                    public Graph(List<Branch> lines , List<Node> nodes) {
                              this.branchs = lines;
                              this.links = copy(lines);
                              this.nodes = nodes;
                              init();
                    }


                    public Matrix getA() {
                              return A;
                    }


                    public Matrix getAl() {
                              return Al;
                    }


                    public Matrix getAt() {
                              return At;
                    }


                    public Matrix getB() {
                              return B;
                    }


                    public Matrix getBl() {
                              return Bl;
                    }


                    public List<Branch> getBranchs() {
                              return new ArrayList<>(branchs);
                    }


                    public Matrix getBt() {
                              return Bt;
                    }


                    public Matrix getC() {
                              return C;
                    }


                    public Matrix getCl() {
                              return Cl;
                    }


                    public Matrix getCt() {
                              return Ct;
                    }


                    public Matrix getEb() {
                              return Eb;
                    }


                    public Matrix getIb() {
                              return Ib;
                    }


                    public Matrix getIl() {
                              return Il;
                    }


                    public Matrix getJb() {
                              return Jb;
                    }


                    public List<Branch> getLinks() {
                              return new ArrayList<>(links);
                    }


                    public List<Branch> getTree() {
                              return this.branchs;
                    }


                    public List<Branch> getCoTree() {
                              return this.links;
                    }


                    public Matrix getVb() {
                              return Vb;
                    }


                    public Matrix getZb() {
                              return Zb;
                    }


                    private Matrix concat(Matrix matrix1 , Matrix matrix2) {
                              double[][] m1 = matrix1.getArrayCopy();
                              double[][] m2 = matrix2.getArrayCopy();
                              double[][] m = new double[m1.length][m1[0].length + m2[0].length];
                              for ( int i = 0 ; i < m.length ; i++ ) {
                                        System.arraycopy(m1[i] , 0 , m[i] , 0 , m1[0].length);
                                        System.arraycopy(m2[i] , 0 , m[i] , m1[0].length , m2[0].length);
                              }
                              return new Matrix(m);
                    }


                    private List<Branch> copy(List<Branch> lines) {
                              return new ArrayList<>(lines);
                    }


                    private Branch getRandomBranch() {
                              return this.branchs.get((int) (Math.random() * branchs.size()));
                    }


                    public void initA() {
                              this.A = concat(getAt() , getAl());
                    }


                    public void initAl() {
                              List<Node> nodes = getNodes();
                              List<Branch> coTree = getCoTree();

                              double[][] Al = new double[nodes.size() - 1][coTree.size()];
                              for ( int i = 0 ; i < coTree.size() ; i++ ) {
                                        for ( int j = 0 ; j < nodes.size() - 1 ; j++ ) {
                                                  Branch thisBranch = coTree.get(i);
                                                  Node thisNode = nodes.get(j);
                                                  Node sp = thisBranch.getStartPoint();
                                                  Node ep = thisBranch.getEndPoint();
                                                  Al[j][i] = (thisNode.equals(sp)
                                                          ? 1 : thisNode.equals(ep)
                                                          ? -1 : 0);
                                        }
                              }
                              this.Al = new Matrix(Al);
                    }


                    public void initAt() {
                              List<Node> nodes = getNodes();
                              List<Branch> tree = getTree();

                              double[][] At = new double[nodes.size() - 1][tree.size()];
                              for ( int i = 0 ; i < tree.size() ; i++ ) {
                                        for ( int j = 0 ; j < nodes.size() - 1 ; j++ ) {
                                                  Branch thisBranch = tree.get(i);
                                                  Node thisNode = nodes.get(j);
                                                  Node sp = thisBranch.getStartPoint();
                                                  Node ep = thisBranch.getEndPoint();
                                                  At[j][i] = (thisNode.equals(sp)
                                                          ? 1 : thisNode.equals(ep)
                                                          ? -1 : 0);
                                        }
                              }
                              this.At = new Matrix(At);
                    }


                    public void initB() {
                              this.B = concat(getBt() , getBl());
                    }


                    public void initBl() {
                              int n = links.size();
                              this.Bl = Matrix.identity(n , n);
                    }


                    public void initBt() {
                              this.Bt = getCl().copy().uminus().transpose();
                    }


                    public void initC() {
                              this.C = concat(getCt() , getCl());
                    }


                    public void initCl() {
//                              this.Cl = new Matrix(new double[][] {
//                                        { 1.00 , 0.00 , 0.00 } ,
//                                        { -1.00 , 1.00 , 0.00 } ,
//                                        { 0.00 , 0.00 , 1.00 }
//                              }).inverse().times(
//                                      new Matrix(new double[][] {
//                                        { -1.00 , 0.00 , -1.00 } ,
//                                        { 0.00 , 1.00 , 0.00 } ,
//                                        { 1.00 , -1.00 , 0.00 }
//                              }));
                              this.Cl = getAt().inverse().times(getAl());
                    }


                    public void initCt() {
                              int n = branchs.size();
                              this.Ct = Matrix.identity(n , n);
                    }


                    private void init() {
//                              Tree -> CoTree 
//                              Bl, Ct, 
//                              (At, Al) -> (A, Cl, /Ct\) -> (C ,Bt, /Bl\) -> B 
//                              (Ib, Zb, Eb) -> Il -> Jb -> Vb
                              initTreeAndCoTree();
                              Thread m = new Thread(() -> {
                                        initAt();
                                        initAl();
                                        initA();
                                        initCl();
                                        initCt();
                                        initC();
                                        initBt();
                                        initBl();
                                        initB();
                              });
                              Thread values = new Thread(() -> {
                                        initIb();
                                        initZb();
                                        initEb();
                              });
                              m.start();
                              values.start();
//                              try {
//                                        m.join();
//                                        values.join();
//                              } catch ( InterruptedException ex ) {
//                              }
                              while ( m.isAlive() || values.isAlive() ) {
                              }
                              initIl();
                              initJb();
                              initVb();
                    }


                    private void initIb() {
                              int m = this.branchs.size() + this.links.size();
                              double[][] matrix = new double[m][1];
                              int i = 0;
                              for ( Branch ele : this.branchs ) {
                                        matrix[i++][0] = ele.getSourceCurrent();
                              }
                              for ( Branch ele : this.links ) {
                                        matrix[i++][0] = ele.getSourceCurrent();
                              }
                              this.Ib = new Matrix(matrix);
                    }


                    private void initEb() {
                              int m = this.branchs.size() + this.links.size();
                              double[][] matrix = new double[m][1];
                              int i = 0;
                              for ( Branch ele : this.branchs ) {
                                        matrix[i++][0] = ele.getSourceVoltage();
                              }
                              for ( Branch ele : this.links ) {
                                        matrix[i++][0] = ele.getSourceVoltage();
                              }
                              this.Eb = new Matrix(matrix);
                    }


                    private void initIl() {
                              Matrix leftSide
                                      = getB().times(
                                              getZb().times(
                                                      getB().transpose()));
                              Matrix rightSide
                                      = getB().times(getEb())
                                              .minus(
                                                      getB().times(
                                                              getZb()
                                                                      .times(getIb())));
                              this.Il = leftSide.solve(rightSide);
                    }


                    private void initJb() {
                              this.Jb = getB().transpose().times(getIl());
                    }


                    private void initVb() {
                              this.Vb = getZb().times(getJb().plus(getIb())).minus(getEb());
                    }


                    private void initZb() {
                              int m = this.branchs.size() + this.links.size();
                              double[][] matrix = new double[m][m];
                              int i = 0;
                              for ( Branch ele : this.branchs ) {
                                        matrix[i][i++] = ele.getResistance();
                              }
                              for ( Branch ele : this.links ) {
                                        matrix[i][i++] = ele.getResistance();
                              }
                              this.Zb = new Matrix(matrix);
                    }


                    private void initTreeAndCoTree() {
                              List<Branch> branchs = new ArrayList<>();

                              Node[] nodes = new Node[this.nodes.size()];
                              Branch b;
                              Node n1;
                              Node n2;

                              b = getRandomBranch();
                              n1 = b.getStartPoint();
                              n2 = b.getEndPoint();
                              nodes[0] = n1;
                              nodes[1] = n2;
                              branchs.add(b);

                              for ( int i = 2 ; i < nodes.length ; ) {
                                        b = getRandomBranch();
                                        n1 = b.getStartPoint();
                                        n2 = b.getEndPoint();
                                        if ( isExist(nodes , n1) ) {
                                                  if ( !isExist(nodes , n2) ) {
                                                            nodes[i] = n2;
                                                            branchs.add(b);
                                                            i++;
                                                  }
                                        } else {
                                                  if ( isExist(nodes , n2) ) {
                                                            nodes[i] = n1;
                                                            branchs.add(b);
                                                            i++;
                                                  }
                                        }
                              }
                              this.links.removeAll(branchs);
                              this.branchs.removeAll(this.links);
                    }


                    private boolean isExist(Node[] nodes , Node n) {
                              for ( Node ele : nodes ) {
                                        if ( n.equals(ele) ) {
                                                  return true;
                                        }
                              }
                              return false;
                    }


                    @Override
                    public String toString() {
                              return getWires().toString();
                    }

          }

}
