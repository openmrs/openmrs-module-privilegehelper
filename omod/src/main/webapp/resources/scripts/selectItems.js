var $j = jQuery.noConflict();

$j(document).ready(function() {
	$j("select[name='selectItems']").change(function() {
	    var select = this;
		$j("option:selected", this).each(
			function() {
				var input = "input[value='" + $j(select).attr('id') + "']";
				var option = $j(this).text();
				if (option == "none") {
					$j(input).attr('checked', false);
				} else if (option == "all") {
					$j(input).attr('checked', true);
				} else if (option == "all required") {
					$j(input).each(function(i, el) {
						var result = $j(el).hasClass("required");
						$j(el).attr('checked', result);
					});
				} else if (option == "missing & required") {
					$j(input).each(function(i, el) {
						var result = $j(el).hasClass("required") && $j(el).hasClass("missing");
						$j(el).attr('checked', result);
					});
				}
				$j(input).change();
			});
		$j(this).val("select");
	});
});