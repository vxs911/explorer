<g:javascript>
	function submitRsId(form) {
		$.ajax(form.action, { data:$(form).serialize(), dataType:"json", 
			success:function(json) {				
				$("#chromosome").selectpicker("val", json.chromosome);
				$("#position").val(json.position);
				
				$("#hetDiv label").html("Heterogenous ("+json.count.het+") samples");
				$("#homRefDiv label").html("Homogenous Reference ("+json.count.hom_ref+") samples");
				$("#homAltDiv label").html("Homogenous Alternate ("+json.count.hom_alt+") samples");
				$("#ref-alt-values").html("Reference: "+json.reference+"&nbsp;&nbsp; Alternate: "+json.alternate)
				$("input[type=radio]").show();
				$('#myTab a[href="#genotypeDiv"]').tab('show');
			}		
		});
		
		return false;
	}
	
	function submitChromosome(form) {
		$("#chromosome-hidden").val($("#chromosome option:selected").val());
		$('#myTab a[href="#positionDiv"]').tab('show');
		
		return false;
	}
	
	function submitChromosomeAndPosition(form) {
		$.ajax(form.action, { data:$(form).serialize(), dataType:"json", 
			success:function(json) {
				$("#hetDiv label").html("Heterogenous ("+json.count.het+") samples");
				$("#homRefDiv label").html("Homogenous Reference ("+json.count.hom_ref+") samples");
				$("#homAltDiv label").html("Homogenous Alternate ("+json.count.hom_alt+") samples");
				$("#ref-alt-values").html("Reference: "+json.reference+"&nbsp;&nbsp; Alternate: "+json.alternate);
				$("input[type=radio]").show();
				$('#myTab a[href="#genotypeDiv"]').tab('show');			
			}
		});
		
		return false;
	}
	
	function submitGeneSymbol(form) {
		$("#pleaseWaitDialog").modal('show');
		$("#rsids-display-toggle").html("");
		$("#rsids-found").html("").hide();
		$.ajax(form.action, { data:$(form).serialize(), dataType:"json",
			success:function(json) {				
				var html = "";
				for(var i = 0; i < json.length; i++) {
					html += "<a href=\"#\">"+json[i]+"</a><br>";
				}
				$("#rsids-found").html(html);
				$("#rsids-found").find("a").click(function(){
					$('#rs-id').val($(this).html());
					$('#myTab a[href="#rsidDiv"]').tab('show');
				});
				$("#rsids-display-toggle").html("DbSNP IDs found in dataset").show().showOrHide($("#rsids-found"));
			},
			complete: function() {
				$("#pleaseWaitDialog").modal('hide');
			}
		});
		return false;
	}
</g:javascript>