
function getPrediction(textToSend, callbackFunc) {
	
	$.ajax({
		type : 'POST',
		url : origin + '/batch',
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		data : JSON.stringify(textToSend),
		success : function(data) {
			callbackFunc(data)
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

function loadPredictionElement(id, description, prediction, percentage) {
	if ($('#responseContainer div').length < 100) {
		$('#responseContainer')
				.append(
						'<hr><div id="'
								+ id
								+ '" class="predictEntryText alignCenter" ><p class="form-control answerText">'
								+ description
								+ '</p>'
								+ '<p class="form-control answerText">'
								+ prediction
								+ '</p>'
								+ '<p class="form-control answerText">'
								+ percentage
								+ '</p>'
								+'<p class="form-control ">Predictie corecta? </p> <input type="checkbox" class="form-control isCorrect"'
								+ false + '></div>');
	}
}

function loadPredictionElementPretty(id, description, prediction, percentage) {
	if ($('#responseContainer div').length < 10000) {
		$('#responseContainer')
				.append(
						'<hr><div class="predictEntryText alignCenter container" ><div class="row"><div class="form-control col-sm"><p class="answerText">'
								+ description
								+ '</p>'
								+ '</div><div class="form-control col-sm"><p class="answerText">'
								+ prediction
								+ '</p></div></div>'
								+ '<div class = "row"><div class="form-control col-sm"><p class=" answerText">'
								+ percentage
								+ '</p></div></div><div class = "row"><div class = "form-control col-sm" id="'
								+id
								+'"><p>Predictie corecta? <input type="checkbox" class="isCorrect"'
								+ false + '></p></div></div></div>');
	}
}

function validateSection() {
	if ($.trim($('.predictTextArea').val()).length < 5) {
		alert("Text needs more than 5 characters");
		return false;
	}

	return true;
}

function predictText() {

	if (validateSection() == false) {
		return;
	}

	getPrediction(getPredictionText(), loadPrediction);

}

function getPredictionText()
{
	textToReturn = $('.predictTextArea').val()
	return textToReturn
}

function loadPrediction(data)
{
	purgePredictor();
	$.each(data.batchSentences, function(index, element) {
			loadPredictionElementPretty(element.id, element.sentenceText,
						element.sentencePrediction, element.sentencePercentages);
			});
}

function nextPrediction()
{
	purgePrediction();
}

function purgePredictor()
{
	$('#predictTextAreaBox').val("");
	$('#textBoxContainer').css('visibility' , 'hidden');
	$('#responseContainer').css('visibility' , 'visible');
	$('#predictBtn').css('visibility' ,  'hidden');
	$('#nextBtn').css('visibility' ,  'visible');
}

function purgePrediction()
{
	$('#responseContainer').children().remove();
	$('#textBoxContainer').css('visibility' ,  'visible');
	$('#responseContainer').css('visibility' ,  'hidden)');
	$('#predictBtn').css('visibility' , 'visible');
	$('#nextBtn').css('visibility' , 'hidden');
}

$(document).on('click', '#predictBtn', predictText);

$(document).on('click', '#nextBtn', nextPrediction);

$(document).on("change", ".isCorrect", function() {
	var sID = parseInt($(this).closest('div').attr('id'));
	var isCorrect;

	if ($(this).is(':checked')) {
		isCorrect = true;
	} else {
		isCorrect = false;
	}

	feedbackSentence(sID, isCorrect);

});

function feedbackSentence(sentenceID, truthVal)
{
	$.ajax({
		type : 'PUT', 
		url : origin + '/sentence/' + sentenceID +'/feedback/' + truthVal,
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
		success : function() {
			console.log("FEEDBACK OK");
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
