
function doTraining(useCSV, useGloVE, callbackFunc) {	
	$.ajax({
		type : 'GET',
		url : origin + '/trainModel/'+useCSV+'/'+useGloVE,
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
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

function doReload(callbackFunc) {	
	$.ajax({
		type : 'GET',
		url : origin + '/trainReload',
		contentType : 'application/json; charset=utf-8',
		dataType : 'json',
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

function trainModel() {
	gloveVal = false;
	csvVal = false;
	var csvCheck = document.getElementById("usecsv");  
 	var gloveCheck = document.getElementById("useglove");
 	 
	if (csvCheck.checked == true) {
		csvVal = true;
	}
	
	if (gloveCheck.checked == true) {
		gloveVal = true;
	}

	doTraining(csvVal,gloveVal,displayResult);

}

function displayResult(truthVal) {
	if (truthVal == true) { 
			alert ("Task OK");
		}
	else {
		alert ("Task Failed");
	}
}

function reloadModel() {

	doReload(displayResult);

}

$(document).on('click', '#reloadBtn', reloadModel);

$(document).on('click', '#trainBtn', trainModel);

