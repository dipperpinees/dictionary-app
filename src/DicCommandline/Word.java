package DicCommandline;

public class Word {
    private String wordTarget;
    private String wordExplain;
    private String wordPronounce;
    public String getWordExplain() {
        return wordExplain;
    }

    public void setWordExplain(String wordExplain) {
        this.wordExplain = wordExplain;
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public String getWordPronounce() {
        return wordPronounce;
    }

    public void setWordPronounce(String word) {
        wordPronounce = word;
    }

    public Word() {
        this.wordTarget = null;
        this.wordExplain = null;
        this.wordPronounce = null;
    }

    public Word(String wordTarget, String wordExplain, String wordPronounce) {
        this.wordTarget = wordTarget;
        this.wordExplain = wordExplain;
        this.wordPronounce = wordPronounce;
    }
}
