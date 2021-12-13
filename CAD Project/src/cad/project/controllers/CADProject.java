/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package cad.project.controllers;

import Jama.Matrix;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**

 @author EMAM
 */
public class CADProject extends Application {

//          public static Circuit circuit;
          public static final double width = 2_000;
          public static final double gab = 30;
          public static int nodeIds = 1;
          public static char branchIds = 'a';

          public static Stage stage;


          @Override
          public void start(Stage s) throws Exception {
                    Skitch.getInstance(width , gab);
//                    Circuit.getInstance();

                    Parent root = FXMLLoader.load(getClass().getResource("..\\designs\\FXMLDocument.fxml"));

                    Scene scene = new Scene(root);
                    stage = s;
                    s.setScene(scene);
                    s.setResizable(false);
                    s.show();
          }


          public static void initMatrixStage(String title , Matrix m) {
                    initMatrixStage(title , m.getArrayCopy());
          }


          public static void initMatrixStage(String title , double[][] m) {
                    double w = m[0].length * 100;
                    double h = m.length * 100;
                    Stage stage = new Stage(StageStyle.TRANSPARENT);

                    GridPane grid = new GridPane();
                    grid.setBorder(new Border(new BorderStroke(null , BorderStrokeStyle.SOLID , CornerRadii.EMPTY , new BorderWidths(0 , 2 , 0 , 2))));
                    grid.setVgap(50);
                    grid.setHgap(50);
                    grid.setPrefSize(w , h);
                    grid.setAlignment(Pos.CENTER);
                    for ( int i = 0 ; i < m.length ; i++ ) {
                              for ( int j = 0 ; j < m[i].length ; j++ ) {
                                        Text t = new Text(m[i][j] + "");
                                        t.setFont(Font.font("Arial" , FontWeight.BOLD , 18));
                                        grid.add(t , j , i);
                              }
                    }

                    Label titleLabel = new Label(title);
                    titleLabel.setFont(Font.font("Arial" , FontWeight.EXTRA_BOLD , 25));
                    Text close = new Text("X");
                    close.setFill(Color.RED);
                    close.setOnMouseClicked((e) -> {
                              stage.close();
                    });
                    HBox par = new HBox(titleLabel , close);
                    par.setAlignment(Pos.CENTER_RIGHT);
                    par.setSpacing(w / 2);

                    VBox root = new VBox(50 , par , grid);
                    root.setAlignment(Pos.CENTER);
                    root.setPadding(new Insets(20));
                    Scene scene = new Scene(root , w * 1.2 , h * 1.2);
                    stage.setScene(scene);
                    stage.setTitle("title");
                    stage.setResizable(false);
                    stage.showAndWait();
          }


          /**
           @param args the command line arguments
           */
          public static void main(String[] args) {
                    launch(args);
          }

}
