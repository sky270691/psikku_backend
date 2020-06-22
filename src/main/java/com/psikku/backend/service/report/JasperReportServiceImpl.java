package com.psikku.backend.service.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperReportServiceImpl implements JasperReportService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void generateReport() {

        try {
            String path = resourceLoader.getResource("classpath:static/report/").getURI().getPath();
            JasperReport jasperReport = JasperCompileManager.compileReport(path+"/Blank_A4.jrxml");
//            JRSaver.saveObject(jasperReport, "bangke.jasper");
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("komoResult",85.00);
//            parameters.put("komoResult2",75.00);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, new JREmptyDataSource(1));

            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput("employeeReport.pdf"));
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"/temp1.pdf");

            SimplePdfReportConfiguration reportConfig
                    = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);

            SimplePdfExporterConfiguration exportConfig
                    = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataAuthor("baeldung");
            exportConfig.setEncrypted(true);
            exportConfig.setAllowedPermissionsHint("PRINTING");

            exporter.setConfiguration(reportConfig);
            exporter.setConfiguration(exportConfig);

            exporter.exportReport();

        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }
}
