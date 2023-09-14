$(function() {

	var kaisu = $("#lendableBooks").val();

	//貸出可能冊数分の入力欄表を生成する
	for (var i = 0; i < kaisu; i++) {

		$("#inputTable>tbody").append("<tr><td>" + (i + 1) + "冊目</td><td>"
			+ "<input type='text' class='bookNumInput' id='bookNum_" + i + "' >"
			+ "</td><td>"
			+ "<input type='text'class='bookSubNumInput' id='bookSubNum_" + i + "' >"
			+ " </td></tr>")
	}

	// 入力欄の値が変更された際に実行される関数
	$(".bookNumInput,.bookSubNumInput").on("input", function() {

		$(this).css("background-color", "");

		// 入力欄の値を保持する配列を生成
		var inpBookArray = [];
		//入力欄数分の書籍番号、書籍枝番号をinpBookArrayで保持する
		$(".bookNumInput").each(function(index) {
			var bookNum = $(this).val();
			var bookSubNum = $(".bookSubNumInput").eq(index).val();

			inpBookArray.push({
				bookNum: bookNum,
				bookSubNum: bookSubNum
			});

		});
		// 入力値重複チェックを行う関数を呼び出す
		checkDupliValues(inpBookArray);
	});

	//貸出ボタン押下時に呼び出す関数
	$("#lendButton").on("click", function() {

		var rentalBookArray = []
		//貸出可能冊数分の書籍番号、書籍情報をrentalBookArrayで保持する
		for (i = 0; i < kaisu; i++) {

			var rentalBookObj = {
				bookNum: $("#bookNum_" + i).val(),
				bookSubNum: $("#bookSubNum_" + i).val()
			}
			rentalBookArray.push(rentalBookObj)
		}

		//ajaxで書籍情報、図書カード番号、エラーメッセージを送信する
		$.ajax({
			url: "http://localhost:8080/topLendScr",
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify({
				rentalBookInfo: rentalBookArray,
				cardNumber: $("#tosyoCardId").val(),
				errorMsg: $("#errorMsg").val(),
			})
		}).done(function(data) {
			//エラーメッセージの有無で画面に表示する項目を判定する関数を呼び出す
			judgeError(data);

		}).fail(function(error) {
			console.log("failが実行された");
		})
	})
})

//書籍番号と書籍枝番号の入力値重複チェックを行う関数
function checkDupliValues(inpBookArray) {
	// 一意の値を持つSetオブジェクトを作成
	var uniqueValues = new Set();
	var errorMsgHtml = [];

	// 入力数分の値重複チェックする
	for (var i = 0; i < inpBookArray.length; i++) {
		var bookNum = inpBookArray[i].bookNum;
		var bookSubNum = inpBookArray[i].bookSubNum;

		if (bookNum && bookSubNum) {
			var key = bookNum + "-" + bookSubNum;

			// 入力値が重複している場合
			if (uniqueValues.has(key)) {
				//入力欄の背景色を赤色にする
				$("#bookNum_" + i).css("background-color", "red");
				$("#bookSubNum_" + i).css("background-color", "red");
				errorMsgHtml.push("<li>" + (i + 1) + '冊目の書籍番号と書籍枝番号が重複しています' + "</li>");

				// 重複していない、または解消された場合
			} else {
				// 一意の値として追加
				uniqueValues.add(key);
				//入力欄の背景色をリセットする
				$("#bookNum_" + i).css("background-color", "");
				$("#bookSubNum_" + i).css("background-color", "");
			}
		//書籍番号のみ入力されている場合
		} else if (bookNum && !bookSubNum) {
			errorMsgHtml.push("<li>" + (i + 1) + '冊目の書籍枝番号が入力されていません' + "</li>");
		}
		//書籍枝番号のみ入力されている場合
		else if (!bookNum && bookSubNum) {
			errorMsgHtml.push("<li>" + (i + 1) + '冊目の書籍番号が入力されていません' + "</li>");
		}
	}
	
	//エラーメッセージがある場合
	if (errorMsgHtml.length != 0) {
		//ボタンを活性、非活性にする関数を呼び出す
		switchButton(true);
		//エラーメッセージをhtmlに設定する
		$("#errorMsgContainer").html(errorMsgHtml);
	}else{
		//ボタンを活性、非活性にする関数を呼び出す
		switchButton(false);
		$("#errorMsgContainer").empty();
	}
}

//エラーメッセージの有無で画面に表示する項目を判定する関数
//■引数
// ・data:サーバーから返却された情報を格納
function judgeError(data) {
	var errorMsg = data.errorMsg;

	//エラーメッセージが無い場合
	if (errorMsg.length == 0) {
		var inpBookInfo = data.inpBookInfo;

		var bookNum = []
		var bookSubNum = []

		
		var tableHtml = "<table border='2'><caption><strong>'該当する書籍'</strong></caption><tr><th>書籍名</th><th>書籍番号</th><th>書籍枝番号</th></tr>";
		//入力された書籍番号、書籍枝番号に該当する書籍表を生成
		for (var i = 0; i < inpBookInfo.length; i++) {

			bookNum.push(inpBookInfo[i].bookNum)
			bookSubNum.push(inpBookInfo[i].bookBranchNum)

			var bookInfo = inpBookInfo[i];
			tableHtml += "<tr><td>" + bookInfo.bookName + "</td><td>" + bookInfo.bookNum + "</td><td>" + bookInfo.bookBranchNum + "</td></tr>";
		}
		tableHtml += "</table>";

		//画面に表示する項目を設定
		var buttonHtml = "<input type = 'button' value = 決定 onclick=\"changeAction('/lendCompScr')\">";
		$("#bookNum").val(bookNum);
		$("#bookSubNum").val(bookSubNum);
		$("#tableContainer").html(tableHtml);
		$("#buttonContainer").html(buttonHtml);
		//エラーメッセージをクリアする
		$("#errorMsgContainer").empty();

	//エラーメッセージがある場合
	} else {
		//エラーメッセージを設定
		errorMsgHtml = "";
		for (var i = 0; i < errorMsg.length; i++) {
			errorMsgHtml += "<li>" + errorMsg[i] + "</li>";
		}

		//画面に表示する項目を設定
		$("#errorMsgContainer").html(errorMsgHtml);
		//書籍表、決定ボタンをクリアする
		$("#tableContainer").empty();
		$("#buttonContainer").empty();
	}
}

//貸出ボタンを活性、非活性にする関数
//■引数
// ・boolean:正常時⇒false、エラー時⇒true
function switchButton(boolean) {
	document.getElementById("lendButton").disabled = boolean;
}

//formタグのactionを変更し、サブミットを関数
//■引数
// ・url:遷移先の画面情報を格納
function changeAction(url) {
	document.topAction.action = url;
	document.topAction.submit();
}

//戻るボタン押下時に前ページに遷移する関数
function goBack() {
	window.history.back();
}