package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class lendCompMdl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//貸出状況テーブルに書籍情報を登録するメソッド
	//■引数
	// ・bookNums:書籍番号を格納
	// ・bookSubNums:書籍枝番号を格納
	// ・tosyoCardId:図書カード番号を格納
	public void insertBookInfo(String[] bookNums, String[] bookSubNums, String tosyoCardId) {

		//DBに接続する
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();

		//返却日を取得するメソッドを呼び出す
		String returnDate = getReturnDate();

		for (int count = 0; count < bookNums.length; count++) {
			//貸出状況テーブルに貸出情報を登録する
			jdbcTemplate.update("INSERT INTO lendtable(cardId,bookNum,bookBranchNum,returnDate)VALUES(?,?,?,?)",
					tosyoCardId, bookNums[count], bookSubNums[count], returnDate);
		}
	}

	//返却日を取得するメソッド
	//■戻り値
	// ・returnDate:返却日を格納
	public String getReturnDate() {

		//現在日時を取得
		LocalDate currentDate = LocalDate.now();
		//現在日時から7日後を取得
		LocalDate sevenDaysLater = currentDate.plusDays(7);
		// 区切り文字を指定したフォーマッターを作成
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		// フォーマッターを使用して指定した形式に変換
		String returnDate = sevenDaysLater.format(formatter);

		return returnDate;
	}
}