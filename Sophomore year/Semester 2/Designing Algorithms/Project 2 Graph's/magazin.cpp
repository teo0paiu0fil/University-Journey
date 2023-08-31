#include <iostream>
#include <cstdint>
#include <fstream>
#include <vector>
#include <stack>
#include <algorithm>

using namespace std;
typedef vector<vector<uint64_t>> matrix;

int bfs_count(uint64_t start, matrix graph, uint64_t dest) {
    size_t n = graph.size();

    vector<uint64_t> result;
    vector<bool> visited(n, false);

    stack<uint64_t> stack;

    stack.push(start);

    while (!stack.empty()) {
        uint64_t s = stack.top();
        stack.pop();

        if (dest == 0) {
            return s;
        }

        dest--;

        if (!visited[s]) {
            result.push_back(s);
            visited[s] = true;
        }

        for (int i = graph[s].size() - 1; i >= 0; i--)
            if (!visited[graph[s][i]])
                stack.push(graph[s][i]);
    }

    return -1;
}

matrix read_graph(uint64_t n, ifstream &fin) {
    matrix a(n + 1);
    uint64_t x;

    for (uint64_t i = 2; i <= n; i++) {
        fin >> x;
        a[x].push_back(i);
    }

    return a;
}

vector<pair<uint64_t, uint64_t>> read_questions(uint64_t q, ifstream &fin) {
    vector<pair<uint64_t, uint64_t>> quiz;
    uint64_t x, y;

    for (size_t i = 0; i < q; i++) {
        fin >> x >> y;
        quiz.push_back(pair(x, y));
    }

    return quiz;
}

void solve_task(matrix graph,
        vector<pair<uint64_t, uint64_t>> quiz, ofstream &fout) {
    size_t size = quiz.size();

    for (size_t i = 0; i < size; i++) {
        uint64_t start = quiz[i].first;
        uint64_t time_pass = quiz[i].second;

        fout << bfs_count(start, graph, time_pass) << endl;
    }
}

int main() {
    ifstream fin("magazin.in");
    ofstream fout("magazin.out");

    uint64_t n, q;
    fin >> n;
    fin >> q;

    matrix graph = read_graph(n, fin);
    vector<pair<uint64_t, uint64_t>>quiz = read_questions(q, fin);

    solve_task(graph, quiz, fout);

    return 0;
}
