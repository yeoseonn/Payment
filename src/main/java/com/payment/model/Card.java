package com.payment.model;

import com.payment.common.utils.DataProcessingUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private String cardNum;
    private String period;
    private String cvc;

    public String getCardNum() {
        return DataProcessingUtils.maskingCardNum(this.cardNum);
    }
}
