import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

public class kCliqueToSAT {

    /**
     *
     * @param G graficul din care luam numarul de noduri pentru care
     *         ne vom asigura ca exista un "al i-elea nod" in clica
     * @param k dimensiunea clicai pe care o cautam
     * @return K clauze
     */
    public static String verificNodi(Graf G, int k) {
        StringBuilder expresie = new StringBuilder();

        for (int i = 0; i < k; i++) {
            expresie.append("(");
            for (int j = 0; j < G.V; j++) {
                expresie.append(SAT.creazaLiteral(1, i + 1, j + 1));
                expresie.append("v");
            }
            expresie.append(")");
            expresie.append("^");
        }

        return  expresie.delete(expresie.length() - 1, expresie.length()).toString();
    }

    public static String elementeUnice(Graf G, int k) {
        StringBuilder expresie = new StringBuilder();

        for (int l = 0; l < G.V; l++) {
            for (int i = 0; i < k - 1; i++) {
                for (int j = i + 1; j < k; j++) {
                    expresie.append("(");
                    expresie.append(SAT.creazaLiteral(-1, i + 1, l + 1));
                    expresie.append("v");
                    expresie.append(SAT.creazaLiteral(-1, j + 1, l + 1));
                    expresie.append(")");
                    expresie.append("^");
                }
            }
        }

        return expresie.delete(expresie.length() - 1, expresie.length()).toString();
    }

    public static String verificareClica(Graf G, int k) {
        StringBuilder expresie = new StringBuilder();

        for (int l = 0; l < G.V - 1; l++) {
            for (int m = l + 1; m < G.V; m++) {
                if (G.E[l][m] == 0 && l != m) {
                    for (int i = 0; i < k - 1; i++) {
                        for (int j = i + 1; j < k; j++) {
                            expresie.append("(");
                            expresie.append(SAT.creazaLiteral(-1, i + 1, l + 1));
                            expresie.append("v");
                            expresie.append(SAT.creazaLiteral(-1, j + 1, m + 1));
                            expresie.append(")");
                            expresie.append("^");

                            expresie.append("(");
                            expresie.append(SAT.creazaLiteral(-1, j + 1, l + 1));
                            expresie.append("v");
                            expresie.append(SAT.creazaLiteral(-1, i + 1, m + 1));
                            expresie.append(")");
                            expresie.append("^");
                        }
                    }
                }
            }
        }

        return expresie.delete(expresie.length() - 1, expresie.length()).toString();
    }

    public static void main(String[] args) {
        BufferedReader br = null;
        FileWriter fw = null;
        try {
            br = new BufferedReader(new FileReader(args[0]));
            String line = br.readLine();
            String[] content = line.split(" ");

            int v = Integer.parseInt(content[0]);
            int k = Integer.parseInt(content[1]);
            int[][] e = new int[v][v];

            int currVertex = 0;

            while ((line = br.readLine()) != null) {
                if(!line.isEmpty()) {
                    content = line.split(" ");

                    for (int i = 0; i < content.length; i++) {
                        int y = Integer.parseInt(content[i]);
                        e[currVertex][y - 1] = 1;
                        e[y - 1][currVertex] = 1;
                    }
                    currVertex++;
                }
            }

            Graf G = Graf.citesteGraf(v, e);
            String ans = verificNodi(G, k) + "^" + elementeUnice(G, k) + "^" + verificareClica(G, k);
            String ret = SAT.convertSATtoDIMACS(ans);

            fw = new FileWriter(args[1]);
            fw.write(ret);

            fw.close();
            br.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
class Graf {
    public int V;
    public int[][] E;

    private Graf(int v, int[][] e) {
        V = v;
        E = e;
    }

    public static Graf citesteGraf(int v, int[][] e) {
        return new Graf(v, e);
    }
}

class SAT {
    public static String creazaLiteral(int tip, int index1, int index2) {
        if (tip == 1)
            return "x" + index1 + "," + index2;
        else return "-x" + index1 + "," + index2;
    }

    /**
     * numarul de variabile in calculez ca find combinari de n + 1 luate de cate 2 unde n este numarul de noduri din graf
     *
     * @param sat sirul de operati logice care trebuie convertit in formatul DIMACS
     * @return daca nu merge inseamna ca am dublicate de forma 1 2 0 // 2 1 0 // faci array de int-uri le sortezi si inainte de append verifici daca nu mai ai array-ull asta undeva  + hashmap de  String si Arraylist
     */
    public static String convertSATtoDIMACS(String sat) {
        HashMap<String, Integer> dictionaryForLiterals = new HashMap<>();
        StringBuilder ans = new StringBuilder("p cnf Y\n");
        String[] clause = sat.split("\\^");
        int numarLiterari = 1;
        int nrClauze = 0;

        for (int i = 0; i < clause.length; i++) {
            clause[i] = clause[i].replace("(", "");
            clause[i] = clause[i].replace(")", "");
            String[] literari = clause[i].split("v");
            for (int j = 0; j < literari.length; j++) {
                if (literari[j].contains("-")) {
                    ans.append("-");
                    literari[j] = literari[j].replace("-", "");
                }

                if (dictionaryForLiterals.containsKey(literari[j])) {
                    ans.append(dictionaryForLiterals.get(literari[j])).append(" ");
                } else {
                    dictionaryForLiterals.put(literari[j], numarLiterari);
                    ans.append(dictionaryForLiterals.get(literari[j])).append(" ");
                    numarLiterari++;
                }

            }
            ans.append("0\n");
            nrClauze++;
        }
        int max1 = Collections.max(dictionaryForLiterals.values());
        ans.replace(6, 7, max1 + " " +nrClauze);

        return ans.toString();
    }
}

