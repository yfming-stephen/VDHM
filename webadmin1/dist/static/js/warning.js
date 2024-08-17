function warning(){
    var timeInter=10;
    var tt=setInterval(warnJS,timeInter);
    function warnJS(){
        document.getElementById("warningModel").style.display='block';
        var classVal = document.getElementById("warningModel").getAttribute("class");
        classVal = classVal.concat("shakeWarn");
        document.getElementById('warningModel').setAttribute("class",classVal);
        setTimeout("document.getElementById('warningModel').style.display='none'",6500);
        setTimeout("var classVal = document.getElementById('warningModel').getAttribute('class');classVal = classVal.replace('shakeWarn','');document.getElementById('warningModel').setAttribute('class',classVal);",6500);
        clearInterval(tt);

    }
}
