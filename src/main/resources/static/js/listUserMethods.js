var origin   = window.location.origin; 

function redirectToPage(data) {
	window.location.reload(true); // false would reload from cache instead
	// from server
}

$(document).on('click', '.banUser', function() {
	banUser(parseInt($(this).attr('id')), redirectToPage);
});

$(document).on('click', '.unbanUser', function() {
	unbanUser(parseInt($(this).attr('id')), redirectToPage);
});

$(document).on('click', '.promoteUser', function() {
	changeRole(parseInt($(this).attr('id')), 'ROLE_ADMIN', redirectToPage);
});

$(document).on('click', '.demoteUser', function() {
	changeRole(parseInt($(this).attr('id')), 'ROLE_NEW', redirectToPage);
});


$(document).on('click', '.demoteAdmin', function() {
	changeRole(parseInt($(this).attr('id')), 'ROLE_USER', redirectToPage);
});


$(document).on('click', '.promoteNew', function() {
	changeRole(parseInt($(this).attr('id')), 'ROLE_USER', redirectToPage);
});

function banUser(profileID, callbackFunc) {
	$.ajax({
		type : 'PUT',
		url : origin + '/profileBan/' + profileID,
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

function unbanUser(profileID, callbackFunc) {
	$.ajax({
		type : 'PUT',
		url : origin + '/profileUnban/' + profileID,
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

function changeRole(profileID, type, callbackFunc) {
	$.ajax({
		type : 'PUT',
		url : origin + '/profileRole/' + profileID +'/' +type,
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
