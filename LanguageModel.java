import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);

        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }

        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c;
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    void calculateProbabilities(List probs) {               
        int totalCount = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            totalCount += probs.get(i).count;
        }

        double cumulativeProb = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData cd = probs.get(i);
            cd.p = (double) cd.count / totalCount;
            cumulativeProb += cd.p;
            cd.cp = cumulativeProb;
        }
    }

    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            if (probs.get(i).cp > r) {
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }

        StringBuilder generatedText = new StringBuilder(initialText);
        String currentWindow = initialText.substring(initialText.length() - windowLength);

        // התיקון הקריטי: textLength הוא מספר התווים שצריך לייצר בנוסף ל-initialText
        while (generatedText.length() < (initialText.length() + textLength)) {
            List probs = CharDataMap.get(currentWindow);
            if (probs == null) {
                break;
            }
            char nextChar = getRandomChar(probs);
            generatedText.append(nextChar);
            currentWindow = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText.toString();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key + " : " + keyProbs + "\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength);
        } else {
            lm = new LanguageModel(windowLength, 20);
        }

        lm.train(fileName);
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}