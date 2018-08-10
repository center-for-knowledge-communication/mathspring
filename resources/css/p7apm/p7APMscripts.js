
/* 

  ================================================
  PVII Accordian Panel Magic 2 scripts
  Copyright (c) 2007-2009 Project Seven Development
  www.projectseven.com
  Version: 2.1.6 -build 53
  ================================================
  
*/

// define the image swap file naming convention

// rollover image for any image in the normal state
var p7APMover='_over';

// image for any trigger that has an open sub menu -no rollover
var p7APMopen='_down';

// image to be used for current marker -no roll over
var p7APMmark='_overdown';

var p7APMi=false,p7APMa=false,p7APMctl=[];
function P7_APMset(){
	var i,h,sh,hd,x,v;
	if(!document.getElementById){
		return;
	}
	sh='.p7APMcwrapper {height:0px;overflow:hidden;}\n';
	if(document.styleSheets){
		h='\n<st' + 'yle type="text/css">\n'+sh+'\n</s' + 'tyle>';
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
P7_APMset();
function P7_APMaddLoad(){
	if(!document.getElementById){
		return;
	}
	if(window.addEventListener){
		document.addEventListener("DOMContentLoaded", P7_initAPM, false);
		window.addEventListener("load",P7_initAPM,false);
		window.addEventListener("unload",P7_APMff,false);
	}
	else if(document.addEventListener){
		document.addEventListener("load",P7_initAPM,false);
	}
	else if(window.attachEvent){
		document.write("<script id=p7ie_apm defer src=\"//:\"><\/script>");
		document.getElementById("p7ie_apm").onreadystatechange=function(){
			if (this.readyState=="complete"){
				if(p7APMctl.length>0){
					P7_initAPM();
				}
			}
		};
		window.attachEvent("onload",P7_initAPM);
	}
	else if(typeof window.onload=='function'){
		var p7vloadit=onload;
		window.onload=function(){
			p7vloadit();
			P7_initAPM();
		};
	}
	else{
		window.onload=P7_initAPM;
	}
}
P7_APMaddLoad();
function P7_APMff(){
	return;
}
function P7_opAPM(){
	if(!document.getElementById){
		return;
	}
	p7APMctl[p7APMctl.length]=arguments;
}
function P7_initAPM(){
	var i,j,cn,tB,tA,tC,iM,sr,x,fnA,fnB,swp,s1,s2,s3;
	if(p7APMi){
		return;
	}
	p7APMi=true;
	document.p7APMpreload=[];
	for(i=0;i<p7APMctl.length;i++){
		tB=document.getElementById('p7APM_'+p7APMctl[i][0]);
		if(tB){
			tB.p7opt=p7APMctl[i];
			if(navigator.appVersion.indexOf("MSIE 5")>-1){
				tB.p7opt[2]=0;
			}
			tB.p7APMcont=new Array();
			tB.p7APMtrig=new Array();
			tB.p7APMmv=false;
			tA=tB.getElementsByTagName('A');
			cn=-1;
			for(j=0;j<tA.length;j++){
				if(tA[j].id&&tA[j].id.indexOf('p7APMt')===0){
					tB.p7APMtrig[tB.p7APMtrig.length]=tA[j];
					tA[j].p7state='closed';
					tA[j].p7APMpr=tB.id;
					tA[j].p7APMct=false;
					tC=document.getElementById(tA[j].id.replace('t','w'));
					tB.p7APMcont[tB.p7APMcont.length]=(tC)?tC:null;
					if(cn==-1){
						P7_APMsetClass(tA[j],'apmfirst');
						P7_APMsetClass(tA[j].parentNode,'apmfirst');
					}
					cn=j;
					if(tC){
						tC.p7state='closed';
						tC.p7APMtrg=tA[j].id;
						tA[j].p7APMct=tC.id;
						if(tB.p7opt[2]==1||tB.p7opt[2]==2){
							tC.style.height='0px';
							tC.p7APMtarget=0;
							tC.p7APMrate=10;
							tC.p7ch=0;
						}
						else{
							tC.style.display='none';
							tC.style.height='auto';
						}
					}
					else{
						P7_APMsetClass(tA[j],'p7APM_ext');
					}
					tA[j].onclick=function(){
						return P7_APMtrig(this);
					};
					if(tB.p7opt[6]==1){
						tA[j].onmouseover=function(){
							var tB=document.getElementById(this.p7APMpr);
							if(this.p7state=='closed'){
								P7_APMopen(this);
							}
						};
					}
					if(tB.p7opt[7]==1){
						tA[j].onmouseout=function(evt){
							var tg,pp,dv,tB,m=true;
							tB=document.getElementById(this.p7APMpr);
							dv=this.id.replace('t','w');
							evt=(evt)?evt:event;
							tg=(evt.toElement)?evt.toElement:evt.relatedTarget;
							if(tg){
								pp=tg;
								while(pp){
									if(pp.id&&pp.id.indexOf(dv)===0){
										m=false;
										break;
									}
									pp=pp.parentNode;
								}
							}
							if(m){
								P7_APMclose(this);
							}
						};
						if(tC){
							tC.onmouseout=function(evt){
								var tg,pp,tA,tB,m=true;
								evt=(evt)?evt:event;
								tg=(evt.toElement)?evt.toElement:evt.relatedTarget;
								tA=document.getElementById(this.p7APMtrg);
								tB=document.getElementById(this.p7APMpr);
								if(tg){
									pp=tg;
									if(tg.id&&tg.id==tA.p7APMpr){
										m=true;
									}
									else{
										while(pp){
											if(pp.id){
												if(pp.id.indexOf('p7APM')===0){
													m=false;
													break;
												}
											}
											pp=pp.parentNode;
										}
									}
									if(m){
										if(tA){
											if(tA.p7state=='open'){
												P7_APMclose(tA);
											}
										}
									}
								}
							};
						}
					}
					tA[j].hasImg=false;
					iM=tA[j].getElementsByTagName("IMG");
					if(iM&&iM[0]){
						sr=iM[0].getAttribute("src");
						swp=tB.p7opt[8];
						iM[0].apmswap=swp;
						x=sr.lastIndexOf(".");
						fnA=sr.substring(0,x);
						fnB='.'+sr.substring(x+1);
						s1=fnA+p7APMover+fnB;
						s2=fnA+p7APMopen+fnB;
						s3=fnA+p7APMmark+fnB;
						if(swp==1){
							iM[0].p7imgswap=[sr,s1,s1,s1];
							P7_APMpreloader(s1);
						}
						else if(swp==2){
							iM[0].p7imgswap=[sr,s1,s2,s2];
							P7_APMpreloader(s1,s2);
						}
						else if(swp==3){
							iM[0].p7imgswap=[sr,s1,s2,s3];
							P7_APMpreloader(s1,s2,s3);
						}
						else{
							iM[0].p7imgswap=[sr,sr,sr,sr];
						}
						iM[0].p7state='closed';
						iM[0].mark=false;
						iM[0].rollover=tB.p7opt[9];
						if(swp>0){
							tA[j].hasImg=true;
							iM[0].onmouseover=function(){
								P7_APMimovr(this);
							};
							iM[0].onmouseout=function(){
								P7_APMimout(this);
							};
						}
					}
				}
			}
			if(cn>0){
				P7_APMsetClass(tA[cn],'apmlast');
				P7_APMsetClass(tA[cn].parentNode,'apmlast');
			}
			if(tB.p7opt[3]==-2){
				P7_APMall(tB.id,'open');
			}
			else if(tB.p7opt[3]==-3){
				P7_APMall(tB.id,'open');
				setTimeout("P7_APMall('"+tB.id+"','close',1)",200);
			}
			else if(tB.p7opt[3]==-1){
				ob=P7_APMrandom(tB.id);
				P7_APMopen(ob,1,1,1);
			}
			else{
				tr=tB.id.replace("_","t")+"_"+tB.p7opt[3];
				ob=document.getElementById(tr);
				if(ob){
					P7_APMopen(ob,1,1,1);
				}
			}
			if(tB.p7opt[5]==1&&tB.p7opt[3]!=-2){
				if(tB.p7opt[3]==-3){
					setTimeout("P7_APMauto('"+tB.id+"')",200);
				}
				else{
					P7_APMauto(tB);
				}
			}
			if(tB.p7opt[10]>0){
				tB.p7rtmr=setTimeout("P7_APMrotate('"+tB.id+"',"+tB.p7opt[10]+")",tB.p7opt[11]);
			}
		}
	}
	for(i=0;i<p7APMctl.length;i++){
		P7_APMurl('p7APM_'+p7APMctl[i][0]);
	}
	p7APMa=true;
}
function P7_APMpreloader(){
	var i,x;
	for(i=0;i<arguments.length;i++){
		x=document.p7APMpreload.length;
		document.p7APMpreload[x]=new Image();
		document.p7APMpreload[x].src=arguments[i];
	}
}
function P7_APMimovr(im){
	var m=false,r=im.rollover;
	if(im.mark){
		m=(r>1)?true:false;
	}
	else if(im.p7state=='open'){
		m=(r==1||r==3)?true:false;
	}
	else{
		m=true;
	}
	if(m){
		im.src=im.p7imgswap[1];
	}
}
function P7_APMimout(im){
	var r=im.rollover;
	if(im.mark){
		if(im.p7state=='open'){
			im.src=im.p7imgswap[2];
		}
		else{
			im.src=im.p7imgswap[3];
		}
	}
	else if(im.p7state=='open'){
		if(r==1||r==3){
			im.src=im.p7imgswap[2];
		}
	}
	else{
		im.src=im.p7imgswap[0];
	}
}
function P7_APMctl(tr,ac,bp,tg,an,rt){
	var tA=document.getElementById(tr);
	if(tA){
		if(ac=='open'){
			if(tA.p7state!='open'){
				P7_APMopen(tA,bp,tg,an,rt);
			}
		}
		else if(ac=='close'){
			if(tA.p7state!='closed'){
				P7_APMclose(tA,bp,tg,an,rt)
			}
		}
		else if(ac=='trigger'){
			P7_APMtrig(tA,bp,tg,an,rt);
		}
	}
	return false;
}
function P7_APMall(dv,ac,rt){
	var i,j,tB,a,tA,an=1;
	if(rt==1){
		an=null;
	}
	if(dv=='all'){
		for(i=0;i<p7APMctl.length;i++){
			tB=document.getElementById('p7APM_'+p7APMctl[i][0]);
			tA=tB.p7APMtrig;
			for(j=0;j<tA.length;j++){
				if(ac=='open'&&tA[j].p7state!='open'){
					P7_APMopen(tA[j],1,1,an);
				}
				else if(ac=='close'&&tA[j].p7state!='closed'){
					P7_APMclose(tA[j],1,1,an);
				}
			}
		}
	}
	else{
		tB=document.getElementById(dv);
		if(tB){
			tA=tB.p7APMtrig;
			for(j=0;j<tA.length;j++){
				if(ac=='open'&&tA[j].p7state!='open'){
					P7_APMopen(tA[j],1,1,an);
				}
				else if(ac=='close'&&tA[j].p7state!='closed'){
					P7_APMclose(tA[j],1,1,an);
				}
			}
		}
	}
}
function P7_APMrandom(dd){
	var i,k,j=0,tB,tA,a,rD=new Array();
	tB=document.getElementById(dd);
	if(tB){
		tA=tB.getElementsByTagName("A");
		for(i=0;i<tA.length;i++){
			if(tA[i].p7APMpr && tA[i].p7APMpr==dd && tA[i].p7APMct){
				rD[j]=tA[i].id;
				j++;
			}
		}
		if(j>0){
			k=Math.floor(Math.random()*j);
			a=document.getElementById(rD[k]);
		}
	}
	return a;
}
function P7_APMrotate(dv,md,pn){
	var i,pl,tB=document.getElementById(dv);
	if(md===0){
		if(tB.p7rtmr){
			clearTimeout(tB.p7rtmr);
		}
		if(tB.p7rtrun){
			tB.p7rtcntr--;
			tB.p7rtrun=false;
		}
		return;
	}
	else{
		if(tB.p7rtrun){
			return;
		}
	}
	if(tB&&tB.p7APMtrig){
		if(md>0){
			tB.p7rtmd=md;
			tB.p7rtcy=1;
			tB.p7rtcntr=1;
		}
		if(!pn||pn<0){
			pn=-1;
			for(i=0;i<tB.p7APMtrig.length;i++){
				if(tB.p7APMtrig[i].p7state=='open'){
					pn=i;
					break;
				}
			}
		}
		else{
			pn--;
		}
		pl=pn;
		pn=(pn<=-1)?0:pn;
		pn=(pn>tB.p7APMtrig.length-1)?tB.p7APMtrig.length-1:pn;
		if(md>0){
			tB.p7rtsp=(pl==-1)?pl:pn;
		}
		if(tB.p7rtmr){
			clearTimeout(tB.p7rtmr);
		}
		tB.p7rtmr=setTimeout("P7_APMrunrt('"+dv+"',"+pn+")",10);
	}
}
function P7_APMrunrt(dv,n){
	var a,tB;
	tB=document.getElementById(dv);
	tB.p7rtrun=true;
	if(tB.p7rtmr){
		clearTimeout(tB.p7rtmr);
	}
	if(n>-1&&n<tB.p7APMtrig.length){
		a=tB.p7APMtrig[n];
		if(a.p7state!="open"){
			P7_APMopen(a,null,null,null,1);
		}
		tB.p7rtcntr++;
	}
	n++;
	if(tB.p7rtcntr>tB.p7APMtrig.length){
		tB.p7rtcy++;
		tB.p7rtcntr=1;
	}
	if(n>=tB.p7APMtrig.length){
		n=0;
	}
	if(tB.p7rtcy>tB.p7rtmd){
		if(tB.p7rtsp==-1){
			tB.p7rtmr=setTimeout("P7_APMall('"+dv+"','close',1)",tB.p7opt[11]);
		}
		else{
			tB.p7rtmr=setTimeout("P7_APMctl('"+	tB.p7APMtrig[n].id+"','open',true,false,false,1)",tB.p7opt[11]);
		}
		tB.p7rtrun=false;
	}
	else{
		tB.p7rtmr=setTimeout("P7_APMrunrt('"+dv+"',"+n+")",tB.p7opt[11]);
	}
}
function P7_APMtrig(a,bp,tg,an,rt){
	var m=false;
	if(!p7APMa&&!bp){
		return false;
	}
	if(!a.p7APMct){
		if(a.href!=window.location.href){
			m=true;
		}
		return m;
	}
	if(a.p7state=='open'){
		P7_APMclose(a,bp,tg,an,rt);
	}
	else{
		P7_APMopen(a,bp,tg,an,rt);
	}
	return m;
}
function P7_APMopen(a,bp,tg,an,rt){
	var i,tB,cT,iM,op;
	if(!p7APMa&&!bp){
		return;
	}
	if(a.p7state=='open'){
		return;
	}
	tB=document.getElementById(a.p7APMpr);
	op=tB.p7opt[2];
	if(!p7APMa||an==1){
		op=0;
	}
	a.p7state='open';
	P7_APMsetClass(a,'p7APMtrig_down');
	if(a.hasImg){
		iM=a.getElementsByTagName("IMG")[0];
		iM.p7state='open';
		iM.src=iM.p7imgswap[2];
	}
	cT=document.getElementById(a.p7APMct);
	if(!cT){
		return;
	}
	if((!tg&&tB.p7opt[1]==1)||rt==1){
		for(i=0;i<tB.p7APMtrig.length;i++){
			if(tB.p7APMtrig[i].p7state=='open'){
				if(tB.p7APMtrig[i]!=a){
					P7_APMclose(tB.p7APMtrig[i],null,1);
				}
			}
		}
	}
	if(cT){
		if(op>0&&P7_APMhasOverflow(cT.getElementsByTagName('DIV')[0])){
			op=0;
		}
		if(op==1||op==2){
			cT.style.height='0px';
			cT.p7ch=0;
			P7_APMsetGlide(a,op,tB.p7opt[12]);
			if(!tB.p7APMrunning){
				tB.p7APMrunning=true;
				tB.p7APMglide=setInterval("P7_APMglide('"+tB.id+"')",cT.p7APMdy);
			}
		}
		else{
			if(tB.p7opt[2]==0){
				cT.style.display='block';
			}
			else{
				cT.style.height='auto';
				P7_APMsetGlide(a,op,tB.p7opt[12]);
				cT.p7ch=cT.p7APMtarget;
			}
		}
	}
}
function P7_APMclose(a,bp,tg,an,rt){
	var i,m=false,tB,cT,iM,op;
	if(!p7APMa&&!bp){
		return;
	}
	if(a.p7state=='closed'){
		return;
	}
	tB=document.getElementById(a.p7APMpr);
	op=tB.p7opt[2];
	if(!p7APMa||an==1){
		op=0;
	}
	if(!tg&&tB.p7opt[4]==1){
		for(i=0;i<tB.p7APMtrig.length;i++){
			if(tB.p7APMtrig[i].p7state=='open'){
				if(tB.p7APMtrig[i]!=a){
					m=true;
					break;
				}
			}
		}
		if(!m){
			return;
		}
	}
	a.p7state='closed';
	P7_APMremClass(a,'p7APMtrig_down');
	if(a.hasImg){
		iM=a.getElementsByTagName("IMG")[0];
		iM.p7state='closed';
		if(iM.mark){
			iM.src=iM.p7imgswap[3];
		}
		else{
			iM.src=iM.p7imgswap[0];
		}
	}
	cT=document.getElementById(a.p7APMct);
	if(cT){
		if(P7_APMhasOverflow(cT.getElementsByTagName('DIV')[0])){
			op=0;
		}
		if(op==1||op==2){
			cT.p7ch=cT.offsetHeight;
			P7_APMsetGlide(a,op,tB.p7opt[12]);
			if(!tB.p7APMrunning){
				tB.p7APMrunning=true;
				tB.p7APMglide=setInterval("P7_APMglide('"+tB.id+"')",cT.p7APMdy);
			}
		}
		else{
			if(tB.p7opt[2]==0){
				cT.style.display='none';
			}
			else{
				cT.style.height='0px';
				cT.p7ch=0;
				P7_APMsetGlide(a,op,tB.p7opt[12]);
			}
		}
	}
}
function P7_APMglide(d){
	var i,ht,tB,tA,tC,st,ch,th,nh,inc,tt,tp,pc=.15,m=false;
	tB=document.getElementById(d);
	tA=tB.p7APMtrig;
	tC=tB.p7APMcont;
	for(i=0;i<tA.length;i++){
		st=tA[i].p7state;
		if(tC[i]){
			ch=tC[i].p7ch;
			if(st=='open'&&tC[i].p7APMtarget==0){
				tC[i].p7APMtarget=tC[i].offsetHeight;
			}
			th=(st=='closed')?0:tC[i].p7APMtarget;
			inc=tC[i].p7APMrate;
			if(tB.p7opt[2]==2){
				tt=Math.abs( parseInt(ch-th) );
				tp=parseInt(tt*pc);
				inc=(tp<1)?1:tp;
			}
			if(st=='closed'&&ch!==0){
				nh=ch-inc;
				nh=(nh<=0)?0:nh;
				m=true;
				tC[i].style.height=nh+'px';
				tC[i].p7ch=nh;
			}
			else if(st=='open'&&ch!=th){
				nh=ch+inc;
				nh=(nh>=th)?th:nh;
				m=true;
				tC[i].style.height=nh+'px';
				tC[i].p7ch=nh;
			}
			else if(st=='open'){
				tC[i].style.height='auto';
			}
			else{
			}
		}
	}
	if(!m){
		tB.p7APMrunning=false;
		clearInterval(tB.p7APMglide);
	}
}
function P7_APMsetGlide(a,op,dur){
	var tC,tS,th,stp,fr,dy;
	dur=(dur>0)?dur:250;
	dy=(op==2)?15:20;
	tC=document.getElementById(a.p7APMct);
	tC.p7APMdy=dy;
	tS=document.getElementById(a.id.replace('t','c'));
	th=tS.offsetHeight;
	tC.p7APMtarget=th;
	stp=dur/dy;
	fr=parseInt(th/stp);
	fr=(fr<=1)?1:fr;
	tC.p7APMrate=fr;
}
function P7_APMurl(dv){
	var i,h,s,x,d='apm',a,n=dv.replace("p7APM_","");
	if(document.getElementById){
		h=document.location.search;
		if(h){
			h=h.replace('?','');
			s=h.split(/[=&]/g);
			if(s&&s.length){
				for(i=0;i<s.length;i+=2){
					if(s[i]==d){
						x=s[i+1];
						if(n!=x.charAt(0)){
							x=false;
						}
						if(x){
							a=document.getElementById('p7APMt'+x);
							if(a&&a.p7state!="open"){
								P7_APMopen(a,1);
							}
						}
					}
				}
			}
		}
		h=document.location.hash;
		if(h){
			x=h.substring(1,h.length);
			if(n!=x.charAt(3)){
				x=false;
			}
			if(x&&x.indexOf(d)===0){
				a=document.getElementById('p7APMt'+x.substring(3));
				if(a&&a.p7state!="open"){
					P7_APMopen(a,1);
				}
			}
		}
	}
}
function P7_APMauto(ob){
	var i,wH,tr,pp,im;
	if (typeof ob!='object'){
		ob=document.getElementById(ob);
	}
	wH=window.location.href;
	if(wH.charAt(wH.length-1)=='#'){
		wH=wH.substring(0,wH.length-1);
	}
	r1=/index\.[\S]*/i;
	tA=ob.getElementsByTagName("A");
	for(i=0;i<tA.length;i++){
		if(tA[i].href==wH){
			if(tA[i].p7APMpr){
				tr=tA[i];
				break;
			}
			else{
				P7_APMsetClass(tA[i],'current_mark');
				pp=tA[i].parentNode;
				while(pp){
					if(pp.id&&pp.id.indexOf('p7APMw')==0){
						tr=document.getElementById(pp.p7APMtrg);
						break;
					}
					pp=pp.parentNode;
				}
				break;
			}
		}
	}
	if(tr){
		P7_APMsetClass(tr,'current_mark');
		P7_APMsetClass(tr.parentNode,'current_mark');
		if(tr.hasImg){
			im=tr.getElementsByTagName('IMG')[0];
			im.mark=true;
			im.src=im.p7imgswap[3];
		}
		P7_APMopen(tr,1);
	}
}
function P7_APMsetClass(ob,cl){
	var cc,nc,r=/\s+/g;
	cc=ob.className;
	nc=cl;
	if(cc&&cc.length>0){
		nc=cc+' '+cl;
	}
	nc=nc.replace(r,' ');
	ob.className=nc;
}
function P7_APMremClass(ob,cl){
	var cc,nc,r=/\s+/g;;
	cc=ob.className;
	if(cc&&cc.indexOf(cl>-1)){
		nc=cc.replace(cl,'');
		nc=nc.replace(r,' ');
		ob.className=nc;
	}
}
function P7_APMhasOverflow(ob){
	var s,m;
	if(navigator.userAgent.toLowerCase().indexOf('gecko')>-1){
		s=ob.style.overflow;
		if(!s){
			if(document.defaultView.getComputedStyle(ob,"")){
				s=document.defaultView.getComputedStyle(ob,"").getPropertyValue("overflow");
			}
		}
	}
	m=(s&&s=='auto')?true:false;
	return m;
}
