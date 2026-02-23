import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    private HashMap<String, List> CharDataMap;
    private int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    public void calculateProbabilities(List probs) {
        int totalCounts = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            totalCounts += probs.get(i).count;
        }
        double cumulativeProb = 0.0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCounts;
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb;
        }
    }

    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            if (probs.get(i).cp > r) {
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public void train(String fileName) {
        In in = new In(fileName);
        String window = "";
        // אתחול החלון הראשון
        for (int i = 0; i < windowLength && !in.isEmpty(); i++) {
            window += in.readChar();
        }
        // אימון על שאר הקובץ
        while (!in.isEmpty()) {
            char c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c;
        }
        // חישוב הסתברויות לכל הרשימות
        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }
        StringBuilder generatedText = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);
        
        // יצירת טקסט עד שמגיעים לאורך המבוקש בדיוק
        while (generatedText.length() < textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) {
                break;
            }
            char nextChar = getRandomChar(probs);
            generatedText.append(nextChar);
            window = generatedText.substring(generatedText.length() - windowLength);
        }
        return generatedText.toString();
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm = randomGeneration ? new LanguageModel(windowLength) : new LanguageModel(windowLength, 20);
        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}