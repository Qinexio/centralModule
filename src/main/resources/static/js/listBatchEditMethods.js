var origin   = window.location.origin; 

function redirectToPage(data) {
	window.location.reload(true); // false would reload from cache instead
	// from server
}


$(document).on('click', '.sentimentAnger', function() {
	changeSentiment(parseInt($(this).attr('id')),"anger", redirectToPage);
});

$(document).on('click', '.sentimentNeutral', function() {
	changeSentiment(parseInt($(this).attr('id')),"neutral", redirectToPage);
});

$(document).on('click', '.sentimentLove', function() {
	changeSentiment(parseInt($(this).attr('id')),"love", redirectToPage);
});


$(document).on('click', '.sentimentFear', function() {
	changeSentiment(parseInt($(this).attr('id')),"fear", redirectToPage);
});


$(document).on('click', '.sentimentSurprise', function() {
	changeSentiment(parseInt($(this).attr('id')),"surprise", redirectToPage);
});


$(document).on('click', '.sentimentJoy', function() {
	changeSentiment(parseInt($(this).attr('id')),"joy", redirectToPage);
});

$(document).on('click', '.sentimentSadness', function() {
	changeSentiment(parseInt($(this).attr('id')),"sadness", redirectToPage);
});

$(document).on('click', '.deleteSentence', function() {
	deleteSentence(parseInt($(this).attr('id')), redirectToPage);
});

function changeSentiment(sentenceID, sentiment, callbackFunc) {
	$.ajax({
		type : 'PUT',
		url : origin + '/sentence/' + sentenceID+'/sentimentUpdate/'+sentiment,
		success : function(data) {
			callbackFunc(data);
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

function deleteSentence(sentenceID, callBackFunc)
{
	$.ajax({
		type : 'DELETE',
		url : origin + '/sentence/' + sentenceID,
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