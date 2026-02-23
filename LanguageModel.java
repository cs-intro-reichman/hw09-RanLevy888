import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    private HashMap<String, List> CharDataMap;
    private int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<>();
    }

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<>();
    }

    public void calculateProbabilities(List probs) {

        int totalCounts = 0;

        for (int i = 0; i < probs.getSize(); i++) {
            totalCounts += probs.get(i).count;
        }

        double cumulative = 0.0;

        for (int i = 0; i < probs.getSize(); i++) {
            List.Node node = probs.get(i);
            node.p = (double) node.count / totalCounts;
            cumulative += node.p;
            node.cp = cumulative;
        }
    }

    public char getRandomChar(List probs) {

        double r = randomGenerator.nextDouble();

        for (int i = 0; i < probs.getSize(); i++) {
            List.Node node = probs.get(i);
            if (node.cp > r) {
                return node.chr;
            }
        }

        return probs.get(probs.getSize() - 1).chr;
    }

    public void train(String fileName) {

        In in = new In(fileName);

        String window = "";

        for (int i = 0; i < windowLength && !in.isEmpty(); i++) {
            window += in.readChar();
        }

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

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    public String generate(String initialText, int textLength) {

        if (initialText.length() < windowLength) {
            return initialText;
        }

        StringBuilder generated = new StringBuilder(initialText);
        String window = initialText.substring(initialText.length() - windowLength);

        while (generated.length() < textLength) {

            List probs = CharDataMap.get(window);

            if (probs == null) {
                break;
            }

            char nextChar = getRandomChar(probs);
            generated.append(nextChar);

            window = generated.substring(generated.length() - windowLength);
        }

        return generated.toString();
    }

    public static void main(String[] args) {

        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];

        LanguageModel lm =
                randomGeneration
                        ? new LanguageModel(windowLength)
                        : new LanguageModel(windowLength, 20);

        lm.train(fileName);

        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}