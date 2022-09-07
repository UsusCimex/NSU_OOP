#include <stdlib.h>
#include <stdio.h>

typedef struct Node {
    int value;
    int height;
    struct Node* left;
    struct Node* right;
} Node;

int max(int a, int b) {
    if (a > b) return a;
    return b;
}

int abs(int a) {
    if (a > 0) return a;
    return -a;
}

int GetHeight(Node* node) {
    if (node)
        return node -> height;
    return 0;
}

Node* LeftRotate(Node* node) {
    node -> height -= 2;
    Node* newNode = node -> left;
    node -> left = newNode -> right;
    newNode -> right = node;
    return newNode;
}

Node* RightRotate(Node* node) {
    node -> height -= 2;
    Node* newNode = (node) -> right;
    node -> right = newNode -> left;
    newNode -> left = node;
    return newNode;
}

Node* AddNode(Node* tree, Node* root, int value, int i) {
    if (root == NULL) {
        tree[i].value = value;
        tree[i].height = 1;
        tree[i].left = NULL;
        tree[i].right = NULL;
        root = &tree[i];
    }
    else {
        if (value > root -> value)
            root -> right = AddNode(tree, root -> right, value, i); 
        else
            root -> left = AddNode(tree, root->left, value, i);
        
        root -> height = max(GetHeight(root -> left), GetHeight(root -> right)) + 1;

        int hLeft = GetHeight(root -> left);
        int hRight = GetHeight(root -> right);
        int diff = hLeft - hRight;

        if (abs(diff) >= 2) {
            if (diff > 0) {
                if ((GetHeight(root) - GetHeight(root -> left -> right)) == 2) {
                    root->left = RightRotate(root -> left);
                    root -> left -> left -> height += 1;
                    root = LeftRotate(root);
                    root -> height += 1;
                }
                else 
                    root = LeftRotate(root);
            }
            else if (diff < 0) {
                if ((GetHeight(root) - GetHeight(root -> right -> left)) == 2) {
                    root->right = LeftRotate(root -> right);
                    root -> right -> right -> height += 1;
                    root = RightRotate(root);
                    root -> height += 1;
                }
                else
                    root = RightRotate(root);
            }
        }
    }
    return root;
}

FILE* file;
int main(void) { //AVL-Tree
    file = fopen("in.txt", "r");

    int N;
    if (!fscanf(file, "%d", &N)) return 0;
    if (N <= 0) {
        printf("0");
        return EXIT_SUCCESS;
    }

    Node* tree = malloc(sizeof(Node) * N);
    Node* root = NULL;
    for (int i = 0; i < N; i++) {
        int val;
        if (!fscanf(file, "%d", &val)) return 0;
        root = AddNode(tree, root, val, i);
    }

    printf("%d", root -> height);
    free(tree);
    fclose(file);
    return EXIT_SUCCESS;
}
