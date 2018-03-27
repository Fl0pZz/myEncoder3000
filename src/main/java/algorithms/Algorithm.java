package algorithms;

import java.util.HashMap;
import java.util.Map;

public class Algorithm {

    private Map<String, IAlgorithm> algorithms;

    public Algorithm () {
        algorithms = new HashMap<>();
        algorithms.put(Huffman.name(), new Huffman());
    }

    /**
     * Проверяем, что алгоритм с таким именем существует
     * @param name - название алогритма
     * @return есть или нет такого алгоритма
     */
    public boolean checkAlgoName (String name) {
        return algorithms.containsKey(name);
    }

    /**
     * @return Возвращает названия всех доступных алгоритмов
     */
    public String[] allAlgorithms () {
        return algorithms.keySet().toArray(new String[algorithms.size()]);
    }

    /**
     * Возрващает выбранный алгоритм, если такой есть
     * @param name - название алгоритма
     * @return IAlgorithm или null
     */
    public IAlgorithm selectAlgo (String name) {
        if (checkAlgoName(name)) {
            return algorithms.get(name);
        }
        return null;
    }
}
