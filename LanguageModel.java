import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // מפה המקשרת בין מחרוזת חלון לרשימת נתוני התווים העוקבים [cite: 179]
    private HashMap<String, List> CharDataMap;
    
    // אורך החלון הקבוע שנקבע למודל [cite: 225]
    private int windowLength;
    
    // מחולל מספרים אקראיים לשימוש בבחירת תווים [cite: 255]
    private Random randomGenerator;

    /** יוצר מודל עם זרע אקראי [cite: 256] */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** יוצר מודל עם זרע קבוע לבדיקות הדירות [cite: 256] */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** מחשב את ההסתברויות p ו-cp לכל איבר ברשימה [cite: 119, 120] */
    public void calculateProbabilities(List probs) {
        int totalCounts = 0;
        // מעבר על הרשימה לחישוב סך המופעים הכולל [cite: 121]
        for (int i = 0; i < probs.getSize(); i++) {
            totalCounts += probs.get(i).count;
        }

        double cumulativeProb = 0.0;
        // חישוב p (הסתברות יחסית) ו-cp (הסתברות מצטברת) [cite: 118, 122]
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts; // הסתברות [cite: 112]
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb; // הסתברות מצטברת [cite: 112]
        }
    }

    /** בוחר תו אקראי מהרשימה לפי שיטת Monte Carlo [cite: 136, 143] */
    public char getRandomChar(List probs) {
        // הגרלת מספר r בטווח [0,1) [cite: 137, 259]
        double r = randomGenerator.nextDouble(); 
        for (int i = 0; i < probs.getSize(); i++) {
            // בחירת התו הראשון שבו ההסתברות המצטברת גדולה מ-r [cite: 139, 140]
            if (probs.get(i).cp > r) {
                return probs.get(i).chr;
            }
        }
        // במקרה של דיוק עשרוני, נחזיר את התו האחרון (שבו cp הוא 1.0) [cite: 115, 141]
        return probs.get(probs.getSize() - 1).chr;
    }

    /** מאמן את המודל על קובץ טקסט נתון [cite: 198] */
    public void train(String fileName) {
        String window = "";
        In in = new In(fileName); // קריאת הקובץ [cite: 373]
        
        // יצירת החלון הראשון באורך windowLength [cite: 374, 375]
        for (int i = 0; i < windowLength && !in.isEmpty(); i++) {
            window += in.readChar();
        }

        // מעבר על כל הטקסט, תו אחרי תו [cite: 187, 376]
        while (!in.isEmpty()) {
            char c = in.readChar(); // קריאת התו הבא [cite: 380]
            List probs = CharDataMap.get(window);
            
            if (probs == null) { // חלון חדש שטרם נצפה [cite: 194, 387]
                probs = new List();
                CharDataMap.put(window, probs);
            }
            
            probs.update(c); // עדכון המופעים של התו אחרי החלון [cite: 195, 391]
            // הזזת החלון: הוספת התו החדש והסרת הראשון [cite: 392]
            window = window.substring(1) + c; 
        }

        // חישוב הסתברויות לכל הרשימות שנוצרו במפה [cite: 397, 399]
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    /** מייצר טקסט אקראי על בסיס המודל שאומן [cite: 205, 209] */
    public String generate(String initialText, int textLength) {
        // אם הטקסט הראשוני קצר מאורך החלון, לא ניתן להמשיך [cite: 226, 227]
        if (initialText.length() < windowLength) {
            return initialText;
        }

        StringBuilder generatedText = new StringBuilder(initialText);
        // קביעת החלון ההתחלתי לפי סוף הטקסט הראשוני [cite: 229]
        String window = initialText.substring(initialText.length() - windowLength);

        // המשך יצירה עד להגעה לאורך המבוקש [cite: 231]
        while (generatedText.length() < textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) { // אם החלון לא קיים במודל, עוצרים [cite: 232]
                break;
            }
            char nextChar = getRandomChar(probs); // בחירת תו אקראי [cite: 218, 219]
            generatedText.append(nextChar); // הוספה לטקסט [cite: 219]
            // עדכון החלון לתווים האחרונים בטקסט שנוצר [cite: 220, 230]
            window = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText.toString();
    }

    public static void main(String[] args) {
        // קריאת ארגומנטים משורת הפקודה [cite: 262, 268, 270]
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength); // זרע אקראי [cite: 274]
        } else {
            lm = new LanguageModel(windowLength, 20); // זרע קבוע לבדיקות [cite: 275]
        }

        lm.train(fileName); // שלב האימון [cite: 276]
        // הדפסת הטקסט שנוצר [cite: 278]
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}