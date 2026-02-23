import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // המפה של מודל השפה המקשרת בין חלון (Window) לרשימת מופעי התווים שאחריו [cite: 179]
    private HashMap<String, List> CharDataMap;
    
    // אורך החלון בו משתמש המודל [cite: 225]
    private int windowLength;
    
    // מחולל המספרים האקראיים של המודל [cite: 255]
    private Random randomGenerator;

    /** יוצר מודל שפה עם זרע (seed) אקראי [cite: 256] */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** יוצר מודל שפה עם זרע קבוע (לצרכי בדיקה) [cite: 256] */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** מחשב את ההסתברויות (p) וההסתברויות המצטברות (cp) לכל איבר ברשימה [cite: 119, 120] */
    public void calculateProbabilities(List probs) {
        int totalCounts = 0;
        // שלב 1: חישוב סך כל המופעים ברשימה [cite: 121]
        for (int i = 0; i < probs.getSize(); i++) {
            totalCounts += probs.get(i).count;
        }

        // שלב 2: חישוב p ו-cp לכל איבר [cite: 118, 122]
        double cumulativeProb = 0.0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts; // הסתברות יחסית [cite: 112]
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb; // הסתברות מצטברת [cite: 112]
        }
    }

    /** בוחר תו אקראי מהרשימה לפי ההסתברות שלו (Monte Carlo) [cite: 136, 143] */
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble(); // הגרלת מספר בין 0 ל-1 [cite: 137, 259]
        for (int i = 0; i < probs.getSize(); i++) {
            if (probs.get(i).cp > r) { // עצירה כשההסתברות המצטברת גדולה מ-r [cite: 139, 140]
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr; // החזרת התו האחרון במקרה של קצה טווח [cite: 141]
    }

    /** מאמן את המודל על קובץ טקסט נתון  */
    public void train(String fileName) {
        String window = "";
        In in = new In(fileName); // קריאת הקובץ [cite: 373]
        
        // קריאת תווים ליצירת החלון הראשון [cite: 374]
        for (int i = 0; i < windowLength && !in.isEmpty(); i++) {
            window += in.readChar();
        }

        // מעבר על שאר הקובץ, תו אחר תו [cite: 376]
        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);
            
            if (probs == null) { // אם החלון נראה לראשונה [cite: 194]
                probs = new List();
                CharDataMap.put(window, probs);
            }
            
            probs.update(c); // עדכון המופעים של התו אחרי החלון [cite: 195, 391]
            window = window.substring(1) + c; // הזזת החלון קדימה [cite: 392]
        }

        // חישוב הסתברויות לכל הרשימות במפה בסיום הקריאה [cite: 397, 399]
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    /** מייצר טקסט אקראי באורך המבוקש [cite: 205, 231] */
    public String generate(String initialText, int textLength) {
        // אם הטקסט הראשוני קצר מאורך החלון, לא ניתן לייצר המשך [cite: 226, 227]
        if (initialText.length() < windowLength) {
            return initialText;
        }

        StringBuilder generatedText = new StringBuilder(initialText);
        // קביעת החלון ההתחלתי לפי סוף הטקסט הראשוני [cite: 229]
        String window = initialText.substring(initialText.length() - windowLength);

        while (generatedText.length() < textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) { // אם החלון הנוכחי לא קיים במודל, עוצרים [cite: 232]
                break;
            }
            char nextChar = getRandomChar(probs); // בחירת תו אקראי [cite: 218]
            generatedText.append(nextChar);
            // עדכון החלון לתווים האחרונים בטקסט שנוצר [cite: 230]
            window = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText.toString();
    }

    /** מתודת ה-main שמריצה את המערכת לפי ארגומנטים משורת הפקודה [cite: 266, 267] */
    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]); // [cite: 268]
        String initialText = args[1]; // [cite: 269]
        int generatedTextLength = Integer.parseInt(args[2]); // [cite: 270]
        Boolean randomGeneration = args[3].equals("random"); // [cite: 270]
        String fileName = args[4]; // [cite: 270]

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength); // [cite: 274]
        } else {
            lm = new LanguageModel(windowLength, 20); // שימוש בזרע קבוע 20 [cite: 275]
        }

        lm.train(fileName); 
        System.out.println(lm.generate(initialText, generatedTextLength)); // הדפסת התוצאה [cite: 278]
    }
}