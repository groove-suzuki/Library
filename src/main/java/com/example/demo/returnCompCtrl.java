package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class returnCompCtrl {
	
	//　画面の入力情報を取得、貸出画面へ情報を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・mv:画面へ返却したい項目を格納
	@RequestMapping("/returnCompScr")
	public ModelAndView deleteBookInfo(@ModelAttribute returnPrcForm form) {
		//modelAndViewをインスタンス化
		ModelAndView mv = new ModelAndView();
		//commonzクラスをインスタンス化
		commonClass common = new commonClass();

		String bookNum = form.getBookNums();
		String bookSubNum = form.getBookSubNums();
		String tosyoCardId = form.getTosyoCardId();
		List lendStatus = new ArrayList();
		String bookNums[] = bookNum.split(",");
		String bookSubNums[] = bookSubNum.split(",");
		//modelクラスをインスタンス化
		returnCompMdl mdl = new returnCompMdl();
		//貸出書籍情報を削除するメソッドを呼び出す
		mdl.deleteLendInfo(bookNums, bookSubNums, tosyoCardId);
		//共通クラスの貸出状況検索メソッドを呼び出す
		common.selectLendStatus(tosyoCardId,lendStatus);
		//画面へ返却する情報を設定
		mv.addObject("lendStatus",lendStatus);
		//遷移先画面を設定
		mv.setViewName("returnCompScr");
		return mv;
	}
}