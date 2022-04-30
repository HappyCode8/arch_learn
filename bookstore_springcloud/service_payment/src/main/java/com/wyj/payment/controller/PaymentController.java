package com.wyj.payment.controller;

import com.wyj.entity.Account;
import com.wyj.model.Role;
import com.wyj.model.web.Result;
import com.wyj.util.ResultUtil;
import com.wyj.payment.biz.PaymentBiz;
import com.wyj.payment.entity.Payment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/restful-api/pay")
public class PaymentController {
    private final PaymentBiz paymentBiz;

    public PaymentController(PaymentBiz paymentBiz) {
        this.paymentBiz = paymentBiz;
    }


    /**
     * 修改支付单据的状态
     */
    @PatchMapping("/{payId}")
    @RolesAllowed(Role.USER)
    public Result updatePaymentState(@PathVariable("payId") String payId, @RequestParam("state") Payment.State state) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return updatePaymentStateAlias(payId, account.getId(), state);
    }

    /**
     * 修改支付单状态的GET方法别名
     * 考虑到该动作要由二维码扫描来触发，只能进行GET请求，所以增加一个别名以便通过二维码调用
     * 这个方法原本应该作为银行支付接口的回调，不控制调用权限（谁付款都行），但都认为是购买用户付的款
     */
    @GetMapping("/modify/{payId}")
    public Result updatePaymentStateAlias(@PathVariable("payId") String payId, @RequestParam("accountId") Long accountId, @RequestParam("state") Payment.State state) {
        if (state == Payment.State.PAYED) {
            paymentBiz.accomplishPayment(accountId, payId);
            return ResultUtil.ok();
        } else {
            paymentBiz.cancelPayment(payId);
            return ResultUtil.ok();
        }
    }
}
