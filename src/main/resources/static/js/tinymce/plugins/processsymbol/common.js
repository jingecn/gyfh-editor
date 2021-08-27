var symbolCfg = symbolCfg || {interValSplitter: '┇'};
var currentSymbol = currentSymbol || {};
var interValSplitter = symbolCfg.interValSplitter || '┇';

function parseTpl(cfgVal, dynamicVals) {
    var dynamic = cfgVal.match(/(\$\{.+?\})?/g);
    if (dynamic && dynamic.length > 0) {
        for (var dc = 0; dc < dynamic.length; dc++) {
            var g = dynamic[dc];//${key}
            if(!g){
                continue;
            }
            var valKey = g.substring(2, g.length - 1);
            var dval = dynamicVals[valKey];
            if (dval === undefined) {
                dval = '';
            }
            cfgVal = cfgVal.replace(new RegExp('\\$\\{' + valKey + '\\}', 'g'), dval);
        }
    }
    return cfgVal;
}
function InputCmp(selector,checkable) {
    this.selector=selector;
    var me=this;
    if(checkable){
        this.setValue= function (v) {
            $(selector).each(function (i, item) {
                if (v == $(this).val()) {
                    $(this).attr('checked', true);
                }
            })
        };
        this.getValue= function () {
            return $(selector+':checked').val();
        };
    }else{
        this.setValue=function(v){
            $(me.selector).val(v);
        };
        this.getValue=function () {
            return $(me.selector).val();
        }
    }
}
function SymbolInputComponent(inputNames, latexTpl, interValTpl) {
    this.inputNames = inputNames;//输入框名称和数组值的对应关系 如：['up','down',{setValue:function(v){},getValue:function(){return ''}}]  若对应多个，实现setValue,getValue
    this.latexTpl = latexTpl;//latex模板 如:\frac{${0}}{${1}}
    this.interValTpl = interValTpl;//内部值字符串，会加到img标签的title属性上，以便编辑时回填
    var me = this;
    this.getValueAry = function () {
        var valAry = [];
        for (var i = 0; i < me.inputNames.length; i++) {
            var inputName=me.inputNames[i];
            if(typeof inputName=='string'){
                 valAry[i]=$('#symbolForm input[name='+inputName+']').val();
            }else if(typeof  inputName=='object'){
                valAry[i]=inputName.getValue();
            }
        }
        return valAry;
    }
    this.setValueAry = function (valAry) {
        for (var i = 0; i < me.inputNames.length; i++) {
            var inputName=me.inputNames[i];
            if(typeof inputName=='string'){
                $('#symbolForm input[name='+inputName+']').val(valAry[i]||'');
            }else if(typeof  inputName=='object'){
                inputName.setValue(valAry[i]);
            }
        }
    }

    this.getLatex=function () {
        var valAry= me.getValueAry();
        var latexStr=me.latexTpl;
        if(typeof me.latexTpl=="function"){
            latexStr=me.latexTpl(valAry);
        }
        return parseTpl(latexStr,valAry);
    }

    this.getInterVal=function () {
        var valAry= me.getValueAry();
        return parseTpl(me.interValTpl,valAry);
    }
}

currentSymbol.inputCmp = new SymbolInputComponent([], '', '');

function setImgWithBase64(latex,interVal, cb) {
    if(latex===undefined||latex===null){
         latex=currentSymbol.inputCmp.getLatex();
    }
    if(interVal===undefined||interVal===null){
        interVal=currentSymbol.inputCmp.getInterVal();
    }
    var imgSelector = '.preview img';
    $.ajax({
        url: (symbolCfg.urlPrefix || '') + 'generatepic/tosvg',
        type: "POST",
        data: {
            latex: latex,
            asBase64:'true'
        },
        success: function (response) {
            $('.preview  fieldset p').remove();
            if(typeof response=='string' && response.indexOf('ERROR')>=0){
                var imgSrc='data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7';
                $(imgSelector).attr('src', imgSrc);
                $('.preview  fieldset').append('<p class="errorTip" style="color: #e03e2d">'+response+'</p>');
                return;
            }

            // $('.preview  fieldset *').remove();
            // $('.preview  fieldset').append(response);
            // cb && cb(response);

            var imgType = 'data:image/svg+xml;base64,'; //imgType='data:image/png;base64,';
            $(imgSelector).attr('src', imgType + response);
            $(imgSelector).attr('title', interVal);
            var imgHtml = $(imgSelector).get(0).outerHTML;
            cb && cb(imgHtml);
        }
    });
}

function getImgHtml(cb) {
    setImgWithBase64(undefined,undefined, cb);
}

$(function () {
    if(typeof currentSymbol.value=='string' &&  currentSymbol.value){
        var ary=currentSymbol.value.split(interValSplitter);
        currentSymbol.inputCmp.setValueAry(ary);
        setImgWithBase64();
    }
})