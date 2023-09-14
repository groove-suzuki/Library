package com.example.demo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class commonClass {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//入力値チェックメソッド
	// ■引数
	//  ・param:入力値を格納
    //  ・regex:正規表現を格納
	// ■戻り値
	//  ・m.matches():正常時→true,エラー時→false
	public boolean inputCheck(String param, String regex) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(param);
		return m.matches();
	}
	
	//貸出状況検索メソッド
	// ■引数
	//  ・tosyoCardId:図書カード番号を格納
    //  ・lendStatus:貸出状況の情報を格納	
	public void selectLendStatus(String tosyoCardId, List lendStatus) {

		//DBに接続する
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		//利用者の貸出状況を取得する
		List lendInfo = jdbcTemplate.queryForList(
				"SELECT bookName,a.bookNum,a.bookBranchNum,returnDate FROM lendtable a INNER JOIN bookmst b ON a.bookNum = b.bookNum and a.bookBranchNum = b.bookBranchNum WHERE cardId = ?",
				tosyoCardId);

		//貸出書籍情報を検索件数分LinkedCaseInsensitiveMapに代入し、lendStatusで保持する
		for (int count = 0; count < lendInfo.size(); count++) {
			LinkedCaseInsensitiveMap infoCount = (LinkedCaseInsensitiveMap) lendInfo.get(count);
			lendStatus.add(infoCount);
		}
	}
}