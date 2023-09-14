package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class passLendCtrl {

	//　画面の入力情報を取得、貸出画面へ情報を返却
	//■引数
	//  ・form:画面とサーバー間の情報のやりとりを行う
	//■戻り値
	//  ・mv:画面へ返却したい項目を格納
	@RequestMapping("/passLend")
	public ModelAndView passForLendTop(@ModelAttribute lendPrcForm form) {
		
		//ModelAndViewをインスタンス化
		ModelAndView mv = new ModelAndView();
		
		String tosyoCardId = form.getTosyoCardId();
		String lendableBooks = form.getLendableBooks();
		
		//画面へ返却する情報を設定
		mv.addObject("tosyoCardId", tosyoCardId);
		mv.addObject("lendableBooks", lendableBooks);
		//遷移先画面を設定
		mv.setViewName("lendTopScr");
		return mv;
	}
}