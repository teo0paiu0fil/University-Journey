#include <cstdint>
#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>

using namespace std;
typedef vector<vector<uint32_t>> matrix;

//  citesc fiecare muchie si maresc gradul nodului corespunzator
matrix read_graph(ifstream &fin, uint32_t n, uint32_t m,
                                 vector<uint32_t> &degree) {
    matrix graph(n + 1);
    uint32_t x, y;

    for (uint32_t i = 0; i < m; ++i) {
        fin >> x >> y;
        graph[x].push_back(y);
        degree[y]++;
    }

    return graph;
}


//  citesc setul de date pentru fiecare task/ nod
vector<uint32_t> read_vector(ifstream &fin, uint32_t n) {
    vector<uint32_t> vec(n + 1);
    uint32_t x;

    for (uint32_t i = 1; i <= n; ++i) {
        fin >> x;
        vec[i] = x;
    }

    return vec;
}

uint32_t solve_task(vector<uint32_t> date, matrix graph,
                 vector<uint32_t> degrees, int ok) {
    int result = 0;  //  numarul de context swichuri

    queue<uint32_t> q1;  //  coada pentru taskurile cu data 1
    queue<uint32_t> q2;  //  coada pentru taskurile cu data 2

    //  initializez cozile cu toate nodurile cu grad egal cu 0
    for (int i = 1; i < graph.size() ; i++) {
        if (degrees[i] == 0) {
            if (date[i] == 1) {
                q1.push(i);
            } else {
                q2.push(i);
            }
        }
    }

    //  verific daca pot incepe cu setul de date pe care mi-l doresc
    //  altfel incep cu celalalt
    if (ok == 2 && q2.empty()) {
        ok = 1;
    } else if (ok == 1 && q1.empty()) {
        ok = 2;
    }

    //  cat timp am noduri
    while (!q1.empty() || !q2.empty()) {
        if (!q1.empty() && ok == 1) {
            uint32_t node = q1.front();  // iau primul nod din coada
            q1.pop();

            //  ii parcurg toate  nodurile la care acesta poate ajunge
            for (uint32_t dest : graph[node]) {
                degrees[dest]--;  // si le scad gradul

                // daca gradul unuia dintre nodurile destinatie a ajuns 0 il pun
                // in coada potrivita
                if (degrees[dest] == 0) {
                    if (date[dest] == 1) {
                        q1.push(dest);
                    } else {
                        q2.push(dest);
                    }
                }
            }

            // daca am golit coada am un context swich
            if (q1.empty()) {
                ok = 2;
                result++;
            }

        } else {
            uint32_t node = q2.front();  // iau primul nod din coada
            q2.pop();

            // ii parcurg toate  nodurile la care acesta poate ajunge
            for (uint32_t dest : graph[node]) {
                degrees[dest]--;  // si le scad gradul

                // daca gradul unuia dintre nodurile destinatie a ajuns 0 il pun
                // in coada potrivita
                if (degrees[dest] == 0) {
                    if (date[dest] == 1) {
                        q1.push(dest);
                    } else {
                        q2.push(dest);
                    }
                }
            }

            // daca am golit coada am un context swich
            if (q2.empty()) {
                ok = 1;
                result++;
            }
        }
    }

    return result;
}

int main() {
    ifstream fin("supercomputer.in");
    ofstream fout("supercomputer.out");

    uint32_t n, m;
    fin >> n >> m;

    // un vector cu gradele de intrarea fiecarui nod iar pentru optimizare o sa
    // il incrementez in momentul citiri grafului
    vector<uint32_t> degrees(n + 1, 0);

    // functi pentru citire
    vector<uint32_t> date = read_vector(fin, n);
    matrix graph = read_graph(fin, n, m, degrees);

    // solutia
    fout << min(solve_task(date, graph, degrees, 1),
                        solve_task(date, graph, degrees, 2)) - 1;

    return 0;
}
