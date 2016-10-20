var scegli = '<option value="0">Scegli...</option>';
var attendere = '<option value="0">Attendere...</option>';
var comuni_testo='';
var provincia = '';
var provincia_testo = '';
var regione = '';
//var regione_testo='';

jQuery(document).ready(function($) {

		$("select#regione").val("");
		$("select#regione").removeAttr("disabled");
		$("select#province").html(scegli);
		$("select#province").attr("disabled", "disabled");
		$("select#comuni").html(scegli);
		$("select#comuni").attr("disabled", "disabled");
		
		$("select#regione").change(function(){
			regione = $("select#regione option:selected").attr('value');
            //regione_testo=$("select#regione option:selected").text();
			
            //$("select#regione option:selected").attr('value',regione_testo);
			$("select#province").html(attendere);
			$("select#province").attr("disabled", "disabled");
			$("select#comuni").html(scegli);
			$("select#comuni").attr("disabled", "disabled");
			
			$.post(paky_ajax.ajaxurl, {action:'the_ajax_hook_prov',whatever:regione}, function(data){
			
                $("select#province").removeAttr("disabled"); 
				$("select#province").html(data);	
			});
		});

        $("select#province").change(function(){
			provincia = $("select#province option:selected").attr('value');
            provincia_testo=$("select#province option:selected").text();
            //$("select#province option:selected").attr('value',provincia_testo);
			$("select#comuni").html(attendere);
			$("select#comuni").attr("disabled", "disabled");
			
			$.post(paky_ajax.ajaxurl, {action:'the_ajax_hook_comu',whatever:provincia}, function(data){
                $("select#comuni").removeAttr("disabled"); 
				$("select#comuni").html(data);
			});
		});	
        
        $("select#comuni").change(function(){
            comuni_testo=$("select#comuni option:selected").text();
            //$("select#comuni option:selected").attr('value',comuni_testo);
			$("input.comu_mail").attr('value',comuni_testo+' ('+provincia_testo+')');
        });
        
});	