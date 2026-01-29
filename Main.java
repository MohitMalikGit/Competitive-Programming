import java.util.*;
import java.io.*;

public class Main {
    static final int MOD = 998244353;
    static long[][] C; // Binomial coefficients
    
    public static void main(String[] args) throws IOException {
        precomputeBinomial(505);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        
        int[] cnt = new int[61]; // cnt[e] = count of reindeer with exponent e
        
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            int c = Integer.parseInt(st.nextToken());
            cnt[c]++;
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (int q = 0; q < m; q++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());
            long x = Long.parseLong(st.nextToken());
            
            if (type == 1) {
                cnt[(int)x]++;
            } else if (type == 2) {
                cnt[(int)x]--;
            } else {
                sb.append(solve(cnt, x)).append("\n");
            }
        }
        
        out.print(sb);
        out.close();
    }
    
    static void precomputeBinomial(int maxN) {
        C = new long[maxN + 1][maxN + 1];
        for (int i = 0; i <= maxN; i++) {
            C[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                C[i][j] = (C[i-1][j-1] + C[i-1][j]) % MOD;
            }
        }
    }
    
    static long solve(int[] cnt, long target) {
        // Get total count and list of non-empty exponents (high to low)
        int totalReindeer = 0;
        List<Integer> exponents = new ArrayList<>();
        for (int e = 60; e >= 0; e--) {
            if (cnt[e] > 0) {
                exponents.add(e);
                totalReindeer += cnt[e];
            }
        }
        
        if (totalReindeer == 0) return 0;
        
        // Calculate maximum achievable capacity to early exit
        long maxCap = 0;
        int[] sortedExp = new int[totalReindeer];
        int idx = 0;
        for (int e = 60; e >= 0; e--) {
            for (int j = 0; j < cnt[e]; j++) {
                sortedExp[idx++] = e;
            }
        }
        for (int i = 0; i < totalReindeer; i++) {
            int e = sortedExp[i];
            int pos = i + 1;
            int divisorExp = pos - 1;
            if (e >= divisorExp) {
                maxCap += 1L << (e - divisorExp);
            }
        }
        
        if (target > maxCap) return 0;
        
        // Use a marker for "capacity >= target"
        final long MARKER = Long.MAX_VALUE;
        
        // dp[pos] = map from capacity to count of ways
        // Process exponents from highest to lowest
        @SuppressWarnings("unchecked")
        Map<Long, Long>[] dp = new HashMap[totalReindeer + 1];
        for (int i = 0; i <= totalReindeer; i++) {
            dp[i] = new HashMap<>();
        }
        dp[0].put(0L, 1L);
        
        int maxPos = 0;
        
        for (int e : exponents) {
            int c = cnt[e];
            
            @SuppressWarnings("unchecked")
            Map<Long, Long>[] newDp = new HashMap[totalReindeer + 1];
            for (int i = 0; i <= totalReindeer; i++) {
                newDp[i] = new HashMap<>();
            }
            
            for (int pos = 0; pos <= maxPos; pos++) {
                for (Map.Entry<Long, Long> entry : dp[pos].entrySet()) {
                    long cap = entry.getKey();
                    long ways = entry.getValue();
                    
                    // If already at or above target, propagate the marker
                    if (cap == MARKER) {
                        for (int k = 0; k <= c; k++) {
                            int newPos = pos + k;
                            long mult = (ways * C[c][k]) % MOD;
                            newDp[newPos].merge(MARKER, mult, (a, b) -> (a + b) % MOD);
                        }
                        continue;
                    }
                    
                    // Choose k reindeer with exponent e (k = 0, 1, ..., c)
                    long addCap = 0;
                    for (int k = 0; k <= c; k++) {
                        int newPos = pos + k;
                        long newCap = cap + addCap;
                        
                        // Use MARKER if capacity >= target
                        if (newCap >= target) newCap = MARKER;
                        
                        long mult = (ways * C[c][k]) % MOD;
                        newDp[newPos].merge(newCap, mult, (a, b) -> (a + b) % MOD);
                        
                        // Calculate contribution for next reindeer at position pos + k + 1
                        if (k < c) {
                            int nextPos = pos + k + 1;
                            int divisorExp = nextPos - 1;
                            if (e >= divisorExp) {
                                addCap += 1L << (e - divisorExp);
                            }
                        }
                    }
                }
            }
            
            dp = newDp;
            maxPos += c;
        }
        
        // Count all non-empty subsets with capacity >= target (marked as MARKER)
        long ans = 0;
        for (int pos = 1; pos <= maxPos; pos++) {
            Long count = dp[pos].get(MARKER);
            if (count != null) {
                ans = (ans + count) % MOD;
            }
        }
        
        return ans;
    }
}