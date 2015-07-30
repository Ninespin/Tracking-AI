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
package UI.fx;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 * A class that Replicates JOpionPane usability, while using javafx, for
 * coolness (and no crash when using JOpionPane on javafx apps)
 *
 * @author Arnaud Paré-Vogt
 */
public class FXOptionPane {

    /**
     * Shows a small window ressembling a
     * <code>JOptionPane.showInputDialog</code>, but looking WAY cooler.
     *
     * @param title the title of the window
     * @param header the header of the message
     * @param message The message to the user
     * @param options Button types representing the options.
     * @return The id of the button, of -1 if cancel was clicked of the window
     * was closed.The id depends of the order in witch they were provided. For
     * instance, if the options are {ok,maybe,shure}, and the user clicks
     * 'maybe', 1 will be returned.
     */
    public static int showOptionDialog(String title, String header, String message, ButtonType... options) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        ButtonType[] allOptions = new ButtonType[options.length + 1];

        System.arraycopy(options, 0, allOptions, 0, options.length);

        allOptions[allOptions.length - 1] = buttonTypeCancel;
        alert.getButtonTypes().setAll(allOptions);

        Optional<ButtonType> result = alert.showAndWait();
        for (int i = 0; i < options.length; i++) {
            if (result.get() == options[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Shows a small window ressembling a
     * <code>JOptionPane.showInputDialog</code>, but looking WAY cooler.
     *
     * @param title the title of the window
     * @param header the header of the message
     * @param message The message to the user
     * @param options Strings representing the options names.
     * @return The id of the button, of -1 if cancel was clicked of the window
     * was closed.The id depends of the order in witch they were provided. For
     * instance, if the options are {ok,maybe,shure}, and the user clicks
     * 'maybe', 1 will be returned.
     */
    public static int showOptionDialog(String title, String header, String message, String ... options) {
        ButtonType[] bOptions = new ButtonType[options.length];
        for (int i = 0; i < options.length; i++) {
            bOptions[i] = new ButtonType(options[i]);
        }
        
        return showOptionDialog(title, header, message, bOptions);
    }
}
