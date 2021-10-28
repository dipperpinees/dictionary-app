package DictionaryApp;

import DicCommandline.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class homeController implements Initializable {
    @FXML
    private TextField text;
    @FXML
    private Text engLabel;
    @FXML
    private Text vieLabel;
    @FXML
    private ListView<String> listView;

    @FXML
    private Button changeButton = new Button();     //button change explain
    @FXML
    private Button changeButton2 = new Button();    //button delete
    @FXML
    private Button changeButton3 = new Button();    //button speak

    private ArrayList<String> listEng = new ArrayList<String>();

    private ArrayList<String> listVie = new ArrayList<String>();

    private DictionaryManagement dic = new DictionaryManagement() ;
    //private ActionEvent event;
    private String currentWord;
    private VoiceGG voiceGG = new VoiceGG("kevin16");

    public void initDataHome(DictionaryManagement dic) {
        this.dic = dic;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        changeButton.setVisible(false);
        changeButton2.setVisible(false);
        changeButton3.setVisible(false);
        dic.insertTxt();
        listEng = getAllWord();
        listView.getItems().setAll(listEng);

        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() != 0) {
                listView.getItems().clear();
                vieLabel.setText("");
                engLabel.setText("");
                listView.getItems().setAll(LookUpWord(newValue));
            } else {
                listView.getItems().setAll(listEng);
            }
            changeButton.setVisible(false);
            changeButton2.setVisible(false);
            changeButton3.setVisible(false);
        });
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null) {
                    currentWord = listView.getSelectionModel().getSelectedItem();
                    if (currentWord.equals("Not Found")) {
                        vieLabel.setText("Không có thông tin");
                        engLabel.setText("Not Found");
                    } else {
                        ArrayList<String> wordInfo = getExplain(currentWord);
                        engLabel.setText(currentWord + " " + wordInfo.get(1));
                        vieLabel.setText(wordInfo.get(0));
                    }
                    changeButton.setVisible(true);
                    changeButton2.setVisible(true);
                    changeButton3.setVisible(true);
                }
            }
        });
    }

    /** Lấy nghĩa của từ. */
    public ArrayList<String> getExplain (String target) {
        ArrayList<String> newList = new ArrayList<String>();
        if(target == null || target.length() == 0) return newList;
        for (int i = 0; i < dic.getDictionnary().getWordsList().size(); i++) {
            if (dic.getDictionnary().getWordsList().get(i).getWordTarget().equals(target)) {
                newList.add(dic.getDictionnary().getWordsList().get(i).getWordExplain());
                newList.add(dic.getDictionnary().getWordsList().get(i).getWordPronounce());
                return newList;
            }
        }
        return newList;
    }

    /** Lấy tất cả từ trong file từ điển. */
    public ArrayList<String> getAllWord() {
        ArrayList<String> newList = new ArrayList<String>();
        for (int i = 0; i<dic.getDictionnary().getWordsList().size(); i++) {
            newList.add(dic.getDictionnary().getWordsList().get(i).getWordTarget());
        }
        return newList;
    }

    /** Tìm kiếm từ. */
    public ArrayList<String> LookUpWord (String word) {
        ArrayList<String> listWord = new ArrayList<String>();

        if(word == null) {
            return listWord;
        }

        boolean check = false;
        for(int i = 0; i <dic.getDictionnary().getWordsList().size(); i++) {
            if(dic.getDictionnary().getWordsList().get(i).getWordTarget().indexOf(word) == 0) {
                listWord.add(dic.getDictionnary().getWordsList().get(i).getWordTarget());
                check = true;
            }
        }
        if(!check) {
            listWord.add("Not Found");
        }
        return listWord;
    }

    /** Đổi sang scene thêm từ. */
    public void gotoAddWord(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addWord.fxml"));
        Parent addWord = loader.load();
        Scene AddScene = new Scene(addWord);
        addWordController addController = loader.<addWordController>getController();
        addController.initData(dic);
        stage.setScene(AddScene);
    }

    /** Đổi sang scene dịch. */
    public void gotoTextTranslation(ActionEvent event1) throws IOException {
        Stage stage1 = (Stage) ((Node) event1.getSource()).getScene().getWindow();
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("GGTranslate.fxml"));
        Parent trans = loader1.load();
        Scene TransScene = new Scene(trans);
        stage1.setScene(TransScene);
    }

    /** Thay đổi nghĩa. */
    public void changeExplain() throws IOException  {

        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Sửa nghĩa từ");
        dialog.setHeaderText("Sửa nghĩa từ: " + engLabel.getText());
        dialog.setContentText("Nghĩa");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            if (name == null || name.length() == 0) {
                showAlert("Nghĩa mới không hợp lệ");
                return;
            }
            boolean check = dic.changeWordExplain(name, this.currentWord);
            vieLabel.setText(name);
            showAlert("Đổi nghĩa thành công");
            if (check) {
                try {
                    dic.dictionaryExportToFile();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        });
    }

    public void showAlert(String newAlert)  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText(newAlert);

        alert.showAndWait();
    }

    /** Xóa từ. */
    public void deleteWord(ActionEvent e) throws IOException  {
        boolean check = dic.removeCurrentWord(currentWord);
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        showAlert("xoá thành công");
        if (check) {
            try {
                dic.dictionaryExportToFile();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    /** Phát âm. */
    public void speak(ActionEvent event) {
        String target = listView.getSelectionModel().getSelectedItem();
        String targetRes = target.replace('-',' ');
        voiceGG.say(targetRes);
    }


}