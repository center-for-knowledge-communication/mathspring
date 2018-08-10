

var s_x=new Array();
var s_y=new Array();
var s_o=new Array();
var s_angle=new Array();


var canvas;
var ctx;
var totalProblems;
var numberOfSkills;

function wrapText(context, text, x, y, maxWidth, lineHeight) {
    var words = text.split(' ');
    var line = '';

    for(var n = 0; n < words.length; n++) {
        var testLine = line + words[n] + ' ';
        var metrics = context.measureText(testLine);
        var testWidth = metrics.width;
        if (testWidth > maxWidth && n > 0) {
            context.fillText(line, x, y);
            line = words[n] + ' ';
            y += lineHeight;
        }
        else {
            line = testLine;
        }
    }
    context.fillText(line, x, y);
}


function loadTreeParameters(problemsSolved,topicsDone){

    totalProblems=problemsSolved;
    numberOfSkills=topicsDone;

    draw();
}

function generateGreetings(studentFirstName,daysSinceLastSession,problemsSolved,topicsDone,problemsDoneInLastSession,totalMasteredTopics,topicsMasteredInLastSession){

    var greetingsText="";

    if (problemsSolved==0)
    {greetingsText="Hi "+studentFirstName+", <br/><br/><br/> Welcome to MathSpring!"}

    else if (problemsSolved>0){
        greetingsText="Hi "+studentFirstName+", <br/><br/> Welcome back!";
    }

    if (daysSinceLastSession==1)
    {greetingsText+="<br/>You had logged in "+daysSinceLastSession+" day ago.";}

    if (daysSinceLastSession>1)
    {greetingsText+="<br/>You had logged in "+daysSinceLastSession+" days ago.";}

    if (problemsSolved==1)
    {
        greetingsText+=" You have solved "+problemsSolved+" problem so far."}

    else if (problemsSolved>1)
    {
        greetingsText+=" You have solved "+problemsSolved+" problems so far."}

    if (totalMasteredTopics==1)
    {greetingsText+=" You have mastered "+totalMasteredTopics+" topic.";}

    else if (totalMasteredTopics>1)
    {greetingsText+=" You have already mastered "+totalMasteredTopics+" topics.";}



    if (problemsDoneInLastSession==1)
    {greetingsText+=" You worked on "+problemsDoneInLastSession+" problem in last session.";}

    else if (problemsDoneInLastSession>0)
    {greetingsText+="  You worked on "+problemsDoneInLastSession+" problems in last session.";}

    if (topicsMasteredInLastSession==1)
    {greetingsText+=" You also mastered "+topicsMasteredInLastSession+" topic in that session.";}

    else if (topicsMasteredInLastSession>1)
    {greetingsText+=" You also mastered "+topicsMasteredInLastSession+" topics in that session.";}



    document.getElementById("greetingsText").innerHTML=greetingsText;
    document.getElementById("finalWords").innerHTML="<br/><br/>Have a good time working on these math problems !";

}





function generateDayHistoryButtons(dayList)
{
    document.getElementById("dayDetailsText").innerHTML="<br/>You can see how you have progressed over days of practice.";

    for (var i=0; i<dayList.length; i++){
        var node=document.createElement("LI");
        var textnode=document.createTextNode("Day "+String(i+1));
        node.appendChild(textnode);

        document.getElementById("dayList_ul").appendChild(node);

    }
}


function dayListClickEvents(){

    var day_index;

    $('ul#dayList_ul li').click(function(e)
    {
        day_index=$(this).index( "ul#dayList_ul li" );
        totalProblems=dayList[day_index][2];
        numberOfSkills=dayList[day_index][3];
        for (var j=0;j<numberOfSkills; j++)
        {
            skillList[j]=dayList[day_index][4][j];
            skillMastery[j]=dayList[day_index][5][j];
        }

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        draw();


    });

}


function drawSampleTree(){

    document.getElementById("dayDetailsText").innerHTML="<br/>You are given this baby tree. It will grow as you practice math problems and learn. There are hints, examples and videos to help you learn to solve the problems. <br/>Do you want to see how your tree may look like in future?";

    var node=document.createElement("LI");
    var textnode=document.createTextNode("Show me the tree");
    node.appendChild(textnode);
    document.getElementById("dayList_ul").appendChild(node);
    $('ul#dayList_ul li').click(function(e)
    {

        totalProblems=30;
        numberOfSkills=16;
        for (var j=0;j<numberOfSkills; j++)
        {
            skillList[j]="";
            skillMastery[j]=1;
        }

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        draw();
    });

}

var treeId;

var treeGrow=new Array();
treeGrow[0]=1;
treeGrow[1]=2;
treeGrow[2]=4;
treeGrow[3]=10;
treeGrow[4]=11;


function identifyTree(){
    if (totalProblems<=treeGrow[0]){ treeId=1;}
    else if (totalProblems>treeGrow[0] && totalProblems<=treeGrow[1] ){treeId=2;}
    else if (totalProblems>treeGrow[1] && totalProblems<=treeGrow[2] ){treeId=3;}
    else if (totalProblems>treeGrow[2] && totalProblems<=treeGrow[3] ){treeId=4;}
    else if (totalProblems>treeGrow[3] && totalProblems<=treeGrow[4] ){treeId=5;}
    else if (totalProblems>treeGrow[4]  ){treeId=6;}

}


/*
 function identifyTree(){
 if (totalProblems<=3){ treeId=1;}
 else if (totalProblems>3 && totalProblems<=15 ){treeId=2;}
 else if (totalProblems>15 && totalProblems<=25 ){treeId=3;}
 else if (totalProblems>25 && totalProblems<35 ){treeId=4;}
 else if (totalProblems>35 && totalProblems<45 ){treeId=5;}
 else if (totalProblems>45  ){treeId=6;}

 */
