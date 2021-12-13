/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.structures.graph;

import java.util.Objects;

/**

 @author EMAM
 */
public class Node implements Comparable<Node> {

          private final String ID;


          public Node(String ID) {
                    this.ID = ID;
          }


          public String getID() {
                    return ID;
          }


          @Override
          public boolean equals(Object o) {
                    if ( o == null ) {
                              return false;
                    }
                    if ( !(o instanceof Node) ) {
                              return false;
                    }
                    if ( o == this ) {
                              return true;
                    }
                    return ((Node) o).getID().equals(this.getID());
          }


          @Override
          public int compareTo(Node n) {
                    return this.getID().compareToIgnoreCase(n.getID());
          }


          @Override
          public int hashCode() {
                    int hash = 5;
                    hash = 37 * hash + Objects.hashCode(this.ID);
                    return hash;
          }


          @Override
          public String toString() {
                    return "(" + getID() + ")";
          }

}
