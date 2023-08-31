#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int mod = 1000000007;

int type1(int x, int y) {
	vector<vector<int>> dp(x + 1, vector<int> (y + 1));

	for (int i = 1; i <= x; i++) {
		dp[i][0] = 1;
	}

	for (int i = 1; i <= x; i++) {
		for (int j = 1; j <= min(i, y); j++) {
			if (j == i) {
				dp[i][j] = i + 1;
			} else if (j - 1 == i) {
				dp[i][j] = 1;
			} else {
				dp[i][j] = (dp[i - 1][j] + dp[i - 1][j - 1]) % mod;
			}
		}
	}

    return dp[x][y];
}

int type2(int x, int y) {
	vector<vector<int>> dp(x + 1, vector<int> (y + 1));
	for (int i = 1; i <= x; i++) {
		dp[i][0] = 1;
	}

	dp[0][1] = 1;
	dp[0][2] = 1;

	int limit = 4;
	for (int i = 1; i <= x; i++) {
		if(i < 2) {
			for (int j = 1; j <= min(limit, y); j++) {
				if (j > 2) {
					dp[i][j] = ((dp[i][j - 1] % mod) + (dp[i - 1][j] % mod) - 1) % mod;
				} else {
					dp[i][j] = ((dp[i][j - 1] % mod) + (dp[i - 1][j] % mod)) % mod;
				}
			}
		} else {
			int k = 0;
			for (int j = 1; j <= min(limit, y); j++) {
				if (j > 2) {
					dp[i][j] = ((dp[i][j - 1] % mod) + (dp[i - 1][j] % mod)) % mod;
					dp[i][j] = (dp[i][j] - (dp[i - 1][k] % mod) + mod) % mod;
					k++;
				} else {
					dp[i][j] = ((dp[i][j - 1] % mod) + (dp[i - 1][j] % mod)) % mod;
				}
			}
		}
		limit += 2;
	}

    return dp[x][y];
}

int main() {
    freopen("semnale.in", "r", stdin);
	freopen("semnale.out", "w", stdout);

	int sig_type, x, y;

	cin >> sig_type >> x >> y;

    switch (sig_type) {
		case 1:
			cout << type1(x, y);;
			break;
		case 2:
			cout << type2(x, y);
			break;
		default:
			cout << "wrong task number" << endl;
	}

    return 0;
}
