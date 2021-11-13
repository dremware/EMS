package com.nyh.wx.controller;

import com.nyh.pojo.Message;
import com.nyh.utils.JSONUtil;
import com.nyh.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/wx/qrcode")
public class QRCodeController {

    @RequestMapping("/createQRCode.do")
    public String createQRCode(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求参数
        String type = request.getParameter("type");
        String code = null;
        String userPhone = null;
        String QRCodeContent = null;
        if ("express".equals(type)) {
            code = request.getParameter("code");
            QRCodeContent = "express_".concat(code);
        } else {
            // 这里要先获取微信用户，再获取其电话号码
            userPhone = UserUtil.getWxUser(request.getSession()).getUserphone();
            QRCodeContent = "userPhone_".concat(userPhone);
        }
        HttpSession session = request.getSession();
        session.setAttribute("qrcode", QRCodeContent);
        // 返回ModelAndView视图，而不是返回字符串
        return "/personQRcode.html";
    }

    @RequestMapping("/qrcode.do")
    public String getQRCode(HttpServletRequest request, HttpServletResponse response) {
        String qrcode = (String) request.getSession().getAttribute("qrcode");
        Message msg = new Message();
        if (qrcode == null) {
            msg.setStatus(-1);
            msg.setResult("取件码获取出错，请用户重新操作");
        } else {
            msg.setStatus(0);
            msg.setResult(qrcode);
        }
        return JSONUtil.toJSON(msg);
    }
}
