//入力値の有無でボタンを活性、非活性する関数
function judgeNull() {
	var tosyoCardId = document.getElementById("tosyoCardId").value;

	if (tosyoCardId != "") {
		document.getElementById("nextButton").disabled = false;
	} else {
		document.getElementById("nextButton").disabled = true;
	}
}