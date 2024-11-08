package org.investment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing File Repository:");
        ISecurityRepository fileRepo = createFileRepository();
        testRepository(fileRepo);
        System.out.println("\nTesting Database Repository:");
        try {
            ISecurityRepository dbRepo = new PostgreSQLRepository();
            testRepository(dbRepo);
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private static ISecurityRepository createFileRepository() {
        String filename = "securities.json";

        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }

        ISerializationStrategy strategy = new JsonSerializationStrategy();
        AbstractSecurityRepository fileRepository = new SecurityRepository(filename, strategy);
        return new SecurityRepositoryAdapter(fileRepository);
    }

    private static void testRepository(ISecurityRepository repository) {
        addSampleSecurities(repository);

        printSecurities(repository);
    }

    private static void addSampleSecurities(ISecurityRepository repository) {
        try {
            System.out.println("Adding sample securities...");

            repository.addSecurity(Security.createNewSecurity(
                    "Apple Inc.",
                    "Stock",
                    new BigDecimal("175.50"),
                    new BigDecimal("12.5")
            ));

            repository.addSecurity(Security.createNewSecurity(
                    "Microsoft Corp",
                    "Stock",
                    new BigDecimal("330.25"),
                    new BigDecimal("10.8")
            ));

            repository.addSecurity(Security.createNewSecurity(
                    "US Treasury Bond",
                    "Bond",
                    new BigDecimal("98.75"),
                    new BigDecimal("4.2")
            ));

            repository.addSecurity(Security.createNewSecurity(
                    "Corporate Bond XYZ",
                    "Bond",
                    new BigDecimal("101.25"),
                    new BigDecimal("5.5")
            ));

            repository.addSecurity(Security.createNewSecurity(
                    "S&P 500 ETF",
                    "ETF",
                    new BigDecimal("450.75"),
                    new BigDecimal("8.3")
            ));

            System.out.println("Sample securities added successfully.");

        } catch (Exception e) {
            System.err.println("Error adding sample securities: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printSecurities(ISecurityRepository repository) {
        try {
            System.out.println("\nRetrieving securities...");

            List<BriefSecurity> securities = repository.get_k_n_short_list(1, 10, "name");

            System.out.println("\nAll securities (brief info):");
            for (BriefSecurity security : securities) {
                System.out.println(security);
            }

            int totalCount = repository.get_count();
            System.out.println("\nTotal number of securities: " + totalCount);

            if (!securities.isEmpty()) {
                int firstId = securities.get(0).getSecurityId();
                Security fullInfo = repository.getById(firstId);
                System.out.println("\nDetailed info for first security:");
                System.out.println("ID: " + fullInfo.getSecurityId());
                System.out.println("Name: " + fullInfo.getName());
                System.out.println("Type: " + fullInfo.getType());
                System.out.println("Current Price: $" + fullInfo.getCurrentPrice());
                System.out.println("Expected Return: " + fullInfo.getExpectedReturn() + "%");
            }

        } catch (Exception e) {
            System.err.println("Error retrieving securities: " + e.getMessage());
            e.printStackTrace();
        }
    }
}