package org.knowm.xchange.omf;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;

public class OMFAdaptersTest extends TestCase {

  @Test
  public void testAdaptSearchResult() {
    String searchRespone =
        "+====~THGUID~String~N~VERIFICATIONCODE~Decimal~N~ENTITYID~String~N~PRODUCTID~String~N~INSTRUMENTID~String~N~PRODUCTPOSITIONTYPE~String~N~TRADENUMBER~Decimal~N~TRANSACTIONDATETIME~DateTime~N~TRADETYPE~String~N~TRADESOURCETYPE~String~N~TRADESOURCECUSTOMER1~String~N~TRADESOURCECUSTOMER2~String~N~REVERSALDATETIME~DateTime~N~LASTMAINTENANCEDATETIME~DateTime~N~LASTMAINTENANCEUSERID~String~N~EXTERNALREFERENCEID~String~N~BLOCKTRADEIND~String~N~TRADEDATE~DateTime~N~TRADEREF1~String~N~TRADEREF2~String~N~LEGID~Decimal~N~TRADEDIRECTION~String~N~CUSTOMERDIRECTION~String~N~CUSTOMERMNEMONIC~String~N~CUSTOMERID~String~N~SHORTNAME~String~N~EXTERNALID~String~N~VALUEDATE~DateTime~N~MATURITYDATE~DateTime~N~SYMBOL~String~N~WINDOWSTARTDATE~DateTime~N~TPCCYID1~String~N~TRADECCY1AMOUNT~Decimal~N~CUSTOMERTRADECCY1AMOUNT~Decimal~N~TPCCYID2~String~N~TRADECCY2AMOUNT~Decimal~N~CUSTOMERTRADECCY2AMOUNT~Decimal~N~TRADEPRICE~Decimal~N~ORDERINPUTUSERID~String~N~PREMIUMDIRECTION~String~N~PREMIUMAMOUNT~Decimal~N~CUSTOMERPREMIUMDIRECTION~String~N~CUSTOMERPREMIUMAMOUNT~Decimal~N~PREMIUMVALUEDATE~DateTime~N~STRIKEPRICE~Decimal~N~OPTIONDIRECTION~String~N~EXERCISESTYLE~String~N~OPTIONSTATUS~String~N~REFIXINGDATE~DateTime~N~REFIXEDIND~String~N~WINDOWFWDTRADENUMBER~Decimal~N~|11111111-2222-3333-4444-555555555555~0~123ABC123ABC123ABC123ABC~1~100~2~OMF~3~FX~4~DELIVERY~5~SPOT~6~601033445~7~2020-01-1 00-11-22~8~ACTUAL~9~PORTAL~10~~11~~12~~13~2020-08-31 23-34-52~14~MHALFPENNY~15~~16~N~17~2020-09-01~18~~19~~20~1~21~BUY~22~SELL~23~SOME CUSTOMER38320~24~38320~25~SOME CUSTOMER DETAILS NAME~26~105428~27~2020-09-03~28~2020-09-03~29~AUD/USD~30~~31~AUD~32~500.0000~33~-500.0000~34~USD~35~-300.00~36~300.00~37~0.7369600000~38~USER~39~~40~~41~~42~~43~~44~~45~~46~~47~~48~~49~N~50~~";
    List<Order> orders = OMFAdapters.adaptSearchResult(searchRespone);
    assertThat(orders.size()).isEqualTo(1);
    Order order = orders.get(0);
    assertThat(order.getType()).isEqualTo(Order.OrderType.ASK);
    assertThat(order.getOriginalAmount()).isEqualByComparingTo(new BigDecimal("500.0000"));
    assertThat(order.getInstrument()).isEqualTo(new CurrencyPair("AUD/USD"));
    assertThat(order.getId()).isEqualTo("601033445");
  }
}
