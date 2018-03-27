package algorithms;


import java.util.*;

/**
 * Дерево Хаффмана
 */
public class Huffman implements Comparable<Huffman>, IAlgorithm {

    private static int OFFSET_META = 4; // Смещение на размер числа, которое содержит информацию о размере meta
    // корень дерева
    private Node root;

    public Huffman() {}

    public Huffman(Node root) {
        this.root = root;
    }

    public static String name () {
        return "huffman";
    }

    /**
     * Класс, описывающий объекты узлов.
     */
    private static class Node {
        // Частота символа
        private Integer frequency;
        // Символ
        private Character character;
        // Код
        private Character code;
        // Левый потомок узла
        private Node leftChild;
        // Правый потомок узла
        private Node rightChild;

        public Node(Integer frequency, Character character) {
            this.frequency = frequency;
            this.character = character;
        }

        public Node(Huffman left, Huffman right) {
            frequency = left.root.frequency + right.root.frequency;
            leftChild = left.root;
            rightChild = right.root;
        }

        @Override
        public String toString() {
            return String.format("%4s\t%6d\t%4s", character, frequency, code);
        }
    }

    private static int[] countCharFrequencies (String text) {
        // Считая, что на вход подаются ASCII символы, ограничимся 256 различными символами
        int[] charFrequencies = new int[256];
        // считываем символы и считаем их частоту
        for (char c : text.toCharArray()) {
            charFrequencies[c]++;
        }

        return charFrequencies;
    }

    /**
     * Рекурсивный метод присваивания кода узлам
     * @param current текущий узел
     * @param code код пути до текущего узла
     */
    private static void injectCodes(Node current, Character code) {
        //если узел листовой
        if (current.character != null) {
            // выводим символ, частоту и код пути от корня до текущкго узла
            current.code = code;
        } else {
            // обходим левое поддерево
            injectCodes(current.leftChild, '0');
            // обходим правое поддерево
            injectCodes(current.rightChild, '1');
        }
    }

    private static void injectCodes (Huffman tree) {
        injectCodes(tree.root, null);
    }

    /**
     * Построение дерева Хаффмана
     * Алгоритм:
     * 1. Создать объект Node для каждого символа, используемого в сообщении.
     * 2. Создать объект дерева для каждого из этих узлов. Узел становится корнем дерева.
     * 3. Вставить эти деревья в приоритетную очередь. Деревья упорядочиваются по частоте,
     * при этом наименьшая частота обладает наибольшим приоритетом. Таким образом,
     * при извлечении всегда выбирается дерево с наименее часто используемым символом.
     * 4. Извлечь два дерева из приоритетной очереди и сделать их потомками нового узла.
     * Частота нового узла является суммой частот потомков. Поле символа может остаться пустым.
     * 5. Вставить новое дерево из двух узлов обратно в приоритетную очередь.
     * 6. Продолжить выполнение шагов 4 и 5. Деревья постепенно увеличиваются, а их
     * количество постепенно сокращается. Когда в очереди останется всего одно дерево, оно
     * представляет собой дерево Хаффмана. Работа алгоритма при этом завершается.
     *
     * @param charFrequencies Массив содержащий частоту символов в сообщении. Номер ячейки соответствует
     * коду символа в ASCII.
     * @return дерево Хаффмана
     */
    private static Huffman buildHuffmanTree(int[] charFrequencies) {
        PriorityQueue<Huffman> trees = new PriorityQueue<>();
        // 1. - 3.
        for (int i = 0; i < charFrequencies.length; i++) {
            if (charFrequencies[i] > 0) {
                trees.offer(new Huffman(new Node(charFrequencies[i], (char)i)));
            }
        }
        // 6. пока в очереди не останется только одно дерево
        while (trees.size() > 1) {
            // 4. - 5.
            Huffman a = trees.poll();
            Huffman b = trees.poll();
            trees.offer(new Huffman(new Node(a, b)));
        }
        /*Когда в очереди останется всего одно дерево, оно представляет собой дерево Хаффмана. */
        Huffman tree = trees.poll();
        // Добавляем код в листья
        injectCodes(tree);

        return tree;
    }

    private static Huffman buildHuffmanTreeFromMeta(String meta) {
        // Строим дерево
        return buildHuffmanTree(deserializeHuffmanCodes(meta));
    }

    private static Huffman buildHuffmanTreeFromText(String text) {
        return buildHuffmanTree(countCharFrequencies(text));
    }

