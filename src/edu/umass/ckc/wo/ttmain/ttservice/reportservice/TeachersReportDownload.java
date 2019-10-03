package edu.umass.ckc.wo.ttmain.ttservice.reportservice;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ResourceBundle;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import edu.umass.ckc.wo.beans.StudentDetails;
import edu.umass.ckc.wo.beans.SurveyQuestionDetails;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;

/**
 * Created by nsmenon on 6/6/2017.
 */

public class TeachersReportDownload extends AbstractXlsView {


	protected ResourceBundle rb = null;
	
	@Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String reportType = (String) map.get("reportType");
        System.out.println("buildExcelDocument");
        try {

        	// Multi=lingual enhancement
        	rb = ResourceBundle.getBundle("MathSpring",httpServletRequest.getLocale());

            System.out.println("buildExcelDocument");

        	if (reportType.equals("perStudentReportDownload"))
        		buildPerStudentTeacherReport(map, workbook, httpServletRequest, httpServletResponse);
        	else if (reportType.equals("perProblmSetReportDownload"))
        		buildPerProblemSetTeacherReport(map, workbook, httpServletRequest, httpServletResponse);
        	else if (reportType.equals("perProblemReportDownload"))
        		buildPerProblemReport(map, workbook, httpServletRequest, httpServletResponse);
        	else if (reportType.equals("perClusterReport"))
        		buildPerClusterReport(map, workbook, httpServletRequest, httpServletResponse);
        	else if(reportType.equals("studentInfoDownload"))
        		buildStudentTagsForDownload(map, workbook, httpServletRequest, httpServletResponse);
        	else if(reportType.equals("perStudentEmotion"))
        		buildEmotionReportForDownload(map, workbook, httpServletRequest, httpServletResponse);
        	else if(reportType.equals("perSummSurveyReport"))
        		buildSummSurveyReport(map, workbook, httpServletRequest, httpServletResponse);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        	
        }
    }

    private void buildEmotionReportForDownload(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"student_emotions" + classId + ".xls\"");
            Map<String, List<String[]>> emotionReportForDownload = (Map<String, List<String[]>>) map.get("dataForEmotionReport");
            AtomicInteger atomicIntegerForHeaderForData = new AtomicInteger(4);
            Sheet sheet = workbook.createSheet(classId);
            Row header = sheet.createRow(3);
            Cell studentIdHeader = header.createCell(3);
            studentIdHeader.setCellValue(rb.getString("student_id"));
            Cell userNameHeader = header.createCell(4);
            userNameHeader.setCellValue(rb.getString("username"));
            Cell problemIDHeader = header.createCell(5);
            problemIDHeader.setCellValue(rb.getString("after_problem"));
            Cell topicIdHeader = header.createCell(6);
            topicIdHeader.setCellValue(rb.getString("topic_id"));
            Cell topicDescriptionHeader = header.createCell(7);
            topicDescriptionHeader.setCellValue(rb.getString("topic_description"));
            Cell timeHeader = header.createCell(8);
            timeHeader.setCellValue(rb.getString("timestamp"));
            Cell problemNameHeader = header.createCell(9);
            problemNameHeader.setCellValue(rb.getString("problem_name"));
            Cell problemNickName = header.createCell(10);
            problemNickName.setCellValue(rb.getString("problem_nick_name"));
            Cell standardIDHeader = header.createCell(11);
            standardIDHeader.setCellValue(rb.getString("standard_id"));
            Cell diffLevel = header.createCell(12);
            diffLevel.setCellValue(rb.getString("difficulty_level"));
            Cell emotionNameHeader = header.createCell(13);
            emotionNameHeader.setCellValue(rb.getString("emotion_recorded"));
            Cell emotionValuesHeader = header.createCell(14);
            emotionValuesHeader.setCellValue(rb.getString("emotion_level"));
            Cell emotionCOmmentsHeader = header.createCell(15);
            emotionCOmmentsHeader.setCellValue(rb.getString("emotion_comments"));
            emotionReportForDownload.forEach((student, emotionValues) -> {
                emotionValues.forEach(emotionArray ->{
                    Row dataRow = sheet.createRow(atomicIntegerForHeaderForData.getAndIncrement());
                    Cell studentId = dataRow.createCell(3);
                    studentId.setCellValue(emotionArray[0]);
                    Cell userName = dataRow.createCell(4);
                    userName.setCellValue(emotionArray[1]);
                    Cell problemID = dataRow.createCell(5);
                    problemID.setCellValue(emotionArray[2]);
                    Cell topicId = dataRow.createCell(6);
                    topicId.setCellValue(emotionArray[3]);
                    Cell topicDescription = dataRow.createCell(7);
                    topicDescription.setCellValue(emotionArray[4]);
                    Cell time = dataRow.createCell(8);
                    time.setCellValue(emotionArray[5]);
                    Cell problemName = dataRow.createCell(9);
                    problemName.setCellValue(emotionArray[6]);
                    Cell problemNickNameValues = dataRow.createCell(10);
                    problemNickNameValues.setCellValue(emotionArray[7]);
                    Cell standardID = dataRow.createCell(11);
                    standardID.setCellValue(emotionArray[8]);
                    Cell diffLevelValues = dataRow.createCell(12);
                    diffLevelValues.setCellValue(emotionArray[9]);
                    Cell emotionName = dataRow.createCell(13);
                    emotionName.setCellValue(emotionArray[10]);
                    Cell emotionValue = dataRow.createCell(14);
                    emotionValue.setCellValue(emotionArray[11]);
                    Cell emotionComments = dataRow.createCell(15);
                    emotionComments.setCellValue(emotionArray[12]);
                });
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buildStudentTagsForDownload(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"student_tags" + classId + ".xls\"");
            List<EditStudentInfoForm> studentInfoForTags = (List<EditStudentInfoForm>) map.get("dataForProblem");
            Sheet sheet = workbook.createSheet(classId);
            AtomicInteger atomicIntegerForNewRow = new AtomicInteger(2);
            AtomicInteger atomicIntegerForRowSplit = new AtomicInteger(0);

            CellStyle classNamestyle = workbook.createCellStyle();
            classNamestyle.setBorderLeft(BorderStyle.DASHED);
            classNamestyle.setBorderTop(BorderStyle.DASHED);

            CellStyle classNameValueStyle = workbook.createCellStyle();
            classNameValueStyle.setBorderRight(BorderStyle.DASHED);
            classNameValueStyle.setBorderTop(BorderStyle.DASHED);

            CellStyle headerStyles = workbook.createCellStyle();
            headerStyles.setBorderLeft(BorderStyle.DASHED);

            CellStyle headerStylesValue = workbook.createCellStyle();
            headerStylesValue.setBorderRight(BorderStyle.DASHED);

            CellStyle urlStyleHeader = workbook.createCellStyle();
            urlStyleHeader.setBorderLeft(BorderStyle.DASHED);
            urlStyleHeader.setBorderBottom(BorderStyle.DASHED);

            CellStyle urlStyleHeaderValue = workbook.createCellStyle();
            urlStyleHeaderValue.setBorderRight(BorderStyle.DASHED);
            urlStyleHeaderValue.setBorderBottom(BorderStyle.DASHED);

            studentInfoForTags.forEach(studentInfoForm -> {
                int currentValue = atomicIntegerForRowSplit.getAndIncrement();

                if(currentValue%2 == 0){
                    Row className = sheet.createRow(atomicIntegerForNewRow.get());
                    Cell classNameHeader = className.createCell(0);
                    classNameHeader.setCellValue(studentInfoForm.getClassName());
                    classNameHeader.setCellStyle(classNamestyle);
                    Cell classNameValue= className.createCell(1);
                    classNameValue.setCellValue("");
                    classNameValue.setCellStyle(classNameValueStyle);

                    Row fname = sheet.createRow(atomicIntegerForNewRow.get()+1);
                    Cell fnameLabel = fname.createCell(0);
                    fnameLabel.setCellValue(rb.getString("first_name") + ":");
                    fnameLabel.setCellStyle(headerStyles);
                    Cell fnameValue = fname.createCell(1);
                    fnameValue.setCellValue(studentInfoForm.getStudentFname());
                    fnameValue.setCellStyle(headerStylesValue);

                    Row lname = sheet.createRow(atomicIntegerForNewRow.get()+2);
                    Cell lnameLabel = lname.createCell(0);
                    lnameLabel.setCellValue(rb.getString("last_name") + ":");
                    lnameLabel.setCellStyle(headerStyles);
                    Cell lnameValue = lname.createCell(1);
                    lnameValue.setCellValue(studentInfoForm.getStudentLname());
                    lnameValue.setCellStyle(headerStylesValue);


                    Row uname = sheet.createRow(atomicIntegerForNewRow.get()+3 );
                    Cell unameLabel = uname.createCell(0);
                    unameLabel.setCellValue(rb.getString("username") + ":");
                    unameLabel.setCellStyle(headerStyles);
                    Cell unameValue = uname.createCell(1);
                    unameValue.setCellValue(studentInfoForm.getStudentUsername());
                    unameValue.setCellStyle(headerStylesValue);

                    Row passwordRow = sheet.createRow(atomicIntegerForNewRow.get()+4 );
                    Cell passwordRowLabel = passwordRow.createCell(0);
                    passwordRowLabel.setCellValue(rb.getString("password") + ":");
                    passwordRowLabel.setCellStyle(headerStyles);
                    Cell passwordValue = passwordRow.createCell(1);
                    passwordValue.setCellValue(studentInfoForm.getClassPassword());
                    passwordValue.setCellStyle(headerStylesValue);

                    Row header = sheet.createRow(atomicIntegerForNewRow.get()+5);
                    Cell clusterIdHeader = header.createCell(0);
                    clusterIdHeader.setCellValue("www.mathspring.org");
                    clusterIdHeader.setCellStyle(urlStyleHeader);
                    Cell clusterIdValue = header.createCell(1);
                    clusterIdValue.setCellValue("");
                    clusterIdValue.setCellStyle(urlStyleHeaderValue);

                }else{
                    //Odd Entries
                    Row className = sheet.getRow(atomicIntegerForNewRow.get());
                    Cell classNameHeader = className.createCell(3);
                    classNameHeader.setCellValue(studentInfoForm.getClassName());
                    classNameHeader.setCellStyle(classNamestyle);
                    Cell classNameValue= className.createCell(4);
                    classNameValue.setCellValue("");
                    classNameValue.setCellStyle(classNameValueStyle);

                    Row fname = sheet.getRow(atomicIntegerForNewRow.get()+1);
                    Cell fnameLabel = fname.createCell(3);
                    fnameLabel.setCellValue(rb.getString("first_name") + ":");
                    fnameLabel.setCellStyle(headerStyles);
                    Cell fnameValue = fname.createCell(4);
                    fnameValue.setCellValue(studentInfoForm.getStudentFname());
                    fnameValue.setCellStyle(headerStylesValue);

                    Row lname = sheet.getRow(atomicIntegerForNewRow.get()+2);
                    Cell lnameLabel = lname.createCell(3);
                    lnameLabel.setCellValue(rb.getString("last_name") + ":");
                    lnameLabel.setCellStyle(headerStyles);
                    Cell lnameValue = lname.createCell(4);
                    lnameValue.setCellValue(studentInfoForm.getStudentLname());
                    lnameValue.setCellStyle(headerStylesValue);


                    Row uname = sheet.getRow(atomicIntegerForNewRow.get()+3 );
                    Cell unameLabel = uname.createCell(3);
                    unameLabel.setCellValue(rb.getString("username") + ":");
                    unameLabel.setCellStyle(headerStyles);
                    Cell unameValue = uname.createCell(4);
                    unameValue.setCellValue(studentInfoForm.getStudentUsername());
                    unameValue.setCellStyle(headerStylesValue);

                    Row passwordRow = sheet.getRow(atomicIntegerForNewRow.get()+4 );
                    Cell passwordRowLabel = passwordRow.createCell(3);
                    passwordRowLabel.setCellValue(rb.getString("password") + ":");
                    passwordRowLabel.setCellStyle(headerStyles);
                    Cell passwordValue = passwordRow.createCell(4);
                    passwordValue.setCellValue(studentInfoForm.getClassPassword());
                    passwordValue.setCellStyle(headerStylesValue);

                    Row header = sheet.getRow(atomicIntegerForNewRow.get()+5);
                    Cell clusterIdHeader = header.createCell(3);
                    clusterIdHeader.setCellValue("www.mathspring.org");
                    clusterIdHeader.setCellStyle(urlStyleHeader);
                    Cell clusterIdValue = header.createCell(4);
                    clusterIdValue.setCellValue("");
                    clusterIdValue.setCellStyle(urlStyleHeaderValue);

                    atomicIntegerForNewRow.set(atomicIntegerForNewRow.get()+7);
                }
            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private void buildPerClusterReport(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            String teacherId = (String) map.get("teacherId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"per_cluster_problem_report" + classId + ".xls\"");
            Map<String, PerClusterObjectBean> dataForProblemObjects = (Map<String, PerClusterObjectBean>) map.get("dataForProblem");

            Sheet sheet = workbook.createSheet(classId);
            Row header = sheet.createRow(3);
            Cell clusterIdHeader = header.createCell(2);
            clusterIdHeader.setCellValue(rb.getString("cluster_id") + ":");


            Cell categoryCodeAndDisplayCodeHeader = header.createCell(3);
            categoryCodeAndDisplayCodeHeader.setCellValue(rb.getString("clusters_in_class") + ":");

            Cell ClusterDescriptionHeader = header.createCell(4);
            ClusterDescriptionHeader.setCellValue(rb.getString("cluster_description") + ":");


            Cell noOfProblemsInClusterHeader = header.createCell(5);
            noOfProblemsInClusterHeader.setCellValue(rb.getString("nbr_problems_in_cluster") + ":");

            Cell noOfProblemsonFirstAttemptHeader = header.createCell(6);
            noOfProblemsonFirstAttemptHeader.setCellValue(rb.getString("pct_solved_first_attempt") + ":");


            Cell totalHintsViewedPerClusterHeader = header.createCell(7);
            totalHintsViewedPerClusterHeader.setCellValue(rb.getString("avg_ratio_hints_requested") + ":");


            AtomicInteger atomicIntegerForHeaderForData = new AtomicInteger(4);
            dataForProblemObjects.forEach((clusterID, clusterObject) -> {
                Row dataRow = sheet.createRow(atomicIntegerForHeaderForData.getAndIncrement());
                Cell clusterId = dataRow.createCell(2);
                clusterId.setCellValue(clusterID);


                Cell categoryCodeAndDisplayCode = dataRow.createCell(3);
                categoryCodeAndDisplayCode.setCellValue(clusterObject.getCategoryCodeAndDisplayCode());


                Cell clusterCCName = dataRow.createCell(4);
                clusterCCName.setCellValue(clusterObject.getClusterCCName());


                Cell noOfProblemsInCluster = dataRow.createCell(5);
                noOfProblemsInCluster.setCellValue(clusterObject.getNoOfProblemsInCluster());


                Cell noOfProblemsonFirstAttempt = dataRow.createCell(6);
                noOfProblemsonFirstAttempt.setCellValue(clusterObject.getNoOfProblemsonFirstAttempt());


                Cell totalHintsViewedPerCluster = dataRow.createCell(7);
                totalHintsViewedPerCluster.setCellValue(clusterObject.getTotalHintsViewedPerCluster());

            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void buildPerProblemReport(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            String teacherId = (String) map.get("teacherId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"problem_report" + classId + ".xls\"");
            Map<String, PerProblemReportBean> dataForProblemObjects = (Map<String, PerProblemReportBean>) map.get("dataForProblem");
            Sheet sheet = workbook.createSheet(classId);
            Row header = sheet.createRow(3);

            Cell problemID = header.createCell(2);
            problemID.setCellValue(rb.getString("problem_id"));


            Cell problemName = header.createCell(3);
            problemName.setCellValue(rb.getString("problem_name"));


            Cell problemStandard = header.createCell(4);
            problemStandard.setCellValue(rb.getString("problem_standard"));


            Cell noStudentsSeenProblem = header.createCell(5);
            noStudentsSeenProblem.setCellValue(rb.getString("nbr_students_seen_problem"));


            Cell getPercStudentsSolvedEventually = header.createCell(6);
            getPercStudentsSolvedEventually.setCellValue(rb.getString("pct_students_solved_problem"));


            Cell getGetPercStudentsSolvedFirstTry = header.createCell(7);
            getGetPercStudentsSolvedFirstTry.setCellValue(rb.getString("pct_students_solved_first_attempt"));


            Cell percStudentsRepeated = header.createCell(8);
            percStudentsRepeated.setCellValue(rb.getString("pct_students_repeated_problem"));


            Cell percStudentsSkipped = header.createCell(9);
            percStudentsSkipped.setCellValue(rb.getString("pct_students_skipped_problem"));


            Cell percStudentsGaveUp = header.createCell(10);
            percStudentsGaveUp.setCellValue(rb.getString("pct_students_gave_up"));


            Cell mostIncorrectResponse = header.createCell(11);
            mostIncorrectResponse.setCellValue(rb.getString("most_frequent_incorrect_reponse"));


            AtomicInteger atomicIntegerForHeaderForData = new AtomicInteger(4);
            dataForProblemObjects.forEach((problemId, problemDetails) -> {
                Row dataRow = sheet.createRow(atomicIntegerForHeaderForData.getAndIncrement());

                Cell problemIDData = dataRow.createCell(2);
                problemIDData.setCellValue(problemId);


                Cell problemNameData = dataRow.createCell(3);
                problemNameData.setCellValue(problemDetails.getProblemName());


                Cell problemStandardData = dataRow.createCell(4);
                problemStandardData.setCellValue(problemDetails.getProblemStandardAndDescription());


                Cell noStudentsSeenProblemData = dataRow.createCell(5);
                noStudentsSeenProblemData.setCellValue(problemDetails.getNoStudentsSeenProblem());


                Cell getPercStudentsSolvedEventuallyData = dataRow.createCell(6);
                getPercStudentsSolvedEventuallyData.setCellValue(problemDetails.getGetPercStudentsSolvedEventually());


                Cell getGetPercStudentsSolvedFirstTryData = dataRow.createCell(7);
                getGetPercStudentsSolvedFirstTryData.setCellValue(problemDetails.getGetGetPercStudentsSolvedFirstTry());


                Cell percStudentsRepeatedData = dataRow.createCell(8);
                percStudentsRepeatedData.setCellValue(problemDetails.getPercStudentsRepeated());


                Cell percStudentsSkippedData = dataRow.createCell(9);
                percStudentsSkippedData.setCellValue(problemDetails.getPercStudentsSkipped());


                Cell percStudentsGaveUpData = dataRow.createCell(10);
                percStudentsGaveUpData.setCellValue(problemDetails.getPercStudentsGaveUp());


                Cell mostIncorrectResponseData = dataRow.createCell(11);
                mostIncorrectResponseData.setCellValue(problemDetails.getMostIncorrectResponse());


            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void buildPerProblemSetTeacherReport(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        try {
            String classId = (String) map.get("classId");
            String teacherId = (String) map.get("teacherId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"problemset_report" + classId + ".xls\"");
            Map<String, Object> dataForProblemSet = (Map<String, Object>) map.get("dataForProblemSet");

            Map<String, List<String>> finalMapLevelOne = (Map<String, List<String>>) dataForProblemSet.get("levelOneData");
            Map<String, String> columnNamesMap = (Map<String, String>) dataForProblemSet.get("columns");

            Sheet sheet = workbook.createSheet(classId);

            Row header = sheet.createRow(3);

            Cell studentNameID = header.createCell(2);
            Cell studentNameCell = header.createCell(3);
            Cell studentUsernameCell = header.createCell(4);
            studentNameID.setCellValue(rb.getString("student_id"));


            studentNameCell.setCellValue(rb.getString("student_name"));


            studentUsernameCell.setCellValue(rb.getString("username"));


            AtomicInteger atomicIntegerForHeader = new AtomicInteger(5);
            AtomicInteger atomicIntegerForHeaderForData = new AtomicInteger(4);
            columnNamesMap.forEach((topicID, topicName) -> {
                Cell columnNameHeaderCell = header.createCell(atomicIntegerForHeader.getAndIncrement());
                columnNameHeaderCell.setCellValue(topicName);

            });
            finalMapLevelOne.forEach((studentId, problemSetDetails) -> {

                Row dataRow = sheet.createRow(atomicIntegerForHeaderForData.getAndIncrement());
                Cell columnNameHeaderCellID = dataRow.createCell(2);
                columnNameHeaderCellID.setCellValue(studentId);


                for (String studentDetails : problemSetDetails) {
                    String datadetails[] = studentDetails.split("~~~");
                    if (studentDetails.contains("studentName")) {
                        Cell columnStudentPersonal = dataRow.createCell(3);
                        columnStudentPersonal.setCellValue(datadetails[1]);


                    } else if (studentDetails.contains("userName")) {
                        Cell columnStudentPersonal = dataRow.createCell(4);
                        columnStudentPersonal.setCellValue(datadetails[1]);


                    } else {
                        if (datadetails.length > 1) {
                            String[] masteryDetailsForproblemSet = datadetails[1].split("---");
                            String problemRatio = masteryDetailsForproblemSet[0];
                            String mastery = masteryDetailsForproblemSet[1];
                            String topicName = columnNamesMap.get(masteryDetailsForproblemSet[3]);
                            int cellIndex = 0;
                            for (Cell cell : header) {
                                if (cell.getStringCellValue().equals(topicName))
                                    cellIndex = cell.getColumnIndex();
                            }
                            Cell columnStudentproblemSetDetails = dataRow.createCell(cellIndex);
                            columnStudentproblemSetDetails.setCellValue(problemRatio + mastery);
                        }
                    }
                }
            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void buildPerStudentTeacherReport(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            String teacherId = (String) map.get("teacherId");
            CreationHelper helper = workbook.getCreationHelper();
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"student_report_" + classId + ".xls\"");
            List<ClassStudents> dataMap = (List<ClassStudents>) map.get("levelOneData");
            Map<String, Map<String, List<String>>> detailDataMap = (Map<String, Map<String, List<String>>>) map.get("levelTwoData");
            Sheet sheet = workbook.createSheet(classId);

            Row header = sheet.createRow(3);
            Cell studentNameID = header.createCell(2);
            Cell studentNameCell = header.createCell(3);
            Cell studentUsernameCell = header.createCell(4);
            Cell childCellHeaderProblemId = header.createCell(5);
            Cell childCellHeaderProblemNickName = header.createCell(6);
            Cell childCellHeaderProblemFinishedOn = header.createCell(7);
            Cell childCellHeaderProblemDescription = header.createCell(8);
            Cell childCellHeaderProblemURL = header.createCell(9);
            Cell childCellHeaderSolvedCorrectly = header.createCell(10);
            Cell childCellHeadermistakesMade = header.createCell(11);
            Cell childCellHedarHintsSeen = header.createCell(12);
            Cell childCellHeaderAttemptsMade = header.createCell(13);
            Cell childCellExampleSeen = header.createCell(14);
            Cell childCellVideosSeen = header.createCell(15);
            Cell childCellHeaderEffort = header.createCell(16);
            Cell childCellHeaderStandard = header.createCell(17);
            Cell childCellHeaderDifficulty = header.createCell(18);
            Cell childCellHeaderMastery = header.createCell(19);
            Cell childCellHeaderTopicID = header.createCell(20);
            Cell childCellHeaderTopicDescription = header.createCell(21);

            studentNameID.setCellValue(rb.getString("student_id"));
            studentNameCell.setCellValue(rb.getString("student_name"));
            studentUsernameCell.setCellValue(rb.getString("username"));
            childCellHeaderProblemId.setCellValue(rb.getString("problem_id"));
            childCellHeaderProblemNickName.setCellValue(rb.getString("problem_nick_name"));
            childCellHeaderProblemFinishedOn.setCellValue(rb.getString("problem_finishd_on"));
            childCellHeaderProblemDescription.setCellValue(rb.getString("problem_description"));
            childCellHeaderProblemURL.setCellValue(rb.getString("problem_url"));
            childCellHeaderSolvedCorrectly.setCellValue(rb.getString("solved_correctly"));
            childCellHeadermistakesMade.setCellValue(rb.getString("nbr_mistakes_made"));
            childCellHedarHintsSeen.setCellValue(rb.getString("nbr_hints_seen"));
            childCellHeaderAttemptsMade.setCellValue(rb.getString("nbr_attempts_made"));
            childCellExampleSeen.setCellValue(rb.getString("effort"));
            childCellVideosSeen.setCellValue(rb.getString("nbr_videos_seen"));
            childCellHeaderEffort.setCellValue(rb.getString("nbr_examples_seen"));
            childCellHeaderStandard.setCellValue(rb.getString("standard"));
            childCellHeaderDifficulty.setCellValue(rb.getString("difficulty"));
            childCellHeaderMastery.setCellValue(rb.getString("mastery"));
            childCellHeaderTopicID.setCellValue(rb.getString("problem_set_id"));
            childCellHeaderTopicDescription.setCellValue(rb.getString("problem_set"));

            AtomicInteger atomicInteger = new AtomicInteger(4);
            detailDataMap.forEach((key, dataObject) -> {

                if ("effortMap".equals(key))
                    return;

                (dataObject).forEach((problemId, studentVal) -> {
                    Row dataRow = sheet.createRow(atomicInteger.getAndIncrement());
                    Cell studentIDCell = dataRow.createCell(2);
                    studentIDCell.setCellValue(key);
                    dataMap.forEach(leveoneId -> {
                        if (key.equals(leveoneId.getStudentId())) {
                            Cell studentNameCellChild = dataRow.createCell(3);
                            studentNameCellChild.setCellValue(leveoneId.getStudentName());
                            Cell studentUserNameCell = dataRow.createCell(4);
                            studentUserNameCell.setCellValue(leveoneId.getUserName());
                        }
                    });
                    Cell childCellProblemId = dataRow.createCell(5);
                    childCellProblemId.setCellValue(studentVal.get(0));
                    Cell childCellProblemNickName = dataRow.createCell(6);
                    childCellProblemNickName.setCellValue(studentVal.get(1));
                    Cell childCelProblemFinishedOn = dataRow.createCell(7);
                    childCelProblemFinishedOn.setCellValue(studentVal.get(10));
                    Cell childCellProblemDescription = dataRow.createCell(8);
                    childCellProblemDescription.setCellValue(studentVal.get(2));
                    Cell childCellProblemURL = dataRow.createCell(9);
                    childCellProblemURL.setCellValue(studentVal.get(3));
                    Cell childCellSolvedCorrectly = dataRow.createCell(10);
                    childCellSolvedCorrectly.setCellValue(studentVal.get(4));
                    Cell childCellmistakesMade = dataRow.createCell(11);
                    childCellmistakesMade.setCellValue(studentVal.get(5));
                    Cell childCellHintsSeen = dataRow.createCell(12);
                    childCellHintsSeen.setCellValue(studentVal.get(6));
                    Cell childCellAttemptsMade = dataRow.createCell(13);
                    childCellAttemptsMade.setCellValue(studentVal.get(7));
                    Cell childCellEffort = dataRow.createCell(14);
                    childCellEffort.setCellValue(studentVal.get(8));
                    Cell childCellExampleSeenValue = dataRow.createCell(15);
                    childCellExampleSeenValue.setCellValue(studentVal.get(13));
                    Cell childCellVideoValue = dataRow.createCell(16);
                    childCellVideoValue.setCellValue(studentVal.get(12));
                    Cell childCellStandardsValue = dataRow.createCell(17);
                    childCellStandardsValue.setCellValue(studentVal.get(14));
                    Cell childCellDifficultyValue = dataRow.createCell(18);
                    childCellDifficultyValue.setCellValue(studentVal.get(15));
                    Cell childCellMastery = dataRow.createCell(19);
                    childCellMastery.setCellValue(studentVal.get(16));
                    Cell childCellTopicID = dataRow.createCell(20);
                    childCellTopicID.setCellValue(studentVal.get(17));
                    Cell childCellTopicDescription = dataRow.createCell(21);
                    childCellTopicDescription.setCellValue(studentVal.get(18));
                });

            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



    private void buildSummSurveyReport(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            String classId = (String) map.get("classId");
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"survey_report" + classId + ".xls\"");
            
            @SuppressWarnings("unchecked")
			Map<String, Map<Integer, StudentDetails>> dataForProblemObjects = (Map<String, Map<Integer, StudentDetails>>) map.get("dataForProblem");
            
            Sheet sheet = workbook.createSheet(classId);
            Row header = sheet.createRow(3);

            Cell surveyName = header.createCell(2);
            surveyName.setCellValue(rb.getString("survey_name"));


            Cell studentName = header.createCell(3);
            studentName.setCellValue(rb.getString("student_name"));


            Cell userName = header.createCell(4);
            userName.setCellValue(rb.getString("username"));


            Cell question = header.createCell(5);
            question.setCellValue(rb.getString("question"));


            Cell studentAnswer = header.createCell(6);
            studentAnswer.setCellValue(rb.getString("student_answer"));

            
            AtomicInteger atomicIntegerForHeaderForData = new AtomicInteger(4);
            dataForProblemObjects.forEach((name, studentDetails) -> {
               
                studentDetails.forEach((studentId, studentDetail) -> {
                	
                    Set<SurveyQuestionDetails> questions = new HashSet<>();
                    questions = studentDetail.getQuestionset();
                    
                    questions.forEach((questionData) -> {
                    	
                    	Row dataRow = sheet.createRow(atomicIntegerForHeaderForData.getAndIncrement());
                    	
                    	Cell surveyNameData = dataRow.createCell(2);
                        surveyNameData.setCellValue(name);
                        
                    	Cell studentNameData = dataRow.createCell(3);
                    	studentNameData.setCellValue(studentDetail.getStudentName());


                        Cell studentUsername = dataRow.createCell(4);
                        studentUsername.setCellValue(studentDetail.getStudentUserName());

                    	Cell questionDesc = dataRow.createCell(5);
                    	questionDesc.setCellValue(questionData.getDescription());
                        
                        Cell studentAnswerData = dataRow.createCell(6);
                        studentAnswerData.setCellValue(questionData.getStudentAnswer());
                        
                    });
                    
                    
                });
                
            });

            workbook.write(httpServletResponse.getOutputStream());
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
