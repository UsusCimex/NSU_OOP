package brainfuck.operation;

import brainfuck.data.CommandContext;

//** Главный интерфейс всех операций */
public interface Operation {
    //** Главная функция данного интерфеса. @param context передаётся вся информация о состоянии приложения */
    public abstract void run(CommandContext context);
}