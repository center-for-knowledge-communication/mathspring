
/* 
 ================================================
 PVII ToolTip Magic scripts
 Copyright (c) 2010-2011 Project Seven Development
 www.projectseven.com
 Version: 1.2.8 -build 45
 ================================================
 
*/

var p7TTMctl=[],p7TTMi=false,p7TTMa=false,p7TTMopentmr;
function P7_TTMset(){
	var h,sh,hd,v;
	if (!document.getElementById){
		return;
	}
	sh='.p7TTMbox {position:absolute; visibility:hidden; left:-3000px; top:-9000px; z-index:999999;}\n';
	sh+='.p7TTMcall, .p7TTMclose {display:none;}\n';
	sh+='#p7TTMholder {position:absolute; visibility:hidden; left:-9000px; top:-9000px;}\n';
	sh+='.p7TTMclose {position:absolute;z-index:9999999;}\n';
	if (document.styleSheets){
		h='\n<st'+'yle type="text/css">\n'+sh+'\n</s' + 'tyle>';
		document.write(h);
	}
	else{
		h=document.createElement('style');
		h.type='text/css';
		h.appendChild(document.createTextNode(sh));
		hd=document.getElementsByTagName('head');
		hd[0].appendChild(h);
	}
}
P7_TTMset();
function P7_opTTM(){
	if(!document.getElementById){
		return;
	}
	p7TTMctl[p7TTMctl.length]=arguments;
}
function P7_TTMaddLoad(){
	if(!document.getElementById||typeof document.createElement=='undefined'){
		return;
	}
	if(window.addEventListener){
		document.addEventListener("DOMContentLoaded",P7_initTTM,false);
		window.addEventListener("resize",P7_TTMrsz,false);
		window.addEventListener("load",P7_initTTM,false);
		window.addEventListener("unload",P7_TTMrf,false);
	}
	else if(document.addEventListener){
		document.addEventListener("load",P7_initTTM,false);
	}
	else if(window.attachEvent){
		document.write("<script id=p7ie_ttm defer src=\"//:\"><\/script>");
		document.getElementById("p7ie_ttm").onreadystatechange=function(){
			if (this.readyState=="complete"){
				if(p7TTMctl.length>0){
					P7_initTTM();
				}
			}
		};
		window.attachEvent("onload",P7_initTTM);
		window.attachEvent("onresize",P7_TTMrsz);
	}
	else if(typeof window.onload=='function'){
		var p7vloadit=onload;
		window.onload=function(){
			p7vloadit();
			P7_initTTM();
		};
	}
	else{
		window.onload=P7_initTTM;
	}
}
P7_TTMaddLoad();
function P7_TTMrf(){
	return;
}
function P7_initTTM(){
	var i,j,k,num=1,tD,eL,cS,tA,pH,eN,iD,tB,att,tm,cL,rel,dv,tC,wR,tr,m,pN,ea;
	if(p7TTMi){
		return;
	}
	p7TTMi=true;
	document.p7TTMboxes=[];
	document.p7TTMtriggers=[];
	document.p7TTMz=999999;
	document.p7TTMall='on';
	for(j=0;j<p7TTMctl.length;j++){
		eL=p7TTMctl[j][0].split(':');
		cS=p7TTMctl[j][1].split(':');
		tA=[];
		tA.length=0;
		if(eL[0].toLowerCase()=='id'){
			tA[0]=document.getElementById(eL[1]);
		}
		else if(eL[0].toLowerCase()=='att'){
			tA=P7_TTMgetElementsByAttribute(eL[1],eL[2]);
		}
		else if(eL[0].toLowerCase()=='tag'){
			tA=document.getElementsByTagName(eL[1].toUpperCase());
		}
		else if(eL[0].toLowerCase()=='class'){
			tA=P7_TTMgetElementsByClassName(eL[1]);
		}
		if(tA&&tA.length&&tA.length>0&&tA[0]){
			for(i=0;i<tA.length;i++){
				m=false;
				rel=tA[i].getAttribute('rel');
				if(rel&&typeof(rel)=='string'&&rel!==''&&rel.toLowerCase().indexOf('no_tooltip')===0){
					m=false;
				}
				else if(cS[0].toLowerCase()=='id'){
					eN=document.getElementById(cS[1]);
					if(eN){
						tA[i].ttmSourceType='id';
						tA[i].ttmSourceID=cS[1];
						tA[i].ttmSourceATT='';
						m=true;
					}
				}
				else if(cS[0].toLowerCase()=='att'){
					att=tA[i].getAttribute(cS[1]);
					if(att&&typeof(att)=='string'&&att!==''){
						tA[i].ttmSourceType='att';
						tA[i].ttmSourceID='';
						tA[i].ttmSourceATT=cS[1];
						m=true;
					}
				}
				else if(cS[0].toLowerCase()=='attid'){
					att=tA[i].getAttribute(cS[1]);
					if(att&&typeof(att)=='string'&&att!==''){
						eN=document.getElementById(att);
						if(eN){
							tA[i].ttmSourceType='attid';
							tA[i].ttmSourceID=att;
							tA[i].ttmSourceATT='';
							m=true;
						}
					}
				}
				if(m){
					tA[i].p7TTMopt=null;
					tA[i].p7TTMopt=p7TTMctl[j];
					tA[i].ttmState='closed';
					if(!tA[i].ttmTrigNum){
						tA[i].ttmTrigNum=document.p7TTMtriggers.length;
						document.p7TTMtriggers[document.p7TTMtriggers.length]=tA[i];
					}
				}
			}
		}
	}
	tA=document.p7TTMtriggers;
	for(i=0;i<tA.length;i++){
		m=true;
		if(tA[i].ttmSourceType=='id' || tA[i].ttmSourceType=='attid'){
			eN=document.getElementById(tA[i].ttmSourceID);
			pN=eN.parentNode;
			if(pN.id&&pN.id.indexOf('p7TTMcontent_')>-1){
				tB=document.getElementById(pN.id.replace('content','box'));
				if(tB){
					tA[i].ttmBox=tB.id;
					m=false;
				}
			}
		}
		else if(tA[i].ttmSourceType=='att'){
			att=tA[i].getAttribute(tA[i].ttmSourceATT);
			if(!att && tA[i].ttmBox){
				ea='att:'+tA[i].ttmSourceATT;
				if(tA[i].p7TTMopt[1]==ea){
					m=false;
				}
			}
		}
		if(m){
			tA[i].ttmBox=P7_TTMbuild(num);
			tA[i].ttmBoxNum=num;
			tB=document.getElementById(tA[i].ttmBox);
			document.p7TTMboxes[num-1]=tB;
			tB.ttmState='closed';
			tB.p7TTMcloseTmr=null;
			tC=document.getElementById('p7TTMcontent_'+num);
			wR=document.getElementById('p7TTM_inner_'+num);
			tB.style.width=tA[i].p7TTMopt[4]+'px';
			if(tA[i].ttmSourceType=='id'){
				tC.appendChild(eN);
			}
			else if(tA[i].ttmSourceType=='att'){
				tC.innerHTML=att;
			}
			else if(tA[i].ttmSourceType=='attid'){
				tC.appendChild(eN);
			}
			num++;
		}
		else{
			tA[i].ttmBoxNum=tA[i].ttmBox.split('_')[1];
		}
		tA[i].setAttribute('title','');
		tB.isIE5=false;
		if(P7_TTMgetIEver()==5){
			tA[i].p7TTMopt[3]=0;
			tA[i].p7TTMopt[6]=0;
			tB.isIE5=true;
		}
		if(tA[i].p7TTMopt[7]==1&&tA[i].p7TTMopt[12]<2){
			tB.onmouseout=P7_TTMout;
		}
		tB.onmouseover=function(){
			if(this.p7TTMcloseTmr){
				clearTimeout(this.p7TTMcloseTmr);
			}
		};
		cL=document.getElementById('p7TTMclose_'+tA[i].ttmBoxNum);
		cL.ttmBox=tB.id;
		cL.onclick=function(){
			var tB=document.getElementById(this.ttmBox);
			P7_TTMparentClose(tB.id,1);
			if(tB.ttmChildID){
				P7_TTMshutChild(tB.id);
			}
			return false;
		};
		if(tA[i].p7TTMopt[10]==1||tA[i].p7TTMopt[7]!=1){
			cL.style.display='block';
		}
		else{
			cL.style.display='none';
		}
		if(tA[i].p7TTMopt[12]<2){
			if(tA[i].p7TTMopt[15]&&tA[i].p7TTMopt[15]==1){
				tA[i].onmouseover=function(e){
					var tB=document.getElementById(this.ttmBox);
					P7_TTMsetCursorPos(e,this);
					if(p7TTMopentmr){
						clearTimeout(p7TTMopentmr);
					}
					if(tB&&tB.p7TTMcloseTmr){
						clearTimeout(tB.p7TTMcloseTmr);
					}
					if(this.ttmState!='open'){
						p7TTMopentmr=setTimeout(" P7_TTMdelayOpen('"+this.ttmTrigNum+"')",160);
					}
				};
			}
			else{
				tA[i].onmouseover=function(e){
					P7_TTMsetCursorPos(e,this);
					P7_TTMopen(this);
				};
			}
		}
		else if(tA[i].p7TTMopt[12]==2&&tA[i].p7TTMopt[7]==1){
			tA[i].onmouseover=function(e){
				var tB=document.getElementById(this.ttmBox);
				P7_TTMsetCursorPos(e,this);
				if(tB.p7TTMcloseTmr){
					clearTimeout(tB.p7TTMcloseTmr);
				}
			};
		}
		if(tA[i].p7TTMopt[7]==1&&tA[i].p7TTMopt[12]<2){
			tA[i].onmouseout=function(){
				var dl,tB=document.getElementById(this.ttmBox);
				dl=(this.ttmDelay)?200:400;
				if(tB.p7TTMcloseTmr){
					clearTimeout(tB.p7TTMcloseTmr);
				}
				tB.p7TTMcloseTmr=setTimeout("P7_TTMparentClose('"+tB.id+"')",dl);
			};
		}
		if(tA[i].p7TTMopt[12]==1){
			tA[i].onclick=function(e){
				P7_TTMsetCursorPos(e,this);
				return P7_TTMclick(this,1);
			};
		}
		else{
			tA[i].onclick=function(e){
				P7_TTMsetCursorPos(e,this);
				return P7_TTMclick(this);
			};
		}
	}
	for(i=0;i<tA.length;i++){
		if(tA[i].p7TTMopt[16]&&tA[i].p7TTMopt[16]==1){
			P7_TTMopen(tA[i],null,1);
		}
	}
	P7_TTMurl();
	p7TTMa=true;
}
function P7_TTMsetCursorPos(evt,a){
	var posx,posy;
	evt=(evt)?evt:window.event;
	if(evt.pageX||evt.pageY){
		posx=evt.pageX;
		posy=evt.pageY;
	}
	else if(evt.clientX||evt.clientY){
		posx=evt.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;
		posy=evt.clientY+document.body.scrollTop+document.documentElement.scrollTop;
	}
	if(a){
		a.ttmPosX=posx;
		a.ttmPosY=posy;
	}
}
function P7_TTMctrl(d,ac){
	P7_TTMcontrol(d,ac);
}
function P7_TTMcontrol(d,ac){
	var i,tR;
	if(typeof(d)=='string'){
		tR=document.getElementById(d);
	}
	else if(typeof(d)=='number'){
		d--;
		if(document.p7TTMtriggers.length>d&&d>-1){
			tR=document.p7TTMtriggers[d];
		}
	}
	if(tR&&tR.p7TTMopt){
		if(ac=='open'){
			P7_TTMopen(tR);
		}
		else if(ac=='close'){
			P7_TTMclose(tR.ttmBox);
		}
		else if(ac=='trig'){
			P7_TTMclick(tR);
		}
	}
}
function P7_TTMdelayOpen(n){
	var tR=document.p7TTMtriggers[n];
	P7_TTMopen(tR,1);
}
function P7_TTMopen(tr,bp,ld){
	var i,num,tB,wR,tC,pH,cD,cS,cL,pD,t,l,ti,m=false,an,dur,stp,dy=30,fr,dh=100;
	if((!p7TTMa&&ld!=1)||document.p7TTMall=='off'){
		return;
	}
	num=tr.ttmBoxNum;
	tB=document.getElementById('p7TTMbox_'+num);
	if(tB.p7TTMcloseTmr && bp!=1){
		clearTimeout(tB.p7TTMcloseTmr);
	}
	if(tB.ttmState=='open'){
		if(tB.ttmTrigger!=tr){
			P7_TTMclose(tB.id,1);
		}
		else{
			return false;
		}
	}
	if(tB.ttmTrigger){
		P7_TTMclearClass(tB,tB.ttmTrigger);
	}
	tB.ttmTrigger=tr;
	P7_TTMsetClass(tB,tr.p7TTMopt[2]);
	an=tr.p7TTMopt[3];
	wR=document.getElementById('p7TTM_inner_'+num);
	tC=document.getElementById('p7TTMcontent_'+num);
	pD=P7_TTMhasParent(tr);
	if(pD){
		pD.ttmParentLock=true;
		pD.ttmChildID=tB.id;
		tB.ttmParentID=pD.id;
	}
	tB.style.visibility='hidden';
	tB.style.left='-3000px';
	tB.style.top='-9000px';
	wR.style.width=tr.p7TTMopt[4]+'px';
	if(tB.isIE5){
		tB.style.width=tr.p7TTMopt[4]+'px';
	}
	else{
		tB.style.width='auto';
	}
	if(typeof P7_TPMrsz == 'function'){
		P7_TPMrsz();
	}
	tB.style.zIndex=document.p7TTMz;
	document.p7TTMz++;
	tB.style.overflow='visible';
	P7_TTMsetCallout(tB,tr);
	P7_TTMsetClass(tr,'p7TTM_open');
	tB.style.height='auto';
	tB.style.width='auto';
	tB.ttmTargetHeight=P7_TTMgetOffset(tB,'height');
	tB.ttmTargetWidth=P7_TTMgetOffset(tB,'width');
	P7_TTMposBox(tB);
	tr.ttmState='open';
	tB.ttmState='open';
	if(an==1){
		tB.style.visibility='visible';
		if(tB.filters){
			tB.style.filter='alpha(opacity=1)';
		}
		else{
			tB.style.opacity=0.01;
		}
		tB.ttmOpacity=1;
		dur=tr.p7TTMopt[11];
		dur=(dur)?dur:300;
		stp=dur/dy;
		fr=parseInt(dh/stp,10);
		fr=(fr<=1)?1:fr;
		tB.ttmFaderFrameRate=fr;
		if(!tB.ttmFaderRunning){
			tB.ttmFaderRunning=true;
			tB.ttmFader=setInterval("P7_TTMfader('"+tB.id+"')",dy);
		}
	}
	else if(an>1){
		P7_TTMsetGrowOpen(tB);
		tB.style.visibility='visible';
		if(!tB.ttmGrowRunning){
			tB.ttmGrowRunning=true;
			tB.ttmGrow=setInterval("P7_TTMGrow('"+tB.id+"')",dy);
		}
	}
	else{
		tB.style.visibility='visible';
	}
	return false;
}
function P7_TTMclose(dv,bp){
	var i,tB,tR,dy=30,an;
	tB=document.getElementById(dv);
	if(tB.ttmParentLock){
		return;
	}
	if(tB.p7TTMcloseTmr){
		clearTimeout(tB.p7TTMcloseTmr);
	}
	tR=tB.ttmTrigger;
	if(tR){
		P7_TTMremClass(tR,'p7TTM_open');
		an=tR.p7TTMopt[3];
		if(tR.p7TTMopt[13]!=1){
			an=0;
		}
		if(bp==1){
			an=0;
			if(tB.ttmGrowRunning){
				clearInterval(tB.ttmGrow);
				tB.ttmGrowRunning=false;
			}
			if(tB.ttmFaderRunning){
				tB.ttmFaderRunning=false;
				clearInterval(tB.ttmFader);
			}
		}
		tR.ttmState='closed';
		tB.ttmState='closed';
		if(an==1){
			if(!tB.ttmFaderRunning){
				tB.ttmFaderRunning=true;
				tB.ttmFader=setInterval("P7_TTMfader('"+tB.id+"')",dy);
			}
		}
		else if(an>1){
			tB.ttmTargetLeft=tB.ttmStartLeft;
			tB.ttmTargetTop=tB.ttmStartTop;
			tB.ttmTargetHeight=tB.ttmStartHeight;
			tB.ttmTargetWidth=tB.ttmStartWidth;
			tB.style.overflow='hidden';
			if(!tB.ttmGrowRunning){
				tB.ttmGrowRunning=true;
				tB.ttmGrow=setInterval("P7_TTMGrow('"+tB.id+"')",dy);
			}
		}
		else{
			tB.visibility='hidden';
			tB.style.left='-3000px';
			tB.style.top='-9000px';
		}
	}
}
function P7_TTMclick(tr,bp){
	var m=false,h,dv,tB,tg;
	tg=tr.nodeName;
	h=tr.getAttribute("href");
	if(h&&h!==''){
		if(h.charAt(h.length-1)!='#'&&h.search(/javas/i)!==0){
			m=true;
		}
	}
	if(!m&&!bp){
		tB=document.getElementById(tr.ttmBox);
		if(tr.ttmState=='open'){
			P7_TTMparentClose(tB.id,1);
			if(tB.ttmChildID){
				P7_TTMshutChild(tB.id);
			}
		}
		else{
			P7_TTMopen(tr);
		}
	}
	if(tg!='A' && tg!='AREA'){
		m=true;
	}
	return m;
}
function P7_TTMshutChild(dv){
	var i,bx=document.p7TTMboxes;
	for(i=0;i<bx.length;i++){
		if(bx[i].ttmState=='open'&&bx[i].ttmParentID&&bx[i].ttmParentID==dv){
			P7_TTMparentClose(bx[i].id,1);
			if(bx[i].ttmChildID){
				P7_TTMshutChild(bx[i].ttmChildID);
			}
		}
	}
}
function P7_TTMclearClass(tB,tr){
	var po,cT=document.getElementById('p7TTMcall_'+tr.ttmBoxNum);
	po=tr.p7TTMopt[5];
	P7_TTMremClass(tB,tr.p7TTMopt[2]);
	P7_TTMremClass(cT,'p7TTM_Arrow_'+po);
	P7_TTMremClass(tB,'Arrow_'+po);
}
function P7_TTMsetCallout(tB,tr){
	var cT,co,po;
	co=tr.p7TTMopt[6];
	po=tr.p7TTMopt[5];
	cT=document.getElementById('p7TTMcall_'+tr.ttmBoxNum);
	if(co==1){
		P7_TTMsetClass(cT,'p7TTM_Arrow_'+po);
		P7_TTMsetClass(tB,'Arrow_'+po);
		cT.style.display='block';
	}
	else{
		P7_TTMremClass(cT,'p7TTM_Arrow_'+po);
		P7_TTMremClass(tB,'Arrow_'+po);
		cT.style.display='none';
	}
}
function P7_TTMposBox(tB){
	var bx,wx;
	if(typeof(tB.ttmTrigger)!='object'){
		return;
	}
	bx=P7_TTMprePos(tB);
	if(tB.ttmTrigger.p7TTMopt[14]==1){
		wx=P7_TTMedge(tB,bx[0],bx[1]);
		if(wx[0]==1&&tB.ttmTrigger.p7TTMopt[6]==1){
			document.getElementById(tB.id.replace('box_','call_')).style.display='none';
			bx=P7_TTMprePos(tB);
			wx=P7_TTMedge(tB,bx[0],bx[1]);
		}
	}
	else{
		wx=[0,bx[0],bx[1]];
	}
	tB.style.top=wx[1]+'px';
	tB.style.left=wx[2]+'px';
}
function P7_TTMprePos(tB){
	var bt,bl,th,tw,p,pp,tl=0,tt=0,bh,bw,tR,rct,ie,ws,tG,hasMap=false,cr,oT;
	p=tB.ttmTrigger.p7TTMopt[5];
	tR=tB.ttmTrigger;
	th=tB.ttmTrigger.offsetHeight;
	tw=tB.ttmTrigger.offsetWidth;
	bh=tB.offsetHeight;
	bw=tB.offsetWidth;
	tG=tR.nodeName;
	if(tG&&(tG=='AREA'||tG=='MAP')){
		if(!tR.p7ttmMapImg){
			P7_TTMgetMapImage(tR,tG);
		}
		if(tR.p7ttmMapImg){
			oT=tR;
			tR=tR.p7ttmMapImg;
			th=tR.offsetHeight;
			tw=tR.offsetWidth;
		}
	}
	ie=P7_TTMgetIEver();
	if(tR.getBoundingClientRect){
		rct=tR.getBoundingClientRect();
		ws=P7_TTMgetWinScroll();
		tl=rct.left+ws[1];
		tt=rct.top+ws[0];
		if(ie>4&&ie<8){
			tl-=2;
			tt-=2;
		}
	}
	else{
		pp=tR;
		while(pp){
			tl+=(pp.offsetLeft)?pp.offsetLeft:0;
			tt+=(pp.offsetTop)?pp.offsetTop:0;
			pp=pp.offsetParent;
		}
	}
	if(tB.ttmTrigger.p7ttmMapImg){
		cr=tB.ttmTrigger.p7ttmCoords;
		tl+=parseInt(cr[0],10);
		tt+=parseInt(cr[1],10);
		tw=parseInt(cr[2],10);
		th=parseInt(cr[3],10);
	}
	if(oT){
		tR=oT;
	}
	if(tR.p7TTMopt[18] && tR.p7TTMopt[18]==1 && tR.ttmPosX){
		tw=3;
		th=3;
		tl=tR.ttmPosX-1;
		tt=tR.ttmPosY-1;
	}
	if(p==1||p==2||p==3){
		bt=tt-bh;
	}
	else if(p==7||p==9){
		bt=tt;
	}
	else if(p==8||p==10){
		bt=tt+((th-bh)/2);
	}
	else{
		bt=tt+th;
	}
	if(p==3||p==6||p==9||p==10){
		bl=tl-bw;
	}
	else if(p==2||p==5){
		bl=tl+((tw-bw)/2);
	}
	else{
		bl=tl+tw;
	}
	bt+=tB.ttmTrigger.p7TTMopt[9];
	bl+=tB.ttmTrigger.p7TTMopt[8];
	return [bt,bl];
}
function P7_TTMedge(tB,bt,bl){
	var nt,nl,bh,bw,wn,ws,m=0;
	wn=P7_TTMgetWinDims();
	ws=P7_TTMgetWinScroll();
	bw=tB.offsetWidth;
	if((bl+bw)>(wn[1]+ws[1])){
		m=1;
		nl=wn[1]-bw+ws[1];
	}
	else{
		nl=bl;
	}
	bh=tB.offsetHeight;
	if(((bt+bh)>(wn[0]+ws[0]))){
		m=1;
		nt=wn[0]-bh+ws[0];
	}
	else{
		nt=bt;
	}
	if(nl<ws[1]){
		nl=ws[1];
		m=1;
	}
	if(nt<ws[0]){
		nt=ws[0];
		m=1;
	}
	return [m,nt,nl];
}
function P7_TTMall(md){
	var cm=document.p7TTMall,nm;
	if(md=='on'||md=='off'){
		nm=md;
	}
	else{
		nm=(cm=='on')?'off':'on';
	}
	document.p7TTMall=nm;
}
function P7_TTMout(evt){
	var tB,tg,pp,m=true;
	evt=(evt)?evt:event;
	tg=(evt.relatedTarget)?evt.relatedTarget:evt.toElement;
	if(tg){
		pp=tg;
		while(pp){
			if(pp==this||pp==this.ttmTrigger){
				m=false;
				break;
			}
			pp=pp.parentNode;
		}
	}
	m=(tg)?m:false;
	if(m){
		if(this.p7TTMcloseTmr){
			clearTimeout(this.p7TTMcloseTmr);
		}
		if(this.ttmParentLock){
			this.p7TTMcloseTmr=setTimeout("P7_TTMparentClose('"+this.id+"')",300);
		}
		else{
			this.p7TTMcloseTmr=setTimeout("P7_TTMclose('"+this.id+"')",300);
		}
	}
}
function P7_TTMparentClose(dv,ck){
	var tB=document.getElementById(dv);
	if(!ck&&tB.ttmChildID&&document.getElementById(tB.ttmChildID).ttmState!='closed'){
		if(tB.p7TTMcloseTmr){
			clearTimeout(tB.p7TTMcloseTmr);
		}
		tB.ttmParentLock=true;
		tB.p7TTMcloseTmr=setTimeout("P7_TTMparentClose('"+tB.id+"')",300);
	}
	else{
		tB.ttmParentLock=false;
		P7_TTMclose(tB.id);
	}
}
function P7_TTMhasParent(tr){
	var m=null,pp=tr.parentNode;
	while(pp){
		if(pp&&pp.id&&typeof(pp.id)=='string'){
			if(pp.id.indexOf('p7TTMbox_')===0){
				m=pp;
			}
			if(pp.nodeName=='BODY'){
				break;
			}
		}
		pp=pp.parentNode;
	}
	return m;
}
function P7_TTMrsz(){
	var i,tB;
	if(p7TTMa&&document.p7TTMboxes){
		for(i=0;i<document.p7TTMboxes.length;i++){
			if(document.p7TTMboxes[i].ttmState=='open'){
				P7_TTMposBox(document.p7TTMboxes[i]);
			}
		}
	}
}
function P7_TTMfader(dv){
	var tB,cP,co,ulm=99,llm=1,fr;
	tB=document.getElementById(dv);
	fr=tB.ttmFaderFrameRate;
	co=tB.ttmOpacity;
	if(tB.ttmState=='open'){
		co+=fr;
		co=(co>=ulm)?ulm:co;
	}
	else{
		co-=fr;
		co=(co<=llm)?llm:co;
	}
	tB.ttmOpacity=co;
	if(tB.filters){
		tB.style.filter='alpha(opacity='+(co)+')';
	}
	else{
		tB.style.opacity=(co/100);
	}
	if((tB.ttmState=='open'&&co==ulm)||(tB.ttmState=='closed'&&co==llm)){
		tB.ttmFaderRunning=false;
		clearInterval(tB.ttmFader);
		if(tB.ttmState=='closed'){
			tB.visibility='hidden';
			tB.style.left='-3000px';
			tB.style.top='-9000px';
		}
		if(tB.filters){
			tB.style.filter='';
		}
		else{
			tB.style.opacity=1;
		}
	}
}
function P7_TTMGrow(dv){
	var an,tB,nl,nh,nw,nt,ml=false,mt=false,mh=false,mw=false;
	tB=document.getElementById(dv);
	nl=tB.ttmCurrentLeft;
	if(tB.ttmTargetLeft<tB.ttmCurrentLeft){
		nl-=(tB.ttmState=='open')?tB.ttmFrameRateLeft:tB.ttmFrameRateLeft*2;
		nl=(nl<=tB.ttmTargetLeft)?tB.ttmTargetLeft:nl;
		ml=true;
	}
	else if(tB.ttmTargetLeft>tB.ttmCurrentLeft){
		nl+=(tB.ttmState=='open')?tB.ttmFrameRateLeft:tB.ttmFrameRateLeft*2;
		nl=(nl>=tB.ttmTargetLeft)?tB.ttmTargetLeft:nl;
		ml=true;
	}
	nt=tB.ttmCurrentTop;
	if(tB.ttmTargetTop<tB.ttmCurrentTop){
		nt-=(tB.ttmState=='open')?tB.ttmFrameRateTop:tB.ttmFrameRateTop*2;
		nt=(nt<=tB.ttmTargetTop)?tB.ttmTargetTop:nt;
		mt=true;
	}
	else if(tB.ttmTargetTop>tB.ttmCurrentTop){
		nt+=(tB.ttmState=='open')?tB.ttmFrameRateTop:tB.ttmFrameRateTop*2;
		nt=(nt>=tB.ttmTargetTop)?tB.ttmTargetTop:nt;
		mt=true;
	}
	nh=tB.ttmCurrentHeight;
	if(tB.ttmCurrentHeight<tB.ttmTargetHeight){
		nh+=(tB.ttmState=='open')?tB.ttmFrameRateHeight:tB.ttmFrameRateHeight*2;
		nh=(nh>=tB.ttmTargetHeight)?tB.ttmTargetHeight:nh;
		mh=true;
	}
	else if(tB.ttmCurrentHeight>tB.ttmTargetHeight){
		nh-=(tB.ttmState=='open')?tB.ttmFrameRateHeight:tB.ttmFrameRateHeight*2;
		nh=(nh<=tB.ttmTargetHeight)?tB.ttmTargetHeight:nh;
		mh=true;
	}
	nw=tB.ttmCurrentWidth;
	if(tB.ttmCurrentWidth<tB.ttmTargetWidth){
		nw+=(tB.ttmState=='open')?tB.ttmFrameRateWidth:tB.ttmFrameRateWidth*2;
		nw=(nw>=tB.ttmTargetWidth)?tB.ttmTargetWidth:nw;
		mw=true;
	}
	else if(tB.ttmCurrentWidth>tB.ttmTargetWidth){
		nw-=(tB.ttmState=='open')?tB.ttmFrameRateWidth:tB.ttmFrameRateWidth*2;
		nw=(nw<=tB.ttmTargetWidth)?tB.ttmTargetWidth:nw;
		mw=true;
	}
	if(ml||mt||mh||mw){
		tB.ttmCurrentLeft=nl;
		tB.ttmCurrentTop=nt;
		tB.ttmCurrentWidth=nw;
		tB.ttmCurrentHeight=nh;
		tB.style.left=nl+'px';
		tB.style.top=nt+'px';
		tB.style.width=nw+'px';
		tB.style.height=nh+'px';
	}
	else{
		clearInterval(tB.ttmGrow);
		tB.ttmGrowRunning=false;
		if(tB.ttmState=='closed'){
			tB.visibility='hidden';
			tB.style.left='-3000px';
			tB.style.top='-9000px';
		}
		else{
			tB.style.height='auto';
			tB.style.overflow='visible';
		}
	}
}
function P7_TTMsetGrowOpen(tB){
	var tR,w,h,fr,dy=30,stp,dsw,dsh,frh,frw,an,dur;
	tR=tB.ttmTrigger;
	tB.style.height='auto';
	tB.style.width='auto';
	an=tR.p7TTMopt[3];
	tB.ttmAnimOpt=an;
	dur=tR.p7TTMopt[11];
	stp=dur/dy;
	tB.ttmTargetLeft=parseInt(tB.style.left,10);
	tB.ttmTargetTop=parseInt(tB.style.top,10);
	h=tB.ttmTargetHeight;
	w=tB.ttmTargetWidth;
	dsw=w;
	dsh=h;
	frh=parseInt(dsh/stp,10);
	frh=(frh<=1)?1:frh;
	frw=parseInt(dsw/stp,10);
	frw=(frw<=1)?1:frw;
	tB.ttmFrameRateHeight=frh;
	tB.ttmFrameRateWidth=frw;
	if(an==8){
		if(frh%2){
			frh+=1;
		}
		if(frw%2){
			frw+=1;
		}
		frw=(frw<2)?2:frw;
		frh=(frh<2)?2:frh;
	}
	tB.ttmFrameRateLeft=frw;
	tB.ttmFrameRateTop=frh;
	tB.ttmStartLeft=tB.ttmTargetLeft;
	tB.ttmStartTop=tB.ttmTargetTop;
	tB.ttmStartHeight=tB.ttmTargetHeight;
	tB.ttmStartWidth=tB.ttmTargetWidth;
	if(an==2){
		tB.ttmStartWidth=1;
	}
	else if(an==3){
		tB.ttmStartLeft=tB.ttmTargetLeft+w;
		tB.ttmStartWidth=1;
	}
	else if(an==4){
		tB.ttmStartHeight=1;
	}
	else if(an==5){
		tB.ttmStartHeight=1;
		tB.ttmStartTop=tB.ttmTargetTop+h;
	}
	else if(an==6){
		tB.ttmStartHeight=1;
		tB.ttmStartWidth=1;
	}
	else if(an==7){
		tB.ttmStartHeight=1;
		tB.ttmStartWidth=1;
		tB.ttmStartLeft=tB.ttmTargetLeft+w;
	}
	else if(an==8){
		tB.ttmStartHeight=1;
		tB.ttmStartWidth=1;
		tB.ttmStartLeft=tB.ttmTargetLeft+w/2;
		tB.ttmStartTop=tB.ttmTargetTop+h/2;
		tB.ttmFrameRateLeft=frw/2;
		tB.ttmFrameRateTop=frh/2;
	}
	tB.ttmCurrentLeft=tB.ttmStartLeft;
	tB.ttmCurrentTop=tB.ttmStartTop;
	tB.ttmCurrentWidth=tB.ttmStartWidth;
	tB.ttmCurrentHeight=tB.ttmStartHeight;
	tB.style.left=tB.ttmStartLeft+'px';
	tB.style.top=tB.ttmStartTop+'px';
	tB.style.width=tB.ttmStartWidth+'px';
	tB.style.height=tB.ttmStartHeight+'px';
	tB.style.overflow='hidden';
}
function P7_TTMbuild(n){
	var box,dv,el,ob,a,inr,il,wr,cls,mdl,mdlw,cnt;
	box=document.createElement('div');
	box.setAttribute('id','p7TTMbox_'+n);
	box.className='p7TTMbox';
	inr=document.createElement('div');
	inr.setAttribute('id','p7TTM_inner_'+n);
	inr.className='p7TTM_inner';
	el=document.createElement('div');
	el.className='p7TTMtop';
	dv=document.createElement('div');
	dv.className='p7TTMtopleft';
	el.appendChild(dv);
	dv=document.createElement('div');
	dv.className='p7TTMtiletop';
	el.appendChild(dv);
	dv=document.createElement('div');
	dv.className='p7TTMtopright';
	el.appendChild(dv);
	inr.appendChild(el);
	mdlw=document.createElement('div');
	mdlw.className='p7TTMmiddlewrapper';
	mdl=document.createElement('div');
	mdl.className='p7TTMmiddle';
	cnt=document.createElement('div');
	cnt.setAttribute('id','p7TTMcnt_'+n);
	cnt.className='p7TTMcnt';
	cls=document.createElement('div');
	cls.setAttribute('id','p7TTMclose_'+n);
	cls.className='p7TTMclose';
	a=document.createElement('a');
	a.setAttribute('href','#');
	il=document.createElement('i');
	il.appendChild(document.createTextNode('Close'));
	a.appendChild(il);
	cls.appendChild(a);
	cnt.appendChild(cls);
	dv=document.createElement('div');
	dv.setAttribute('id','p7TTMcontent_'+n);
	dv.className='p7TTMcontent';
	cnt.appendChild(dv);
	mdl.appendChild(cnt);
	mdlw.appendChild(mdl);
	inr.appendChild(mdlw);
	el=document.createElement('div');
	el.className='p7TTMbottom';
	dv=document.createElement('div');
	dv.className='p7TTMbottomleft';
	el.appendChild(dv);
	dv=document.createElement('div');
	dv.className='p7TTMtilebottom';
	el.appendChild(dv);
	dv=document.createElement('div');
	dv.className='p7TTMbottomright';
	el.appendChild(dv);
	inr.appendChild(el);
	dv=document.createElement('div');
	dv.setAttribute('id','p7TTMcall_'+n);
	dv.className='p7TTMcall';
	box.appendChild(dv);
	box.appendChild(inr);
	document.getElementsByTagName('body')[0].appendChild(box);
	return 'p7TTMbox_'+n;
}
function P7_TTMgetIEver(){
	var j,v=-1,nv,m=false;
	nv=navigator.userAgent.toLowerCase();
	j=nv.indexOf("msie");
	if(j>-1){
		v=parseInt(nv.substring(j+4,j+6),10);
		if(document.documentMode){
			v=document.documentMode;
		}
	}
	return v;
}
function P7_TTMgetWinDims(){
	var h,w,st;
	if(document.documentElement&&document.documentElement.clientHeight){
		w=document.documentElement.clientWidth;
		h=document.documentElement.clientHeight;
	}
	else if(window.innerHeight){
		if(document.documentElement.clientWidth){
			w=document.documentElement.clientWidth;
		}
		else{
			w=window.innerWidth;
		}
		h=window.innerHeight;
	}
	else if(document.body){
		w=document.body.clientWidth;
		h=document.body.clientHeight;
	}
	return [h,w];
}
function P7_TTMgetWinScroll(){
	var st=0,sl=0,IEver=P7_TTMgetIEver();
	if(navigator.userAgent.toLowerCase().indexOf('iphone')>-1){
		return [0,0];
	}
	st=document.body.parentNode.scrollTop;
	if(!st||IEver==5){
		st=document.body.scrollTop;
		if(!st){
			st=window.scrollY?window.scrollY:0;
		}
	}
	sl=document.body.parentNode.scrollLeft;
	if(!sl||IEver==5){
		sl=document.body.scrollLeft;
		if(sl){
			sl=window.scrollX?window.scrollX:0;
		}
	}
	return [st,sl];
}
function P7_TTMgetOffset(el,md,bb){
	var d=0,v,vv,vr,tx=0,bx=0,lx=0,rx=0;
	if(md=='width'){
		el.style.width='auto';
		v=el.offsetWidth;
		el.style.width=v+'px';
		vv=el.offsetWidth;
		el.style.width='auto';
		vr=vv-v;
		d=v-vr;
	}
	else if(md=='height'){
		el.style.height='auto';
		v=el.offsetHeight;
		el.style.height=v+'px';
		vv=el.offsetHeight;
		el.style.height='auto';
		vr=vv-v;
		d=v-vr;
	}
	return d;
}
function P7_TTMsetClass(ob,cl){
	if(ob){
		var cc,nc,r=/\s+/g;
		cc=ob.className;
		nc=cl;
		if(cc&&cc.length>0){
			if(cc.indexOf(cl)==-1){
				nc=cc+' '+cl;
			}
			else{
				nc=cc;
			}
		}
		nc=nc.replace(r,' ');
		ob.className=nc;
	}
}
function P7_TTMremClass(ob,cl){
	if(ob){
		var cc,nc,r=/\s+/g;
		cc=ob.className;
		if(cc&&cc.indexOf(cl>-1)){
			nc=cc.replace(cl,'');
			nc=nc.replace(r,' ');
			nc=nc.replace(/\s$/,'');
			ob.className=nc;
		}
	}
}
function P7_TTMgetElementsByAttribute(att,val){
	var i,x=0,aL,aT,rS=[];
	aL=document.getElementsByTagName('*');
	for(i=0;i<aL.length;i++){
		aT=aL[i].getAttribute(att);
		if(aT&&aT==val){
			rS[x]=aL[i];
			x++;
		}
	}
	return rS;
}
function P7_TTMgetElementsByClassName(cls){
	var i,x=0,aL,aT,rS=[];
	if(typeof(document.getElementsByClassName)!='function'){
		aL=document.getElementsByTagName('*');
		for(i=0;i<aL.length;i++){
			aT=aL[i].className;
			if(aT&&aT==cls){
				rS[x]=aL[i];
				x++;
			}
		}
	}
	else{
		rS=document.getElementsByClassName(cls);
	}
	return rS;
}
function P7_TTMgetMapImage(tR,tg){
	var i,tMap,tArea,tIM,mp,shp,crd,ml,mt,mw,mh;
	tMap=(tg=='MAP')?tR:tR.parentNode;
	if(tMap.nodeName=='MAP'&&tMap.id){
		tIM=document.getElementsByTagName('IMG');
		for(i=0;i<tIM.length;i++){
			mp=tIM[i].getAttribute('usemap');
			if(mp){
				mp=mp.replace('#','');
				if(mp==tMap.id){
					tR.p7ttmMapImg=tIM[i];
					tR.p7ttmCoords=[0,0,0,0];
					break;
				}
			}
		}
	}
	if(tR.p7ttmMapImg && tg=='AREA'){
		if(tR.coords){
			crd=tR.coords.split(",");
			if(tR.shape.toLowerCase()=='rect'&&crd.length==4){
				ml=crd[0];
				mt=crd[1];
				mw=crd[2]-crd[0];
				mh=crd[3]-crd[1];
			}
			else if(tR.shape.toLowerCase()=='circle'&&crd.length==3){
				ml=crd[0]-crd[2];
				mt=crd[1]-crd[2];
				mw=crd[2]*2;
				mh=mw;
			}
			else if(tR.shape.toLowerCase()=='poly'&&crd.length>1){
				ml=crd[0];
				mt=crd[1];
				mw=20;
				mh=20;
			}
		}
		if(ml){
			tR.p7ttmCoords=[ml,mt,mw,mh];
		}
	}
}
function P7_TTMurl(){
	var i,h,x,param,tT,val,d='ttm',pn,dv,m=false;
	if(document.getElementById){
		h=document.location.search;
		if(h){
			h=h.replace('?','');
			param=h.split(/[=&]/g);
			if(param&&param.length){
				for(i=0;i<param.length;i+=2){
					m=false;
					if(param[i]==d){
						val=param[i+1];
						tT=document.getElementById(val);
						if(tT&&tT.p7TTMopt){
							m=true;
						}
						else{
							x=parseInt(val,10);
							if(x&&x>0&&x<document.p7TTMtriggers.length+1){
								tT=document.p7TTMtriggers[x-1];
								m=true;
							}
						}
						if(m){
							if(tT.p7TTMopt.length>17&&tT.p7TTMopt[17]==1){
								P7_TTMopen(tT,null,1);
							}
						}
					}
				}
			}
		}
		h=document.location.hash;
		m=false;
		if(h){
			if(h.indexOf('ttm_')==1){
				val=h.replace('#ttm_','');
				if(val&&val!==''){
					tT=document.getElementById(val);
					if(tT&&tT.p7TTMopt){
						m=true;
					}
					else{
						x=parseInt(val,10);
						if(x&&x>0&&x<document.p7TTMtriggers.length+1){
							tT=document.p7TTMtriggers[x-1];
							m=true;
						}
					}
					if(m){
						if(tT.p7TTMopt.length>17&&tT.p7TTMopt[17]==1){
							alert('opening trigger:'+tT.id);
							P7_TTMopen(tT,null,1);
						}
					}
				}
			}
		}
	}
}
