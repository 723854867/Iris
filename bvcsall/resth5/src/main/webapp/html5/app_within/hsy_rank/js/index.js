$(function(){
    var imgurl= $('#serverUrlimg').val()+'/restwww/download';
    var serverUrl= $('#serverUrl').val();


    //$('.user-info').each(function(){
    //    var desc= $(this).find('p').html();
    //    var newStr= $.trim(desc);
        //if(newStr.length > 11){
        //    $(this).find('p').html(newStr.substring(0,11)+'...');
        //}else{
        //    $(this).find('p').html(newStr);
        //}
    //});
    $('.user-pho').each(function(){
        var oSrc= $(this).attr('data-src');
        $(this).attr('src',imgurl+oSrc)
    })

    $('.rank-list li:gt(2)').css({
        'marginTop':'-25px'
    });
    //console.log($(window).width())
})