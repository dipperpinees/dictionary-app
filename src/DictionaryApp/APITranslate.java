package DictionaryApp;


import com.darkprograms.speech.translator.GoogleTranslate;
import com.google.api.translate.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class APITranslate {

    @FXML
    private TextArea TextEng;
    @FXML
    private Text TextVie;

    public void goHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent homeParent = loader.load();
        Scene homeScene = new Scene(homeParent);
        stage.setScene(homeScene);

    }

    public static String textTranslate(String languageTarget,String target) throws IOException {
        String trans = GoogleTranslate.translate(languageTarget,target);
        return trans;
    }

    public void TTranslate(ActionEvent event) throws IOException {
        String toVie = Language.VIETNAMESE.toString();
        String text0 = TextEng.getText().replaceAll("\n"," ");
        if (text0 == null || text0.length() == 0) {
            TextVie.setText("");
            return;
        }
        String text = textTranslate(toVie,text0);
        TextVie.setText(text);
    }
}