    /**
     * Десериализуем мета инфу для алгоритма
     * @param meta
     * @return
     */
    private static int[] deserializeHuffmanCodes (String meta) {
        // Считая, что на вход подаются ASCII символы, ограничимся 256 различными символами
        int[] charFrequencies = new int[256];

        int charFrequencyPairLength = 9;

        for (int i = 0; i < meta.length(); i += charFrequencyPairLength) {
            Character character = meta.charAt(i);
            charFrequencies[character] = Integer.parseInt(meta.substring(i + 1, i + 9).trim(), 16);
        }

        return charFrequencies;
    }

    /**
     * Преобразование последовательности битов в текстовую строку.
     * Декодирование каждого символа начинается с корневого узла.
     * Если в потоке обнаружен бит 0, вы переходите налево к следующему узлу,
     * а если 1 - то направо.
     * Например, для кода 010 нужно двигаться налево, направо, потом снова налево.
     * Если существует символ с таким кодом, то вы окажитесь в листовом узле.
     * При достижении листового узла начинается поиск нового символа.
     * @param bytes массив байт, который требуется декодировать.
     * @return метод возвращает декодированную строку
     */
    private String decodePayload(String bytes) {
        StringBuilder result = new StringBuilder();
        Node current = root;

        for (char c : bytes.toCharArray()) {
            if (c == '0') {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }

            if (current.character != null) {
                result.append(current.character);
                current = root;
            }
        }

        return result.toString();
    }


    public String decode (String data) {
        String meta = parseMetaData(data);
        String encodedData = parseEncodedData(data);

        Huffman tree = buildHuffmanTreeFromMeta(meta);
        String text = tree.decodePayload(encodedData);


        return text;
    }

    private static String parseMetaData (String data) {
        int size = getSizeOfMeta(data);
        return data.substring(OFFSET_META,  OFFSET_META + size);
    }

    private static String parseEncodedData (String data) {
        int size = getSizeOfMeta(data);
        return data.substring(OFFSET_META + size);
    }

    private static int getSizeOfMeta (String data) {
        return Integer.parseInt(data.substring(0, 4).trim());
    }

    public String encode(String text) {
        String meta = serializeMeta(countCharFrequencies(text));
        String sizeOfMetaStr = serializeSizeOfMeta(meta);

        Huffman tree = buildHuffmanTreeFromText(text);

        /*Для кодирования сообщения необходимо создать кодовую таблицу,
         * в которой для каждого символа приводится соответствующий код Хаффмана.*/
        String[] codes = codeTable(tree);
        StringBuilder result = new StringBuilder();

        result.append(sizeOfMetaStr);
        result.append(meta);

        /*Далее коды Хаффмана раз за разом присоединяются к кодированному сообщению,
         * пока оно не будет завершено.*/
        for (int i = 0; i < text.length(); i++) {
            result.append(codes[text.charAt(i)]);
        }
        return result.toString();

    }

    /**
     * Сериализуем таблицу частотности символов, по которой можно будет восстановить дерево Хаффмана
     * @param charFrequencies
     * @return
     */
    static String serializeMeta (int[] charFrequencies) {
        StringBuilder serialized = new StringBuilder();

        for (int i = 0; i < charFrequencies.length; ++i) {
            int count = charFrequencies[i];

            if (count > 0) {
                serialized.append((char)i);
                // Сериализуем числа в hex для экономии размера
                serialized.append(String.format("%8x", count));
            }
        }

        return serialized.toString();
    }

    static String serializeSizeOfMeta (String meta) {
        return String.format("%4d", meta.length());
    }

    /**
     * Создание кодовой таблицы по данному дереву Хаффмана
     * @return возвращает кодовую таблицу, в которой для каждого символа
     * приводится соответствующий код Хаффмана.
     */
    private static String[] codeTable(Huffman tree) {
        String[] codeTable = new String[256];
        codeTable(tree.root, new StringBuilder(), codeTable);
        return codeTable;
    }

    /**
     * Постороение кодовой таблицы реализовано посредством вызова метода,
     * который начинается от корня таблицы, а затем рекурсивно вызывает себя
     * для каждого потомка. Через некоторое время алгоритм обойдет все пути
     * ко всем листовым узлам, и кодовая таблица будет построена.
     * @param node текущий узел
     * @param code код из 0(лево) и 1(право), отражающий путь от корня до текущего узла
     * @param codeTable кодовая таблица
     */
    private static void codeTable(Node node, StringBuilder code, String[] codeTable) {
        if (node.character != null) {
            codeTable[(char)node.character] = code.toString();
            return;
        }
        codeTable(node.leftChild, code.append('0'), codeTable);
        code.deleteCharAt(code.length() - 1);
        codeTable(node.rightChild, code.append('1'), codeTable);
        code.deleteCharAt(code.length() - 1);
    }

    @Override
    public int compareTo(Huffman tree) {
        return root.frequency - tree.root.frequency;
    }
}
