import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

  private TextField directoryPathField;
  private TextField searchField;
  private TextArea resultArea;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("File Browser and Search");

    directoryPathField = new TextField();
    directoryPathField.setPromptText("Enter directory path");

    searchField = new TextField();
    searchField.setPromptText("Enter search phrase");

    resultArea = new TextArea();
    resultArea.setPrefHeight(400);

    Button searchButton = new Button("Search");
    searchButton.setOnAction(e -> searchFiles());
    Button browseButton = new Button("Browse");
    browseButton.setOnAction(e -> browseDirectory());

    HBox hBox = new HBox(10, directoryPathField, browseButton);
    VBox vBox = new VBox(10, hBox, searchField, searchButton, resultArea);

    Scene scene = new Scene(vBox, 600, 200);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void browseDirectory() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(null);
    if (selectedDirectory != null) {
      directoryPathField.setText(selectedDirectory.getAbsolutePath());
    }
  }

  private void searchFiles() {
    String directoryPath = directoryPathField.getText();
    String searchPhrase = searchField.getText();

    if (directoryPath.isEmpty()) {
      resultArea.setText("Please provide a directory path.");
      return;
    }

    File directory = new File(directoryPath);
    if (!directory.isDirectory()) {
      resultArea.setText("The provided path is not a directory.");
      return;
    }

    StringBuilder results = new StringBuilder();
    searchInDirectory(directory, searchPhrase, results);
    resultArea.setText(results.toString());
  }

  private void searchInDirectory(File directory, String searchPhrase, StringBuilder results) {
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          searchInDirectory(file, searchPhrase, results);
        } else {
          if (containsPhrase(file, searchPhrase)) {
            results.append(file.getAbsolutePath()).append("\n");
          }
        }
      }
    }
  }

  private boolean containsPhrase(File file, String searchPhrase) {
    try {
      String filename = file.getName();
      System.out.println(filename + " contains " + searchPhrase + ": " + filename.contains(searchPhrase));
      return filename.contains(searchPhrase);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}