package com.psikku.backend.service.report;

import com.psikku.backend.entity.TestResult;
import com.psikku.backend.entity.User;
import com.psikku.backend.entity.Voucher;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.service.testresult.TestResultService;
import com.psikku.backend.service.user.UserService;
import com.psikku.backend.service.voucher.VoucherService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {


    private final ResourceLoader resourceLoader;
    private final String riasecPkuLocation;
    private final Logger logger;
    private final UserService userService;
    private final TestResultService testResultService;
    private final String path;
    private final VoucherService voucherService;

    @Autowired
    public ReportServiceImpl(ResourceLoader resourceLoader,
                             @Value("${riasec-pku.location}") String riasecPkuLocation,
                             UserService userService,
                             TestResultService testResultService,
                             VoucherService voucherService) throws IOException{
        this.resourceLoader = resourceLoader;
        this.riasecPkuLocation = riasecPkuLocation;
        this.userService = userService;
        this.testResultService = testResultService;
        this.voucherService = voucherService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.path = resourceLoader.getResource("classpath:static/report").getURI().getPath();
    }

    @Override
    public void generateReportByUsernameAndVoucher(String username, String voucher) {

        User user = userService.findByUsername(username);

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(path+"/template"+"/psycheFix.jrxml");
            pdfSingleReportExporter(jasperReport,user,voucher);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateAllReportByVoucher(String voucher){

        Voucher voucherFromDb = voucherService.getVoucherByCode(voucher);
        JasperReport jasperReport;

        List<User> userList = voucherFromDb.getUserList();

        try {
            jasperReport = JasperCompileManager.compileReport(path+"/psycheFix.jrxml");
            for (User user : userList) {
                System.out.println("username: "+user.getUsername());
                pdfSingleReportExporter(jasperReport,user,voucher);
            }

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private List<TestResult> getTestResultByUserAndVoucher(User user,String voucher){
        return testResultService.findAllResultByVoucherAndUsername(user,voucher);
    }

    private void pdfSingleReportExporter(JasperReport jasperReport, User user, String voucher){

        List<TestResult> testResultList = getTestResultByUserAndVoucher(user,voucher);
        if(testResultList.size()>0){

            JasperPrint jasperPrint;
            try {

                jasperPrint = JasperFillManager.fillReport(jasperReport,parametersBuilder(user,testResultList), new JREmptyDataSource(1));

                JRPdfExporter exporter = new JRPdfExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

                String pdfFileName = user.getUsername();
                //        Resource resource = resourceLoader.getResource("classpath:static/report");
                exporter.setExporterOutput(
                        new SimpleOutputStreamExporterOutput(path+"/"+pdfFileName+".pdf"));

                SimplePdfReportConfiguration reportConfig
                        = new SimplePdfReportConfiguration();
                reportConfig.setSizePageToContent(true);
                reportConfig.setForceLineBreakPolicy(false);


                SimplePdfExporterConfiguration exportConfig
                        = new SimplePdfExporterConfiguration();
                exportConfig.setMetadataAuthor("Psyche Indonesia");
                exportConfig.setEncrypted(false);
                exportConfig.setAllowedPermissionsHint("PRINTING");

                exporter.setConfiguration(reportConfig);
                exporter.setConfiguration(exportConfig);

                exporter.exportReport();

            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String,Object> parametersBuilder(User user, List<TestResult> testResultList){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fullname",user.getFirstname()+" "+user.getLastname());
        parameters.put("sex",user.getSex().equalsIgnoreCase("male")? "laki-laki" : "perempuan");
        parameters.put("dateOfBirth",user.getDateOfBirth().toString());
        parameters.put("address",user.getAddress()+" "+user.getCity()+" "+user.getProvince());

        int iqResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("cfit"))
                .map(result->Integer.parseInt(result.getResult().split(",")[0].split(":")[1]))
                .findAny()
                .orElse(0);

        parameters.put("iqResult",iqResult);

        String[] eqResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("eq"))
                .map(result->result.getResult().split(","))
                .findAny()
                .orElse(new String[0]);

        if(eqResult.length>0){

            Double eqTotalResult = Double.parseDouble(eqResult[eqResult.length-1].split(":")[1]);
            parameters.put("eqTotalResult",eqTotalResult);

            Double eqMengenalEmosiDiriResult = Double.parseDouble(eqResult[0].split(":")[1]);
            parameters.put("eqMengenalEmosiDiriResult",eqMengenalEmosiDiriResult);

            Double eqMengelolaEmosiDiriResult = Double.parseDouble(eqResult[1].split(":")[1]);
            parameters.put("eqMengelolaEmosiDiriResult",eqMengelolaEmosiDiriResult);

            Double eqMemotivasiDiriResult = Double.parseDouble(eqResult[2].split(":")[1]);
            parameters.put("eqMemotivasiDiriResult",eqMemotivasiDiriResult);

            Double eqMengenalEmosiOrangLainResult = Double.parseDouble(eqResult[3].split(":")[1]);
            parameters.put("eqMengenalEmosiOrangLainResult",eqMengenalEmosiOrangLainResult);

            Double eqMembinaHubunganResult = Double.parseDouble(eqResult[4].split(":")[1]);
            parameters.put("eqMembinaHubunganResult",eqMembinaHubunganResult);
        }


        String gayaBelajar1FullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("belajar1"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!gayaBelajar1FullResult.equals("")){
            String gb1Result = gayaBelajar1ResultCalc(gayaBelajar1FullResult).get(0).getKey();
            parameters.put("gayaBelajar1Result",gb1Result);
        }

        String gayaBelajar2FullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("belajar2"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!gayaBelajar2FullResult.equals("")){
            parameters.put("gayaBelajar2Result",gayaBelajar2ResultCalc(gayaBelajar2FullResult).get(0).split(":")[1]);
            parameters.put("kombinasiGb2",gayaBelajar2ResultCalc(gayaBelajar2FullResult).get(1).split(":")[1]);
        }

        String bullyFullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("bully"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!bullyFullResult.equals("")){

            Map.Entry<String, Double> fisik = bullyResultCalc(bullyFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("fisik"))
                    .findAny().orElse(null);

            Map.Entry<String, Double> verbal = bullyResultCalc(bullyFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("verbal"))
                    .findAny().orElse(null);

            Map.Entry<String, Double> nonVerbal = bullyResultCalc(bullyFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("nonVerbal"))
                    .findAny().orElse(null);

            Map.Entry<String, Double> relasional = bullyResultCalc(bullyFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("relasional"))
                    .findAny().orElse(null);

            Map.Entry<String, Double> cyber = bullyResultCalc(bullyFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("cyber"))
                    .findAny().orElse(null);

            parameters.put("bullyFisikResult", fisik.getValue());
            parameters.put("bullyVerbalResult", verbal.getValue());
            parameters.put("bullyNonVerbalResult", nonVerbal.getValue());
            parameters.put("bullyRelasionalResult", relasional.getValue());
            parameters.put("bullyCyberResult", cyber.getValue());

        }

        String surveyKarakterFullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("karakter"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!surveyKarakterFullResult.equalsIgnoreCase("")){

            Map.Entry<String, Integer> toleransi = surveyKarakterCalc(surveyKarakterFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("toleransi"))
                    .findAny().orElse(null);

            Map.Entry<String, Integer> gotongRoyong = surveyKarakterCalc(surveyKarakterFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("gotongroyong"))
                    .findAny().orElse(null);

            Map.Entry<String, Integer> nasionalisme = surveyKarakterCalc(surveyKarakterFullResult).stream()
                    .filter(x -> x.getKey().contains("wellBeing"))
                    .findAny().orElse(null);

            Map.Entry<String, Integer> pluralisme = surveyKarakterCalc(surveyKarakterFullResult).stream()
                    .filter(x -> x.getKey().equalsIgnoreCase("pluralisme"))
                    .findAny().orElse(null);


            parameters.put("skToleransiResult",toleransi.getValue());
            parameters.put("skGotongRoyongResult",gotongRoyong.getValue());
            parameters.put("skNasionalismeResult",nasionalisme.getValue());
            parameters.put("skPluralismeResult",pluralisme.getValue());
        }

        String covidFullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("covid"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!covidFullResult.equals("")){
            parameters.put("covidTestResult",covidCalc(covidFullResult));
        }

        String kecemasanFullResultCalculation = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("stateanxiety"))
                .map(result->result.getResultCalculation())
                .findAny()
                .orElse("");

        if(!kecemasanFullResultCalculation.equals("")){
            double kecemasanResultPercentage = kecemasanCalc(kecemasanFullResultCalculation);
            String kecemasanResult;
            if(kecemasanResultPercentage<33){
                kecemasanResult = "KECEMASAN RINGAN";
            }else if(kecemasanResultPercentage<66){
                kecemasanResult = "KECEMASAN SEDANG";
            }else{
                kecemasanResult = "KECEMASAN BERAT";
            }
            parameters.put("kecemasanResult",kecemasanResult);
            parameters.put("kecemasanResultPercentage",kecemasanResultPercentage);
        }

        String bakatFullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("bakat"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        if(!bakatFullResult.equals("")){
        Map.Entry<String,Double> exact = bakatCalc(bakatFullResult).stream()
                .filter(x -> x.getKey().equalsIgnoreCase("eksak"))
                .findAny()
                .orElse(null);
        Map.Entry<String,Double> nonExact = bakatCalc(bakatFullResult).stream()
                .filter(x -> x.getKey().equalsIgnoreCase("nonEksak"))
                .findAny()
                .orElse(null);
        Map.Entry<String,Double> literasi = bakatCalc(bakatFullResult).stream()
                .filter(x -> x.getKey().equalsIgnoreCase("literasi"))
                .findAny()
                .orElse(null);
        Map.Entry<String,Double> numerasi = bakatCalc(bakatFullResult).stream()
                .filter(x -> x.getKey().equalsIgnoreCase("numerasi"))
                .findAny()
                .orElse(null);

            parameters.put("kognitifExactResult",exact.getValue());
            parameters.put("kognitifNonExactResult",nonExact.getValue());
            parameters.put("kognitifLiterasiResult",literasi.getValue());
            parameters.put("kognitifNumerasiResult",numerasi.getValue());
        }

        String riasecFullResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("riasec"))
                .map(result->result.getResult())
                .findAny()
                .orElse("");

        String riasecFullCalculateResult = testResultList.stream()
                .filter(result->result.getTest().getInternalName().contains("riasec"))
                .map(result->result.getResultCalculation())
                .findAny()
                .orElse("");

        if(!riasecFullResult.equals("")){
            parameters.put("riasecResult",riasecCalc(riasecFullResult).toUpperCase());
            System.out.println(getCombinationRiasec(riasecFullCalculateResult));
            String jobList = getCombinationData(getCombinationRiasec(riasecFullCalculateResult)).toString();
            String cleanJobList = jobList.substring(1,jobList.length()-1);
            parameters.put("jobRecommendation",cleanJobList);
        }
        return parameters;
    }

    private List<Map.Entry<String,Integer>> gayaBelajar1ResultCalc(String result){

        int visual = Integer.parseInt(result.split(",")[0].split(":")[1]);
        int auditori = Integer.parseInt(result.split(",")[1].split(":")[1]);
        int kinestetik = Integer.parseInt(result.split(",")[2].split(":")[1]);


        Map<String,Integer> hasil = new HashMap<>();
        hasil.put("VISUAL",visual);
        hasil.put("AUDITORI",auditori);
        hasil.put("KINESTETIK",kinestetik);
        return hasil.entrySet().stream()
                .sorted((x,y)->y.getValue()-x.getValue())
                .collect(Collectors.toList());
    }

    private List<String> gayaBelajar2ResultCalc(String result){
        List<String> resultList = Arrays.asList(result.split(","));
        return resultList;
    }

    private List<Map.Entry<String,Double>> bullyResultCalc(String result){

        double fisik = Double.parseDouble(result.split(",")[0].split(":")[1]);
        double verbal = Double.parseDouble(result.split(",")[1].split(":")[1]);
        double nonVerbal = Double.parseDouble(result.split(",")[2].split(":")[1]);
        double relasional = Double.parseDouble(result.split(",")[3].split(":")[1]);
        double cyber = Double.parseDouble(result.split(",")[4].split(":")[1]);

        Map<String,Double> hasil = new HashMap<>();
        hasil.put("fisik",fisik);
        hasil.put("verbal",verbal);
        hasil.put("nonVerbal",nonVerbal);
        hasil.put("relasional",relasional);
        hasil.put("cyber",cyber);

        return new ArrayList<>(hasil.entrySet());
    }

    private List<Map.Entry<String,Integer>> surveyKarakterCalc(String result){

        int toleransi = Integer.parseInt(result.split(",")[0].split(":")[1]);
        int gotongRoyong = Integer.parseInt(result.split(",")[1].split(":")[1]);
        int wellBeing = Integer.parseInt(result.split(",")[2].split(":")[1]);
        int pluralisme = Integer.parseInt(result.split(",")[3].split(":")[1]);

        Map<String,Integer> hasil = new HashMap<>();
        hasil.put("toleransi",toleransi);
        hasil.put("gotongRoyong",gotongRoyong);
        hasil.put("wellBeing",wellBeing);
        hasil.put("pluralisme",pluralisme);

        return new ArrayList<>(hasil.entrySet());

    }

    private String covidCalc(String result){

        return result.split(":")[1].toUpperCase();

    }

    private double kecemasanCalc(String calculateResult){

        double result = Double.parseDouble(calculateResult.split(":")[1]);

        return (result*100)/80;

    }

    private List<Map.Entry<String,Double>> bakatCalc(String result){

        double eksak = Double.parseDouble(result.split(",")[4].split(":")[1]);
        double nonEksak = Double.parseDouble(result.split(",")[5].split(":")[1]);
        double literasi = Double.parseDouble(result.split(",")[6].split(":")[1]);
        double numerasi = Double.parseDouble(result.split(",")[7].split(":")[1]);

        Map<String,Double> hasil = new HashMap<>();
        hasil.put("eksak",eksak);
        hasil.put("nonEksak",nonEksak);
        hasil.put("literasi",literasi);
        hasil.put("numerasi",numerasi);

        return new ArrayList<>(hasil.entrySet());

    }

    private String riasecCalc(String result){
        return result.split(";")[0].split(":")[1];
    }

    private String getCombinationRiasec(String resultCalculation){
        int r = Integer.parseInt(resultCalculation.split(",")[0].split(":")[1]);
        int i = Integer.parseInt(resultCalculation.split(",")[1].split(":")[1]);;
        int a = Integer.parseInt(resultCalculation.split(",")[2].split(":")[1]);;
        int s = Integer.parseInt(resultCalculation.split(",")[3].split(":")[1]);;
        int e = Integer.parseInt(resultCalculation.split(",")[4].split(":")[1]);;
        int c = Integer.parseInt(resultCalculation.split(",")[5].split(":")[1]);;

        Map<String,Integer> resultIntMap = new HashMap<>();
        resultIntMap.put("r",r);
        resultIntMap.put("i",i);
        resultIntMap.put("a",a);
        resultIntMap.put("s",s);
        resultIntMap.put("e",e);
        resultIntMap.put("c",c);

        List<Map.Entry<String,Integer>> test = resultIntMap.entrySet().stream()
                .sorted((x,y)->y.getValue()-x.getValue())
                .collect(Collectors.toCollection(LinkedList::new));

        return test.get(0).getKey()+test.get(1).getKey()+test.get(2).getKey();
    }

    private List<String> getCombinationData(String combination){
        String filePrefix = combination.substring(0,1);
        Resource dataResource = resourceLoader.getResource(this.riasecPkuLocation+"/"+filePrefix+".pku");
        List<String> jobList = new ArrayList<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(dataResource.getInputStream())))){
            String eachLine;
            while(scanner.hasNextLine()){
                eachLine = scanner.nextLine();
                if(eachLine.equalsIgnoreCase("code-"+combination)){
                    String job;
                    while(scanner.hasNextLine()){
                        job = scanner.nextLine();
                        if(job.equalsIgnoreCase("---")){
                            break;
                        }
                        jobList.add(job);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            logger.error("error getting the riasec *.pku file from classpath");
            throw new TestResultException("Result error please check console");
        }
        return jobList;
    }
}