function draw(){



    identifyTree();
    canvas = document.getElementById('myCanvas')
    if (canvas.getContext){
        ctx = canvas.getContext('2d');
        var center_x=225;
        var bottom_y=500;
        var canvas_height=500;
        var tree_h=canvas_height-94;

        var n1x,n2x,n1y,n2y,nmx,nmy;
        var l1x,l2x,l1y,l2y,u_x1,u_x2,u_y1,u_y2,l_x1,l_x2,l_y1,l_y2;
        var r1x,r2x,r1y,r2y;
        var x2,y2;


        if (treeId<6){
            var img = new Image();
            img.src = "img/pp/mud.png";

            if (treeId<5) {
                img.onload = function() {
                    ctx.drawImage(img, center_x-57,bottom_y-28);
                }
            }
            else if (treeId==5) {
                img.onload = function() {
                    ctx.drawImage(img, center_x-57,bottom_y-18);
                }
            }}

        if (treeId==1)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-94;
            b_h[0]=canvas_height-40;
            b_h[1]=b_h[0]-12;


            s_h[0]=b_h[1]-8;
            s_h[1]=s_h[0]-10;

            for (var i=2; i<10;i+=2)
            {
                if (numberOfSkills>i)
                {
                    tree_h-=40;
                    s_h[i]=s_h[i-1]-30;
                    s_h[i+1]=s_h[i]-10;
                }

            }

            drawTrunk();

        }
        else if (treeId==2)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-136;
            b_h[0]=canvas_height-40;
            b_h[1]=b_h[0]-12;
            b_h[2]=tree_h+36;
            b_h[3]=b_h[2]-4;

            s_h[0]=b_h[1]-8;
            s_h[1]=s_h[0]-10;


            for (var i=2; i<10;i+=2)
            {
                if (numberOfSkills>i)
                {
                    tree_h-=40;
                    b_h[2]=tree_h+36;
                    b_h[3]=b_h[2]-4;
                    s_h[i]=s_h[i-1]-30;
                    s_h[i+1]=s_h[i]-10;
                }

            }

            drawTrunk();

        }

        else if (treeId==3)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-180;
            b_h[0]=canvas_height-60;
            b_h[1]=b_h[0]-12;

            b_h[2]=tree_h+30;
            b_h[3]=b_h[2]-4;



            s_h[0]=b_h[1]-40;
            s_h[1]=s_h[0]-10;



            for (var i=2; i<10;i+=2)
            {
                if (numberOfSkills>i)
                {
                    tree_h-=40;
                    b_h[2]=tree_h+30;
                    b_h[3]=b_h[2]-4;

                    s_h[i]=s_h[i-1]-30;
                    s_h[i+1]=s_h[i]-10;
                }

            }

            drawTrunk();

        }

        else if (treeId==4)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-230;
            b_h[0]=canvas_height-60;
            b_h[1]=b_h[0]-12;
            b_h[2]=tree_h+30;
            b_h[3]=b_h[2]-4;

            s_h[0]=b_h[1]-50;
            s_h[1]=s_h[0]-10;

            b_h[4]=s_h[0]-26;
            b_h[5]=b_h[4]-14;



            if (numberOfSkills>2)
            {
                tree_h-=30;
                b_h[2]=tree_h+30;
                b_h[3]=b_h[2]-4;
                s_h[2]=b_h[5]-40;
                s_h[3]=s_h[2]-10;

            }

            for (var i=4; i<12;i+=2)
            {
                if (numberOfSkills>i)
                {
                    tree_h-=40;
                    b_h[2]=tree_h+36;
                    b_h[3]=b_h[2]-4;

                    s_h[i]=s_h[i-1]-30;
                    s_h[i+1]=s_h[i]-10;
                }

            }

            drawTrunk();

        }

        else if (treeId==5)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-310;
            b_h[0]=canvas_height-120;
            b_h[1]=b_h[0]-12;


            s_h[0]=b_h[1]-50;
            s_h[1]=s_h[0]-10;

            b_h[2]=tree_h+26;
            b_h[3]=b_h[2]-4;

            b_h[4]=s_h[0]-30;
            b_h[5]=b_h[4]-14;





            if (numberOfSkills>2)
            {
                tree_h-=30;
                b_h[2]=tree_h+26;
                b_h[3]=b_h[2]-4;
                s_h[2]=b_h[5]-45;
                s_h[3]=s_h[2]-10;

            }

            for (var i=4; i<12;i+=2)
            {
                if (numberOfSkills>i)
                {
                    tree_h-=40;
                    b_h[2]=tree_h+36;
                    b_h[3]=b_h[2]-4;

                    s_h[i]=s_h[i-1]-30;
                    s_h[i+1]=s_h[i]-10;
                }

            }

            drawTrunk();

        }

        else if (treeId==6)
        {
            var b_h=new Array(); //branch height
            var s_h=new Array(); //skill height

            tree_h=canvas_height-370;
            b_h[0]=canvas_height-90;
            b_h[1]=b_h[0]-12;

            b_h[2]=b_h[0]-80;
            b_h[3]=b_h[2]-4;

            b_h[4]=b_h[3]-80;
            b_h[5]=b_h[4]-70;

            b_h[6]=tree_h;
            b_h[7]=b_h[6]+34;

            s_h[0]=b_h[1]-50;
            s_h[1]=s_h[0]-10;


            drawTrunk();

        }


        var node = (function () {

            // constructor
            function node(x,y,w,h,t,o) { //x,y,width, height,thinkness, orientation

                this.x = x;
                this.y = y;
                this.h = h;
                this.w = w;
                this.t = t;
                this.o = o;

                this.draw(x,y,h,w,t,o);
                return (this);
            }

            node.prototype.draw = function (x,y,w,h,t,o) {



                n2y=y-h;
                nmy=y-h;

                if (o=="right"){
                    n2x=x+w;
                    nmx=x+w/3;
                }
                if (o=="right_c"){
                    n2x=x+w;
                    nmx=x+w/3;
                    n2y=y-h;
                    nmy=y;
                }
                else if (o=="left"){
                    n2x=x-w;
                    nmx=x-w/3;
                }
                else if (o=="left_c"){
                    n2x=x-w;
                    nmx=x-w;
                    n2y=y-h;
                    nmy=y-h/3;
                }
                else if (o=="leftdown"){
                    nmy=y+h;
                    n2y=y+h;
                    n2x=x-w;
                    nmx=x-w/3;}
                else if (o=="leftdown_u"){
                    nmy=y+h;
                    n2y=y-h;
                    n2x=x-w;
                    nmx=x-w/3;}
                else if (o=="leftdown_c"){
                    nmy=y;
                    n2y=y+h;
                    n2x=x-w;
                    nmx=x-w/3;}
                else if (o=="rightdown"){
                    nmy=y+10;
                    n2y=y+h;
                    n2x=x+w;
                    nmx=x+w/2;}
                else if (o=="rightdown_u"){
                    nmy=y+h/2;
                    n2y=y-h;
                    n2x=x+w;
                    nmx=x+w/3;}
                else if (o=="rightdown_c"){
                    nmy=y-h/2;
                    n2y=y+h;
                    n2x=x+w;
                    nmx=x+w/3;}
                else if (o=="lefttop"){
                    n2x=x-w;
                    nmx=x-w/3;}
                else if (o=="lefttop_c"){
                    nmy=y;
                    n2y=y-h;
                    n2x=x-w;
                    nmx=x-w;}
                else if (o=="righttop_c"){
                    nmy=y-h/2;
                    n2y=y-h;
                    n2x=x+w;
                    nmx=x;}


                ctx.lineWidth   = t;
                if (treeId<5) {
                    ctx.strokeStyle = '#4d8700';
                }
                else
                {
                    ctx.strokeStyle = '#614434';
                    ctx.fillStyle   = '#614434';}

                ctx.beginPath();
                ctx.moveTo(x,y);
                ctx.quadraticCurveTo(nmx,nmy, n2x, n2y);
                ctx.stroke();

            }
            return node;
        })();

        var nodes = [];


        var leaf = (function () {

            // constructor
            function leaf(x,y,w,h,angle,type,o) { //x,y,width, height,thinkness, orientation

                this.x = x;
                this.y = y;
                this.h = h;
                this.w = w;
                this.angle = angle;
                this.type = type;
                this.o = o;

                this.draw(x,y,w,h,angle,type,o);
                return (this);
            }

            leaf.prototype.draw = function (x,y,w,h,angle,type,o) {

                ctx.save();
                ctx.translate(x,y);

                ctx.rotate(angle*Math.PI/180);
                ctx.lineWidth   = 2;

                ctx.strokeStyle = '#4d8700';
                ctx.fillStyle   = '#B2cc2E';
                ctx.beginPath();

                if (o=="right")
                {
                    l2x=w;
                    l2y=0;

                    u_x1=6; u_x2=w-10;
                    u_y1=-h; u_y2=-h;

                    l_x1=10; l_x2=w-6;
                    l_y1=h;  l_y2=h;

                    r1x=5;
                    r2x=w*(3/5);

                    r1y=2;
                    r2y=0;

                    if (type=="curved"){
                        u_y1=u_y1-5; u_y2=+2;
                        l_y1+=5; u_y2=-2;
                        l_x1=l_x1-3;
                        r1y=4;
                        r2y=2;
                    }
                }

                else if (o=="left")
                {
                    l2x=-w;
                    l2y=0;

                    u_x1=-6; u_x2=-w+10;
                    u_y1=-h; u_y2=-h;

                    l_x1=-10; l_x2=-w+6;
                    l_y1=h;  l_y2=h;

                    r1x=-5;
                    r2x=-w*(3/5);

                    r1y=-2;
                    r2y=0;

                    if (type=="small"){
                        u_x1=-3; l_x1=-5;}


                    if (type=="curved"){
                        u_y1=u_y1-5; u_y2=+2;
                        l_y1+=5; u_y2=-2;
                        l_x1=l_x1+3;
                        r1y=4;
                        r2y=2;
                    }


                }



                ctx.moveTo(0,0);
                ctx.bezierCurveTo(u_x1,u_y1,u_x2,u_y2,l2x,l2y);
                ctx.moveTo(0,0);
                ctx.bezierCurveTo(l_x1,l_y1,l_x2,l_y2,l2x,l2y);
                ctx.fill();
                ctx.stroke();

                ctx.moveTo(0,0);
                ctx.quadraticCurveTo(r1x,r1y,r2x,r2y);
                ctx.stroke();

                ctx.restore();


            }
            return leaf;
        })();

        var leaves = [];


        var skill = (function () {

            // constructor
            function skill(skillName,skillProblems,index,o) {

                this.skillName = skillName;
                this.skillProblems = skillProblems;
                this.index = index;
                this.o = o;

                this.draw(skillName,skillProblems,index,o);
                return (this);
            }

            skill.prototype.draw = function (skillName,skillProblems,index,o) {


                var angle,align;

                if (o=="right"){
                    n1x=center_x+1;
                    n1y=s_h[index];
                    n2x=n1x+8;
                    n2y=n1y-12;
                    ctx.beginPath();
                    ctx.moveTo(n1x,n1y);
                    ctx.bezierCurveTo(n1x-1, n1y-1, n1x-2, n1y-4, n2x, n2y);
                    ctx.stroke();
                    angle=-20;
                    align="left";
                }
                else if (o=="left"){
                    n1x=center_x-1;
                    n1y=s_h[index];
                    n2x=n1x-8;
                    n2y=n1y-10;
                    ctx.moveTo(n1x,n1y);
                    ctx.bezierCurveTo(n1x-1, n1y-1, n1x-2, n1y-4, n2x, n2y);
                    ctx.stroke();
                    angle=20;
                    align="right";
                }


                l1x=n2x;
                l1y=n2y;

                ctx.save();
                ctx.translate(l1x, l1y);

                ctx.rotate(angle*Math.PI/180);
                ctx.fillStyle = '#d16700';
                ctx.font=" 16px Calibri";
                ctx.textAlign = align;

                wrapText(ctx,skillName,0, 0,120,14);

                ctx.restore();

            }
            return skill;
        })();


        var skills = [];



        if (treeId==1){


            n1x=center_x+1;
            n1y=b_h[0];


            nodes.push(new node(n1x,n1y,12,10,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","right"));

            n1x=center_x+1;
            n1y=b_h[1];

            nodes.push(new node(n1x,n1y,8,8,2,"left"));
            leaves.push(new leaf(n2x,n2y,40,12,30,"regular","left"));

            //top leaves

            drawTopLeaves();

            // skill
            drawSkills();

        }

//tree2 starts here--------------------


        else if (treeId==2){



            n1x=center_x+1;
            n1y=b_h[0];


            nodes.push(new node(n1x,n1y,12,10,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","right"));

            n1x=center_x+1;
            n1y=b_h[1];

            nodes.push(new node(n1x,n1y,8,8,2,"left"));
            leaves.push(new leaf(n2x,n2y,40,12,30,"regular","left"));


            //top leaves

            drawTopLeaves();

            // skill

            drawSkills();


        }


        else if (treeId==3){

            l1x=center_x;
            l1y=b_h[0];


            drawBranch(l1x, l1y, 55,26,2,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","left"));

            nodes.push(new node(l1x-14,l1y-20,2,3,2,"leftdown"));
            leaves.push(new leaf(n2x,n2y,34,10,-30,"regular","left"));

            nodes.push(new node(l1x-30,l1y-26,10,4,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-110,"regular","right"));

            l1y=b_h[1];
            l1x=center_x+2;
            drawBranch(l1x, l1y, 50,40,2,"right","s");
            nodes.push(new node(x2,y2,2,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-40,"regular","right"));

            nodes.push(new node(l1x+20,l1y-28,2,8,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,34,10,30,"regular","right"));


            //top leaves

            drawTopLeaves();

            // skill

            drawSkills();

        }

        else if (treeId==4){

            l1x=center_x;
            l1y=b_h[0];


            drawBranch(l1x, l1y, 85,36,3,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","left"));

            nodes.push(new node(l1x-34,l1y-32,4,4,2,"leftdown"));
            leaves.push(new leaf(n2x,n2y,34,10,-30,"regular","left"));

            nodes.push(new node(l1x-54,l1y-40,6,2,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-150,"regular","right"));

            nodes.push(new node(l1x-14,l1y-23,10,4,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-110,"regular","right"));

            l1y=b_h[1];
            l1x=center_x+2;
            drawBranch(l1x, l1y, 80,50,3,"right","ls");
            nodes.push(new node(x2,y2,2,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-50,"regular","right"));

            nodes.push(new node(l1x+58,l1y-36,6,5,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,34,10,0,"regular","right"));

            nodes.push(new node(l1x+60,l1y-36,8,3,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-80,"regular","right"));

            nodes.push(new node(l1x+12,l1y-20,6,9,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,34,10,20,"regular","right"));

            nodes.push(new node(l1x+25,l1y-28,8,3,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-80,"regular","right"));

            l1y=b_h[4];

            l1x=center_x;
            drawBranch(l1x, l1y, 30,40,2,"right","s");
            nodes.push(new node(x2,y2,2,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-60,"regular","right"));

            nodes.push(new node(l1x+12,l1y-28,2,8,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,34,8,-5,"curved","right"));

            nodes.push(new node(l1x+7,l1y-24,8,3,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-80,"regular","right"));

            l1y=b_h[5];

            drawBranch(l1x, l1y, 35,16,2,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","left"));


            nodes.push(new node(l1x-10,l1y-15,14,12,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-150,"regular","right"));

            drawTopLeaves();
            drawSkills();

        }

        else if (treeId==5){

            l1x=center_x;
            l1y=b_h[0];


            drawFullBranch(l1x, l1y, 120,46,2,10,"left","u");
            nodes.push(new node(x2,y2,0,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-10,"curved","left"));

            nodes.push(new node(l1x-34,l1y-32,4,10,2,"leftdown"));
            leaves.push(new leaf(n2x,n2y,34,10,-10,"curved","left"));

            nodes.push(new node(l1x-74,l1y-46,10,4,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-150,"curved","right"));

            nodes.push(new node(l1x-14,l1y-23,10,4,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-110,"regular","right"));

            l1y=b_h[1];

            drawFullBranch(l1x, l1y, 110,60,2,10,"right","l");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-10,"regular","right"));

            nodes.push(new node(l1x+60,l1y-40,3,10,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,34,10,0,"regular","right"));

            nodes.push(new node(l1x+60,l1y-40,12,8,2,"right_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-80,"curved","right"));

            nodes.push(new node(l1x+12,l1y-10,3,16,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,36,12,10,"curved","right"));

            nodes.push(new node(l1x+25,l1y-20,8,3,2,"right_c"));
            leaves.push(new leaf(n2x,n2y,34,10,-80,"normal","right"));

            l1y=b_h[4];

            drawFullBranch(l1x, l1y, 55,55,2,8,"right","s");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-60,"regular","right"));

            nodes.push(new node(l1x+22,l1y-36,3,12,2,"rightdown_c"));
            leaves.push(new leaf(n2x,n2y,32,10,-10,"curved","right"));

            nodes.push(new node(l1x+8,l1y-24,12,3,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-80,"regular","right"));

            l1y=b_h[5];

            drawFullBranch(l1x, l1y, 55,26,2,6,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","left"));


            nodes.push(new node(l1x-20,l1y-24,14,12,2,"lefttop_c"));
            leaves.push(new leaf(n2x,n2y,32,10,-150,"curved","right"));

            nodes.push(new node(l1x-16,l1y-22,14,12,2,"leftdown"));
            leaves.push(new leaf(n2x,n2y,30,8,10,"normal","left"));


            drawTopLeaves();
            drawSkills();

        }


        else if (treeId==6){

            l1x=center_x+10;
            l1y=b_h[0];

            drawFullBranch(l1x, l1y,130,30,2,16,"right","lu");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,5,"regular","right"));

            nodes.push(new node(l1x+20,l1y-8,30,23,3,"right_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-70,"regular","right"));

            nodes.push(new node(l1x+80,l1y-30,20,23,3,"right_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-50,"regular","right"));

            nodes.push(new node(l1x+20,l1y-8,8,82,3,"rightdown"));
            leaves.push(new leaf(n2x,n2y,30,10,0,"regular","right"));

            nodes.push(new node(l1x+66,l1y,6,13,3,"right_c"));
            leaves.push(new leaf(n2x,n2y,25,8,-50,"regular","right"));

            l1x=center_x-10;
            l1y=b_h[1];

            drawFullBranch(l1x, l1y, 130,26,2,20,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-20,"regular","left"));

            nodes.push(new node(l1x-100,l1y-30,12,13,2,"left_l"));
            leaves.push(new leaf(n2x,n2y,30,10,40,"regular","left"));

            nodes.push(new node(l1x-26,l1y-18,6,3,2,"left"));
            leaves.push(new leaf(n2x,n2y,30,10,50,"regular","left"));

            nodes.push(new node(l1x-10,l1y-7,13,30,3,"leftdown"));
            leaves.push(new leaf(n2x,n2y,36,10,10,"regular","left"));

            nodes.push(new node(l1x-60,l1y-24,28,30,3,"leftdown_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-40,"regular","left"));

            nodes.push(new node(l1x-75,l1y-14,4,12,2,"leftdown_c"));
            leaves.push(new leaf(n2x,n2y,30,10,5,"regular","left"));


            l1x=center_x+10;
            l1y=b_h[2];

            drawFullBranch(l1x, l1y, 180,65,2,22,"right","u");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,10,"regular","right"));

            nodes.push(new node(x2-40,y2-3,12,18,2,"right_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-30,"regular","right"));
            leaves.push(new leaf(x2-70,y2-2,30,10,-40,"regular","right"));

            nodes.push(new node(x2-50,y2-3,12,18,2,"rightdown"));
            leaves.push(new leaf(n2x,n2y,30,10,20,"regular","right"));

            nodes.push(new node(l1x+10,l1y-24,34,23,3,"right"));
            s_x[0]=n2x; s_y[0]=n2y; s_o[0]="left"; s_angle[0]=-20;

            nodes.push(new node(l1x+20,l1y-20,20,46,3,"rightdown_c"));
            if (numberOfSkills>6){
                s_x[6]=n2x; s_y[6]=n2y; s_o[6]="left"; s_angle[6]=-10;}
            else leaves.push(new leaf(n2x,n2y,30,10,20,"regular","right"));

            nodes.push(new node(l1x+42,l1y-18,10,26,3,"right_c"));
            if (numberOfSkills>7){
                s_x[7]=n2x; s_y[7]=n2y; s_o[7]="left"; s_angle[7]=-10;}
            else leaves.push(new leaf(n2x,n2y,30,10,10,"regular","right"));

            l1x=center_x-10;
            l1y=b_h[3];

            drawFullBranch(l1x, l1y, 160,80,2,16,"left","uu");
            nodes.push(new node(x2,y2,2,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,-40,"regular","left"));

            nodes.push(new node(x2+24,y2-18,2,12,2,"left_c"));
            leaves.push(new leaf(n2x,n2y,30,10,5,"regular","left"));

            nodes.push(new node(x2+55,y2-24,12,18,2,"leftdown_c"));
            leaves.push(new leaf(n2x,n2y,30,10,-40,"regular","left"));

            nodes.push(new node(l1x-40,l1y-52,80,48,3,"left_c"));
            leaves.push(new leaf(n2x,n2y,30,10,90,"regular","left"));
            leaves.push(new leaf(l1x-30,l1y-50,30,10,90,"regular","left"));

            nodes.push(new node(n2x+14,n2y+50,20,20,3,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-20,"regular","right"));
            leaves.push(new leaf(n2x-12,n2y+3,30,10,90,"regular","left"));

            nodes.push(new node(l1x-64,l1y-70,10,22,2,"left"));
            if (numberOfSkills>1){s_x[1]=n2x; s_y[1]=n2y; s_o[1]="right"; s_angle[1]=5;
            } else leaves.push(new leaf(n2x,n2y,30,10,0,"regular","left"));

            nodes.push(new node(l1x-79,l1y-92,10,18,2,"left"));
            if (numberOfSkills>8){
                s_x[8]=n2x; s_y[8]=n2y; s_o[8]="right"; s_angle[8]=5;
            } else leaves.push(new leaf(n2x,n2y,27,8,10,"regular","left"));

            nodes.push(new node(l1x-86,l1y-113,10,18,2,"left"));
            if (numberOfSkills>11){
                s_x[11]=n2x; s_y[11]=n2y; s_o[11]="right"; s_angle[11]=5;}
            else leaves.push(new leaf(n2x,n2y,30,10,30,"regular","left"));
            //down triangle
            nodes.push(new node(l1x-10,l1y-24,20,48,3,"leftdown"));
            if (numberOfSkills>14){
                s_x[14]=n2x; s_y[14]=n2y; s_o[14]="right"; s_angle[14]=5;}
            else leaves.push(new leaf(n2x,n2y,30,10,20,"regular","left"));

            nodes.push(new node(l1x-20,l1y-14,28,18,3,"leftdown"));
            if (numberOfSkills>9){s_x[9]=n2x; s_y[9]=n2y; s_o[9]="right"; s_angle[9]=-5;}
            else leaves.push(new leaf(n2x,n2y,28,8,0,"regular","left"));

            nodes.push(new node(l1x-35,l1y-6,20,18,3,"left"));
            if (numberOfSkills>10){s_x[10]=n2x; s_y[10]=n2y; s_o[10]="right"; s_angle[10]=5;}
            else leaves.push(new leaf(n2x,n2y,30,8,30,"regular","left"));


            l1x=center_x+10;
            l1y=b_h[4];

            drawFullBranch(l1x, l1y, 125,75,2,18,"right","ss");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-80,"regular","right"));

            nodes.push(new node(n2x-20,n2y+34,18,7,2,"right_c"));
            leaves.push(new leaf(n2x,n2y,28,8,-100,"regular","right"));

            nodes.push(new node(l1x+120, l1y-57,4,12,2,"right"));
            leaves.push(new leaf(n2x,n2y,28,10,-40,"regular","right"));

            nodes.push(new node(l1x+70, l1y-37,8,8,2,"right_c"));
            leaves.push(new leaf(n2x,n2y,26,8,-60,"regular","right"));

            nodes.push(new node(l1x+90, l1y-37,2,38,2,"rightdown"));
            leaves.push(new leaf(n2x,n2y,26,8,-30,"regular","right"));

            nodes.push(new node(l1x, l1y+8,20,62,3,"right"));
            leaves.push(new leaf(n2x,n2y,25,8,-5,"regular","right"));
            leaves.push(new leaf(l1x+20, l1y-3,25,8,15,"regular","right"));
            leaves.push(new leaf(l1x+40, l1y-11,20,6,-40,"regular","right"));


            nodes.push(new node(l1x+20,l1y-24,50,8,3,"right"));
            if (numberOfSkills>2){s_x[2]=n2x; s_y[2]=n2y; s_o[2]="left"; s_angle[2]=-20;}
            else leaves.push(new leaf(n2x,n2y,30,10,-60,"regular","right"));

            nodes.push(new node(l1x+22,l1y-44,14,20,3,"right"));
            if (numberOfSkills>15){s_x[15]=n2x; s_y[15]=n2y; s_o[15]="left"; s_angle[15]=-20;}
            else leaves.push(new leaf(n2x,n2y,30,10,-30,"regular","right"));


            l1x=center_x-10;
            l1y=b_h[5];
            drawFullBranch(l1x, l1y, 5,0,2,6,"left","u");
            nodes.push(new node(x2,y2,2,0,2,"left"));

            leaves.push(new leaf(n2x,n2y,34,12,60,"regular","left"));

            leaves.push(new leaf(n2x+4,n2y+6,30,10,-60,"regular","left"));



            l1x=center_x+8;
            l1y=b_h[6];

            drawFullBranch(l1x, l1y, 75,55,2,30,"right","c");
            nodes.push(new node(x2,y2,0,0,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-40,"regular","right"));

            leaves.push(new leaf(x2-30,y2+12,22,6,10,"regular","right"));

            nodes.push(new node(l1x+2,l1y-10,40,20,2,"right_c"));
            if (numberOfSkills>4){s_x[4]=n2x; s_y[4]=n2y; s_o[4]="left"; s_angle[4]=-22;}
            else leaves.push(new leaf(n2x,n2y,28,8,-60,"regular","right"));


            nodes.push(new node(l1x+14,l1y,3,40,2,"rightdown_u"));
            if (numberOfSkills>5){s_x[5]=n2x; s_y[5]=n2y; s_o[5]="left"; s_angle[5]=-10;}
            else leaves.push(new leaf(n2x,n2y,30,10,-20,"regular","right"));

            nodes.push(new node(l1x+5,l1y,10,20,3,"rightdown_u"));
            if (numberOfSkills>13){s_x[13]=n2x; s_y[13]=n2y; s_o[13]="left"; s_angle[13]=-20;}
            else leaves.push(new leaf(n2x,n2y,28,10,-40,"regular","right"));

            l1x=center_x-8;
            l1y=b_h[7];

            drawFullBranch(l1x, l1y, 55,70,2,56,"left","v");
            nodes.push(new node(x2,y2,0,0,2,"left"));
            leaves.push(new leaf(n2x,n2y,34,10,20,"regular","left"));


            nodes.push(new node(x2+35,y2+15,10,2,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,30,10,-130,"regular","right"));


            nodes.push(new node(l1x-10,l1y-46,28,38,2,"leftdown"));
            if (numberOfSkills>3){s_x[3]=n2x; s_y[3]=n2y; s_o[3]="right"; s_angle[3]=-2;}
            else leaves.push(new leaf(n2x,n2y,30,10,5,"regular","left"));

            nodes.push(new node(x2+35,y2+17,8,28,2,"leftdown"));
            if (numberOfSkills>12){s_x[12]=n2x; s_y[12]=n2y; s_o[12]="right"; s_angle[12]=2;}
            else leaves.push(new leaf(n2x,n2y,30,8,10,"regular","left"));

            drawTopLeaves();
            arrangeSkills();

        }
//functions

// trunk
        function drawTrunk(){


            if (treeId<=4){
                ctx.strokeStyle = '#4d8700';
                ctx.beginPath();

                if (treeId>2){
                    ctx.lineWidth   = 4;
                    ctx.moveTo(center_x, bottom_y);
                    ctx.bezierCurveTo(center_x+5, b_h[0]-10,center_x-2, b_h[0]-20,center_x, b_h[0]);
                    ctx.stroke();
                }
                ctx.lineWidth   = 3;
                ctx.moveTo(center_x, bottom_y);
                ctx.bezierCurveTo(center_x+5, b_h[0]-10,center_x-2, b_h[0]-20,center_x, tree_h);
                ctx.stroke();

            }



        if (treeId==5){

            ctx.strokeStyle = '#614434';
            ctx.fillStyle   = '#614434';
            ctx.beginPath();
            ctx.lineWidth   = 1;

            ctx.moveTo(center_x-8, bottom_y);
            ctx.bezierCurveTo(center_x-2, b_h[0]+14,center_x-4, b_h[0]+24,center_x-4, b_h[0]);
            ctx.lineTo(center_x+5, b_h[0]);
            ctx.bezierCurveTo(center_x+3, b_h[0]+24,center_x+4, b_h[0]+14,center_x+10, bottom_y);

            ctx.moveTo(center_x-4, b_h[0]);
            ctx.bezierCurveTo(center_x-2, b_h[4]+14,center_x-2, b_h[4]+24,center_x-4, b_h[4]);
            ctx.lineTo(center_x+3, b_h[4]);
            ctx.bezierCurveTo(center_x+3, b_h[4]+24,center_x+2, b_h[4]+14,center_x+5, b_h[0]);

            ctx.moveTo(center_x-4, b_h[4]);
            ctx.bezierCurveTo(center_x-2, b_h[2]+14,center_x-2, b_h[2]+24,center_x-2, b_h[2]);
            ctx.lineTo(center_x+2, b_h[2]);
            ctx.bezierCurveTo(center_x+2, b_h[2]+24,center_x+2, b_h[2]+14,center_x+3, b_h[4]);

            ctx.moveTo(center_x-2, b_h[2]);
            ctx.bezierCurveTo(center_x-2, tree_h+14,center_x-1, tree_h+24,center_x, tree_h);
            ctx.lineTo(center_x+1, tree_h);
            ctx.bezierCurveTo(center_x+2, tree_h+24,center_x+1, tree_h+14,center_x+2, b_h[2]);

            ctx.fill();
            ctx.stroke();

        }


        if (treeId==6){

            ctx.strokeStyle = '#614434';
            ctx.fillStyle   = '#614434';
            ctx.beginPath();
            ctx.lineWidth   = 1;

            ctx.moveTo(center_x-16, bottom_y);
            ctx.bezierCurveTo(center_x-12, b_h[0]+104,center_x-12, b_h[0]+24,center_x-12, b_h[0]);
            ctx.lineTo(center_x+16, b_h[0]);
            ctx.bezierCurveTo(center_x+18, b_h[0]+14,center_x+16, b_h[0]+28,center_x+20, bottom_y);


            ctx.fill();
            ctx.stroke();

            ctx.moveTo(center_x-12, b_h[0]);
            ctx.bezierCurveTo(center_x-12, b_h[2]+14,center_x-10, b_h[2]+24,center_x-10, b_h[2]);
            ctx.lineTo(center_x+14, b_h[2]);
            ctx.lineTo(center_x+16, b_h[0]);
            ctx.lineTo(center_x, b_h[0]);


            ctx.moveTo(center_x-11,b_h[2]);
            ctx.quadraticCurveTo(center_x-9, b_h[2]-10,center_x-9, tree_h);
            ctx.lineTo(center_x+9, tree_h);
            ctx.lineTo(center_x+12, tree_h+122);
            ctx.lineTo(center_x+14, b_h[2]);


            ctx.moveTo(center_x-9,tree_h);
            ctx.bezierCurveTo(center_x-6, tree_h-60,center_x-28, tree_h-75,center_x-66, tree_h-98);
            ctx.lineTo(center_x-64, tree_h-98);
            ctx.bezierCurveTo(center_x-20, tree_h-74,center_x-6, tree_h-60,center_x+8,tree_h);
            ctx.lineTo(center_x-9,tree_h);
            ctx.stroke();

            ctx.stroke();
            ctx.fill();





        }
    }




    function drawSkills(){
        for (var i=0; i<numberOfSkills;i++)
        {
            if (i%2==0 )
            {

                skills.push(new skill(skillList[i],0,i,"right"));
            }
            else
            {
                skills.push(new skill(skillList[i],0,i,"left"));
            }
        }

    }

    function drawTopLeaves(){
        ctx.lineWidth   = 2;
        if (treeId==1)
        {
            l1x=center_x;
            l1y=tree_h;

            nodes.push(new node(l1x+1,l1y+4,8,4,2,"left"));
            leaves.push(new leaf(n2x,n2y,24,8,50,"small","left"));

            nodes.push(new node(l1x,l1y+4,10,4,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,8,-40,"small","right"));
        }

        else if (treeId<6)
        {l1x=center_x;
            l1y=tree_h;

            nodes.push(new node(l1x+1,l1y+4,8,4,2,"left"));
            leaves.push(new leaf(n2x,n2y,26,8,30,"small","left"));

            nodes.push(new node(l1x+1,l1y+4,12,1,2,"right"));
            leaves.push(new leaf(n2x,n2y,24,6,-90,"small","right"));

            nodes.push(new node(l1x,l1y+4,10,6,2,"right"));
            leaves.push(new leaf(n2x,n2y,34,10,-25,"small","right"));

            n1x=center_x+1;
            n1y=b_h[2];

            nodes.push(new node(n1x,n1y,8,8,2,"right"));
            leaves.push(new leaf(n2x,n2y,38,12,-40,"curved","right"));

            n1x=center_x+1;
            n1y=b_h[3];

            nodes.push(new node(n1x,n1y,8,8,2,"left"));
            leaves.push(new leaf(n2x,n2y,32,10,30,"curved","left"));

        }


        else if (treeId==6)
        {l1x=center_x-64;l1y=tree_h-98;

            nodes.push(new node(l1x,l1y,0,0,2,"lefttop"));
            leaves.push(new leaf(n2x,n2y,34,10,-150,"regular","right"));

            nodes.push(new node(l1x+24,l1y+14,20,10,2,"lefttop_c"));
            leaves.push(new leaf(n2x,n2y,24,8,80,"regular","left"));

            leaves.push(new leaf(n2x+20,n2y+27,34,10,100,"regular","left"));

            nodes.push(new node(l1x+48,l1y+38,6,40,2,"leftdown_u"));
            leaves.push(new leaf(n2x,n2y,30,8,-160,"regular","right"));


            nodes.push(new node(l1x+58,l1y+60,60,12,3,"righttop_c"));
            leaves.push(new leaf(n2x,n2y,30,8,-80,"regular","right"));

            nodes.push(new node(l1x+59,l1y+40,22,20,2,"right"));
            leaves.push(new leaf(n2x,n2y,30,10,-30,"regular","right"));
}
    }



    function drawBranch(x1,y1,l,h,t,o,s)
    {


        var x3,y3,x4,y4;

        if (o=="left")
        { x2=x1-l;
            x3=x1-6;
            x4=x2+12;
        }
        else
        {x2=x1+l;
            x3=x1+6;
            x4=x2-16;
        }

        y2=y1-h;
        y3=y1-h;
        y4=y2-10;


        if (s=="s")
        {
            x3=x1+l/4;     x4=x2-12;
            y3=y1-h;     y4=y3+12;
        }

        if (s=="ls")
        {
            x3=x1+l/4;     x4=x2-20;
            y3=y1-h;     y4=y3+30;
        }



        ctx.lineWidth   = t;

        if (treeId<5) {
            ctx.strokeStyle = '#4d8700';
        }
        else
        {
            ctx.strokeStyle = '#614434';
            ctx.fillStyle   = '#614434';}

        ctx.beginPath();
        ctx.moveTo(x1,y1);
        ctx.bezierCurveTo(x3,y3,x4,y4,x2,y2);
        ctx.stroke();

    }


    function drawFullBranch(x1,y1,l,h,t,w,o,s)
    {


        var x3,y3,x4,y4;

        if (o=="left")
        { x2=x1-l;
            x3=x1-6;
            x4=x2+12;
        }
        else
        {x2=x1+l;
            x3=x1+6;
            x4=x2-16;
        }

        y2=y1-h;
        y3=y1-h;
        y4=y2-10;


        if (s=="s")
        {
            x3=x1+l/4;     x4=x2-12;
            y3=y1-h;     y4=y3+12;
        }

        if (s=="ss")
        {
            x3=x1+l/4;     x4=x2-w/2;
            y3=y1-h;     y4=y3+h;
        }

        if (s=="ls")
        {
            x3=x1+l/4;     x4=x2-20;
            y3=y1-h;     y4=y3+30;
        }

        if (s=="l")
        {
            x3=x1+l/4;     x4=x2-20;
            y3=y1-h/2;     y4=y1-h;
        }
        if (s=="lu")
        {
            x3=x1+l/2;     x4=x2-10;
            y3=y1-h-h/2;     y4=y1-h;
        }

        if (s=="c")
        {
            if (o=="left")
            {
                x3=x1-6;     x4=x2+20;}
            y3=y1-h/2-10;     y4=y1-h;
        }

        if (s=="v")
        {
            if (o=="left")
            {
                x3=x1-6;     x4=x1-6;}
            y3=y1-h/2-10;     y4=y1-(0.8)*h;
        }

        if (s=="uu")
        {
            y2=y1-h/2;
            y3=y1-h;     y4=y1-h;

            x3=x1-w/2;
            x4=x2+30;
        }

        if (s=="ld")
        {

            y2=y1+h;
            y3=y1+h;
            x3=x1-l/4;     x4=x2+20;
            y3=y1+h/2;     y4=y1+h;
        }

        ctx.lineWidth   = t;

        if (treeId<5) {
            ctx.strokeStyle = '#4d8700';
        }
        else
        {
            ctx.strokeStyle = '#614434';
            ctx.fillStyle   = '#614434';}


        ctx.beginPath();
        ctx.moveTo(x1,y1);
        ctx.bezierCurveTo(x3,y3,x4,y4,x2,y2);
        ctx.bezierCurveTo(x4,y4,x3,y3+2,x1,y1+w);
        ctx.moveTo(x1,y1);
        ctx.fill();
        ctx.stroke();


    }



    function arrangeSkills() {

        var blossom = new Image();
        blossom.src = "img/pp/blossom_s.png";



        var angle,align,skillName;
        blossom.onload = function() {

            for (var i=0; i<numberOfSkills;i++){


                angle=s_angle[i];
                align=s_o[i];
                skillName=skillList[i];

                ctx.save();

                ctx.translate(s_x[i],s_y[i]);
                ctx.rotate(angle*Math.PI/180);

                ctx.fillStyle = '#d16700';
                ctx.font=" 16px Calibri";
                ctx.textAlign = align;

                if (skillMastery[i]>=masteryThreshold){
                    if (align=="right")
                    {
                        ctx.drawImage(blossom,-20,-14);
                        wrapText(ctx,skillName,-20, 0,120,14);

                    }
                    else
                    {
                        ctx.drawImage(blossom,0,-14);
                        wrapText(ctx,skillName, 24, 0, 120,14);
                    }
                }
                else
                    wrapText(ctx,skillName, 0, 0, 150,14);
                ctx.restore();
            }
        }

    }


}




}


