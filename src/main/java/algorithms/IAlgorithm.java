package algorithms;

public interface IAlgorithm {

    /**
     * Кодирование сообщения
     * @param text - текст для кодировки
     * @return закодированный текст
     */
    String encode(String text);

    /**
     * Преобразование последовательности битов в текстовую строку.
     * @param bytes - входные данные для раскодировки
     * @return раскодированный текст
     */
    String decode(String bytes);

    /**
     * Название алгоритма
     * @return название
     */
    static String name () {
        return null;
    }
}
