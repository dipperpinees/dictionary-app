package DictionaryApp;

import DicCommandline.DictionaryManagement;
import DicCommandline.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class addWordController {
    private DictionaryManagement dic;

    @FXML
    private TextField engText;
    @FXML
    private TextField vieText;
    @FXML
    private TextField proText;

    public void initData(DictionaryManagement dic) {
        this.dic = dic;
    }


    /** quay lại scene home. */
    public void goHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent homeParent = loader.load();
        Scene homeScene = new Scene(homeParent);
        homeController home = loader.<homeController>getController();
        home.initDataHome(dic);
        stage.setScene(homeScene);

    }

    /** Thêm từ vào list Word và file. */
    public void addWo(ActionEvent event) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String eng = engText.getText();
        String vie = vieText.getText();
        String pronounce = proText.getText();
        Alert alertFalse = new Alert(Alert.AlertType.WARNING);
        alertFalse.setTitle("Không thành công");
        alertFalse.setHeaderText("Từ đã có hoặc từ không hợp lệ");
        boolean check = false;
        for (int i=0;i<dic.getDictionnary().getWordsList().size();i++) {
            if (dic.getDictionnary().getWordsList().get(i).getWordTarget().equals(eng)) {
                check = true;
                break;
            }
        }
        if (check) {
            alertFalse.show();
            return;
        } else if (eng != null && vie != null && eng.length() * vie.length() != 0) {
                if (pronounce == null) {
                    pronounce = "";
                }
                pronounce = "/" + pronounce + "/";
                String s = "\n@" + eng + " " + pronounce + "\n";
                try {
                    fw = new FileWriter("src/AnhViet.txt", true);
                    bw = new BufferedWriter(fw);
                    bw.write(s);
                    bw.write(vie);
                    bw.write("\n");
                    bw.close();
                    fw.close();
                } catch (Exception ex) {

                }
                dic.getDictionnary().getWordsList().add(new Word(eng,vie,pronounce));
                Alert alertTrue = new Alert(Alert.AlertType.INFORMATION);
                alertTrue.setTitle("Thông tin");
                alertTrue.setHeaderText("Thêm thành công");
                alertTrue.show();
                return;
            }
        alertFalse.show();
    }
}
