package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class lendCompCtrl {
	
	//画面の入力情報を取得、処理結果を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・mv:画面へ返却したい項目を格納
	@RequestMapping("/lendCompScr")
	public ModelAndView insertBookInfo(@ModelAttribute lendPrcForm form) {

		String bookNum = form.getBookNum();
		String bookSubNum = form.getBookSubNum();
		String tosyoCardId = form.getTosyoCardId();

		List lendStatus = new ArrayList();

		String[] bookNums = bookNum.split(",");
		String[] bookSubNums = bookSubNum.split(",");
		
		//lendCompMdlクラスをインスタンス化
		lendCompMdl mdl = new lendCompMdl();
		//共通クラスをインスタンス化
		commonClass common = new commonClass();
		//ModelAndViewをインスタンス化
		ModelAndView mv = new ModelAndView();

		//貸出状況テーブルに書籍情報を登録するメソッドを呼び出し
		mdl.insertBookInfo(bookNums, bookSubNums, tosyoCardId);
		
		//共通クラスの貸出状況検索を行うメソッドを呼び出し
		common.selectLendStatus(tosyoCardId, lendStatus);
		
		//画面へ返却する情報を設定
		mv.addObject("lendStatus", lendStatus);
		//遷移先画面を設定
		mv.setViewName("lendCompScr");
		return mv;
	}
}