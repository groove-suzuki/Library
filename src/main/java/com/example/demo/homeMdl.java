package com.example.demo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

public class homeMdl{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//共通クラスをインスタンス化
	commonClass common = new commonClass();

	//図書カード番号の入力チェック
	//■引数
	// ・userInfo:利用者情報を格納
	// ・errorMsg:エラーメッセージを追加する
	//■戻り値
	// ・scrTrans:遷移先の画面情報を格納
	public String idInputCheck(HashMap userInfo, List errorMsg) {
		String scrTrans = "";
		String tosyoCardId = (String) userInfo.get("tosyoCardId");
		String regex = "^[0-9]{5}";
		
		//図書カード番号の入力チェックへ
		boolean inputCheck = common.inputCheck(tosyoCardId, regex);

		//エラー時のメッセージと遷移先画面情報を保持
		if (!inputCheck) {
			errorMsg.add("図書カード番号は半角数字5桁で入力してください。");
			scrTrans = "homeScr";
		}
		return scrTrans;
	}

	//図書カード番号の存在チェック
	//■引数
	// ・userInfo:利用者情報を格納
	// ・scrTrans:遷移先の画面情報を格納
	// ・errorMsg:エラーメッセージを追加する
	//■戻り値
	// ・scrTrans:遷移先の画面情報を格納
	public String idExistCheck(HashMap userInfo, String scrTrans, List errorMsg) {

		if (scrTrans != "homeScr") {
			String tosyoCardId = (String) userInfo.get("tosyoCardId");

			//DBに接続する
			envFile e = new envFile();
			jdbcTemplate = e.jdbcTemplate();

			//図書カード番号の存在チェックと利用者名取得の検索
			List getUserName = jdbcTemplate
					.queryForList("SELECT userName FROM userMst WHERE cardId = ?", tosyoCardId);

			//エラー時のメッセージと遷移先の画面を設定
			if (getUserName.size() == 0) {
				String existErrorMsg = "入力された図書カード番号は登録されていません";
				errorMsg.add(existErrorMsg);
				scrTrans = "homeScr";
			
			} else {
				//利用者名をLinkedCaseInsensitiveMapに代入し、userInfoで保持する。
				LinkedCaseInsensitiveMap holdName = (LinkedCaseInsensitiveMap) getUserName.get(0);
				userInfo.put("userName", holdName.get("userName"));

				//正常時の遷移先画面情報を保持
				scrTrans = "selectScr";
			}
		}
		return scrTrans;
	}

	//書籍番号、書籍枝番号に該当する書籍の存在チェック
	//■引数
	// ・userInfo:利用者情報を格納
	// ・lendStatus:貸出している書籍の情報を格納
	// ・delayMsg:延滞時のメッセージを格納
	// ・scrTrans:遷移先の画面情報を格納
	public void selectLendStatus(HashMap userInfo, List lendStatus, List delayMsg, String scrTrans) {

		String tosyoCardId = (String) userInfo.get("tosyoCardId");

		if (scrTrans == "selectScr") {
			//貸出状況検索メソッドを呼び出し
			common.selectLendStatus(tosyoCardId, lendStatus);

			//貸出書籍延滞チェックメソッドを呼び出し
			checkDelay(userInfo, lendStatus, delayMsg);
		}
	}

	//貸出書籍延滞チェック
	//■引数
	// ・userInfo:利用者情報を格納
	// ・lendStatus:貸出している書籍の情報を格納
	// ・delayMsg:延滞時のメッセージを格納
	public void checkDelay(HashMap userInfo, List lendStatus, List delayMsg) {

		int count = 0;
		boolean judgeDelay = true;
		//貸出している書籍数分の返却日延滞チェック
		for (count = 0; count < lendStatus.size(); count++) {

			// 取得した検索結果のうち1レコード分の情報を代入
			Object selectResultTemp = lendStatus.get(count);

			// 1レコード分の情報から日付項目取得+文字列に直す
			Object foundingDateTemp = ((LinkedCaseInsensitiveMap) selectResultTemp).get("returnDate");
			String foundingDate = foundingDateTemp.toString();

			// YYYY/MM/DDの形式を、"/"でsplitして年・月・日に分割する
			String[] splitDate = foundingDate.split("/");
			int seireki = Integer.parseInt(splitDate[0]);
			int tuki = Integer.parseInt(splitDate[1]);
			int hi = Integer.parseInt(splitDate[2]);
			// 分割した日付をreturnDateにセット
			LocalDate returnDate = LocalDate.of(seireki, tuki, hi);
			//現在日を取得
			LocalDate currentDate = LocalDate.now();

			//返却日が現在日時よりも前か判定
			if (returnDate.isBefore(currentDate)) {
				if(judgeDelay) {
				delayMsg.add("延滞されたので、返却のみ利用できます");
				judgeDelay = false;;
				}
			}
		}
		//貸出可能書籍数を取得、userInfoで保持する
		count = 5 - count;
		userInfo.put("lendableBooks", String.valueOf(count));
	}
}