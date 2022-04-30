package com.wyj.controller;

import com.wyj.biz.PaymentBiz;
import com.wyj.entity.Payment;
import com.wyj.entity.Settlement;
import com.wyj.model.security.Role;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/restful-api/settlements")
public class SettlementController {
    private final PaymentBiz paymentBiz;

    public SettlementController(PaymentBiz paymentBiz) {
        this.paymentBiz = paymentBiz;
    }

    @PostMapping
    @RolesAllowed(Role.USER)
    public Payment executeSettlement(@RequestBody Settlement settlement) {
        return paymentBiz.executeBySettlement(settlement);
    }
}
