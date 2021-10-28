package DicCommandline;

import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class DictionaryManagement {
    private Dictionary dictionary;

    public DictionaryManagement() {
        this.dictionary = new Dictionary();
    }

    public Dictionary getDictionnary() {
        return dictionary;
    }

    public void insertFromCommandline() {
        Scanner scan = new Scanner(System.in);
        FileWriter fw = null;
        BufferedWriter bw = null;
        System.out.print("Nhap so luong tu muon them : ");
        int num = Integer.parseInt(scan.next());
        String target;
        String expain;
        scan.nextLine();
        for (int i = 0; i < num; i++) {
            System.out.print("Nhap tu tieng anh : ");
            target = scan.nextLine();
            System.out.print("Nhap nghia : ");
            expain = scan.nextLine();
            Word word = new Word(target,expain, "");
            dictionary.getWordsList().add(word);
            String s = target + '\t' + expain;
            try {
                fw = new FileWriter("src/dictionaries.txt", true);
                bw = new BufferedWriter(fw);
                bw.write(s);
                bw.newLine();
                bw.close();
                fw.close();
            } catch (Exception ex) {

            }
        }
    }

    public void dictionaryLookup() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap tu tieng anh muon tim : ");
        String str  = sc.nextLine();
        boolean check = false;
        for (int i = 0; i < dictionary.getWordsList().size(); i++) {
            if (dictionary.getWordsList().get(i).getWordTarget().equals(str)) {
                System.out.println(dictionary.showWordAt(i));
                check = true;
            }
        }
        if (!check) {
            System.out.println("Khong tim thay!");
        }
    }

    public void removeWord() {
        Scanner sc = new Scanner(System.in);
        int choose;
        do {
            System.out.println("Lua chon tim kiem tu muon xoa : ");
            System.out.println("1 : Tieng Anh");
            System.out.println("2 : Tieng Viet");
            choose = sc.nextInt();
        } while (choose != 1 && choose != 2);
        sc.nextLine();
        boolean check = false;
        if (choose == 1) {
            System.out.println("Nhap tu tieng anh can tim xoa : ");
            String wordRemove = sc.nextLine();
            for (int i = 0; i < dictionary.getWordsList().size(); i++) {
                if (dictionary.getWordsList().get(i).getWordTarget().equals(wordRemove)) {
                    dictionary.getWordsList().remove(i);
                    check = true;
                }
            }
        } else if (choose == 2) {
            System.out.print("Nhap tu tieng viet can tim xoa : ");
            String wordRemove = sc.nextLine();
            for (int i = 0; i < dictionary.getWordsList().size(); i++) {
                if (dictionary.getWordsList().get(i).getWordExplain().equals(wordRemove)) {
                    dictionary.getWordsList().remove(i);
                    check = true;
                }
            }
        }
        if (!check) {
            System.out.println("Khong tim thay tu can xoa!");
        }
    }

    public void insertFromFile() {
        File file = new File("src/dictionaries.txt");
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str;
        String eng;
        String vie;
        int numOfTab;
        while (scan.hasNext()) {
            str = scan.nextLine();
            numOfTab = str.indexOf('\t');
            eng = str.substring(0,numOfTab);
            vie = str.substring(numOfTab + 1);
            Word word = new Word(eng,vie, "");
            dictionary.getWordsList().add(word);
        }

    }

    /** Nếu line chứa kí tự @ ở đầu theo định dạng thì lấy từ và phát âm. */
    private String getWord(String line) {
        if(line.indexOf('/') == -1) {
            return line;
        }
        return line.substring(1, line.indexOf('/') - 1);
    }

    /** lấy phát âm của từ. */
    private String getPronounce(String line) {
        if(line.indexOf('/') == -1) {
            return "";
        }
        return line.substring(line.indexOf('/'));
    }

    /** Nạp từ trong file vào listWord. */
    public void insertTxt() {
        String line;
        boolean check = false;
        String target = "";
        String explain = "";
        String pronounce = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader("src/AnhViet.txt"));

            while((line = reader.readLine()) != null){
                if(line.length() == 0 && target.length() != 0) {
                    dictionary.getWordsList().add(new Word(target, explain, pronounce));
                    target = "";
                    explain = "";
                    pronounce = "";
                    check = false;
                }
                if(check && line.length() != 0) {
                    explain = explain.concat(line + "\n");
                }
                if(line.length() > 1 && line.charAt(0) == '@') {    // lấy phát âm và target nếu char[0] = @
                    target = getWord(line);                         // lấy từ
                    pronounce = getPronounce(line);                 // lấy phát âm
                    check = true;
                }
            }
            if (target.length() != 0) {
                dictionary.getWordsList().add(new Word(target, explain, pronounce));
            }
        }catch (Exception e){
            System.out.println("Can't read file " + e);
        }
    }

    public void changeWordFromCommandLine () {
        System.out.println("Nhap tu ban muon sua nghia:");
        Scanner scan = new Scanner(System.in);
        String target = scan.nextLine();
        System.out.println("Nhap nghia cua tu ban muon sua:");
        String explain = scan.nextLine();
        boolean check = false;
        for(int i = 0; i < dictionary.getWordsList().size(); i++) {
            if( dictionary.getWordsList().get(i).getWordTarget().equals(target)) {
               check = true;
               dictionary.getWordsList().get(i).setWordExplain(explain);
            }
        }
        if(!check) {
            System.out.println("Tu cua ban chua co trong tu dien");
        }
    }

    /** Nạp từ trong list vào file. */
    public void dictionaryExportToFile () throws IOException {
        File file = new File("src/AnhViet.txt");
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        for(int i = 0; i < dictionary.getWordsList().size(); i++) {
            String wordLabel = "\n" + "@" + dictionary.getWordsList().get(i).getWordTarget() + " " + dictionary.getWordsList().get(i).getWordPronounce() + "\n";
            outputStreamWriter.write(wordLabel);
            String wordExplain = dictionary.getWordsList().get(i).getWordExplain() + "\n";
            outputStreamWriter.write(wordExplain);
        }
        //outputStreamWriter.write("\n@" );
        outputStreamWriter.flush();
    }

    /** Thay đổi nghĩa, trả về true nếu đổi thành công, false nếu k có từ đó. */
    public boolean changeWordExplain (String newExplain, String target) {
        for(int i = 0; i < dictionary.getWordsList().size(); i++) {
            if( dictionary.getWordsList().get(i).getWordTarget().equals(target)) {
                dictionary.getWordsList().get(i).setWordExplain(newExplain);
                return true;
            }
        }
        return false;
    }

    /** Xóa từ trong list từ điển, trả về true nếu xóa thành công, false nếu k có từ đó. */
    public boolean removeCurrentWord(String wordRemove) {
        if(wordRemove == null) return false;
        for (int i = 0; i < dictionary.getWordsList().size(); i++) {
            if (dictionary.getWordsList().get(i).getWordTarget().equals(wordRemove)) {
                dictionary.getWordsList().remove(i);
                return true;
            }
        }
        return false;
    }
}
