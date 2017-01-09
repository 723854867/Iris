(function ( $ ) {
    $.fn.togglePassword = function( options ) {
        var s = $.extend( $.fn.togglePassword.defaults, options ),
        input = $( this );

        $( s.el ).bind( s.ev, function() {
            "password" == $( input ).attr( "type" ) ?
                $( input ).attr( "type", "text" ).next().removeClass('pwdSHBtn1').addClass('pwdSHBtn') :
                $( input ).attr( "type", "password" ).next().removeClass('pwdSHBtn').addClass('pwdSHBtn1');
        });
    };

    $.fn.togglePassword.defaults = {
        ev: "click"
    };
}( jQuery ));

