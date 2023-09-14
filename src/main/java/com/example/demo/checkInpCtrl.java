package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class checkInpCtrl {
	
	//　画面の入力情報を取得、画面に情報を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・map:画面へ返却したい項目を格納
	@RequestMapping(value="/topLendScr", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> checkinputNum(@RequestBody lendPrcForm form) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//modelクラスのインスタンス化
		checkInpMdl mdl = new checkInpMdl();
		
		String tosyoCardId = form.getCardNumber();
		List rentalBookInfo = (List)form.getRentalBookInfo();
		List inpBookInfo = new ArrayList();
		List errorMsg = new ArrayList();
	
		//書籍番号と書籍枝番号の入力チェックメソッドを呼び出し
		mdl.checkParam(rentalBookInfo,errorMsg);
		
		//書籍番号と書籍枝番号の存在チェック、書籍名取得メソッドを呼び出し
		inpBookInfo = mdl.selectBook(rentalBookInfo,errorMsg);
		
		//画面へ返却する項目を設定
		map.put("tosyoCardId",tosyoCardId);
		map.put("inpBookInfo",inpBookInfo);
		map.put("errorMsg", errorMsg);
		return map;
	}
}