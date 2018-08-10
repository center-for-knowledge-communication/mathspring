<%--
  Created by IntelliJ IDEA.
  User: david
  Date: Sep 22, 2011
  Time: 4:27:39 PM
  A header file that is used to display basic info about a class
--%>
<jsp:useBean id="classInfo" scope="request" type="edu.umass.ckc.wo.beans.ClassInfo"/>
<%--@elvariable id="classInfo" type="edu.umass.ckc.wo.beans.ClassInfo"--%>
<div class="mainPageMargin">
<div id="Layer1" >
  <p align="center" class="a2"></p><b>
      <font face="Arial, Helvetica, sans-serif">
          Class Information
      </font></b></p>

    <input type="hidden" name="classId" value="${classInfo.classid}"/>
    <table width="334" border="0" height="98">
       <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">ID:</font></td>
        <td width="222" class="whArial">${classInfo.classid}</td>
      </tr>
      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Class Name:</font></td>
        <td width="222" class="whArial">${classInfo.name}</td>
      </tr>
      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Town:</font></td>
        <td width="222" class="whArial">${classInfo.town}</td>
      </tr>
      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">School:</font></td>
        <td width="222" class="whArial"> ${classInfo.school}</td>
      </tr>

      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Year:</font></td>
        <td width="222" class="whArial">${classInfo.schoolYear}</td>
      </tr>
      <tr>
        <td width="102"><font  face="Arial, Helvetica, sans-serif">Section:</font></td>
        <td width="222" class="whArial">${classInfo.section}</td>
      </tr>

    </table>
   </div>
</div>