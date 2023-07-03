//<script th:src="@{/js/yu/Spot.js}"></script>
//back
function insert() {
	location.href = "/TM/spotList/insert";
}

function update(id) {
	location.href = `/TM/spotList/update?id=${id}`;
}

function deleteNo(spotNo) {
	console.log(spotNo);
	document.querySelector("#deleteTarget").value = spotNo;
	document.querySelector("#ConfirmMsg").innerHTML = `確定刪除(id=${spotNo})?刪除後無法修改!`;
	$("#exampleModal").modal("show");
	$("#exampleModal").css("z-index", "1500");
}


function doDeleteNo() {
	let deleteTarget = document.querySelector("#deleteTarget").value;
	location.href = `/TM/spotList/delete?spotNo=${deleteTarget}`;
}

var myModal;
var exampleModal;
$(document).ready(function() {
	$('#queryResult').DataTable();
	myModal = document.getElementById("deleteBtn");
	exampleModal = new bootstrap.Modal(document.getElementById('exampleModal'));

});	

//front
function allSpot() {
	location.href = "allSpot";
}

function singleSpot() {
	location.href = "singleSpot";
}

const heartIcons = document.querySelectorAll('.heart-icon');

  heartIcons.forEach(function(icon) {
    icon.addEventListener('click', function() {

      icon.classList.replace('fa-regular','fa-solid')
    });
  });