<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="${sideMenu}" />


<jsp:useBean id="tutorConfigParams" scope="request" type="edu.umass.ckc.wo.tutor.probSel.ClassTutorConfigParams"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="mainPageMargin">
  <style type="text/css">@import url("css/dragdroptable.css");</style>
  <div id="Layer1" align="center" >
      <p class="a2"><b>Active Topic Order</b></p>
      <p>Topics will be presented in the order below.</p>

      <%--@elvariable id="classGradeColumn" type="int"--%>
      <%--@elvariable id="gradeColumnMask" type="boolean[]"--%>

      <form name="form1" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminTopicControl">
      <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
      <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
      <table class="altrows" data-context-path="<c:out value="${pageContext.request.contextPath}"/>" data-teacher-id="<c:out value="${teacherId}"/>" data-class-id="<c:out value="${classId}"/>">
          <tr class="rowheader">
              <td rowspan="2" colspan="4">Reorder</td>
              <td rowspan="2">Topic</td>
              <td rowspan="2" style="max-width:100px">Total Active Problems</td>
              <td colspan="10">Active Problems by Grade</td>
          </tr>
          <tr class="rowheader">
              <c:forEach var="visible" varStatus="status" items="${gradeColumnMask}">
                  <td style="${status.index eq classGradeColumn ? 'font-weight:bold;' : ''}${gradeColumnMask[status.index] ? '' : 'display:none'}">
                      <c:out value="${status.index eq 0 ? 'K' : (status.index eq 9 ? 'H' : status.index.toString().concat('th'))}"/>
                  </td>
              </c:forEach>
          </tr>

          <c:set var="ix" value="0"/>
          <%--@elvariable id="topics" type="edu.umass.ckc.wo.tutor.Topic[]"--%>
          <%--@elvariable id="numTopics" type="int"--%>
          <c:forEach var="topic" items="${topics}">
              <tr draggable="true" data-topic-id="<c:out value="${topic.id}"/>" data-index="<c:out value="${ix}"/>">
                  <td><a title="drag and drop" class="dragdrophandle" draggable="false" href="#"></a></td>
                  <td><a title="move up" class="moveupbutton<c:out value="${ix > 0 ? '' : ' disabled' }"/>" draggable="false" href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminReorderTopics&reorderType=move&topicFrom=<c:out value="${ix}"/>&topicTo=<c:out value="${ix-1}"/>&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&topicId=<c:out value="${topic.id}"/>"></a></td>
                  <td><a title="move down" class="movedownbutton<c:out value="${ix < numTopics - 1 ? '' : ' disabled' }"/>" draggable="false" href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminReorderTopics&reorderType=move&topicFrom=<c:out value="${ix}"/>&topicTo=<c:out value="${ix+1}"/>&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&topicId=<c:out value="${topic.id}"/>"></a></td>
                  <td><a title="deactivate" class="removebutton" draggable="false" href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminReorderTopics&reorderType=omit&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&topicId=<c:out value="${topic.id}"/>"></a></td>
                  <td><a title="${topic.standards}" draggable="false" href="#"><c:out value="${topic.name}"/></a></td>
                  <td>
                      <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminSelectTopicProblems&teacherId=${teacherId}&classId=${classId}&topicId=${topic.id}">
                          <c:out value="${topic.numProbs}"/>
                      </a>
                  </td>
                  <c:forEach var="problemsByGrade" varStatus="status" items="${topic.problemsByGrade}">
                      <td style="${status.index eq classGradeColumn ? 'font-weight:bold' : ''}${gradeColumnMask[status.index] ? '' : 'display:none;'}">
                          <c:if test="${problemsByGrade > 0}"><c:out value="${problemsByGrade}"/></c:if>
                      </td>
                  </c:forEach>
              </tr>
              <c:set var="ix" value="${ix+1}"/>
          </c:forEach>
      </table>
      <p/>
      <c:if test="${!empty inactiveTopics}">
      <p align="center" class="a2"><b>Inactive Topics </b></p>
      <table class="altrows">
          <tr class="rowheader">
              <td rowspan="2" valign="center">Reactivate</td>
              <td rowspan="2">Topic</td>
              <td rowspan="2" style="max-width:100px">Total Active Problems</td>
              <td colspan="10">Active Problems by Grade</td>
          </tr>
          <tr class="rowheader">
              <c:forEach var="visible" varStatus="status" items="${gradeColumnMask}">
                  <td style="${status.index eq classGradeColumn ? 'font-weight:bold;' : ''}${gradeColumnMask[status.index] ? '' : 'display:none'}">
                      <c:out value="${status.index eq 0 ? 'K' : (status.index eq 9 ? 'H' : status.index.toString().concat('th'))}"/>
                  </td>
              </c:forEach>
          </tr>
          <c:set var="ix" value="0"/>
          <%--@elvariable id="inactiveTopics" type="edu.umass.ckc.wo.tutor.Topic[]"--%>
          <c:forEach var="topic" items="${inactiveTopics}">
              <c:set var="ix" value="${ix+1}"/>
              <tr>
                  <td valign="center">
                     <a title="activate" class="moveupbutton" href="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminReorderTopics&reorderType=reactivate&teacherId=<c:out value="${teacherId}"/>&classId=<c:out value="${classId}"/>&topicId=<c:out value="${topic.id}"/>"></a>
                  </td>

              <!--    <input name='<c:out value="topicPosition"/>' type="text" value='<c:out value="${topic.seqPos}"/>' size="3" /></td> -->
                  <td><c:out value="${topic.name}"/></td>
                  <td>
                      <a href="${pageContext.request.contextPath}/WoAdmin?action=AdminSelectTopicProblems&teacherId=${teacherId}&classId=${classId}&topicId=${topic.id}">
                          <c:out value="${topic.numProbs}"/>
                      </a>
                  </td>
                  <c:forEach var="problemsByGrade" varStatus="status" items="${topic.problemsByGrade}">
                      <td style="${status.index eq classGradeColumn ? 'font-weight:bold' : ''}${gradeColumnMask[status.index] ? '' : 'display:none;'}">
                          <c:if test="${problemsByGrade > 0}"><c:out value="${problemsByGrade}"/></c:if>
                      </td>
                  </c:forEach>
              </tr>
          </c:forEach>
      </table>
     </c:if>
    <br>


    </form>

    <div style="height:54px"></div>
    <form name="form3" id="form3" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/WoAdmin?action=AdminProblemSelection">
          <input type="submit" name="Submit" value="Examine Problems in Each Topic" style="font-size:16px;padding:10px"/>
          <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
          <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
    </form>

