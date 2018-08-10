/**
 * Created by IntelliJ IDEA.
 * User: edo
 * Date: 10/15/12
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */

//A function to extract the new class we want to change to
function changeClass(newClass,teach) {
    var newClassId =newClass.value;
    var tID = teach;
    //Create the path that we now need to open with an action, and path
    var pathRepeat= path2+"/WoAdmin?action="+repeatAct+"&teacherId="+tID+"&classId="+newClassId;
    window.open(pathRepeat,'_self',false);
}