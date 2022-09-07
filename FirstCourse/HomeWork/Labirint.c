#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <locale.h>

#define VISITED 0b10000000
#define SEEKED 0b01000000
#define LEFT 0b00001000
#define UP 0b00000100
#define RIGHT 0b00000010
#define DOWN 0b00000001

void PushEdge(int* edges, int* count, int from, int to) {
    edges[*count] = to;
    *count += 1;
}

void SelectCell(char* maze, int* edges, int* count, int x, int y, int size) {
    if (x < size - 1) if (!(maze[y * size + x + 1] & SEEKED)) {
        PushEdge(edges, count, y * size + x,  y * size + x + 1);
        maze[y * size + x + 1] |= SEEKED;
    }
    if (x > 0) if (!(maze[y * size + x - 1] & SEEKED)) {
        PushEdge(edges, count, y * size + x, y * size + x - 1);
        maze[y * size + x - 1] |= SEEKED;
    }
    if (y > 0) if (!(maze[(y - 1) * size + x] & SEEKED)) {
        PushEdge(edges, count, y * size + x, (y - 1) * size + x);
        maze[(y - 1) * size + x] |= SEEKED;
    }
    if (y < size - 1) if (!(maze[(y + 1) * size + x] & SEEKED)) {
        PushEdge(edges, count, y * size + x, (y + 1) * size + x);
        maze[(y + 1) * size + x] |= SEEKED;
    }
}

void EditWalls(char* maze, int size, int to) {
    maze[to] |= VISITED;
    int from;
    while (1) {
        int status = rand() % 4;
        if (status == 0 && to - size >= 0) if (maze[to - size] & VISITED) { from = to - size; break; } else continue;
        else if (status == 1 && to % size != size - 1) if (maze[to + 1] & VISITED) { from = to + 1; break; } else continue;
        else if (status == 2 && to + size < size * size) if (maze[to + size] & VISITED) { from = to + size; break; } else continue;
        else if (status == 3 && to % size != 0) if (maze[to - 1] & VISITED) { from = to - 1; break; } else continue;
    }

    int move = from - to;
    if (move == 1) {
        maze[from] |= LEFT;
        maze[to] |= RIGHT;
    }
    else if (move == -1) {
        maze[from] |= RIGHT;
        maze[to] |= LEFT;
    }
    else if (move > 1) {
        maze[from] |= UP;
        maze[to] |= DOWN;
    }
    else if (move < -1) {
        maze[from] |= DOWN;
        maze[to] |= UP;
    }
}

int PopRandCell(int* edges, int* count) {
    int num = rand() % *count;
    int res = edges[num];
    edges[num] = edges[*count - 1];
    *count -= 1;
    return res;
}

char* PrimMaze(int size) {
    char* maze = calloc(size * size, sizeof(char));
    int rH = rand() % size;
    int rW = rand() % size;
    maze[rH * size + rW] |= VISITED;
    maze[rH * size + rW] |= SEEKED;
    int* edges = calloc(size * size, sizeof(int));
    
    int countConnected = 1;
    int countSeeked = 0;
    SelectCell(maze, edges, &countSeeked, rW, rH, size);

    while (countConnected != size * size) {
        int edge = PopRandCell(edges, &countSeeked);
        EditWalls(maze, size, edge);
        SelectCell(maze, edges, &countSeeked, edge % size, edge / size, size);
        countConnected += 1;
    }

    free(edges);
    return maze;
}

void PrintMaze(char* maze, int size) {
    char* printer = calloc((size + 1) * (size + 1), sizeof(char));
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (!(maze[i * size + j] & LEFT)) {
                printer[i * (size + 1) + j] |= DOWN;
                printer[(i + 1) * (size + 1) + j] |= UP;
            }
            if (!(maze[i * size + j] & UP)) {
                printer[i * (size + 1) + j] |= RIGHT;
                printer[i * (size + 1) + j + 1] |= LEFT;
            }
            if (!(maze[i * size + j] & RIGHT)) {
                printer[i * (size + 1) + j + 1] |= DOWN;
                printer[(i + 1) * (size + 1) + j + 1] |= UP;
            }
            if (!(maze[i * size + j] & DOWN)) {
                printer[(i + 1) * (size + 1) + j] |= RIGHT;
                printer[(i + 1) * (size + 1) + j + 1] |= LEFT;
            }
        }
    }

    for (int i = 0; i < size + 1; i++) {
        for (int j = 0; j < size + 1; j++) {
            if ((i == 0 && j == 0) || (i == size && j == size)) {
                wprintf(L" ");
            }
            else {
                int index = i * (size + 1) + j;
                if ((printer[index] & LEFT) && (printer[index] & UP) && (printer[index] & RIGHT) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'╋');
                }
                else if ((printer[index] & LEFT) && (printer[index] & UP) && (printer[index] & RIGHT)) {
                    wprintf(L"%Lc", L'┻');
                }
                else if ((printer[index] & LEFT) && (printer[index] & UP) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'┫');
                }
                else if ((printer[index] & LEFT) && (printer[index] & DOWN) && (printer[index] & RIGHT)) {
                    wprintf(L"%Lc", L'┳');
                }
                else if ((printer[index] & UP) && (printer[index] & RIGHT) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'┣');
                }
                else if ((printer[index] & LEFT) && (printer[index] & UP)) {
                    wprintf(L"%Lc", L'┛');
                }
                else if ((printer[index] & LEFT) && (printer[index] & RIGHT)) {
                    wprintf(L"%Lc", L'━');
                }
                else if ((printer[index] & LEFT) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'┓');
                }
                else if ((printer[index] & UP) && (printer[index] & RIGHT)) {
                    wprintf(L"%Lc", L'┗');
                }
                else if ((printer[index] & UP) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'┃');
                }
                else if ((printer[index] & RIGHT) && (printer[index] & DOWN)) {
                    wprintf(L"%Lc", L'┏');
                }
                else if (printer[index] & UP) {
                    wprintf(L"%Lc", L'╹');
                }
                else if (printer[index] & LEFT) {
                    wprintf(L"%Lc", L'╸');
                }
                else if (printer[index] & RIGHT) {
                    wprintf(L"%Lc", L'╺');
                }
                else if (printer[index] & DOWN) {
                    wprintf(L"%Lc", L'╻');
                }
            }
        }
        wprintf(L"\n");
    }
    free(printer);
}

int main() {
    freopen("output.txt", "w", stdout);
    setlocale(LC_ALL, "en_US.UTF-8");
    srand(time(0));
    int N;
    if (scanf("%d", &N) != 1)
        return 0;
    char* maze = PrimMaze(N);
    PrintMaze(maze, N);
    free(maze);
    return 0;
}