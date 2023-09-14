package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class returnCompMdl {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//貸出書籍情報を削除するメソッド
	// ■引数
	//  ・bookNums:書籍番号を格納
    //  ・bookSubNums:書籍枝番号を格納
    //  ・tosyoCardId:図書カード番号を格納
	public void deleteLendInfo(String[] bookNums,String[] bookSubNums,String tosyoCardId) {
		//DBに接続する
		envFile e = new envFile();
		jdbcTemplate = e.jdbcTemplate();
		
		//選択数分削除処理を繰り返す
		for(int i = 0;i<bookNums.length;i++) {
		jdbcTemplate.update("DELETE FROM lendtable WHERE cardId = ? AND bookNum = ? AND bookBranchNum = ?",tosyoCardId,bookNums[i],bookSubNums[i]);
		}
	}
}