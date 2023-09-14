package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class passReturnCtrl {

	//　画面の入力情報を取得、画面に情報を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・map:画面へ返却したい項目を格納
	@RequestMapping(value = "/topReturnScr", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> passForReturnTop(@RequestBody returnPrcForm form) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tosyoCardId = form.getTosyoCardId();
		List lendStatus = (List) form.getRentalBookInfo();
		map.put("tosyoCardId", tosyoCardId);
		map.put("lendStatus", lendStatus);
		return map;
	}
}