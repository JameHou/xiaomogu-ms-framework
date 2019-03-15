package com.github.xiaomogu.framework.web.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.web.common.AlipayConfig;
import com.github.xiaomogu.framework.web.domain.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class PayServiceController {
	private Map<String,PaymentInfo> tokenMap = new HashMap<>();

	// 创建支付令牌
	@RequestMapping(value = "/createPayToken")
	public ResponseResult<String> createToken() {
		// 1.创建支付请求信息
		/*Integer savePaymentType = paymentInfoDao.savePaymentType(paymentInfo);
		if (savePaymentType <= 0) {
			return ResponseResult.fail("创建支付订单支付失败");
		}*/
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setOrderId(System.currentTimeMillis()+"");
		paymentInfo.setPrice(5000L);
		paymentInfo.setState(1);
		paymentInfo.setUserId("123456789");


		// 2.生成对应的token
		String payToken = UUID.randomUUID()+"";
		// 3.存放在Redis中，key为 token value 支付id
		//baseRedisService.setString(payToken, paymentInfo.getId() + "", Constants.PAY_TOKEN_MEMBER_TIME);
		tokenMap.put(payToken,paymentInfo);
		// 4.返回token
		return ResponseResult.success(payToken);
	}

	// 使用支付令牌查找支付信息
	@RequestMapping(value = "/findPayToken", produces = "text/html;charset=utf-8")
	public void findPayToken(@RequestParam("payToken")  String payToken, HttpServletResponse response) {
		// 1.参数验证
		if (StringUtils.isEmpty(payToken)) {
			return ;
	}
		// 2.判断token有效期
		// 3.使用token 查找redis 找到对应支付id
		/*String payId = (String) baseRedisService.getString(payToken);
		if (StringUtils.isEmpty(payId)) {
			return ResponseResult.fail("支付请求已经超时!");
		}*/
		// 4.使用支付id，进行下单
	//	Long payIDl = Long.parseLong(payId);

		// 5.使用支付id查询支付信息
		PaymentInfo paymentInfo = tokenMap.get(payToken);
		/*if (paymentInfo == null) {
			return setResultError("未找到支付信息");
		}*/
		// 6.对接支付代码 返回提交支付from表单元素给客户端
		// 获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
				AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
				AlipayConfig.sign_type);

		// 设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = paymentInfo.getOrderId();
		// 付款金额，必填 企业金额
		String total_amount = paymentInfo.getPrice() + "";
		// 订单名称，必填
		String subject = "惠农网充值会员";
		// 商品描述，可空
		// String body = new
		// String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");

		alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
				+ "\"," + "\"subject\":\"" + subject + "\","
				// + "\"body\":\""+ body +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		// 若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		// alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no
		// +"\","
		// + "\"total_amount\":\""+ total_amount +"\","
		// + "\"subject\":\""+ subject +"\","
		// + "\"body\":\""+ body +"\","
		// + "\"timeout_express\":\"10m\","
		// + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		// 请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

		// 请求
		try {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter printWriter = response.getWriter();
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			printWriter.println(result);
		} catch (Exception e) {
			log.error("支付异常");
		}

	}

	// 创建支付令牌
	@RequestMapping(value = "/notify_url")
	public ResponseResult<String> notify_url(HttpServletRequest request) {
		log.info("支付宝回调成功.......................notify_url,请求参数：{}",request);
		return ResponseResult.success("notify_url");
	}


	// 创建支付令牌
	@RequestMapping(value = "/return_url")
	public ResponseResult<String> return_url(HttpServletRequest request) {
		log.info("支付宝回调成功.......................return_url，请求参数：{}",request);
		return ResponseResult.success("return_url");
	}




}
