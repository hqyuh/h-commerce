package com.shopme.admin.user.export;

import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), STANDARD_PREFERENCE);

        String[] csvHeader = {"UserID", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);
        for (User user : listUsers) {
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();
    }

}
