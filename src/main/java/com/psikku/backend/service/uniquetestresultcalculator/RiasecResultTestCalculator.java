package com.psikku.backend.service.uniquetestresultcalculator;

import com.psikku.backend.dto.useranswer.SubmittedAnswerDto;
import com.psikku.backend.entity.Answer;
import com.psikku.backend.entity.Question;
import com.psikku.backend.entity.TestResult;
import com.psikku.backend.exception.TestResultException;
import com.psikku.backend.service.answer.AnswerService;
import com.psikku.backend.service.question.QuestionService;
import com.psikku.backend.service.test.TestService;
import com.psikku.backend.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiasecResultTestCalculator implements UniqueResultTestCalculator{

    private final Logger logger;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final TestService testService;
    private final UserService userService;
    private final String name;
    private final ResourceLoader resourceLoader;
    private final String riasecPkuLocation;

    private String testResult;

    public RiasecResultTestCalculator(AnswerService answerService,
                                      QuestionService questionService,
                                      TestService testService,
                                      UserService userService,
                                      ResourceLoader resourceLoader,
                                      @Value("${riasec-pku.location}") String riasecPkuLocation) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.testService = testService;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(RiasecResultTestCalculator.class);
        this.name = "riasec";
        this.testResult = "";
        this.resourceLoader = resourceLoader;
        this.riasecPkuLocation = riasecPkuLocation;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TestResult calculateNewResult(List<SubmittedAnswerDto> surveyKarakterAnsDtoOnly){

        String[] surveyKarakterAnsSplit = surveyKarakterAnsDtoOnly.get(0).getQuestionId().split("_");
        String testName = surveyKarakterAnsSplit[0];
        List<Answer> surveyKarakterAnsFromDb =
                answerService.findByIdStartingWith(testName);
        List<Question> questionsFromDb = questionService.findByIdStartingWith(testName);

        int r = 0;
        int i = 0;
        int a = 0;
        int s = 0;
        int e = 0;
        int c = 0;

        // looping thru the submitted answer
        for (SubmittedAnswerDto answerDto : surveyKarakterAnsDtoOnly) {
            for (Answer answerFromDb : surveyKarakterAnsFromDb) {
                if(answerDto.getAnswers().get(0).equals(answerFromDb.getId())){
                    for (Question questionFromDb : questionsFromDb) {
                        if(answerDto.getQuestionId().equals(questionFromDb.getId())){
                            // check the answer category and add the category value (0 or 1) into num of category for outputting
                            if(answerFromDb.getAnswerCategory().equalsIgnoreCase("1")){
                                if(questionFromDb.getQuestionCategory().equalsIgnoreCase("r")){
                                    r++;
                                }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("i")){
                                    i++;
                                }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("a")){
                                    a++;
                                }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("s")){
                                    s++;
                                }else if(questionFromDb.getQuestionCategory().equalsIgnoreCase("e")){
                                    e++;
                                }else{
                                    c++;
                                }
                            }
                        }
                    }
                }
            }
        }

        Map<String,Integer> riasecResultMap = new HashMap<>();
        riasecResultMap.put("r",r);
        riasecResultMap.put("i",i);
        riasecResultMap.put("a",a);
        riasecResultMap.put("s",s);
        riasecResultMap.put("e",e);
        riasecResultMap.put("c",c);
        List<Map.Entry<String,Integer>> riasecResultMapList=
                riasecResultMap.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .skip(3)
                .collect(Collectors.toList());

        //outputting this result

        StringBuilder sb = new StringBuilder();
        String dominant = perCategoryPredicate(riasecResultMapList.get(2).getKey());
        sb.append("Tipe Dominan:");
        sb.append(dominant);
        sb.append(";");
        sb.append("deskripsi:");
        sb.append(getResultDescription(dominant));
        sb.append(";");
        sb.append("pekerjaan:");
        List<String> jobList = getCombinationData(
                                riasecResultMapList.get(2).getKey()+
                                riasecResultMapList.get(1).getKey()+
                                riasecResultMapList.get(0).getKey());
        for(int j=0; j<jobList.size(); j++){
            if(j==jobList.size()-1){
                sb.append(jobList.get(j));
            }
            else{
                sb.append(jobList.get(j)).append(",");
            }
        }
        setResult(sb.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        TestResult testResult = new TestResult();
        testResult.setUser(userService.findByUsername(username));
        testResult.setTest(testService.findTestByInternalName(testName));
        testResult.setResult(getResult());
        testResult.setResultCalculation("r:"+r+",i:"+i+",a:"+a+
                ",s:"+s+",e:"+e+",c:"+c);
        logger.info("username: '"+username+"' RIASEC test answer calculated successfully");
        return testResult;
    }

    private String perCategoryPredicate(String riasec){
        if(riasec.equalsIgnoreCase("r")){
            return "realistis";
        }else if(riasec.equalsIgnoreCase("i")){
            return "investigasi";
        }else if(riasec.equalsIgnoreCase("a")){
            return "artistik";
        }else if(riasec.equalsIgnoreCase("s")){
            return "sosial";
        }else if(riasec.equalsIgnoreCase("e")){
            return "enterprising";
        }else if(riasec.equalsIgnoreCase("c")){
            return "conventional";
        }else{
            logger.info("error riasec category");
            throw new TestResultException("error getting result");
        }
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

    private String getResultDescription(String dominantType){
        Resource resource = this.resourceLoader.getResource(this.riasecPkuLocation+"/riasec.pku");
        try(Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(resource.getInputStream())))){
            while(scanner.hasNextLine()){
                String pointer = scanner.nextLine();
                    if(pointer.equals(dominantType)){
                        pointer = scanner.nextLine();
                        if(pointer.equals("---")){
                            break;
                        }else{
                            return pointer;
                        }
                    }
            }
        }catch (IOException e){
            logger.error("error getting riasec *.pku file for dominant description");
            e.printStackTrace();
            throw new TestResultException("answer submission error for this test");
        }
        throw new TestResultException("answer submission error for this test");
    }

    @Override
    public String getResult() {
        return testResult;
    }

    @Override
    public void setResult(String testResult) {
        this.testResult = testResult;
    }
}