<c:if test="${isAdmin}">
     <form method="post" action="${pageContext.request.contextPath}/WoAdmin?action=AdminSetTopicModelParameters">
         <input type="hidden" name="classId" value="<c:out value="${classId}"/>">
         <input type="hidden" name="teacherId" value="<c:out value="${teacherId}"/>">
        <%--@elvariable id="tutorConfigParams" type="edu.umass.ckc.wo.tutor.probSel.ClassTutorConfigParams"--%>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

         <script>

             function checkClicked(t, amountText, slider) {
                 if (t.is(':checked')) {
                     $(amountText).val(-1);
                     $(slider).slider('disable');
                 } else {
                     $(amountText).val($(slider).slider('option','value'));
                     $(slider).slider('enable');
                 }

             }

             $( function() {
                 $( "#maxNumProbsSlider" ).slider({
                     min: -1,
                     max: 100,
                     value: ${tutorConfigParams.maxProbs},
                     slide: function( event, ui ) {
                         $( "#maxNumberProbsPerTopic" ).val( ui.value );
                     }
                 });
                 $( "#minNumProbsSlider" ).slider({
                     min: -1,
                     max: 100,
                     step: 1,
                     value: ${tutorConfigParams.minProbs},
                     slide: function( event, ui ) {
                         $( "#minNumberProbsPerTopic" ).val( ui.value );
                     }
                 });
                 $( "#maxTimeSlider" ).slider({
                     min: -1,
                     max: 60,
                     step: 1,
                     value: ${tutorConfigParams.maxTimeMinutes},
                     slide: function( event, ui ) {
                         $( "#maxTimeInTopic" ).val( ui.value );
                     }
                 });
                 $( "#minTimeSlider" ).slider({
                     min: -1,
                     max: 60,
                     step: 1,
                     value: ${tutorConfigParams.minTimeMinutes},
                     slide: function( event, ui ) {
                         $( "#minTimeInTopic" ).val( ui.value );
                     }
                 });
                 $( "#contentFailureSlider" ).slider({
                     min: -1,
                     max: 5,
                     step: 1,
                     value: ${tutorConfigParams.contentFailureThreshold},
                     slide: function( event, ui ) {
                         $( "#contentFailureThreshold" ).val( ui.value );
                     }
                 });
                 $( "#masterySlider" ).slider({
                     min: -1,
                     max: 1.2,
                     step: 0.05,
                     value: ${tutorConfigParams.desiredMastery},
                     slide: function( event, ui ) {
                         $( "#mastery" ).val( ui.value );
                     }
                 });
                 $( "#diffRateSlider" ).slider({
                     min: -1,
                     max: 5.0,
                     step: 0.1,
                     value: ${tutorConfigParams.difficultyRate},
                     slide: function( event, ui ) {
                         $( "#difficultyRate" ).val( ui.value );
                     }
                 });

                 $( "#maxNumberProbsPerTopic" ).val( $( "#maxNumProbsSlider" ).slider( "value" ) );
                 $( "#minNumberProbsPerTopic" ).val( $( "#minNumProbsSlider" ).slider( "value" ) );
                 $( "#maxTimeInTopic" ).val( $( "#maxTimeSlider" ).slider( "value" ) );
                 $( "#minTimeInTopic" ).val( $( "#minTimeSlider" ).slider( "value" ) );
                 $( "#contentFailureThreshold" ).val( $( "#contentFailureSlider" ).slider( "value" ) );
                 $( "#mastery" ).val( $( "#masterySlider" ).slider( "value" ) );
                 $( "#difficultyRate" ).val( $( "#diffRateSlider" ).slider( "value" ) );
             } );
         </script>

         <table>
             <tr><th></th><th>Omit</th><th/><th/></tr>
             <tr>
                 <td>Max Number of Problems Per Topic:</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#maxNumberProbsPerTopic','#maxNumProbsSlider');"></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="maxNumProbsSlider" style="width:100px;"></div>

                 </td>

                 <td>
                     <input type="text" id="maxNumberProbsPerTopic" name="maxNumberProbsPerTopic" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>

             </tr>

             <tr>
                 <td>Min Number of Problems Per Topic:</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#minNumberProbsPerTopic','#minNumProbsSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="minNumProbsSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="minNumberProbsPerTopic" name="minNumberProbsPerTopic" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>

             <tr>
                 <td>Max Time in Topic (minutes):</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#maxTimeInTopic','#maxTimeSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="maxTimeSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="maxTimeInTopic" name="maxTimeInTopic" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>

             <tr>
                 <td>Min Time in Topic (minutes):</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#minTimeInTopic','#minTimeSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="minTimeSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="minTimeInTopic" name="minTimeInTopic" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>

             <tr>
                 <td>Content Failure Threshold (#problems):</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#contentFailureThreshold','#contentFailureSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="contentFailureSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="contentFailureThreshold" name="contentFailureThreshold" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>

             <tr>
                 <td>Desired Topic Mastery:</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#mastery','#masterySlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="masterySlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="mastery" name="mastery" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>

             <tr>
                 <td>Difficulty Rate:</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#difficultyRate','#diffRateSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="diffRateSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="difficultyRate" name="difficultyRate" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>
             <tr>
                 <td>Time Before External Activities begin:</td>
                 <td style="width:12px"><input type="checkbox" onclick="checkClicked($(this), '#externalActivityTimeThreshold','#extActSlider');"/></td>
                 <td style="padding:0 15px 0 15px;">
                     <div id="extActSlider" style="width:100px;"></div>

                 </td>
                 <td>
                     <input type="text" id="externalActivityTimeThreshold" name="externalActivityTimeThreshold" readonly style="border:0; color:#f6931f; font-weight:bold;">
                 </td>
             </tr>
         </table>


        <input type="submit" name="submit" value="Save Topic Control Params"/>

     </form>
</c:if>
    <div style="height:54px"></div>
</div>
</div>
</body>
</html>
