/**
 * 显示错误信息
 * @param httpRequest XMLHttpRequest对象
 */
function displayErrorMessage(httpRequest) {
    var json = eval("(" + httpRequest.responseText + ")");
    var text = '';
    for (var i in json.content) {
        text = text + json.content[i] + '<br/>';
    }
    $.messager.alert(json.title, text, 'error');
}

/**
 * 显示信息
 * @param responseText 返回信息
 */
function displayMessage(responseText) {
    if ((typeof responseText == 'string') && responseText.constructor == String) var json = eval("(" + responseText + ")");
    if ((typeof responseText == 'object') && responseText.constructor == Object) var json = responseText;
    var text = '';
    for (var i in json.content) {
        text = text + json.content[i] + '<br/>';
    }
    if (json.success) {
        $.messager.alert(json.title, text, 'info');
        closeWindow();
        doSearch();
    } else {
        $.messager.alert(json.title, text, 'error');
    }
}

/**
 *  窗口初始化方法
 * @param title 标题
 * @param url 请求路径
 * @param width 宽度
 * @param height 高度
 */
function initReportWindow(windowName, url) {
    var params = 'height=' + (screen.availHeight - 80) + ',width=' + (screen.availWidth - 20) + ',';
    params += 'top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no';
    window.open(url, windowName, params);
}

/**
 *  窗口初始化方法
 * @param title 标题
 * @param url 请求路径
 * @param width 宽度
 * @param height 高度
 */
function initWindow(title, url, width, height) {
    $('#dialogWindow').window({
        width: width,
        height: height,
        title: title,
        top: ($(window).height() - height) * 0.5,
        left: ($(window).width() - width) * 0.5
    });
    $('#dialogWindow').window('open');
    $('#dialogWindow').window('refresh', url);
}

/**
 * 关闭表单窗口 跟initWindow配对使用
 */
function closeWindow() {
    $('#dialogWindow').window('close');
}

/**
 * 日期格式化
 * @param value 日期
 * @returns {string}
 */
function formatDate(value) {
    var reallydate = new Date(value);
    var year = reallydate.getFullYear();
    var month = reallydate.getMonth() + 1;
    var date = reallydate.getDate();
    var hour = reallydate.getHours();
    var minute = reallydate.getMinutes();
    var second = reallydate.getSeconds();
    return  year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (date < 10 ? "0" + date : date) + "  " +
        (hour < 10 ? "0" + hour : hour) + ":" +
        (minute < 10 ? "0" + minute : minute) + ":" +
        (second < 10 ? "0" + second : second);
}

/**
 * 日期格式化
 * @param value 日期
 * @returns {string}
 */
function format_date(value) {
    var reallydate = new Date(value);
    var year = reallydate.getFullYear();
    var month = reallydate.getMonth() + 1;
    var date = reallydate.getDate();
    return  year + "-" +
        (month < 10 ? "0" + month : month) + "-" +
        (date < 10 ? "0" + date : date);
}

if ((typeof $ == 'object') && $.constructor == Object) {
    $.fn.datebox.defaults.formatter = function (date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + m + '-' + d;
    }

    $.fn.datebox.defaults.parser = function (s) {
        var t = Date.parse(s);
        if (!isNaN(t)) {
            return new Date(t);
        } else {
            return new Date();
        }
    }
}

