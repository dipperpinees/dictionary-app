package DicCommandline;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private List<Word> wordsList = new ArrayList<Word>();

    public List<Word> getWordsList() {
        return wordsList;
    }

    public String showWordAt(int i) {
        return wordsList.get(i).getWordTarget() + "\t\t| " + wordsList.get(i).getWordExplain();
    }


}
