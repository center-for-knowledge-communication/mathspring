/* 
 ================================================
 PVII Equal Height Columns 2 scripts
 Copyright (c) 2011 Project Seven Development
 www.projectseven.com
 Version: 1.0.0 -build 05
 ================================================
 
*/

var p7EHC={
	init: false,
	built: false,
	groups:[],
	currentHeight:[],
	columns:[],
	wrappers:[],
	counter:1
};
function P7_EHCaddLoad(){
	if(!document.getElementById){
		return;
	}
	if(window.addEventListener){
		document.addEventListener("DOMContentLoaded",P7_initEHC,false);
		window.addEventListener("load",P7_initEHC,false);
		window.addEventListener("unload",P7_EHCrf,false);
	}
	else if(window.attachEvent){
		document.write("<script id=p7ie_EHC defer src=\"//:\"><\/script>");
		document.getElementById("p7ie_EHC").onreadystatechange=function(){
			if(this.readyState=="complete"){
				P7_initEHC();
			}
		};
		window.attachEvent("onload",P7_initEHC);
	}
}
P7_EHCaddLoad();
function P7_EHCrf(){
	return;
}
function P7_initEHC(){
	var i,j,nL,cl,sl;
	if(p7EHC.init){
		return;
	}
	p7EHC.init=true;
	nL=document.getElementsByTagName('*');
	for(i=0;i<nL.length;i++){
		cl=nL[i].className;
		if(cl && cl.indexOf('p7ehc-')>-1){
			sl=cl.split(' ');
			for(j=0;j<sl.length;j++){
				if(sl[j].indexOf('p7ehc-')>-1){
					P7_EHCcreate(sl[j],nL[i]);
					p7EHC.built=true;
				}
			}
		}
	}
	if(p7EHC.built){
		setInterval("P7_EHCsizer()",20);
	}
}
function P7_EHCcreate(cl,cD){
	var i,x,tW,m=false;
	for(i=0;i<p7EHC.groups.length;i++){
		if(p7EHC.groups[i]==cl){
			m=true;
			break;
		}
	}
	if(!m){
		i=p7EHC.groups.length;
		p7EHC.groups[i]=cl;
		p7EHC.currentHeight[i]=0;
		p7EHC.columns[i]=[];
		p7EHC.wrappers[i]=[];
		x=0;
	}
	else{
		x=p7EHC.columns[i].length;
	}
	tW=document.createElement('div');
	tW.setAttribute('id','p7EHCd_'+(p7EHC.counter));
	p7EHC.counter++;
	tW.ehcPaddingCounter=0;
	tW.ehcPaddingTop=false;
	tW.ehcPaddingBottom=false;
	cD.appendChild(tW);
	while(cD.childNodes[0]!=tW){
		tW.appendChild(cD.childNodes[0]);
	}
	p7EHC.columns[i][x]=cD;
	p7EHC.wrappers[i][x]=tW;
	tW.ehcPaddingTimer=setTimeout("P7_EHCcheckPadding('"+tW.id+"')",1);
}
function P7_EHCcheckPadding(d,v){
	var p,cD,tW=document.getElementById(d);
	cD=tW.parentNode;
	tW.ehcPaddingCounter++;
	if(!tW.ehcPaddingTop){
		p=P7_EHCgetStyle(cD,'paddingTop','padding-top');
		if(p&&parseInt(p,10)!==0){
			tW.style.paddingTop=p;
			cD.style.paddingTop='0px';
			tW.ehcPaddingTop=true;
		}
	}
	if(!tW.ehcPaddingBottom){
		p=P7_EHCgetStyle(cD,'paddingBottom','padding-bottom');
		if(p&&parseInt(p,10)!==0){
			tW.style.paddingBottom=p;
			cD.style.paddingBottom='0px';
			tW.ehcPaddingBottom=true;
		}
	}
	if(tW.ehcPaddingCounter<50){
		if(!tW.ehcPaddingTop || !tW.ehcPaddingBottom){
			tW.ehcPaddingTimer=setTimeout("P7_EHCcheckPadding('"+d+"')",10);
		}
	}
}
function P7_EHCsizer(){
	var i,j,oh,h;
	for(i=0;i<p7EHC.groups.length;i++){
		oh=0;
		for(j=0;j<p7EHC.wrappers[i].length;j++){
			h=p7EHC.wrappers[i][j].offsetHeight;
			oh=(h>oh)?h:oh;
		}
		if(oh!=p7EHC.currentHeight[i]){
			p7EHC.currentHeight[i]=oh;
			for(j=0;j<p7EHC.columns[i].length;j++){
				p7EHC.columns[i][j].style.height=oh+'px';
			}
		}
	}
}
function P7_EHCgetStyle(el,s1,s2){
	var s='';
	if(el.currentStyle){
		s=el.currentStyle[s1];
	}
	else{
		s=document.defaultView.getComputedStyle(el,"").getPropertyValue(s2);
	}
	return s;
}
