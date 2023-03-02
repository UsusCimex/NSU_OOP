package brainfuck.data;

/** Вспомогательная оболочка для стека циклов. Используется для хранения адреса начала и конца цикла. */
public record Loop(Integer from, Integer to) { }