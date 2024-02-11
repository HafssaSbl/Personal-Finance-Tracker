import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PersonalFinanceTracker {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez le chemin du fichier de transactions : ");
        String transactionFilePath = scanner.nextLine();

        System.out.print("Entrez le chemin de sortie pour le rapport : ");
        String reportOutputPath = scanner.nextLine();

        processTransactions(transactionFilePath, reportOutputPath);

        scanner.close();
    }

    public static void processTransactions(String transactionFilePath, String reportOutputPath) {
        Map<String, Double> expensesByCategory = new HashMap<>();
        double totalIncome = 0;
        double totalExpenses = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(transactionFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionDetails = line.split(",");
                if (transactionDetails.length != 4) {
                    System.err.println("Format de transaction invalide: " + line);
                    continue;
                }

                String type = transactionDetails[1];
                String category = transactionDetails[2];
                double amount = Double.parseDouble(transactionDetails[3]);

                if (type.equals("ACHAT")) {
                    totalExpenses += amount;
                    expensesByCategory.put(category, expensesByCategory.getOrDefault(category, 0.0) + amount);
                } else if (type.equals("DÉPENSE")) {
                    totalIncome += amount;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier de transactions: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        double netSavings = totalIncome - totalExpenses;

        generateReport(reportOutputPath, totalIncome, totalExpenses, netSavings, expensesByCategory);
    }

    public static void generateReport(String reportOutputPath, double totalIncome, double totalExpenses, double netSavings,
                                      Map<String, Double> expensesByCategory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportOutputPath))) {

            writer.println("Rapport financier");
            writer.println("=================");
            writer.println();

            writer.println("Total Income: " + totalIncome);

            writer.println("Total Expenses: " + totalExpenses);

            writer.println("Net Savings: " + netSavings);

            writer.println("Expenses by Category:");
            for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du rapport: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
