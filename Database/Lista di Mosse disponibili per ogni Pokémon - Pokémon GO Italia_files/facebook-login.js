(function( $ ) {
	'use strict';

    $( document).ready( function(){
        $( 'body' ).on( 'click', '.js-fbl', function( e ) {
            e.preventDefault();
            window.fbl_button    = $(this);
            window.fbl_button.addClass('fbl-loading');
            if( navigator.userAgent.match('CriOS') ) {
                $('<p class="fbl_error">'+fbl.l18n.chrome_ios_alert+'</p>').insertAfter( window.fbl_button);
                FB.getLoginStatus( handleResponse );
            } else {
                try {
                    FB.login( handleResponse , {
                        scope: fbl.scopes,
                        return_scopes: true,
                        auth_type: 'rerequest'
                    });
                } catch (err) {
                    window.fbl_button.removeClass('fbl-loading');
                    alert('Facebook Init is not loaded. Check that you are not running any blocking software or that you have tracking protection turned off if you use Firefox');
                }
            }
        });
        $('body').on('click', '.flp_register_text, .flp_register', function(e){
           e.preventDefault();
           var _form = $(this).closest('.flp_form');
           _form.slideUp();
           if( !$(this).is('.flp_register') )
                $(this).hide();
           setTimeout(function(){
               _form.find('.flp_forgot_text').hide();
               _form.find('.login_register').hide();
               _form.removeClass('flp_remember_form');
               _form.find('.remember_pass').hide();
               _form.find('.flp_login_text').show();
               _form.find('.flp_login-password').show();
               _form.find('.flp_register-email').show();
               _form.find('.flp_login-username input').attr( 'placeholder', fbl.l18n.username).val("").focus().blur();
               _form.find('.flp_btn').val(fbl.l18n.submit_register_text);
               _form.find('.flp_action').val('flp_register');
               _form.slideDown();
           },300);
        });
        $('body').on('click', '.flp_login_text, .flp_login', function(e){
           e.preventDefault();
           var _form = $(this).closest('.flp_form');
           _form.slideUp();
           if( !$(this).is('.flp_login') )
           $(this).hide();
           setTimeout(function(){
               _form.find('.flp_forgot_text').show();
               _form.find('.flp_login-password').show();
               _form.find('.flp_login_text').hide();
               _form.find('.login_register').hide();
               _form.removeClass('flp_remember_form');
               _form.find('.remember_pass').hide();
               _form.find('.flp_register_text').show();
               _form.find('.flp_register-email').hide();
               _form.find('.flp_login-username input').attr( 'placeholder', fbl.l18n.username_or_email).val("").focus().blur();
               _form.find('.flp_btn').val(fbl.l18n.submit_login_text);
               _form.find('.flp_action').val('flp_login');
               _form.slideDown();
           },300);
        });
        $('body').on('click', '.flp_forgot_text', function(e){
           e.preventDefault();
           var _form = $(this).closest('.flp_form');
           _form.slideUp();
           $(this).hide();
           setTimeout(function(){
               _form.addClass('flp_remember_form');
               _form.find('.flp_forgot_pass').hide();
               _form.find('.flp_login-password').hide();
               _form.find('.flp_login_text').hide();
               _form.find('.remember_pass').show();
               _form.find('.login_register').show();
               _form.find('.flp_register-email').hide();
               _form.find('.flp_register_text').hide();
               _form.find('.flp_btn').val(fbl.l18n.submit_getnewpass_text);
               _form.find('.flp_action').val('flp_remember_pass');
               _form.slideDown();
           },300);
        });
        $('body').on('submit','.flp_form', function(e){
            e.preventDefault();
            var _form       = $(this),
                errors_box  = _form.parent().find('.flp_errors'),
                action      = _form.find('.flp_action').val();
            _form.slideUp();
            $('<p style="text-align:center" class="fbl_sending"><img src="'+fbl.site_url+'/wp-includes/js/mediaelement/loading.gif" alt=""/></p>').insertAfter(_form);
            setTimeout(function() {
                if (!flp_validates(_form)) {
                    $('.fbl_sending').remove();
                    _form.slideDown();
                    return;
                }
                $.ajax(
                    fbl.ajaxurl,{
                        data: _form.serialize(),
                        dataType: 'json',
                        method: 'POST',
                        success: function(response) {
                            if( response.error ) {
                                errors_box.append(response.error);
                                $('.fbl_sending').remove();
                                _form.slideDown();
                            }
                            if( response.success )
                                if( 'flp_login' == action )
                                    location.replace( response.success );
                                if( 'flp_remember_pass' == action ) {
                                    _form.html(response.success);
                                    $('.fbl_sending').remove();
                                    _form.slideDown();
                                }
                                if( 'flp_register' == action ) {
                                    _form.append(response.success);
                                    $('.fbl_sending').remove();
                                    $('.flp_login_text').click();
                                    _form.slideDown();
                                    if( response.redirect && response.redirect.length )
                                        location.replace( response.redirect );
                                }


                        },
                        error:  function(response) {
                            console.log(response)
                        }
                    }

                ).done();
            },300);


        });
        $('.flp_form').each(function () {
            if( $(this).hasClass('spu_register_form') ){
                $(this).find(".flp_register_text").click();
            }

        });
    });
    var flp_validates = function ( form ) {
        var username_email   = form.find('input[name="flp_log"]'),
           email            = form.find('input[name="flp_email"]'),
           pass             = form.find('input[name="flp_pass"]'),
           action           = form.find('input[name="action"]'),
           errors_box       = form.parent().find('.flp_errors'),
           validates        = true;

        form.find('.flp-error').removeClass('flp-error');
        errors_box.html('');
        if( 'flp_login' == action.val() ) {
           if( username_email.pVal().length < 1 ) {
               username_email.addClass('flp-error');
               errors_box.append('<li>'+fbl.l18n.errors_fill_email+'</li>').hide().fadeIn();
               validates = false;
           }
           if( pass.pVal().length < 1 ) {
               pass.addClass('flp-error');
               errors_box.append('<li>'+fbl.l18n.errors_fill_pass+'</li>').hide().fadeIn();
               validates = false;
           }
        }

        if( 'flp_remember_pass' == action.val() ) {
           if( username_email.pVal().length < 1 ) {
               username_email.addClass('flp-error');
               errors_box.append('<li>'+fbl.l18n.errors_fill_email+'</li>').hide().fadeIn();
               validates = false;
           }
        }

        if( 'flp_register' == action.val() ) {
            if( username_email.pVal().length < 1 ) {
                username_email.addClass('flp-error');
                errors_box.append('<li>'+fbl.l18n.errors_fill_username+'</li>').hide().fadeIn();
                validates = false;
            }
            if( email.pVal().length < 1 || !validateEmail(email.pVal()) ) {
                email.addClass('flp-error');
                errors_box.append('<li>'+fbl.l18n.errors_invalid_email+'</li>').hide().fadeIn();
                validates = false;
            }
            if( pass.pVal().length < 1 ) {
                pass.addClass('flp-error');
                errors_box.append('<li>'+fbl.l18n.errors_fill_pass+'</li>').hide().fadeIn();
                validates = false;
            }
        }
        return validates;
    };

    var handleResponse = function( response ) {
        var $form_obj       = window.fbl_button.parents('.flp_wrapper').find('form') || false,
            $redirect_to    = $form_obj.find('input[name="redirect_to"]').val() || window.fbl_button.data('redirect');
        /**
         * If we get a successful authorization response we handle it
         */
        if (response.status == 'connected') {

            var fb_response = response;

            /**
             * Make an Ajax request to the "facebook_login" function
             * passing the params: username, fb_id and email.
             *
             * @note Not all users have user names, but all have email
             * @note Must set global to false to prevent gloabl ajax methods
             */
            $.ajax({
                data: {
                    action: "fbl_facebook_login",
                    fb_response: fb_response,
                    security:  window.fbl_button.data('fb_nonce')
                },
                global: false,
                type: "POST",
                url: fbl.ajaxurl,
                success: function (data) {
                    if (data && data.success) {
                        if( data.redirect && data.redirect.length ) {
                            location.href = data.redirect.length;
                        } else if ($redirect_to.length) {
                            location.href = $redirect_to;
                        } else {
                            location.href = fbl.site_url;
                        }
                    } else if (data && data.error) {
                         window.fbl_button.removeClass('fbl-loading');
                        $('.fbl_error').remove();
                        if ($form_obj.length) {
                            $form_obj.append('<p class="fbl_error">' + data.error + '</p>');
                        } else {
                            // we just have a button
                            $('<p class="fbl_error">' + data.error + '</p>').insertAfter( window.fbl_button);
                        }
                    }

                }
            });

        } else {
             window.fbl_button.removeClass('fbl-loading');
            if( navigator.userAgent.match('CriOS') )
                window.open('https://www.facebook.com/dialog/oauth?client_id=' + fbl.appId + '&redirect_uri=' + document.location.href + '&scope=email,public_profile', '', null);
        }
    };
    $.fn.pVal = function(){
        var  $this = $(this),
            val = $this.eq(0).val();
        if(val == $this.attr('placeholder'))
            return '';
        else
            return val;
    }

})( jQuery );
function validateEmail($email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,6})?$/;
    return emailReg.test( $email );
}