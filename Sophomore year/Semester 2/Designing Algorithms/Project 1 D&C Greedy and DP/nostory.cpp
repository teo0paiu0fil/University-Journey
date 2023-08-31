/*
 * Acest schelet citește datele de intrare și scrie răspunsul generat de voi,
 * astfel că e suficient să completați cele două funcții.
 *
 * Scheletul este doar un punct de plecare, îl puteți modifica oricum doriți:
 * puteți schimba parametrii, reordona funcțiile etc.
 */

#include <cstdint>
#include <fstream>
#include <vector>
#include <algorithm>


using namespace std;

bool crescator(int& e1, int& e2) {
    return  e1 >= e2;
}

bool descrescator(int& e1, int& e2) {
    return  e1 <= e2;
}

int64_t SolveTask1(vector<int> a, vector<int> b) {
    sort(a.begin(), a.end(), crescator);
    sort(b.begin(), b.end(), descrescator);

    int64_t sum = 0;

    for (unsigned int i = 0; i < a.size(); i++) {
        if (a[i] > b[i]) {
            sum += a[i];
        } else {
            sum += b[i];
        }
    }

    return sum;
}

#define elem_max(i) max(a[i], b[i])
#define elem_min(i) min(a[i], b[i])

int64_t SolveTask2(vector<int> a, vector<int> b, int moves) {
    unsigned int n = a.size();
    moves++;
    vector<vector<int>> dp(n, vector<int> (moves));

    dp[0][0] = elem_max(0);

    for (unsigned int i = 1; i < n; i++) {
        for (int j = 0; j < moves; j++) {
            if (j == 0) {
                dp[i][j] = dp[i - 1][j] + elem_max(i);
            } else {
                int sum_aux = dp[i][j - 1];

                if (elem_max(i - 1) < elem_min(i)) {
                    sum_aux = (sum_aux - elem_max(i - 1)) + elem_min(i);
                } else if (elem_min(i - 1) > elem_max(i)) {
                    sum_aux = (sum_aux - elem_max(i)) + elem_min(i - 1);
                }

                int varianta1 = dp[i - 1][j - 1] + elem_max(i);
                int varianta2 = dp[i - 1][j] + elem_max(i);

                if (varianta1 >= varianta2 && varianta1 >= sum_aux) {
                    dp[i][j] = varianta1;
                } else if (varianta2 >= varianta1 && varianta2 >= sum_aux) {
                    dp[i][j] = varianta2;
                } else {
                    dp[i][j] = sum_aux;
                    int temp = a[i - 1];
                    a[i - 1] = b[i];
                    b[i] = temp;
                }
            }
        }
    }

    for (unsigned int i = 0; i < n; i++) {
        for (int j = 0; j < moves; j++) {
            printf("%3d ", dp[i][j]);
        }
        printf("\n");
    }

    return dp[n - 1][moves - 1];
}

vector<int> ReadVector(istream& is, int size) {
    vector<int> vec(size);
    for (auto& num : vec) {
        is >> num;
    }
    return vec;
}

int main() {
    ifstream fin("nostory.in");
    ofstream fout("nostory.out");

    int task;
    fin >> task;

    int n, moves;
    if (task == 1) {
        fin >> n;
    } else {
        fin >> n >> moves;
    }

    auto a = ReadVector(fin, n);
    auto b = ReadVector(fin, n);

    auto res = task == 1 ? SolveTask1(a, b) : SolveTask2(a, b, moves);
    fout << res << "\n";

    return 0;
}
