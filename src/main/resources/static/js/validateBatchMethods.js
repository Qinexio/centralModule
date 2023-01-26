var origin = window.location.origin;

var sentenceRow = new Array();

var sentimentRow = new Array();

var qCount = 0;

var eval = new Array();

var batchID;
var userID;

$(document).ready(initTest);

function validateChoice() {
	var selectedACount = 0;
	$('#sentimentContainer').children('.sentimentEntry').each(
		function() {
			if ($(this).children('.sentimentButton').first().hasClass(
				'selectedSentiment') == true) {
				selectedACount += 1;
			}
		});

	if (selectedACount != 1) {
		alert('Please select only one sentiment');
		return false;
	}

	return true;
}

function redirectPage() {
	window.location.href = origin + "/home/";
}

function initSentences() {
	$.ajax({
		type: 'GET',
		url: origin + '/batch/incomplete/' + batchID,
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		timeout: 100000,
		async: false,
		success: function(data) {
			$.each(data.batchSentences, function(index, element) {
				sentenceRow.push(element.id);
			});
		},
		error: function(e) {
			console.log("ERROR: ", e);
			alert(e);
		},
		done: function(e) {
			console.log("DONE");
		}
	});
}

function initSentiments(callbackFunc) {
	$
		.ajax({
			type: 'GET',
			url: origin + '/sentiments/',
			contentType: 'application/json; charset=utf-8',
			dataType: 'json',
			timeout: 100000,
			success: function(data) {
				data.forEach(sent => { sentimentRow.push(sent); });
				callbackFunc();
			},
			error: function(e) {
				console.log("ERROR: ", e);
				alert(e);
			},
			done: function(e) {
				console.log("DONE");
			}
		});
}

function initTest() {
	batchID = parseInt($('#batchID').text());
	userID = parseInt($('#userID').text());
	initSentences();
	
	if (sentenceRow.length == 0)
		{
			displayError();
			return	
		}
	
	initSentiments(getSentiments);
	setMaxSentences(sentenceRow.length);
	getSentence(sentenceRow[qCount]);

}

function displayError()
{
$('#ensambleContainer').children().remove();
	$('#ensambleContainer')
		.append(
			'<div class="finalScore alignCenter"><h2>Nu exista preziceri de verificat, sistemul va valida acest grup pana maine</h2><hr><h3>Va multumim de incercare '
			+ '</h3><hr><button class="goHome btn btn-light alignCenter">Intoarcere</button></div>');
}	

function setMaxSentences(size) {
	$('#maxSentence').text(size);
}

function getSentence(sentenceID) {
	$.ajax({
		type: 'GET',
		url: origin + '/sentence/' + sentenceID,
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		timeout: 100000,
		success: function(data) {
			loadSentence(data.id, data.sentenceText, data.setencePrediction);
		},
		error: function(e) {
			console.log("ERROR: ", e);
			alert(e);
		},
		done: function(e) {
			console.log("DONE");
		}
	});
}

function getSentiments() {
	sentimentRow.forEach(sent => { loadSentiment(sent); });
}

function updateSentence(sentenceID, sentiment) {
	$.ajax({
		type: 'PUT',
		url: origin + '/sentence/' + sentenceID
			+ '/sentimentUpdateMod/' + sentiment + '/',
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		timeout: 100000,
		success: function(data) {
			disableSentimentContainer();
		},
		error: function(e) {
			console.log("ERROR: ", e);
			alert(e);
		},
		done: function(e) {
			console.log("DONE");
		}
	});
}

function loadSentiment(description) {
	if ($('#sentimentContainer div').length < 10) {
		$('#sentimentContainer').append(
			'<div class="sentimentEntry"><a'
			+ ' class="sentimentButton form-control alignCenter">' + description
			+ '</a></div>');
	}
}

function disableSentimentContainer() {
	var buttons = $('#sentimentContainer .sentimentButton')
	
	buttons.each(
			function() {
				$(this).prop('disabled', true);
				});
}


function finalizeProcess() {
	$('#ensambleContainer').children().remove();
	$('#ensambleContainer')
		.append(
			'<div class="finalScore alignCenter"><h2>Finalizare verificare predictii</h2><hr><h3>Va multumim pentru timpul acordat '
			+ '</h3><hr><button class="goHome btn btn-light alignCenter">Iesire</button></div>');
}

function loadSentence(id, description, prediction) {
	$('.sentenceText').attr('id', id);
	$('.sentenceText').text(description);
	$('.sentencePrediction').text(prediction);
}

$(document).on('click', '.goHome', function() {
	redirectPage();
});

$(document).on('click', '.sentimentButton', function() {
	$(this).toggleClass('selectedSentiment');
});

$(document).on(
	'click',
	'.submitSentiment',
	function() {
		if (validateChoice() == false) {
			return;
		}

		var sentiment = [];

		$('#sentimentContainer').children('.sentimentEntry').each(
			function() {
				if ($(this).children('.sentimentButton').first().hasClass(
					'selectedSentiment')) {
					sentiment.push($(this).children(
						'.sentimentButton').first().text());
				}
			});

		qCount += 1;

		updateSentence(parseInt($('.sentenceText').attr('id')), sentiment[0]);
		$(this).attr('class', 'nextSentence btn btn-light alignCenter');
		$(this).html('Urmatoarea prezicere');

		if (qCount > sentenceRow.length - 1) {
			$(this).html('Finalizare');
		}
	});

$(document).on('click', '.nextSentence', function() {
	$(this).attr('class', 'submitSentiment btn btn-light alignCenter');
	$(this).html('Raspunde');
	$('#sentenceCount').text(qCount + 1);
	$('#sentimentContainer').children().remove();

	if (qCount > sentenceRow.length - 1) {
		finalizeProcess();
		return;
	}
	getSentence(sentenceRow[qCount]);
	getSentiments();
});