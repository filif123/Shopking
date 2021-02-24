package sk.shopking.tools;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class FXMLTools {

    /**
     * Zavrie aktualne okno
     * @param event event na zistenie okna
     */
    public static void closeWindow(Event event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public static void closeWindow(Control control){
        Stage stage = (Stage) control.getScene().getWindow();
        stage.close();
    }

    public static AlertResult showAlert(String title, String header, String content, Alert.AlertType type, Style style, StageStyle stageStyle){
        Alert alert = new Alert(type);
        new JMetro(alert.getDialogPane().getScene(), style);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(stageStyle);

        Optional<ButtonType> result = alert.showAndWait();
        alert.close();

        if (result.isPresent()){
            if (result.get() == ButtonType.APPLY)
                return AlertResult.APPLY;
            else if (result.get() == ButtonType.CANCEL)
                return AlertResult.CANCEL;
            else if (result.get() == ButtonType.CLOSE)
                return AlertResult.CLOSE;
            else if (result.get() == ButtonType.FINISH)
                return AlertResult.FINISH;
            else if (result.get() == ButtonType.NEXT)
                return AlertResult.NEXT;
            else if (result.get() == ButtonType.YES)
                return AlertResult.YES;
            else if (result.get() == ButtonType.NO)
                return AlertResult.NO;
            else if (result.get() == ButtonType.PREVIOUS)
                return AlertResult.PREVIOUS;
            else if (result.get() == ButtonType.OK)
                return AlertResult.OK;
            else
                return AlertResult.NONE;
        }
        else {
            return AlertResult.NONE;
        }
    }

    public static AlertResult showAlert(String content){
        return showAlert("Message", "", content, Alert.AlertType.NONE, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String content, Alert.AlertType type){
        return showAlert("Message", "", content, type, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String content, Alert.AlertType type,Style style){
        return showAlert("Message", "", content, type, style, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String content, Alert.AlertType type,Style style,StageStyle stageStyle){
        return showAlert("Message", "", content, type, style, stageStyle);
    }

    public static AlertResult showAlert(String title,String content){
        return showAlert(title, "", content, Alert.AlertType.NONE, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String title, String content, Alert.AlertType type){
        return showAlert(title, "", content, type, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String title, String content, Alert.AlertType type,Style style){
        return showAlert(title, "", content, type, style, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String title, String content, Alert.AlertType type, Style style, StageStyle stageStyle){
        return showAlert(title, "", content, type, style, stageStyle);
    }

    public static AlertResult showAlert(String title, String header, String content){
        return showAlert(title, header, content, Alert.AlertType.NONE, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String title, String header, String content, Alert.AlertType type){
        return showAlert(title, header, content, type, Style.LIGHT, StageStyle.DECORATED);
    }

    public static AlertResult showAlert(String title, String header, String content, Alert.AlertType type,Style style){
        return showAlert(title, header, content, type, style, StageStyle.DECORATED);
    }

    public static void showExceptionAlert(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Zistená výnimka");
        alert.setHeaderText("Bola zachytená výnimka");
        alert.setContentText("");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Znenie výnimky:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}

