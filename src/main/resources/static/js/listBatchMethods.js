var origin   = window.location.origin; 

function redirectToPage() {
	window.location.reload(true); // false would reload from cache instead
	// from server
}

function deleteBatch(batchID, callBackFunc)
{
	$.ajax({
		type : 'DELETE',
		url : origin + '/batch/' + batchID,
		success : function() {
			callBackFunc();
		},
		error : function(e) {
			console.log("ERROR: ", e);
			alert(e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
}

function validateBatch(batchID, callBackFunc)
{
	$.ajax({
		type : 'PUT',
		url : origin + '/batch/validate/' + batchID,
		success : function() {
			callBackFunc();
		},
		error : function(e) {
			console.log("ERROR: ", e);
			alert(e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});
}
$(document).on('click', '.deleteButton', function() {
	deleteBatch(parseInt($(this).attr('id')), redirectToPage);
});
$(document).on('click', '.validateButton', function() {
	validateBatch(parseInt($(this).attr('id')), redirectToPage);
});
