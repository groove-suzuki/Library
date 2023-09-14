package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class checkInpMdl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//書籍番号と書籍枝番号を取得するメソッド
	// ■引数
	//   ・rentalBookInfo:書籍番号、書籍枝番号を格納
	//   ・bookNums:書籍番号を格納
	//   ・bookSubNums:書籍枝番号を格納
	public void getBookInfo(List rentalBookInfo, List bookNums, List bookSubNums) {
		for (int i = 0; i < rentalBookInfo.size(); i++) {
			HashMap bookNumInfo = (HashMap) rentalBookInfo.get(i);
			bookNums.add(bookNumInfo.get("bookNum"));
			bookSubNums.add(bookNumInfo.get("bookSubNum"));
		}
	}

	//入力された書籍番号、書籍枝番号の入力チェックメソッド
	// ■引数
	//  ・rentalBookInfo:書籍番号、書籍枝番号を格納
	//  ・errorMsg:エラーメッセージを格納
	public void checkParam(List rentalBookInfo, List errorMsg) {

		//共通クラスをインスタンス化
		commonClass common = new commonClass();

		List bookNums = new ArrayList();
		List bookSubNums = new ArrayList();
		//書籍番号と書籍枝番号を取得するメソッドを呼び出し
		getBookInfo(rentalBookInfo, bookNums, bookSubNums);
		boolean inputCheck = true;

		//入力数分の書籍番号入力チェックを行う
		for (int count = 0; count < bookNums.size(); count++) {
			String param = (String) bookNums.get(count);

			if (param != "") {
				String regex = "^[0-9a-zA-Z]{5}";
				String bookNum = (String) bookNums.get(count);
				//共通クラスの入力チェックメソッドを呼び出す
				inputCheck = common.inputCheck(bookNum, regex);

				if (!inputCheck) {
					errorMsg.add((count + 1) + "冊目の書籍番号が半角英数字5桁で入力されていません");

				}
				regex = "^[0-9]{2}";
				//入力数分の書籍枝番号入力チェックを行う

				String bookSubNum = (String) bookSubNums.get(count);

				//共通クラスの入力チェックメソッドを呼び出す
				inputCheck = common.inputCheck(bookSubNum, regex);
				if (!inputCheck) {
					errorMsg.add((count + 1) + "冊目の書籍枝番号が半角英数字2桁で入力されていません");
				}
			}
		}
	}

	//書籍の存在、貸出重複チェックメソッド
	// ■引数
	//  ・rentalBookInfo:書籍番号、書籍枝番号を格納
	//   ・errorMsg:エラーメッセージを格納
	public List selectBook(List rentalBookInfo, List errorMsg) {

		List inpBookInfo = new ArrayList();

		//入力チェック正常時に存在、重複チェックを行う
		if (errorMsg.size() == 0) {

			//DBに接続する
			envFile e = new envFile();
			jdbcTemplate = e.jdbcTemplate();

			List bookNums = new ArrayList();
			List bookSubNums = new ArrayList();
			//書籍番号と書籍枝番号を取得するメソッドを呼び出し
			getBookInfo(rentalBookInfo, bookNums, bookSubNums);

			//入力数分の書籍の存在、貸出重複チェックを行う
			for (int count = 0; count < bookNums.size(); count++) {

				//入力値がある場合
				if (bookNums.get(count) != "") {
					List bookInfo = jdbcTemplate.queryForList(
							"SELECT b.bookNum,b.bookBranchNum,bookName,l.cardId FROM bookmst b LEFT JOIN lendtable l ON b.bookNum = l.bookNum AND b.bookBranchNum = l.bookBranchNum WHERE b.bookNum = ? AND b.bookBranchNum = ?",
							bookNums.get(count), bookSubNums.get(count));

					//検索結果が存在しない場合
					if (bookInfo.size() == 0) {
						errorMsg.add((count + 1) + "冊目で入力された書籍番号、書籍枝番号に該当する書籍はありません");

						//検索結果が存在する場合
					} else {
						LinkedCaseInsensitiveMap getBookInfo = (LinkedCaseInsensitiveMap) bookInfo.get(0);

						//書籍が既に貸出されている場合
						if (getBookInfo.get("cardId") != null) {
							errorMsg.add((count + 1) + "冊目で入力された書籍番号、書籍枝番号に該当する書籍は既に貸出されています");
						} else {
							inpBookInfo.add(getBookInfo);
						}
					}
				}
			}
		}
		return inpBookInfo;
	}
}