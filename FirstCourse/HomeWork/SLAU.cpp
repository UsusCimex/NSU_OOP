#include <iostream>

using namespace std;

double GetLine(double* matrix, int line, int size, double* ValArray) {
    double res = 0;
    for (int i = 0; i < size; i++) {
        res += matrix[line * (size + 1) + i] * ValArray[i];
    }
    return res;
}

double Abs(double val) {
    if (val > 0) return val;
    return -val;
}

double Max(double a, double b) {
    if (a > b) return a;
    return b;
}

char CheckRes(double* arr1, double* arr2, int size, double accuracy) {
    double dif = 0;
    for (int i = 0; i < size; i++) {
        dif = Max(dif, Abs(arr1[i] - arr2[i]));
    }
    if (dif < accuracy) return 1;
    return 0;
}

int main() {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);
    int N;
    cin >> N;
    double* matrix = (double*)malloc(N * (N + 1) * sizeof(double));
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N + 1; j++) {
            int val;
            cin >> val;
            matrix[i * (N + 1) + j] = (double)val;
        }
    }

    double* xArray = (double*)calloc(N, sizeof(double));
    double* yArray = (double*)malloc(N * sizeof(double));
    for (int i = 0; i < N; i++) {
        yArray[i] = matrix[i * (N + 1) + N] / matrix[i * (N + 1) + i];
    }
    int stopper = 0;
    do {
        for (int i = 0; i < N; i++) {
            xArray[i] = yArray[i];
            yArray[i] = (double)1 / matrix[i * (N + 1) + i] * (matrix[i * (N + 1) + N] - GetLine(matrix, i, N, yArray) + matrix[i * (N + 1) + i] * yArray[i]);
        }
        stopper += 1;
		if (stopper % 20 == 0) {
        	if (CheckRes(xArray, yArray, N, (double)1 / 10000)) {
        		break;
            }
        }
    } while (stopper < 1000);
    
	if (stopper == 1000)
        cout << "No solutions";
	else {
        for (int i = 0; i < N; i++) {
        	cout << xArray[i] << " ";
    	}
    }

    free(matrix);
    free(xArray);
    free(yArray);
    return 0;
}