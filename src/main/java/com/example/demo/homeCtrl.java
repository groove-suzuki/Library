package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homeCtrl{

	@RequestMapping("/Top") 
	public String searchScreen() {
		return "homeScr";
	}
	
	//画面の入力情報を取得、処理結果を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・mv:画面へ返却したい項目を格納
	@RequestMapping("/homeScr")
	public ModelAndView inputTosyoCardID(@ModelAttribute homeForm form) {
		
		//ModelAndViewをインスタンス化
		ModelAndView mv = new ModelAndView();
		
		List lendStatus = new ArrayList();
		List errorMsg = new ArrayList();
		List delayMsg = new ArrayList();
		
		HashMap userInfo = new HashMap();
		userInfo.put("tosyoCardId",form.getTosyoCardId());

		//homeMdlクラスをインスタンス化
		homeMdl model = new homeMdl();
		
		//図書カード番号の入力チェックメソッドを呼び出し
		String scrTrans= model.idInputCheck(userInfo,errorMsg);
		
		//図書カード番号存在チェックメソッドを呼び出し
		scrTrans = model.idExistCheck(userInfo,scrTrans,errorMsg);
		
		//貸出状況検索、延滞チェックメソッドを呼び出し
		model.selectLendStatus(userInfo,lendStatus,delayMsg,scrTrans);
		
		//画面へ返却する情報を設定
		mv.addObject("tosyoCardId",userInfo.get("tosyoCardId"));
		mv.addObject("userName",userInfo.get("userName"));
		mv.addObject("lendableBooks",userInfo.get("lendableBooks"));
		mv.addObject("lendStatus",lendStatus);
		mv.addObject("errorMsg",errorMsg);
		mv.addObject("delayMsg",delayMsg);
		//遷移先画面を設定
		mv.setViewName(scrTrans);
		return mv;
	}
}