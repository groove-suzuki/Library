//貸出状況によってボタンの活性、非活性を行う関数
function actButton() {
	var canBorrowBook = document.getElementById("lendableBooks").value;
	var delayMsg = document.getElementById("delayMsg").value;

	//延滞メッセージが無い場合
	if (delayMsg == "[]") {
		if (canBorrowBook == 0) {
			document.getElementById("returnButton").disabled = false;
		} else if (canBorrowBook == 5) {
			document.getElementById("lendButton").disabled = false;
		} else {
			document.getElementById("returnButton").disabled = false;
			document.getElementById("lendButton").disabled = false;
		}
	} else {
		document.getElementById("returnButton").disabled = false;
	}
}

//ボタンで選択された処理の画面へ遷移する関数
//■引数
// ・url:遷移先の画面情報を格納
function changeAction(url) {
	document.selectScreen.action = url;
	document.selectScreen.submit();
}

//戻るボタン押下時に前ページに遷移する関数
function goBack() {
	window.history.back();
}

$(function() {
	//返却ボタン押下時に呼び出す関数
	$("#returnButton").on("click", function() {
		//貸出書籍数を取得
		var lendBookCount = 5 - $("#lendableBooks").val()
		var rentalBookArray = []
		//貸出書籍数分の情報を取得
		for (var count = 0; count < lendBookCount; count++) {
			var rentalBookObj = {
				bookName: $("#bookName-" + count).text(),
				bookNum: $("#bookNum-" + count).text(),
				bookBranchNum: $("#bookBranchNum-" + count).text(),
				returnDate: $("#returnDate-" + count).text()
			}
			rentalBookArray.push(rentalBookObj)
		}
		//貸出書籍情報をajaxで送信する
		$.ajax({
			url: "http://localhost:8080/topReturnScr",
			type: "POST",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify({
				rentalBookInfo: rentalBookArray,
				tosyoCardId: $("#tosyoCardId").val(),
			})
		}).done(function(data) {
			showReturnTable(data)
		}).fail(function() {
			console.log("failが実行された")
		})
	})
	
	//チェックボックスの状態変更時に呼び出す関数
	$(document).on("change", ".selectCheckBox", function() {
		// 選択されたチェックボックスの配列を取得
		var selectedCheckBoxes = $(".selectCheckBox:checked");
		//チェックボックス選択時に書籍情報を取得する関数を呼び出す
		getBookInfo(selectedCheckBoxes)

	});
})

//書籍返却表を生成する関数
//■引数
// ・data:サーバーから返却された情報を格納
function showReturnTable(data) {
	var rentalBookInfo = data.lendStatus
	var tableHtml = "<table border='2'><caption><strong>返却したい書籍を選んでください</strong></caption><tr><th>書籍名</th><th>書籍番号</th><th>書籍枝番号</th><th>返却日</th><th>選択欄</th></tr>";
	for (var i = 0; i < rentalBookInfo.length; i++) {
			var bookInfo = rentalBookInfo[i]
			tableHtml += "<tr><td>" + bookInfo.bookName + "</td><td>" + bookInfo.bookNum + "</td><td>" + bookInfo.bookBranchNum + "</td><td>" + bookInfo.returnDate + "</td><td>返却する:<input type='checkbox' class='selectCheckBox' id='checkNum-" + i + "'></td></tr>";
	}
	tableHtml += "</table>"
	//画面に表示するボタンのタグを生成
	var nextButtonHtml = "<input type = 'button' value = 決定 disabled id = 'compButton' onclick=\"changeAction('/returnCompScr')\">";
	var selectAllButtonHtml = "<input type ='button' value = すべて選択 id='selectAllButton' onclick='selectAll()'>"
	var deselectAllButtonHtml = "<input type ='button' value = 選択解除 id='deselectAllButton' onclick = 'deselectAll()'>"
	//生成したタグを画面に設定する
	$("#tableCtr").html(tableHtml);
	$("#selectAllButtonCtr").html(selectAllButtonHtml);
	$("#deselectAllButtonCtr").html(deselectAllButtonHtml);
	$("#nextButtonCtr").html(nextButtonHtml);
}

//全て選択ボタン押下時に呼び出す関数
function selectAll() {
	$(".selectCheckBox").prop("checked", true);
	// 選択されたチェックボックスの配列を取得
	var selectedCheckBoxes = $(".selectCheckBox:checked");
	//チェックボックス選択時に書籍情報を取得する関数を呼び出し
	getBookInfo(selectedCheckBoxes)
	//ボタンの活性、非活性を切り替える関数を呼び出し
	switchButton(false);
}
//選択解除ボタン押下時に呼び出す関数
function deselectAll() {
	$(".selectCheckBox").prop("checked", false);
	//ボタンの活性、非活性を切り替える関数を呼び出し
	switchButton(true);
}

//チェックボックス選択時に書籍情報を取得する関数
function getBookInfo(selectedCheckBoxes) {
	
	if (selectedCheckBoxes.length > 0) {
		var bookNums = []
		var bookSubNums = []

		selectedCheckBoxes.each(function() {
			// チェックボックスのIDを取得
			var checkBoxId = $(this).attr("id");
			// インデックスを取得
			var index = checkBoxId.split("-")[1];

			var bookNum = $("#bookNum-" + index).text();
			var bookBranchNum = $("#bookBranchNum-" + index).text();

			bookNums.push(bookNum)
			bookSubNums.push(bookBranchNum)

			console.log("選択された書籍の情報:");
			console.log("書籍番号: " + bookNums);
			console.log("書籍枝番号: " + bookSubNums);
		});
		$("#bookNums").val(bookNums)
		$("#bookSubNums").val(bookSubNums)
		switchButton(false);
	} else {
		switchButton(true);
	}
}
//ボタンの活性、非活性を切り替える関数
//■引数
// ・boolean:正常時⇒false、エラー時⇒true
function switchButton(boolean) {
	document.getElementById("compButton").disabled = boolean;
}




