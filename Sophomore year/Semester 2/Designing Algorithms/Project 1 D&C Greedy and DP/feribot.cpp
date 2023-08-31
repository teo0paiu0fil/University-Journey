#include <cstdint>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

int64_t sum_vec(vector<int64_t> vec, int64_t start, int64_t end) {
    int64_t sum = 0;
    for (int i = start; i <= end; i++)
        sum += vec[i];
    return sum;
}

int64_t solveTask(int64_t n, int64_t k, vector<int64_t> weights) {
    int64_t start = 0;
    int64_t end = sum_vec(weights, start, n - 1);
    int64_t sol = 0;

    while (start <= end) {
        int64_t mid = (end + start) / 2;

        int64_t aux_sum = 0;
        int64_t aux_feri = 1;

        for (int i = 0; i < n; i++) {
            if (weights[i] > mid) {
                aux_feri = k + 1;
                break;
            } else if (aux_sum + weights[i] > mid) {
                aux_feri++;
                aux_sum = weights[i];
            } else {
                aux_sum += weights[i];
            }
        }

        if (aux_feri > k) {
            start = mid + 1;
        } else if (aux_feri <= k) {
            end = mid - 1;
            sol = mid;
        }
    }

    return sol;
}

vector<int64_t> ReadVector(istream& is, int size) {
    vector<int64_t> vec(size);
    for (auto& num : vec) {
        is >> num;
    }
    return vec;
}

int main() {
    ifstream fin("feribot.in");
    ofstream fout("feribot.out");

    int64_t n, k;
    fin >> n;
    fin >> k;

    auto weights = ReadVector(fin, n);

    auto res = solveTask(n, k, weights);
    fout << res << "\n";

    return 0;
}
