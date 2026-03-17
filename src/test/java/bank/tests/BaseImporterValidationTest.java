// bank.tests.BaseImporterValidationTest
package bank.tests;

import Bank.domain.model.BankAccount;
import Bank.service.Files.CsvImporter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BaseImporterValidationTest {

    @Test
    public void shouldSkipRecordsWithMissingRequiredFields() {
        CsvImporter importer = new CsvImporter();
        String csvData = "id,name\n" +
                "a1-0000-0000-0000-000000000001,Тест";

        List<BankAccount> accounts = importer.importAccounts(csvData);

        assertTrue(accounts.isEmpty());
    }

    @Test
    public void shouldSkipRecordsWithNullId() {
        CsvImporter importer = new CsvImporter();
        String csvData = "id,name,balance\n" +
                ",Тест,100.00";

        List<BankAccount> accounts = importer.importAccounts(csvData);

        assertTrue(accounts.isEmpty());
    }
}