/*
 * The MIT License
 *
 * Copyright 2015 Eloi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package UI.fx.controller;

import UI.fx.FXDisplay;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import application.TrackingAIController;
import java.io.File;
import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import remote.controller.RemoteController;

/**
 * FXML Controller class
 *
 * @author Arnaud Paré-Vogt
 */
public class FXMLMainGuiController implements Initializable {

    @FXML
    private Button goButton;

    @FXML
    private Button chooseTemplateButton;
    @FXML
    private Button chooseImageButton;

    @FXML
    private Label templatePathLabel;
    @FXML
    private Label imagePathLabel;
    
    @FXML
    private ImageView display;
    
    @FXML
    private ScrollPane displayView;
    
    @FXML
    private CheckBox checkShowMatch;
    @FXML
    private CheckBox checkPaintImage;
    @FXML
    private CheckBox checkEmphasizeShapes;
    @FXML
    private CheckBox checkAutoResize;

    private TrackingAIController controller;

    private Stage primaryStage;

    private final String TEMPLATE_PATH_PREFIX = "Template Path : ";
    private final String IMAGE_PATH_PREFIX = "Image Path : ";

    /**
     * Initializes the controller class.
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initController();

        goButton.setOnAction((ActionEvent ae) -> {
            if (controller.init()) {
                controller.start();
            }
        });

        chooseTemplateButton.setOnAction((ActionEvent ae) -> {
            chooseTemplateButtonAction(ae);
        });

        chooseImageButton.setOnAction((ActionEvent ae) -> {
            chooseImageButtonAction(ae);
        });
        
        checkShowMatch.setOnAction((ActionEvent ae)->{
            controller.getDisplay().setMatchStringVisible(checkShowMatch.isSelected());
        });
        
        checkPaintImage.setOnAction((ActionEvent ae)->{
            controller.getDisplay().setPaintOriginal(checkPaintImage.isSelected());
        });
        
        checkEmphasizeShapes.setOnAction((ActionEvent ae)->{
            controller.getDisplay().setEmphasis(checkEmphasizeShapes.isSelected());
        });
        
        checkAutoResize.setOnAction((ActionEvent ae)->{
            controller.getDisplay().setAutoResizeImage(checkAutoResize.isSelected());
        });
    }

    public void initController() {
        FXDisplay dis = new FXDisplay(display, displayView);
        controller = new TrackingAIController("E:\\Documents\\ArnaudDossiers\\Prog\\Templates\\temp.jpg", "E:\\Documents\\ArnaudDossiers\\Prog", new RemoteController(),dis);
    }

    public void setController(TrackingAIController controller) {
        this.controller = controller;
    }

    public void closeProgram() {
        controller.stop();
        Platform.exit();
        System.exit(0);
    }

    //-- Action methods
    private void chooseTemplateButtonAction(ActionEvent ae) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Template Image", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            controller.setTemplatePath(selectedFile.getPath());
            templatePathLabel.setText(TEMPLATE_PATH_PREFIX + selectedFile.getPath());
        }
    }

    private void chooseImageButtonAction(ActionEvent ae) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select folder containing images");
        File selectedDirectory = chooser.showDialog(primaryStage);
        if (selectedDirectory != null) {
            controller.setImagePath(selectedDirectory.getPath());
            imagePathLabel.setText(TEMPLATE_PATH_PREFIX + selectedDirectory.getPath());
        }
    }

    //setters and getters
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
